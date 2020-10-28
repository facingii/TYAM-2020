package mx.uv.fiee.iinf.backgroundservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    public static final String TEXT_URL = "https://firebasestorage.googleapis.com/v0/b/mymovieswishlist-927ff.appspot.com/o/images%2Florem.txt?alt=media&token=4cbd995f-a6fe-4cb3-b802-55696954c31c";
    TextView textView;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        textView = findViewById (R.id.tvFetchedText);

        findViewById (R.id.btnStart).setOnClickListener (view -> {
            Intent intent = new Intent (getBaseContext (), DownloadService.class);
            intent.putExtra (DownloadService.URL_TAG, TEXT_URL);
            startService (intent);
        });
    }

    @Override
    protected void onResume () {
        super.onResume ();
        registerReceiver (receiver, new IntentFilter (DownloadService.INTENT_FILTER));
    }

    @Override
    protected void onPause () {
        super.onPause ();
        unregisterReceiver (receiver);
    }

    /**
     * Objeto receiver que escucha por los mensajes de broadcast enviados
     * desde el servicio.
     */
    BroadcastReceiver receiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            Bundle extras = intent.getExtras ();
            if (extras == null) return;

            String data = extras.getString (DownloadService.EXTRA_TAG);
            textView.setText (data);
        }
    };
}
