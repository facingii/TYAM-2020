package mx.uv.fiee.iinf.fbemailauthenticationapp.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import mx.uv.fiee.iinf.fbemailauthenticationapp.BuildConfig;

public class GithubOAuth2Fragment extends Fragment {
    public static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    public static final String OAUTH_SERVICE_CODE_URL = "https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID;
    public static final String OAUTH_URL_ACCESS_TOKEN = "https://github.com/login/oauth/access_token";
    public static final String OAUTH_URL_CALLBACK = BuildConfig.OAUTH_URL_CALLBACK;
    public static final String QUERY_CODE_KEY_PARAMETER = "code";
    public static final String GET_REQUEST_TOKEN_CONTENT_TYPE = "application/json";
    public static final String REQUEST_PROPERTY_CONTENT_TYPE = "Content-type";
    public static final String REQUEST_PROPERTY_ACCEPT = "Accept";
    public static final String ACCESS_TOKEN_REQUEST_METHOD = "POST";
    public static final String TAG = "PKAT";

    public interface OnAuthenticationComplete {
        void authenticationComplete (String token);
    }

    private OnAuthenticationComplete listener;
    private WebView webView;

    public void setOnAuthenticationCompleteListener (OnAuthenticationComplete l) {
        this.listener = l;
    }

    @SuppressLint ("SetJavaScriptEnabled")
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity ();
        if (activity == null) return null;

        webView = new WebView (activity.getBaseContext ());
        webView.setWebChromeClient (new WebChromeClient ());

        WebViewClient webViewClient = new WebViewClient () {
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                Log.i (TAG, "Redirecting..." + url);
                if (url.startsWith (OAUTH_URL_CALLBACK)) {
                    Uri uri = Uri.parse (url);
                    if (uri.getQueryParameter (QUERY_CODE_KEY_PARAMETER) != null) {
                        String code = uri.getQueryParameter ( QUERY_CODE_KEY_PARAMETER);
                        Log.i (TAG, "Code: " + code);
                        getAuthenticateToken (code);
                        return true;
                    } else if (uri.getQueryParameter ("error") != null) {
                        String message = uri.getQueryParameter ("error");
                        if (message != null) Log.e (TAG, message);
                    }
                }
                return super.shouldOverrideUrlLoading (view, url);
            }
        };

        webView.setWebViewClient (webViewClient);
        WebSettings settings = webView.getSettings ();
        settings.setJavaScriptEnabled (true);

        return webView;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);

        Log.i (TAG, OAUTH_SERVICE_CODE_URL);
        webView.loadUrl (OAUTH_SERVICE_CODE_URL);
    }

    public void getAuthenticateToken (String code) {
        new GetAccessTokenAsyncTask (listener).execute (code);
    }
}

class GetAccessTokenAsyncTask extends AsyncTask<String, Void, AccessTokenRequestResponse> {
    private GithubOAuth2Fragment.OnAuthenticationComplete listener;

    GetAccessTokenAsyncTask (GithubOAuth2Fragment.OnAuthenticationComplete l) {
        listener = l;
    }

    @Override
    protected AccessTokenRequestResponse doInBackground (String... strings) {
        return donwloadStreamAndParseJson (strings [0]);
    }

    private AccessTokenRequestResponse donwloadStreamAndParseJson (String code) {
        AccessTokenRequestResponse token = null;

        try {
            URL url = new URL (GithubOAuth2Fragment.OAUTH_URL_ACCESS_TOKEN);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection ();
            connection.setRequestMethod (GithubOAuth2Fragment.ACCESS_TOKEN_REQUEST_METHOD);
            connection.setDoOutput (true);
            connection.setRequestProperty (GithubOAuth2Fragment.REQUEST_PROPERTY_CONTENT_TYPE, GithubOAuth2Fragment.GET_REQUEST_TOKEN_CONTENT_TYPE);
            connection.setRequestProperty (GithubOAuth2Fragment.REQUEST_PROPERTY_ACCEPT, GithubOAuth2Fragment.GET_REQUEST_TOKEN_CONTENT_TYPE);

            OutputStream outputStream = connection.getOutputStream ();
            byte [] buffer = getRequestTokenBody (code).getBytes (StandardCharsets.UTF_8);
            outputStream.write (buffer);
            outputStream.flush ();
            outputStream.close ();

            InputStreamReader isr = new InputStreamReader (connection.getInputStream (), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader (isr);
            StringBuilder builder = new StringBuilder ();
            String line;

            while ((line = reader.readLine ()) != null) {
                builder.append (line);
            }

            reader.close ();
            isr.close();
            connection.disconnect ();

            String content = builder.toString ();
            Log.i (GithubOAuth2Fragment.TAG, content);

            token = new Gson().fromJson (content, AccessTokenRequestResponse.class);
        } catch (IOException e) {
            Log.e (GithubOAuth2Fragment.TAG, "IOError", e);
        }

        return token;
    }

    private String getRequestTokenBody (String code) {
        AcessTokenRequestBody body = new AcessTokenRequestBody ();
        body.client_id = GithubOAuth2Fragment.CLIENT_ID;
        body.client_secret = GithubOAuth2Fragment.CLIENT_SECRET;
        body.code = code;

        String foo = new Gson ().toJson (body);
        Log.i (GithubOAuth2Fragment.TAG, "Sending JSON: " + foo);

        return foo;
    }

    @Override
    protected void onPostExecute (AccessTokenRequestResponse token) {
        super.onPostExecute(token);
        listener.authenticationComplete (token.access_token);
    }
}

class AccessTokenRequestResponse {
    String access_token;
    String scope;
    String token_type;
}


class AcessTokenRequestBody {
    String client_id;
    String client_secret;
    String code;
}