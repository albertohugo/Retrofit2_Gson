package hugo.alberto.retrofit2_gson;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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
    private ListView listView;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private UdacityCatalog catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        listItems = new ArrayList<>();


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
                    catalog = response.body();
                    for (Courses c : catalog.courses) {
                        Log.i(TAG, String.format("%s: %s", c.title, c.subtitle));
                        listItems.add(c.title);

                        for (Instructor i : c.instructors) {
                            Log.i(TAG, i.name);
                        }
                        Log.i(TAG, "-------------");
                    }
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textviewlist, listItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view,
                                            int posicao, long id) {
                        TextView v = (TextView) view.findViewById(R.id.tv);
                        String itemStr = (String) v.getText();

                        Snackbar.make(view, itemStr, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();


                     }
                });

            }

            @Override
            public void onFailure(Call<UdacityCatalog> call, Throwable t) {
                Log.e(TAG, "Error" + t.getMessage());
            }
        });

    }


}
