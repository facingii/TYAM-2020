package mx.uv.fiee.iinf.tyam.API.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mx.uv.fiee.iinf.tyam.API.ApiContants;
import mx.uv.fiee.iinf.tyam.API.LastFMService;
import mx.uv.fiee.iinf.tyam.R;
import mx.uv.fiee.iinfo.lastfmtracker.Models.BaseTracksObject;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopTracksFragment extends Fragment {
    Retrofit retrofit;
    LastFMService service;
    RecyclerView rvTopTracks;

    Callback<BaseTracksObject> cbHandler = new Callback<BaseTracksObject> () {
        @Override
        public void onResponse(Call<BaseTracksObject> call, Response<BaseTracksObject> response) {
            if (!response.isSuccessful ()) return;

            BaseTracksObject boo = response.body ();
            if (boo == null || boo.tracks == null || boo.tracks.track == null) return;

            rvTopTracks.setLayoutManager (new GridLayoutManager(getContext (), 2));
            rvTopTracks.setAdapter (new TopTracksAdapter (getContext (), boo));
        }

        @Override
        public void onFailure(@NonNull Call<BaseTracksObject> call, Throwable t) {
        }
    };


    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_toptracks, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        retrofit = new Retrofit.Builder ()
                .baseUrl (ApiContants.API_BASE_URL)
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();

        service = retrofit.create (LastFMService.class);

        rvTopTracks = view.findViewById (R.id.rvTopTracks);
    }

    @Override
    public void onResume () {
        super.onResume ();

        Call<BaseTracksObject> foo = service.chartGetTopTracks (ApiContants.API_KEY, ApiContants.API_REQUEST_FORMAT, null);
        foo.enqueue (cbHandler);
    }
}

class TopTracksAdapter extends RecyclerView.Adapter<TopTracksViewHolder> {
    private final BaseTracksObject baseTracksObject;
    private final Context context;

    TopTracksAdapter (Context context, BaseTracksObject baseTracksObject) {
        this.baseTracksObject = baseTracksObject;
        this.context = context;
    }

    @NonNull
    @Override
    public TopTracksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (context).inflate (R.layout.item_top_tracks, viewGroup, false);
        return new TopTracksViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopTracksViewHolder topTracksViewHolder, int i) {
        String songName = baseTracksObject.tracks.track.get (i).name;
        String imgUri = baseTracksObject.tracks.track.get (i).image.get (ApiContants.TRACK_IMAGE_INDEX).text;
        topTracksViewHolder.bind (songName, imgUri);
    }

    @Override
    public int getItemCount() {
        return baseTracksObject.tracks.track.size ();
    }
}

class TopTracksViewHolder extends RecyclerView.ViewHolder {
    private ImageView artist_img;
    private TextView song_name;

    TopTracksViewHolder(@NonNull View itemView) {
        super (itemView);

        artist_img = itemView.findViewById (R.id.artist_img);
        song_name = itemView.findViewById (R.id.song_name);
    }

    void bind (String songName, String imgUri) {
        song_name.setText (songName);

        if (imgUri.startsWith ("http://")) {
            imgUri = imgUri.replaceFirst ("http://", "https://");
        }

        Picasso.get ().load (imgUri).placeholder (R.drawable.artist_placeholder).into (artist_img);
    }
}
