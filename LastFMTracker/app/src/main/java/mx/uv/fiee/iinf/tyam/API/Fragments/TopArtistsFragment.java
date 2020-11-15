package mx.uv.fiee.iinf.tyam.API.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mx.uv.fiee.iinf.tyam.API.ApiContants;
import mx.uv.fiee.iinf.tyam.API.LastFMService;
import mx.uv.fiee.iinf.tyam.R;
import mx.uv.fiee.iinfo.lastfmtracker.Models.BaseArtistsObject;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopArtistsFragment extends Fragment {
    Retrofit retrofit;
    LastFMService service;
    RecyclerView rvTopArtists;

    Callback<BaseArtistsObject> cbHandler = new Callback<BaseArtistsObject>() {
        @Override
        public void onResponse (Call<BaseArtistsObject> call, Response<BaseArtistsObject> response) {
            if (!response.isSuccessful ()) return;

            BaseArtistsObject boo = response.body ();
            if (boo == null || boo.artists == null || boo.artists.artist == null) return;

            rvTopArtists.setLayoutManager (new LinearLayoutManager(getContext ()));
            rvTopArtists.setAdapter (new TopArtistsAdapter (getContext (), boo));
        }

        @Override
        public void onFailure (Call<BaseArtistsObject> call, Throwable t) {
            Log.e ("TYAM", t.getMessage ());
        }
    };

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_topartists, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = new Retrofit.Builder ()
                .baseUrl (ApiContants.API_BASE_URL)
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();

        service = retrofit.create (LastFMService.class);

        rvTopArtists = view.findViewById (R.id.rvTopArtists);
    }

    @Override
    public void onResume () {
        super.onResume ();

        Call<BaseArtistsObject> foo = service.chartGetTopArtists (ApiContants.API_KEY, ApiContants.API_REQUEST_FORMAT, null);
        foo.enqueue (cbHandler);
    }
}

class TopArtistsAdapter extends RecyclerView.Adapter<TopArtistsViewHolder> {
    private final BaseArtistsObject baseObject;
    private final Context context;

    TopArtistsAdapter (Context context, BaseArtistsObject baseObject) {
        this.baseObject = baseObject;
        this.context = context;
    }

    @NonNull
    @Override
    public TopArtistsViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (context).inflate (R.layout.item_top_artist, viewGroup, false);
        return new TopArtistsViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull TopArtistsViewHolder topArtistsViewHolder, int i) {
        String name = baseObject.artists.artist.get (i).name;
        String playcount = baseObject.artists.artist.get (i).playcount;
        String imgUri = baseObject.artists.artist.get (i).image.get (ApiContants.ARTIST_IMAGE_INDEX).text;
        topArtistsViewHolder.bind (name, playcount, imgUri);
    }

    @Override
    public int getItemCount () {
        return baseObject.artists.artist.size ();
    }
}

class TopArtistsViewHolder extends RecyclerView.ViewHolder {
    private final TextView artist_name;
    private final TextView artist_playcount;
    private final ImageView artist_img;

    TopArtistsViewHolder (@NonNull View itemView) {
        super (itemView);

        artist_name = itemView.findViewById (R.id.artist_name);
        artist_playcount = itemView.findViewById (R.id.artist_playcount);
        artist_img = itemView.findViewById (R.id.artist_img);
    }

    void bind (String name, String listeners, String imgUri) {
        artist_name.setText (name);
        artist_playcount.setText (listeners);

        if (imgUri.startsWith ("http://")) {
            imgUri = imgUri.replaceFirst ("http://", "https://");
        }

        Log.i ("TYAM",  imgUri);
        Picasso.get ()
                .load (imgUri)
                .placeholder (R.drawable.artist_placeholder)
                .into (artist_img);
    }
}