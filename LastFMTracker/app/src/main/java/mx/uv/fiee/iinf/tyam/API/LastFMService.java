package mx.uv.fiee.iinf.tyam.API;

import mx.uv.fiee.iinfo.lastfmtracker.Models.BaseArtistsObject;
import mx.uv.fiee.iinfo.lastfmtracker.Models.BaseTracksObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFMService {
    @GET (ApiContants.TOP_ARTISTS_URL)
    Call<BaseArtistsObject> chartGetTopArtists (@Query ("api_key") String apiKey, @Query ("format") String format, @Query ("page") String page);

    @GET (ApiContants.TOP_TRACKS_URL)
    Call<BaseTracksObject> chartGetTopTracks (@Query ("api_key") String apiKey, @Query ("format") String format, @Query ("page") String page);
}