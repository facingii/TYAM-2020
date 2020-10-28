package mx.uv.fiee.iinf.backgroundservice;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DownloadService extends IntentService {
    public static final String INTENT_FILTER = "textDownloader";
    public static final String EXTRA_TAG = "data";
    public static final String URL_TAG = "url";
    public static final String HTTPS_TAG = "https";
    public static final String TITLE = "Downloader";

    public DownloadService () {
        super (TITLE);
    }

    @Override
    protected void onHandleIntent (@Nullable Intent intent) {
        if (intent == null) return;

        String urlString = intent.getStringExtra (URL_TAG);

        try {
            URL url = new URL (urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;

            if (urlString.startsWith (HTTPS_TAG)) {
                connection = (HttpsURLConnection) urlConnection;
            } else {
                connection = (HttpURLConnection) urlConnection;
            }

            connection.connect ();
            if (connection.getResponseCode () != HttpURLConnection.HTTP_OK) return;

            InputStream inputStream = connection.getInputStream ();
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream);
            BufferedReader reader = new BufferedReader (inputStreamReader);
            StringBuilder builder = new StringBuilder ();
            String line;

            while ((line = reader.readLine ()) != null) {
                builder.append (line);
                builder.append (System.lineSeparator ());
            }

            reader.close ();
            inputStream.close ();
            inputStream.close ();

            sendResults (builder.toString ());
        } catch (IOException ex) {
            ex.printStackTrace ();
        }
    }

    private void sendResults (String data) {
        Intent intent = new Intent (INTENT_FILTER);
        intent.putExtra (EXTRA_TAG, data);
        sendBroadcast (intent);
    }

}
