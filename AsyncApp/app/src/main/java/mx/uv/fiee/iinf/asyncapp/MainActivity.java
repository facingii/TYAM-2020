package mx.uv.fiee.iinf.asyncapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

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
        Bitmap bitmap = getBitmapFromDrawable (ivCanvas.getDrawable ());
        bitmap  = toSephia (bitmap);
        ivCanvas.setImageBitmap (bitmap);
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


    public Bitmap toSephia (Bitmap bmpOriginal)
    {
        int width, height, r,g, b, c, gry, depth = 20;
        height = bmpOriginal.getHeight ();
        width = bmpOriginal.getWidth ();

        Bitmap bmpSephia = Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bmpSephia);

        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix ();
        cm.setScale (.3f, .3f, .3f, 1.0f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter (cm);

        paint.setColorFilter (f);

        canvas.drawBitmap (bmpOriginal, 0, 0, paint);
        for(int x=0; x < width; x++) {
            for(int y=0; y < height; y++) {
                c = bmpOriginal.getPixel(x, y);

                r = Color.red (c);
                g = Color.green (c);
                b = Color.blue (c);

                gry = (r + g + b) / 3;
                r = g = b = gry;

                r = r + (depth * 2);
                g = g + depth;

                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                bmpSephia.setPixel (x, y, Color.rgb (r, g, b));
            }
        }

        return bmpSephia;
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
