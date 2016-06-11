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

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SightsActivity extends ListActivity {

    float longitude;
    float latitude;
    int money;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sights);
        setTitle("Sights");
        setContentView(R.layout.activity_options_);

        Intent inputIntent = getIntent();
        longitude = inputIntent.getFloatExtra("longitude",0);   //0 returned if no longitude found
        latitude = inputIntent.getFloatExtra("latitude",0);
        money=inputIntent.getIntExtra("money",0);
        time=inputIntent.getIntExtra("time",15);

        //Get Yelp reviews at hardcoded location
        YelpAPIFactory apiFactory = new YelpAPIFactory(
                "BYz7q1kDnFUD5txoqFDitw",
                "Zn8ZbR704jCzRpVjUC174EzlDOA",
                "ly2Ps-VXz2yGAUBMoQMT-iaMYysBp7PL",
                "HUS2-YVkkG5cqzu69wZyuzG4rP4");
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap();

        // general params
        params.put("term", "food");
        params.put("limit", "3");

        // locale params
        params.put("lang", "fr");

        Call<SearchResponse> call = yelpAPI.search("Munich", params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                // Update UI text with the searchResponse.
                System.out.println("Yelp answered.");
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

        String[] values = new String[] {"stuff", "more stuff", "more", "moer", "more",
        "more", "more", "different", "don't do this"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.activity_sights, R.id.label, values
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
