package mx.uv.fiee.iinf.rssfeedsfetcher;

import android.app.Activity;
import android.content.Context;
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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        RecyclerView rvRssFeeds = findViewById(R.id.rvRssFeeds);
        rvRssFeeds.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));
        rvRssFeeds.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));

        OkHttpClient client = new OkHttpClient ();
        Request request = new Request.Builder().url ("https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss").build ();
        client.newCall (request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body () == null) return;
                String xml = response.body().string ();
                LinkedList<XMLFeedParser.RSSItem> items = new XMLFeedParser().parse (xml);
                runOnUiThread(() -> rvRssFeeds.setAdapter (new MyRSSAdapter (getBaseContext (), items)));
            }
        });
    }

}

class MyRSSAdapter extends RecyclerView.Adapter<MyRSSAdapter.MyViewHolder> {
    private Context context;
    private LinkedList<XMLFeedParser.RSSItem> items;

    MyRSSAdapter (Context context, LinkedList<XMLFeedParser.RSSItem> items) {
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

        MyViewHolder(@NonNull View itemView) {
            super (itemView);
            ivPicture = itemView.findViewById (R.id.ivPicture);
            tvRssFeed = itemView.findViewById (R.id.tvRssfeed);
        }

        void setData (String text, String imageURL) {
            tvRssFeed.setText (text);

            // avoiding HTTP ClearText traffic exception
            if (imageURL.startsWith ("http://")) {
                imageURL = imageURL.replaceFirst ("http://", "https://");
            }

            //como la imagen debe descargarse de internet debemos realizar esta asignación en un objeto AsyncTask
            //new GetImageAsyncTask (ivPicture).execute (imageURL);
            Picasso.get ()
                    .load (imageURL)
                    .resize (200, 200)
                    .centerCrop ()
                    .placeholder (R.drawable.placeholder)
                    .into (ivPicture);
        }
    }
}
