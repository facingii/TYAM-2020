package mx.uv.fiee.iinf.rssfeedsfetcher;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.uv.fiee.iinf.rssfeedsfetcher.RSS.Item;
import mx.uv.fiee.iinf.rssfeedsfetcher.RSS.Rss;
import mx.uv.fiee.iinf.rssfeedsfetcher.RSS.RssApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings ("deprecation")
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        RecyclerView rvRssFeeds = findViewById(R.id.rvRssFeeds);
        rvRssFeeds.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));
        rvRssFeeds.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));

        Retrofit retrofit = new Retrofit.Builder ()
                .baseUrl ("https://www.nasa.gov/rss/dyn/")
                .addConverterFactory (SimpleXmlConverterFactory.create ())
                .build ();

        RssApi api = retrofit.create (RssApi.class);
        api.getAllItems ().enqueue (new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                if (response.body () == null) return;
                List<Item> items = response.body ().getItems ();
                runOnUiThread (() -> rvRssFeeds.setAdapter (new MyRSSAdapter (getBaseContext (), items)));
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                if (t.getMessage () != null) {
                    Log.e ("PKAT", t.getMessage ());
                    runOnUiThread (() ->
                            Toast.makeText (getBaseContext (), "Ocurrió un error al descargar el archivo", Toast.LENGTH_LONG).show ());
                }
            }
        });

    }
}

class MyRSSAdapter extends RecyclerView.Adapter<MyRSSAdapter.MyViewHolder> {
    private final Context context;
    private final List<Item> items;

    MyRSSAdapter (Context context, List<Item> items) {
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
        Item item = items.get (position);
        holder.setData (item.getTitle (), item.getEnclosure().getUrl ());
    }

    @Override
    public int getItemCount() {
        return items.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPicture;
        final TextView tvRssFeed;

        MyViewHolder(@NonNull View itemView) {
            super (itemView);
            ivPicture = itemView.findViewById (R.id.ivPicture);
            tvRssFeed = itemView.findViewById (R.id.tvRssfeed);
        }

        void setData (String text, String imageURL) {
            tvRssFeed.setText (text);

            // debido a la restricción de tráfico http, las URL deben apuntar a un recurso seguro
            // para ser descargados.
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
