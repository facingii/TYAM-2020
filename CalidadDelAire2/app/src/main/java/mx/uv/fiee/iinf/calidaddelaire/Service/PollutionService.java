package mx.uv.fiee.iinf.calidaddelaire.Service;

import mx.uv.fiee.iinf.calidaddelaire.Models.ApiRoot;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PollutionService {

    @GET ("calidadAire")
    Call<ApiRoot> getPollutionMeasurements ();

}
