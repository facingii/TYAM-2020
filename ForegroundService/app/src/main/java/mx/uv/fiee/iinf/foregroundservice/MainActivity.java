package mx.uv.fiee.iinf.foregroundservice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    public static final int FOREGROUND_PERMISSION_CODE = 5001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        findViewById (R.id.startService).setOnClickListener (view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                int permission = checkSelfPermission (Manifest.permission.FOREGROUND_SERVICE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.FOREGROUND_SERVICE},
                            FOREGROUND_PERMISSION_CODE
                    );

                    return;
                }
            }

            launchService ();
        });
    }

    private void launchService () {
        Intent intent = new Intent(getBaseContext (), MyForegroundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService (intent);
        } else {
            startService (intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FOREGROUND_PERMISSION_CODE &&
                grantResults.length > 0 &&
                grantResults [0] == PackageManager.PERMISSION_GRANTED) {

            launchService ();

        }

    }
}
