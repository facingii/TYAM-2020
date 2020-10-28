package mx.uv.fiee.iinf.basicserviceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    Intent intent;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        findViewById(R.id.startService).setOnClickListener (view -> {
            intent = new Intent (getBaseContext (), MyOsomService.class);
            startService (intent);
        });

        findViewById (R.id.stopService).setOnClickListener (view -> {
            if (intent != null) stopService (intent);
        });
    }
}
