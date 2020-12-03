package mx.uv.fiee.iinf.fbmessaging;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFCMHandler extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken (s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived (remoteMessage);
        Intent intent = new Intent ("FBMessaging");
        intent.putExtra ("MESSAGE_TITLE", remoteMessage.getNotification().getTitle ());
        intent.putExtra ("MESSAGE_BODY", remoteMessage.getNotification().getBody ());
        Log.i ("TYAM", remoteMessage.getNotification().getBody ());
        LocalBroadcastManager.getInstance (this).sendBroadcast (intent);
    }

}
