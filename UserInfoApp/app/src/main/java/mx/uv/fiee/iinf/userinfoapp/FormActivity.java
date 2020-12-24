package mx.uv.fiee.iinf.userinfoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

public class FormActivity extends Activity {
    private static final int SELECT_IMAGE_REQUEST_CODE = 2001;
    private static final String BASE_STORAGE_REFERENCE = "images";
    private static final String BASE_DATABASE_REFERENCE = "Usuarios";

    private RelativeLayout root;
    private Snackbar snackbar;
    private ImageView ivProfilePic;
    private EditText edtName, edtLastName, edtAge, edtAddress, edtTelephone;

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference songs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_form);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (toolbar);

        database = FirebaseDatabase.getInstance ();
        storage = FirebaseStorage.getInstance ();

        root = findViewById (R.id.root);

        Button btnChangePic = findViewById (R.id.btnChangePic);
        btnChangePic.setOnClickListener (view -> selectImage ());

        ivProfilePic = findViewById (R.id.ivProfilePic);

        edtName = findViewById (R.id.edtName);
        edtLastName = findViewById (R.id.edtlastName);
        edtAge = findViewById (R.id.edtAge);
        edtAddress = findViewById (R.id.edtAddress);
        edtTelephone = findViewById (R.id.edtTelephone);

        Button btnSave = findViewById (R.id.btnSave);
        btnSave.setOnClickListener (view -> {
            snackbar = Snackbar.make (root, "Guardando...", Snackbar.LENGTH_INDEFINITE);
            ViewGroup layer = (ViewGroup) snackbar.getView ().findViewById (com.google.android.material.R.id.snackbar_text).getParent ();
            ProgressBar bar = new ProgressBar (getBaseContext ());
            layer.addView (bar);
            snackbar.show ();

            saveInfo ();
        });
    }

    private void saveInfo () {
        songs = database.getReference (BASE_DATABASE_REFERENCE);

        User user       = new User ();
        user.nombre     = edtName.getText().toString ();
        user.apellidos  = edtLastName.getText().toString ();
        user.edad       = Integer.parseInt (edtAge.getText().toString ());
        user.direccion  = edtAddress.getText().toString ();
        user.telefono   = edtTelephone.getText().toString ();

        /*try {
            Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), u);

            int size = b.getRowBytes () * b.getHeight ();
            ByteBuffer buffer = ByteBuffer.allocate (size);
            b.copyPixelsToBuffer (buffer);
            buffer.rewind ();

            data = new byte [size];
            buffer.get (data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace ();
            return;
        }*/

//        Bitmap bitmap = getBitmapFromDrawable (ivProfilePic.getDrawable ());
//        ByteBuffer buffer = ByteBuffer.allocate (bitmap.getHeight () * bitmap.getRowBytes ());
//        bitmap.copyPixelsToBuffer (buffer);
//        buffer.position (0);
//        byte [] data = buffer.array ();

        Bitmap bitmap = getBitmapFromDrawable (ivProfilePic.getDrawable ());
        ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat.JPEG, 100, bos);
        byte [] data = bos.toByteArray ();

        try {
            bos.close();
        } catch (IOException ex) {
            if (ex.getMessage () != null) {
                Log.e ("TYAM", ex.getMessage ());
                return;
            }

            Log.e ("TYAM", "Error getting bytearray...", ex);
        }

        String fileReferece = String.format (Locale.US, "%s/%s_%s_%d.jpg",
                BASE_STORAGE_REFERENCE, user.nombre, user.apellidos, System.currentTimeMillis ());

        StorageReference images = storage.getReference (fileReferece);
        images.putBytes (data)
                .addOnCompleteListener (task -> {
                    if (task.isComplete ()) {
                        Task<Uri> dlUrlTask = images.getDownloadUrl ();

                        dlUrlTask.addOnCompleteListener (task1 -> {
                            Uri dlUrl = task1.getResult ();
                            if (dlUrl == null) return;

                            user.foto = dlUrl.toString ();
                            doSave (user);
                        });
                    }
                })
                .addOnFailureListener (e -> {
                    Log.e ("TYAM", e.getMessage ());
                });
    }

    private void doSave (User user) {
        String nodeId = calculateStringHash (user.toString ());
        HashMap<String, Object> entry = new HashMap<> ();
        entry.put (nodeId, user);

        songs.updateChildren (entry)
                .addOnSuccessListener (aVoid -> {
                    snackbar.dismiss ();
                    Snackbar.make (root, "Información almacenada!", Snackbar.LENGTH_LONG).show ();
                })
                .addOnFailureListener (e -> Toast.makeText (getBaseContext (),
                        "Error actualizando la BD: " + e.getMessage (),
                        Toast.LENGTH_LONG).show ());
    }

    private String calculateStringHash (String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance ("MD5");
            md5.update(input.getBytes());
            byte[] digest = md5.digest();

            StringBuilder sb = new StringBuilder(digest.length * 2);

            for (byte b : digest) {
                sb.append(Character.forDigit((b >> 8) & 0xf, 16));
                sb.append(Character.forDigit(b & 0xf, 16));
            }

            return sb.toString ();
        } catch (NoSuchAlgorithmException ex) {
            Log.e ("TYAM", ex.getMessage ());
        }

        return null;
    }

    private void selectImage () {
        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType ("image/*");

        String [] mimeTypes = { "image/jpeg", "image/png" };
        intent.putExtra (Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult (intent, SELECT_IMAGE_REQUEST_CODE);
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
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) return;

            Uri uri = data.getData ();
            ivProfilePic.setImageURI (uri);
        }

        super.onActivityResult (requestCode, resultCode, data);
    }

}

