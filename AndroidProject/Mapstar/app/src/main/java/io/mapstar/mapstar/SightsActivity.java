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
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SightsActivity extends ListActivity {

    String longitude;
    String latitude;
    int money;
    int time;
    String category;

    //String[] values = null;
    List<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sights);
        setTitle("Sights");
        setContentView(R.layout.activity_options_);

        Intent inputIntent = getIntent();
        longitude = inputIntent.getStringExtra("longitude");   //0 returned if no longitude found
        latitude = inputIntent.getStringExtra("latitude");
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

        params.put("latitude", latitude);
        params.put("longitude", longitude);

        //filter time using hashmap

        Call<SearchResponse> call = yelpAPI.search("Munich", params);


        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                // Update UI text with the searchResponse.
                System.out.println("Yelp answered." + searchResponse.businesses().size());

              //  values = [searchResponse.businesses().size()];
                for(int i = 0; i < searchResponse.businesses().size(); ++i) {
                    values.add(searchResponse.businesses().get(i).name());
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

        String[] valuesArray = new String[values.size()];
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.activity_sights, R.id.label, values.toArray(valuesArray)
        );

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position,
                                   long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, "Somebody clicked something!", Toast.LENGTH_LONG).show();
    }
}
