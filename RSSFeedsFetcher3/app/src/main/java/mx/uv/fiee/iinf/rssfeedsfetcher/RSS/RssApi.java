package mx.uv.fiee.iinf.rssfeedsfetcher.RSS;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RssApi {

    @GET ("lg_image_of_the_day.rss")
    Call<Rss> getAllItems ();

}
