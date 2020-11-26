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

/**
 * Esta clase heredad de Fragment de modo que la interfaz de usuario pueda ser
 * creada y cargada modularmente.
 *
 * Implementa la lógica necesaria para interactuar con el conjunto de resultados definido
 * por el método chart.TopTracks del API.
 */
public class TopTracksFragment extends Fragment {
    Retrofit retrofit;
    LastFMService service;
    RecyclerView rvTopTracks;

    /**
     * Manejador del evento Callback invocado por la librería retrofit
     * cuando la solicitud al método char.TopTracks regresa, ya sea
     * con un conjunto de resultados o un error en la solicitud.
     */
    Callback<BaseTracksObject> cbHandler = new Callback<BaseTracksObject> () {
        @Override
        public void onResponse(Call<BaseTracksObject> call, Response<BaseTracksObject> response) {
            if (!response.isSuccessful ()) return; // si la respuesta está vacía, sale del método

            // el método body contiene la respuesta de la solicitud ya convertida
            // en objetos Java.
            BaseTracksObject boo = response.body ();
            if (boo == null || boo.tracks == null || boo.tracks.track == null) return;

            // crea al layout y asigna el adaptador correspondiente al componenten recyclerview
            rvTopTracks.setLayoutManager (new GridLayoutManager(getContext (), 2));
            rvTopTracks.setAdapter (new TopTracksAdapter (getContext (), boo));
        }

        @Override
        public void onFailure(@NonNull Call<BaseTracksObject> call, Throwable t) {
        }
    };

    /**
     * Crea la vista a partir del recurso indicado.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_toptracks, container, false);
    }

    /**
     * Una vez creada la interfaz, se invoca al método TopTracks utilizando
     * la librería retrofit.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        // obtiene una instancia de la clase retrofit, tomando como parámetros: la URL base del
        // servicio y el objeto convertidor que formateará los resultados en clase Java.
        retrofit = new Retrofit.Builder ()
                .baseUrl (ApiContants.API_BASE_URL)
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();

        // mediante el objeto retrofit, se crea al servicio que define los métodos disponibles
        service = retrofit.create (LastFMService.class);

        rvTopTracks = view.findViewById (R.id.rvTopTracks);
    }

    @Override
    public void onResume () {
        super.onResume ();

        // cuando el fragmento es visible al usuario, se invoca al método TopTracks
        // y obtiene sus resultados
        Call<BaseTracksObject> foo = service.chartGetTopTracks (ApiContants.API_KEY, ApiContants.API_REQUEST_FORMAT, null);
        foo.enqueue (cbHandler);
    }
}

/**
 * Implementa el adaptador utilizado por el RecyclerView para dibujar la lista.
 * La clase recorre todos los elementos incluido en el conjunto de resultados y
 * los coloca en la vista definida para cada vista.
 */
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
        // para cada objeto Track dentro del objeto obtenido del servicio, se extrae el campo requerido
        String songName = baseTracksObject.tracks.track.get (i).name;
        String imgUri = baseTracksObject.tracks.track.get (i).image.get (ApiContants.TRACK_IMAGE_INDEX).text;
        topTracksViewHolder.bind (songName, imgUri);
    }

    @Override
    public int getItemCount() {
        return baseTracksObject.tracks.track.size ();
    }
}

/**
 * Implementa la objeto ViewHolder para la vista definida en cada renglón de la lista.
 */
class TopTracksViewHolder extends RecyclerView.ViewHolder {
    private ImageView artist_img;
    private TextView song_name;

    TopTracksViewHolder(@NonNull View itemView) {
        super (itemView);

        artist_img = itemView.findViewById (R.id.artist_img);
        song_name = itemView.findViewById (R.id.song_name);
    }

    /**
     * Asígna los valores obtenidos a las vistas
     *
     * @param songName nombre del artista
     * @param imgUri url de la imagen de portada
     */
    void bind (String songName, String imgUri) {
        song_name.setText (songName);

        // debido a la restricción que impide utilizar conexiones planas a partir del api 29
        // es necesario reemplazar al protocolo por su versión segura
        if (imgUri.startsWith ("http://")) {
            imgUri = imgUri.replaceFirst ("http://", "https://");
        }

        // mediante la librería Picasso se descarga la imagen utilizada como fondo de las vistas
        Picasso.get ()
                .load (imgUri)
                .placeholder (R.drawable.artist_placeholder)
                .into (artist_img);
    }
}
