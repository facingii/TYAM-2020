package mx.uv.fiee.iinf.tyam.swinfoapp.interfaces;

import mx.uv.fiee.iinf.tyam.swinfoapp.models.PeopleHeader;
import mx.uv.fiee.iinf.tyam.swinfoapp.models.PlanetHeader;
import mx.uv.fiee.iinf.tyam.swinfoapp.models.VehicleHeader;
import mx.uv.fiee.iinf.tyam.swinfoapp.Utils;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SWApiService {

    @GET(Utils.API_PEOPLE)
    Call<PeopleHeader> getPeople ();

    @GET(Utils.API_PLANETS)
    Call<PlanetHeader> getPlanets ();

    @GET(Utils.API_VEHICLES)
    Call<VehicleHeader> getVehicles ();

}
