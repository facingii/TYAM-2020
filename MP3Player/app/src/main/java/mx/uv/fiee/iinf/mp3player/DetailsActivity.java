package mx.uv.fiee.iinf.mp3player;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsActivity extends Activity {
    private static final String SONG_KEY = "song";
    private static final String PROGRESS_KEY = "progress";
    private static final String ISPLAYING_KEY = "isplaying";

    //MediaPlayer player;
    Thread posThread;
    Uri mediaUri;
    int pos;
    boolean updateProgressBar;

    PlayerService playerService;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_details);

        SeekBar sbProgress = findViewById (R.id.sbProgress);
        sbProgress.setOnSeekBarChangeListener (new MySeekBarChangeListener ());

        // hilo adicional para controlar la actualización de la barra de progreso
//        posThread = new Thread (() -> {
//            try {
//                while (true) {
//                    if (updateProgressBar) {
//                        Thread.sleep (1000);
//                        sbProgress.setProgress (player.getCurrentPosition ());
//                    }
//                }
//            } catch (InterruptedException in) { in.printStackTrace (); }
//        });

//        player = new MediaPlayer ();

        // establece el manejador del evento de reproducción finalizada
//        player.setOnCompletionListener (mediaPlayer -> {
//            // operaciones de limpieza cuando termina la reproducción
//            updateProgressBar = false;
//            //if (posThread != null) posThread.interrupt ();
//            sbProgress.setProgress (0);
//        });

        // manejador del evento media player preparado
//        player.setOnPreparedListener (mediaPlayer -> {
//            sbProgress.setMax (mediaPlayer.getDuration ());
//            if (pos > -1) mediaPlayer.seekTo (pos);
//            mediaPlayer.start ();
//            posThread.start ();
//            updateProgressBar = true;
//        });

        // botón play
        ImageButton btnAudio1 = findViewById (R.id.btnAudio1);
        btnAudio1.setOnClickListener (view -> {
            // verifica si el player se encuentra en un estado de pausa
//            if (!player.isLooping () && player.getCurrentPosition () > 1) {
//                player.start ();
//                player.seekTo (pos);
//                updateProgressBar = true;
//                return;
//            }

            // si es la primera vez que se carga el archivo de audio
            // se invoca al método de preparación del media player
//            try {
//                player.setDataSource (getBaseContext (), mediaUri);
//                player.prepare ();
//            } catch (IOException ex) { ex.printStackTrace (); }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                int perm = checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE);
                if (perm != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions (
                            new String [] { Manifest.permission.FOREGROUND_SERVICE },
                            1001
                    );
                }

                return;
            }

            play ();
        });

        // botón pausar
        ImageButton btnAudio2 = findViewById (R.id.btnAudio2);
        btnAudio2.setOnClickListener (view -> {
//            if (player.isPlaying ()) {
//                pos = player.getCurrentPosition ();
//                player.pause ();
//                updateProgressBar = false;
//            }

            playerService.pause ();
        });

    }

    void play () {
        playerService.play (mediaUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                play ();
            }
        }
    }

    //    @Override
//    protected void onSaveInstanceState (@NonNull Bundle outState) {
//        super.onSaveInstanceState (outState);
//
//        outState.putString (SONG_KEY, mediaUri != null ? mediaUri.toString (): "");
//        outState.putInt (PROGRESS_KEY, player != null ?  player.getCurrentPosition () : -1);
//        outState.putBoolean (ISPLAYING_KEY, player != null && player.isPlaying ());
//
//        posThread.interrupt ();
//
//        if (player.isPlaying ()) {
//            player.stop ();
//            player.seekTo (0);
//        }
//
//        player.release ();
//        player = null;
//    }

//    @Override
//    protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState (savedInstanceState);
//
//        mediaUri = Uri.parse (savedInstanceState.getString (SONG_KEY));
//        pos = savedInstanceState.getInt (PROGRESS_KEY);
//        boolean isPlaying = savedInstanceState.getBoolean (ISPLAYING_KEY);
//        updateProgressBar = isPlaying;
//
//        if (player == null) return;
//
//        try {
//            player.reset ();
//
//            if (isPlaying) {
//                player.setDataSource (getBaseContext (), mediaUri);
//                player.prepareAsync();
//            }
//        } catch (IOException | IllegalStateException ioex) {
//            ioex.printStackTrace ();
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (posThread.isAlive ()) posThread.interrupt ();
//    }

//    @Override
//    protected void onDestroy () {
//        super.onDestroy();
//        // cleanup
//        if (player != null) {
//            if (player.isPlaying ()) {
//                player.stop ();
//            }
//
//            player.release ();
//        }
//
//        player = null;
//    }


    @Override
    protected void onResume () {
        super.onResume ();

        Intent intent = new Intent (getBaseContext (), PlayerService.class);
        bindService (intent, serviceConnection, BIND_AUTO_CREATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService (intent);
        } else {
            startService (intent);
        }
    }

    @Override
    protected void onPause () {
        super.onPause ();

        unbindService (serviceConnection);
        playerService.stop ();
    }

    @Override
    protected void onStart () {
        super.onStart ();

        Intent intent = getIntent ();
        if (intent != null) {
            String audio = intent.getStringExtra ("AUDIO");
            mediaUri = Uri.parse (audio);
        }

    }

    /**
     * Clase que implementa a la interfaz OnSeekBarChangeListener para responder
     * al evento de búsqueda en la barra de progreso
     */
    class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged (SeekBar seekBar, int i, boolean b) {
            if (b) { // si el evento fue disparado por el usuario, se reposiciona el audio
//                player.pause ();
//                player.seekTo (i);
//                player.start ();
            }
        }

        @Override
        public void onStartTrackingTouch (SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch (SeekBar seekBar) {}

    }

    ServiceConnection serviceConnection = new ServiceConnection () {
        @Override
        public void onServiceConnected (ComponentName componentName, IBinder iBinder) {
            PlayerService.MyBinder myBinder = (PlayerService.MyBinder) iBinder;
            playerService = myBinder.getService ();
        }

        @Override
        public void onServiceDisconnected (ComponentName componentName) {}
    };
}
