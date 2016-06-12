package io.mapstar.mapstar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient client;

    ArrayList<Business> businesses;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient
                .Builder(this)
                .addApi(AppIndex.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        client.connect();

        Intent inputIntent = getIntent();
        businesses = (ArrayList<Business>) inputIntent.getSerializableExtra("sights");
        longitude = inputIntent.getDoubleExtra("longitude", 0);
        latitude = inputIntent.getDoubleExtra("latitude", 0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Karlsruhe and move the camera
        LatLng karlsruhe = new LatLng(49.0068901, 8.4036527);
        mMap.addMarker(new MarkerOptions().position(karlsruhe).title("DHBW Karlsruhe"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(karlsruhe, 12.0f));

        LatLng target = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(target).title("Karlsruhe Hbf"));

        for(Business b : businesses) {
            LatLng temp = new LatLng(b.location().coordinate().latitude(),
                    b.location().coordinate().longitude());
            mMap.addMarker(new MarkerOptions().position(temp).title(b.name()));
        }

        //iterate over all goals and find the shortest one
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(karlsruhe, target)
                .width(5)
                .color(Color.RED));

        List<LatLng> bestPermutation = new ArrayList<LatLng>(businesses.size()+2);

        List<LatLng> curPermutation = new ArrayList<LatLng>(businesses.size()+2);

        //for(int i = 0; i < )
    }
}
