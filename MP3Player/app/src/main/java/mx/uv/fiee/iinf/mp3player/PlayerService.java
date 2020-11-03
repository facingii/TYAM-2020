package mx.uv.fiee.iinf.mp3player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class PlayerService extends Service {
    public static final String CHANNEL_ID = "MyServiceChannel";
    public static final String CHANNEL_DESC = "Foreground Service Notifications";
    public static final int MP3PLAYER_NOTIFICATION_ID = 100;

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder ();
    }

    Notification createNotification (String text) {
        Intent notificationIntent = new Intent (this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity (this, 0, notificationIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights (false);
            channel.setShowBadge (true);
            NotificationManager manager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
            manager.createNotificationChannel (channel);
        }

        Bitmap icon = BitmapFactory.decodeResource (getResources (), R.mipmap.ic_launcher_round);

        return new NotificationCompat.Builder (this, CHANNEL_ID)
                .setContentTitle ("MP3Player Running")
                .setContentText (text)
                .setSmallIcon (R.mipmap.ic_launcher_round)
                .setOnlyAlertOnce (true)
                .setLargeIcon (icon)
                .build ();
    }


    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Notification notification = createNotification ("Audio not selected");
        startForeground (MP3PLAYER_NOTIFICATION_ID, notification); // se establece la bandera para servicio en primer plano y la notificación

        player = new MediaPlayer ();
        player.setOnPreparedListener (new PreparedMediaPlayerHandler ());

        return START_NOT_STICKY;
    }

    public void play (Uri audio) {
        try {
            player.setDataSource (getBaseContext (), audio);
            player.prepareAsync ();
            updateNotification (audio);
        } catch (IOException ex) {
            ex.printStackTrace ();
        }
    }

    private void updateNotification (Uri audio) {
        String text = audio.toString ();
        Notification notification = createNotification (text);
        NotificationManager notificationManager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
        notificationManager.notify (MP3PLAYER_NOTIFICATION_ID, notification);
    }

    public void pause () {
        if (player.isPlaying ()) player.pause ();
    }
    public void stop () {
        stopSelf ();
    }
    public void resume () {}

    @Override
    public void onDestroy () {
        super.onDestroy ();
        if (player != null) {
            if (player.isPlaying ()) {
                player.stop ();
            }

            player.release ();
            player = null;
        }
    }

    class MyBinder extends Binder {
        PlayerService getService () {
            return PlayerService.this;
        }
    }

    private class PreparedMediaPlayerHandler implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared (MediaPlayer mediaPlayer) {
            player.start ();
        }
    }
    
}
