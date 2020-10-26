package mx.uv.fiee.iinf.asyncapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends Activity {
    public static final int FILE_CHOOSER_REQUEST_CODE = 4001;
    ImageView ivCanvas;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setTitle (R.string.app_name);

        ivCanvas = findViewById (R.id.ivCanvas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate (R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {

        int id = item.getItemId ();
        if (id == R.id.mnuOpen) {
            openDialog ();
        } else if (id == R.id.mnuSepia) {
            convertSepia ();
        } else if (id == R.id.mnuGrayScale) {
            convertMonochrome ();
        } else if (id == R.id.mnuNegative) {
            convertNegative ();
        }

        return super.onOptionsItemSelected(item);

    }

    private void openDialog () {
        Intent intent = new Intent ();
        intent.setType ("image/jpeg");
        intent.setAction (Intent.ACTION_GET_CONTENT);

        startActivityForResult (Intent.createChooser (intent, "Select image file"), FILE_CHOOSER_REQUEST_CODE);
    }

    private void convertSepia () {
        final Bitmap bitmap = getBitmapFromDrawable (ivCanvas.getDrawable ());

        ExecutorService executorService = Executors.newFixedThreadPool (1);
        Future<Bitmap> bitmapFuture = executorService.submit (new SephiaCallable (bitmap));
        executorService.shutdown ();

        // do other work

//        if (bitmapFuture.isDone ()) {
//            try {
//                ivCanvas.setImageBitmap (bitmapFuture.get ()); // .get puede bloquear la UI
//            } catch (ExecutionException | InterruptedException ex) {
//                ex.printStackTrace ();
//            }
//        }

        // se obtiene la referencia al objeto handler del hilo principal, lo que permite enviar
        // mensajes y tareas a la cola de mensaje de dicho hilo.
        Handler myHandler = getWindow().getDecorView().getHandler ();

        // para verificar el estado de la tarea, se utiliza un hilo adicional de modo que no
        // se bloquee la interfaz de usuario.
        new Thread (() -> {
            while (true) {
                Log.d ("TYAM", "Checking Future task's finish...");
                if (bitmapFuture.isDone ()) {
                    // se utiliza la función post del handler obtenido para colocar un objeto runnable
                    // que será ejecutado dentro del loop del hilo principal, donde se tiene
                    // acceso a los controles de la interfaz de usuario.
                    myHandler.post (() -> {
                        try {
                            Bitmap bmp = bitmapFuture.get ();
                            ivCanvas.setImageBitmap (bmp);
                        } catch (ExecutionException | InterruptedException ex) {
                            ex.printStackTrace ();
                        }
                    });

                    break;
                }
            }
        }).start ();
    }

    private void convertMonochrome () {
        Bitmap bitmap = getBitmapFromDrawable (ivCanvas.getDrawable ());

        Thread thread = new Thread (() -> {
            Bitmap bmp = toGrayscale (bitmap);

            this.runOnUiThread (() -> {
                ivCanvas.setImageBitmap (bmp);
            });
        });

        thread.start ();
    }

    public Bitmap toGrayscale (Bitmap src) {
        float [] matrix = new float [] {
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0, 0, 0, 1, 0, };

        Bitmap dest = Bitmap.createBitmap (
                src.getWidth (),
                src.getHeight (),
                src.getConfig ());

        Canvas canvas = new Canvas (dest);
        Paint paint = new Paint ();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter (matrix);
        paint.setColorFilter (filter);
        canvas.drawBitmap (src, 0, 0, paint);

        return dest;
    }

    private void convertNegative () {
        Bitmap bitmap = getBitmapFromDrawable (ivCanvas.getDrawable ());
        NegativeFilterAsyncTask task = new NegativeFilterAsyncTask (ivCanvas);
        task.execute (bitmap);
    }

    /**
     * Obtiene un objeto de mapa de bits a partir del objeto Drawable (canvas) recibido.
     *
     * @param drble Drawable que contiene la imagen deseada.
     * @return objeto de mapa de bits con la estructura de la imagen.
     */
    private Bitmap getBitmapFromDrawable (Drawable drble) {
        // debido a la forma que el sistema dibuja una imagen en un el sistema gráfico
        // es necearios realzar comprobaciones para saber del tipo de objeto Drawable
        // con que se está trabajando.
        //
        // si el objeto recibido es del tipo BitmapDrawable no se requieren más conversiones
        if (drble instanceof BitmapDrawable) {
            return  ((BitmapDrawable) drble).getBitmap ();
        }

        // en caso contrario, se crea un nuevo objeto Bitmap a partir del contenido
        // del objeto Drawable
        Bitmap bitmap = Bitmap.createBitmap (drble.getIntrinsicWidth (), drble.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);

        return bitmap;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            Uri selectedImage = data.getData ();
            ivCanvas.setImageURI (selectedImage);
        }
    }
}

