package hugo.alberto.retrofit2_gson;

import hugo.alberto.retrofit2_gson.models.UdacityCatalog;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Alberto on 29/05/2016.
 */
public interface UdacityService {

    public static final String BASE_URL= "https://www.udacity.com/public-api/v0/";
    @GET("courses")

    Call<UdacityCatalog> listCatalog();

}
