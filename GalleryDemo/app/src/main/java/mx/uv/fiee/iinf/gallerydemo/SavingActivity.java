package mx.uv.fiee.iinf.gallerydemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SavingActivity extends Activity {
    public static final int REQUEST_CAMERA_OPEN = 4001;
    public static final int REQUEST_PERMISSION_CAMERA_LOCATION = 3001;

    LocationManager locationManager;
    LocationListener locationListener;
    ImageView iv;
    double latitude, longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_saving);

        iv = findViewById (R.id.ivSource);

        locationListener = (location) -> {
            latitude = location.getLatitude ();
            longitude = location.getLongitude ();
            Log.d ("TYAM", "Latitude " + latitude + " - Longitude " + longitude);
        };

        Button button = findViewById (R.id.btnSave);
        button.setOnClickListener (v -> {

            int camPermission = checkSelfPermission (Manifest.permission.CAMERA);
            int coarsePermission = checkSelfPermission (Manifest.permission.ACCESS_COARSE_LOCATION);

            if (camPermission != PackageManager.PERMISSION_GRANTED || coarsePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (
                        new String [] { Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION },
                        REQUEST_PERMISSION_CAMERA_LOCATION
                );

                return;
            }

            abrirCamara ();

//            Bitmap bitmap = getBitmapFromDrawable (iv.getDrawable ());
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                saveImage (bitmap);
//            } else {
//                String imageDir = Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_PICTURES).toString ();
//                File file = new File(imageDir, "/mypic.jpg");
//
//                try {
//                    OutputStream fos = new FileOutputStream (file);
//                    bitmap.compress (Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.close ();
//                } catch (IOException ex) {
//                    ex.printStackTrace ();
//                }
//            }

        });
    }

    @SuppressLint ("MissingPermission")
    void abrirCamara () {
        locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
        }

        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult (intent, REQUEST_CAMERA_OPEN);
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

    /**
     * Almacena el mapa de bits recibido en el almacenamiento externo, dentro de la carpeta destinada
     * para contener archivos de imagen.
     *
     * @param bitmap imagen en mapa de bits a guardar.
     */
    boolean saveImage (Bitmap bitmap, String filename) {
        boolean result = false;
        ContentResolver resolver = getContentResolver ();

        ContentValues values = new ContentValues ();
        values.put (MediaStore.MediaColumns.DISPLAY_NAME, filename);
        values.put (MediaStore.Images.Media.TITLE, filename);
        values.put (MediaStore.Images.Media.DESCRIPTION, "");
        values.put (MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put (MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis ());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put (MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
            values.put (MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            values.put (MediaStore.MediaColumns.IS_PENDING, true);
        } else {
            String pictureDirectory =
                    String.format ("%s/%s", Environment.getExternalStorageDirectory (), Environment.DIRECTORY_DCIM);

            String fullPath = String.format ("%s/%s", pictureDirectory, filename);

            values.put (MediaStore.MediaColumns.DATA, fullPath);
            values.put (MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis ());
        }

        Uri targetUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Uri imageUri =  resolver.insert (targetUri, values);

        try {
            OutputStream fos = resolver.openOutputStream (imageUri);
            bitmap.compress (Bitmap.CompressFormat.JPEG,100, fos);
            fos.flush ();
            fos.close ();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values = new ContentValues ();
                values.put (MediaStore.Images.ImageColumns.IS_PENDING, false);
                resolver.update (imageUri, values, null, null);
            }

            result = true;
        } catch (Exception ex) {
            ex.printStackTrace ();
        }

        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CAMERA_LOCATION) {
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara ();
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA_OPEN && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get ("data");
            iv.setImageBitmap (bitmap);

            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyy-MM-dd-HH-mm-ss", Locale.US);
            String now = dateFormat.format (Calendar.getInstance().getTime ());
            String filename = "IMG_GALLERY_APP_" + now +  ".jpg";
            boolean ok = saveImage (bitmap, filename);

            if (ok) {
                String fullPath = String.format ("%s/%s/%s", Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DCIM, filename);
                saveGeoTag (fullPath);
            }
        }
    }

    void saveGeoTag (String fullPath) {
        try {
            ExifInterface exifInterface = new ExifInterface (new File (fullPath).getAbsolutePath ());
            exifInterface.setAttribute (ExifInterface.TAG_GPS_LATITUDE, convertGPS2DMS (latitude));
            exifInterface.setAttribute (ExifInterface.TAG_GPS_ALTITUDE_REF, latitude < 0 ? "S" : "N");
            exifInterface.setAttribute (ExifInterface.TAG_GPS_LONGITUDE, convertGPS2DMS (longitude));
            exifInterface.setAttribute (ExifInterface.TAG_GPS_ALTITUDE_REF, longitude < 0 ? "W" : "E");
            exifInterface.saveAttributes ();
        } catch (IOException ex) {
            ex.printStackTrace ();
        }
    }

    String convertGPS2DMS (double loc) {
        String [] foo = Location.convert (loc, Location.FORMAT_SECONDS).split (":");
        int degress = Math.abs (Integer.parseInt (foo [0]));
        int seconds = (int) (Double.parseDouble (foo [2]) * 10000);

        return String.format (Locale.US, "%d/1,%s/1,%d/10000", degress, foo [1], seconds);
    }

    @Override
    protected void onPause () {
        super.onPause ();
        locationManager.removeUpdates (locationListener);
    }
}
