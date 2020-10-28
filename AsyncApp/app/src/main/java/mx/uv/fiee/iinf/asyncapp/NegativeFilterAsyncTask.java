package mx.uv.fiee.iinf.asyncapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Aplica el filtro negative al mapa de bits recibido.
 *
 * La clase espera recibir parámetros de tipo Bitmap, no utiliza variables para
 * definir progreso de la tarea y al finalizar el cómputo asíncrono, devuelve un
 * objeto de mapa de bits.
 */
public class NegativeFilterAsyncTask extends AsyncTask<Bitmap, Void, Bitmap> {
    WeakReference<ImageView> imageView;

    public NegativeFilterAsyncTask (ImageView imageView) {
        this.imageView = new WeakReference<ImageView>(imageView);
    }

    /**
     * Método de preparación, invocado antes de iniciar el cómputo asíncrono.
     *
     * Se ejecuta en el mimos hilo del objeto que invoca la clase.
     */
    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
    }

    /**
     * Método que realiza el cómputo asíncrono. Su ejecución es llevada a cabo en un hilo
     * paralelo.
     *
     * @param bitmaps mapa de bits a procesar
     * @return mapa de bits resultante
     */
    @Override
    protected Bitmap doInBackground (Bitmap... bitmaps) {
        Bitmap bitmap = bitmaps [0];
        return invert (bitmap);
    }

    /**
     * Método que se ejecuta posterior a finalizar el cómputo asíncrono.
     *
     * Este método es ejecutado en el mismo hilo del objeto que invoca la clase.
     *
     * @param bitmap mapa de bits resultante del cómputo asíncrono.
     */
    @Override
    protected void onPostExecute (Bitmap bitmap) {
        super.onPostExecute (bitmap);
        imageView.get().setImageBitmap (bitmap);
    }

    public Bitmap invert (Bitmap original) {
        final int RGB_MASK = 0x00FFFFFF;

        Bitmap inversion = original.copy(Bitmap.Config.ARGB_8888, true);

        int width = inversion.getWidth();
        int height = inversion.getHeight();
        int pixels = width * height;

        int[] pixel = new int[pixels];
        inversion.getPixels(pixel, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels; i++) {
            pixel[i] ^= RGB_MASK;
        }

        inversion.setPixels (pixel, 0, width, 0, 0, width, height);

        return inversion;
    }


}
