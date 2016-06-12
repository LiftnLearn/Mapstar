package io.mapstar.mapstar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SightsActivity extends Activity {

    Double longitude;
    Double latitude;
    int money;
    int time;
    String category;
    SearchResponse searchResponse;
    ArrayList<Business> sightslist = new ArrayList<>(4);

    String[] values = new String[20];
    String[] image = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sights);

        ListView list;

       // setTitle("Sights");
        for(int i = 0; i < values.length; ++i) { values[i] = "";}

        Intent inputIntent = getIntent();
        longitude = inputIntent.getDoubleExtra("longitude", 0);   //0 returned if no longitude found
        latitude = inputIntent.getDoubleExtra("latitude", 0);
        money=inputIntent.getIntExtra("money",0);
        time=inputIntent.getIntExtra("time",15);
        category = inputIntent.getStringExtra("category");

        //Get Yelp reviews at hardcoded location
        YelpAPIFactory apiFactory = new YelpAPIFactory(
                
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap();

        //filter time using hashmap/distance
        CoordinateOptions coordinateOptions = new CoordinateOptions() {
            @Override
            public Double latitude() {
                return latitude;
            }

            @Override
            public Double longitude() {
                return longitude;
            }

            @Override
            public Double accuracy() {
                return 10.0;
            }

            @Override
            public Double altitude() {
                return 0.0;
            }

            @Override
            public Double altitudeAccuracy() {
                return 10.0;
            }
        };
        Call<SearchResponse> call = yelpAPI.search(coordinateOptions, params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                searchResponse = response.body();
                // Update UI text with the searchResponse.
                System.out.println("Yelp answered." + searchResponse.businesses().size());

                //values = [searchResponse.businesses().size()];
                for(int i = 0; i < searchResponse.businesses().size(); ++i) {
                    values[i] = searchResponse.businesses().get(i).name();
                    image[i] = searchResponse.businesses().get(i).snippetImageUrl();
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                System.err.println("Yelp failed.");
                t.printStackTrace();
                t.getMessage();
                t.toString();
                System.out.println(call.toString());
            }
        };

        try {
            call.enqueue(callback);
        } catch(Exception e) {
            System.err.println("Call to Yelp failed.");
            e.printStackTrace();
        }

        CustomList adapter = new
                CustomList(SightsActivity.this, values, image);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(SightsActivity.this, "You're going to visit " +values[+ position], Toast.LENGTH_SHORT).show();
                sightslist.add(searchResponse.businesses().get(position));
            }
        });

        Button sightsBtn = (Button) findViewById(R.id.sightBtn);
        sightsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getApplicationContext(), MapsActivity2.class);
                i.putExtra("sights", (Serializable) sightslist);
                i.putExtra("longitude",longitude);
                i.putExtra("latitude",latitude);
                startActivity(i);
            }
        });
    }
}
