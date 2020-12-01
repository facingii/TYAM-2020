package mx.uv.fiee.iinf.fbstorage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        FirebaseStorage storage = FirebaseStorage.getInstance ();

        ImageView iv = findViewById (R.id.ivSource);

        Button btnRecover = findViewById (R.id.btnRecover);
        btnRecover.setOnClickListener (view -> {
            StorageReference folder = storage.getReference ("assets");
            StorageReference imageFile = folder.child ("spock.jpg");

            final long SIZE_BUFFER = 1024 * 1024;
            imageFile.getBytes (SIZE_BUFFER)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess (byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray (bytes, 0, bytes.length);
                            iv.setImageBitmap (bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure (@NonNull Exception e) {
                    Toast.makeText (getBaseContext (), "Error: " + e.getMessage (), Toast.LENGTH_LONG).show();
                }
            });
        });

        Button btnUpload = findViewById (R.id.btnUpload);
        btnUpload.setOnClickListener (view -> {
            StorageReference imagesFolder = storage.getReference ("images/");
            StorageReference  image = imagesFolder.child ("myPicture.png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream ();
            Bitmap bitmap = getBitmapFromDrawable (iv.getDrawable ());
            bitmap.compress (Bitmap.CompressFormat.PNG, 100, bos);
            byte [] buffer = bos.toByteArray ();

            image.putBytes (buffer)
                    .addOnFailureListener (e -> {
                        Toast.makeText (getBaseContext (), "Error uploading file: " + e.getMessage (), Toast.LENGTH_LONG).show ();
                        Log.e ("TYAM", "Error uploading file: " + e.getMessage ());
                    })
                    .addOnCompleteListener (task -> {
                        if (task.isComplete ()) {
                            Task<Uri> getUriTask = image.getDownloadUrl ();

                            getUriTask.addOnCompleteListener (t -> {
                                Uri uri = t.getResult ();
                                if (uri == null) return;

                                Toast.makeText (getBaseContext (), "Download URL " + uri.toString (), Toast.LENGTH_LONG).show ();
                                Log.i ("TYAM", "Download URL " + uri.toString ());
                            });
                        }
                    });
        });

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

}
