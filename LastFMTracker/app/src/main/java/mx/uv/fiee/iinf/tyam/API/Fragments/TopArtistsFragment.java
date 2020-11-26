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

/**
 * Esta clase heredad de Fragment de modo que la interfaz de usuario pueda ser
 * creada y cargada modularmente.
 *
 * Implementa la lógica necesaria para interactuar con el conjunto de resultados definido
 * por el método chart.TopArtists del API.
 */
public class TopArtistsFragment extends Fragment {
    Retrofit retrofit; // referencia al objeto retrofit
    LastFMService service; // mantiene la referencia al servicio definido en la interfaz
    RecyclerView rvTopArtists;

    /**
     * Manejador del evento Callback invocado por la librería retrofit
     * cuando la solicitud al método chart.TopArtists regresa, ya sea
     * con un conjunto de resultados o un error en la solicitud.
     */
    Callback<BaseArtistsObject> cbHandler = new Callback<BaseArtistsObject>() {
        @Override
        public void onResponse (Call<BaseArtistsObject> call, Response<BaseArtistsObject> response) {
            if (!response.isSuccessful ()) return; // si la respuesta está vacía, sale del método

            // el método body contiene la respuesta de la solicitud ya convertida
            // en objetos Java.
            BaseArtistsObject boo = response.body ();
            if (boo == null || boo.artists == null || boo.artists.artist == null) return;

            // crea al layout y asigna el adaptador correspondiente al componenten recyclerview
            rvTopArtists.setLayoutManager (new LinearLayoutManager(getContext ()));
            rvTopArtists.setAdapter (new TopArtistsAdapter (getContext (), boo));
        }

        @Override
        public void onFailure (Call<BaseArtistsObject> call, Throwable t) {
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
        return inflater.inflate (R.layout.fragment_topartists, container, false);
    }

    /**
     * Una vez creada la interfaz, se invoca al método chart.TopArtists utilizando
     * la librería retrofit.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // obtiene una instancia de la clase retrofit, tomando como parámetros: la URL base del
        // servicio y el objeto convertidor que formateará los resultados en clase Java.
        retrofit = new Retrofit.Builder ()
                .baseUrl (ApiContants.API_BASE_URL)
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();

        // mediante el objeto retrofit, se crea al servicio que define los métodos disponibles
        service = retrofit.create (LastFMService.class);

        rvTopArtists = view.findViewById (R.id.rvTopArtists);
    }

    @Override
    public void onResume () {
        super.onResume ();

        // cuando el fragmento es visible al usuario, se invoca al método chart.TopArtists
        // y obtiene sus resultados
        Call<BaseArtistsObject> foo = service.chartGetTopArtists (ApiContants.API_KEY, ApiContants.API_REQUEST_FORMAT, null);
        foo.enqueue (cbHandler); // indicando qué objeto manejará los datos obtenidos
    }
}

/**
 * Implementa el adaptador utilizado por el RecyclerView para dibujar la lista.
 * La clase recorre todos los elementos incluido en el conjunto de resultados y
 * los coloca en la vista definida para cada vista.
 */
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
        // para cada objeto Artist dentro del objeto obtenido del servicio, se extrae el campo requerido
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

/**
 * Implementa la objeto ViewHolder para la vista definida en cada renglón de la lista.
 */
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

    /**
     * Asígna los valores obtenidos a las vistas
     *
     * @param name nombre del artista
     * @param listeners número de reproducciones totales
     * @param imgUri url de la imagen de portada
     */
    void bind (String name, String listeners, String imgUri) {
        artist_name.setText (name);
        artist_playcount.setText (listeners);

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