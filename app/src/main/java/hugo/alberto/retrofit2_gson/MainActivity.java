package hugo.alberto.retrofit2_gson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import hugo.alberto.retrofit2_gson.models.Courses;
import hugo.alberto.retrofit2_gson.models.Instructor;
import hugo.alberto.retrofit2_gson.models.UdacityCatalog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "retrofit2_gson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UdacityService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UdacityService service = retrofit.create(UdacityService.class);
        Call<UdacityCatalog> requestCatalog = service.listCatalog();

        requestCatalog.enqueue(new Callback<UdacityCatalog>() {
            @Override
            public void onResponse(Call<UdacityCatalog> call, Response<UdacityCatalog> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "Error" + response.code());
                } else {
                    UdacityCatalog catalog = response.body();
                    for (Courses c : catalog.courses) {
                        Log.i(TAG, String.format("%s: %s", c.title, c.subtitle));
                        for (Instructor i : c.instructors) {
                            Log.i(TAG, i.name);
                        }
                        Log.i(TAG, "-------------");
                    }
                }
            }

            @Override
            public void onFailure(Call<UdacityCatalog> call, Throwable t) {
                Log.e(TAG, "Error" + t.getMessage());
            }
        });

    }
}
