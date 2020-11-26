package mx.uv.fiee.iinf.tyam.API;

import mx.uv.fiee.iinfo.lastfmtracker.Models.BaseArtistsObject;
import mx.uv.fiee.iinfo.lastfmtracker.Models.BaseTracksObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Define a la interfaz que utilizando la librería RetroFit interactúa con el API
 * establecida en el servicio de Last.fm.
 *
 * Cada uno de los métodos obtiene un conjunto de resultados específico.
 */
public interface LastFMService {
    @GET (ApiContants.TOP_ARTISTS_URL)
    Call<BaseArtistsObject> chartGetTopArtists (@Query ("api_key") String apiKey, @Query ("format") String format, @Query ("page") String page);

    @GET (ApiContants.TOP_TRACKS_URL)
    Call<BaseTracksObject> chartGetTopTracks (@Query ("api_key") String apiKey, @Query ("format") String format, @Query ("page") String page);
}