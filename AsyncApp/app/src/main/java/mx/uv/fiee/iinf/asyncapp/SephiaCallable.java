package mx.uv.fiee.iinf.asyncapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import java.util.concurrent.Callable;

public class SephiaCallable implements Callable<Bitmap> {
    Bitmap bitmap;

    public SephiaCallable (Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Bitmap call() throws Exception {
        return toSephia (bitmap);
    }

    public Bitmap toSephia (Bitmap bmpOriginal)
    {
        Log.d ("TYAM", "STARTING CONVERT TO SEPHIA");
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
        Log.d ("TYAM", "ENDING CONVERT TO SEPHIA");

        return bmpSephia;
    }

}
