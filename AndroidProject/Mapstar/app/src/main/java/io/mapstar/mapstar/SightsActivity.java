package io.mapstar.mapstar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SightsActivity extends ListActivity {

    Double longitude;
    Double latitude;
    int money;
    int time;
    String category;
    ArrayList<Business> sightslist = new ArrayList<>(4);
    SearchResponse searchResponse;

    //String[] values = null;
    //List<String> values = new ArrayList<String>();
    String[] values = new String[21];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       // setTitle("Sights");
        for(int i = 0; i < values.length; ++i) { values[i] = "";}
        values[values.length-1] = "Click to proceed to Route";

        Intent inputIntent = getIntent();
        longitude = inputIntent.getDoubleExtra("longitude", 0);   //0 returned if no longitude found
        latitude = inputIntent.getDoubleExtra("latitude", 0);
        money=inputIntent.getIntExtra("money",0);
        time=inputIntent.getIntExtra("time",15);
        category = inputIntent.getStringExtra("category");

        //Get Yelp reviews at hardcoded location
        YelpAPIFactory apiFactory = new YelpAPIFactory(
                "BYz7q1kDnFUD5txoqFDitw",
                "Zn8ZbR704jCzRpVjUC174EzlDOA",
                "ly2Ps-VXz2yGAUBMoQMT-iaMYysBp7PL",
                "HUS2-YVkkG5cqzu69wZyuzG4rP4");
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

        //String[] valuesArray = {"hi", "ho"};//new String[20];

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.activity_sights, R.id.label, values
        );

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position,
                                   long id) {
        System.out.println(position);
        if(position == values.length-1) {
            Intent i =  new Intent(getApplicationContext(), MapsActivity.class);
            i.putExtra("param", 2);
            i.putExtra("sights", sightslist);
            startActivity(i);
        } else {
            String item = (String) getListAdapter().getItem(position);
            Toast.makeText(this, "Somebody clicked something!", Toast.LENGTH_LONG).show();
            sightslist.add(searchResponse.businesses().get(position));
        }
    }
}
