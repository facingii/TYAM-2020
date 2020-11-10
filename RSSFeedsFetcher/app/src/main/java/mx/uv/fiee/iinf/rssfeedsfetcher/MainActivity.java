package mx.uv.fiee.iinf.rssfeedsfetcher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        RecyclerView rvRssFeeds = findViewById (R.id.rvRssFeeds);
        rvRssFeeds.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));
        rvRssFeeds.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));

        new GetRSSAsyncTask (this, rvRssFeeds).execute ("https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss");
    }

}

class MyRSSAdapter extends RecyclerView.Adapter<MyRSSAdapter.MyViewHolder> {
    private final Context context;
    private final LinkedList<XMLFeedParser.RSSItem> items;

    public MyRSSAdapter (Context context, LinkedList<XMLFeedParser.RSSItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (context).inflate (R.layout.recyclerview_row, parent, false);
        return new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull MyViewHolder holder, int position) {
        XMLFeedParser.RSSItem item = items.get (position);
        holder.setData (item.title, item.enclosure.url);
    }

    @Override
    public int getItemCount() {
        return items.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView tvRssFeed;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);
            ivPicture = itemView.findViewById (R.id.ivPicture);
            tvRssFeed = itemView.findViewById (R.id.tvRssfeed);
        }

        public void setData (String text, String imageURL) {
            tvRssFeed.setText (text);
            //como la imagen debe descargarse de internet debemos realizar esta asignación en un objeto AsyncTask
            new GetImageAsyncTask (ivPicture).execute (imageURL);
        }
    }
}

/**
 * Objeto que realiza la descarga asíncrona de un recurso de texto
 */
class GetRSSAsyncTask extends AsyncTask<String, Void, String> {
    private WeakReference<RecyclerView> rvRssFeeds; //es necesario envolver los controles en una clase WeakReference para evitar que una tarea
    private WeakReference<Context> context; // asíncrona no finalizada impida que los objetos (controles) sean recolectados por GC

    GetRSSAsyncTask (Context context, RecyclerView rvRssFeeds) {
        this.rvRssFeeds = new WeakReference<> (rvRssFeeds);
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPostExecute (String s) {
        super.onPostExecute (s);
        LinkedList<XMLFeedParser.RSSItem> data = new XMLFeedParser().parse (s);
        rvRssFeeds.get().setAdapter (new MyRSSAdapter (context.get (), data));
    }

    @Override
    protected String doInBackground (String... strings) {
        return downloadRSS (strings [0]);
    }

    private String downloadRSS (String uri) {
        String foo = null;

        try {
            URL url = new URL (uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setConnectTimeout (5000);

            if (urlConnection.getResponseCode () != HttpURLConnection.HTTP_OK) return foo;

            InputStreamReader inputStreamReader = new InputStreamReader (urlConnection.getInputStream (), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder ();
            String line;

            while ((line = bufferedReader.readLine ()) != null) {
                stringBuilder.append (line);
            }

            bufferedReader.close ();
            inputStreamReader.close ();

            urlConnection.disconnect ();
            foo = stringBuilder.toString ();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return foo;
    }
}

/**
 *
 * Objeto que realiza la descarga de una imagen de forma asíncrona
 *
 */
class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageView;

    /**
     * Constructor de clase que establece el control donde colocar la imagen una vez descargada
     * @param imageView control ImageView donde se visualizará la imagen.
     */
    GetImageAsyncTask (ImageView imageView) {
        this.imageView = new WeakReference<>(imageView);
    }

    @Override
    protected void onPostExecute (Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.get ().setImageBitmap (bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return downloadImage (strings [0]);
    }

    private Bitmap downloadImage (String uri) {
        Bitmap foo = null;

        // avoiding HTTP ClearText traffic exception
        if (uri.startsWith ("http://")) {
            uri = uri.replaceFirst("http://", "https://");
        }

        try {
            URL url = new URL (uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection ();

            urlConnection.setInstanceFollowRedirects (true);
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
                String newUrl = urlConnection.getHeaderField ("Location");
                urlConnection = (HttpURLConnection) new URL (newUrl).openConnection ();
            }

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) return foo;

            InputStream inputStream = urlConnection.getInputStream ();
            foo = BitmapFactory.decodeStream (inputStream);

//            BufferedInputStream bis = new BufferedInputStream (inputStream);
//            ByteArrayOutputStream data = new ByteArrayOutputStream ();
//            byte [] buffer = new byte [1024];
//            int bytesRead = 0;
//
//            while ((bytesRead = bis.read (buffer, 0, buffer.length)) > 0) {
//                data.write (buffer, 0, bytesRead);
//            }
//
//            foo = BitmapFactory.decodeByteArray (data.toByteArray (), 0, data.size ());
//            bis.close ();

            inputStream.close ();
            urlConnection.disconnect ();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foo;
    }
}

