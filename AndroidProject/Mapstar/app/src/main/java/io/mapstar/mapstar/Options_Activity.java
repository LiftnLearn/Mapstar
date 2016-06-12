package io.mapstar.mapstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Options_Activity extends AppCompatActivity {

    EditText moneyTxt;
    EditText timeTxt;

    Double longitude;
    Double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Preferences");
        setContentView(R.layout.activity_options_);

        Intent inputIntent = getIntent();
        longitude = inputIntent.getDoubleExtra("longitude", 0);   //0 returned if no longitude found
        latitude = inputIntent.getDoubleExtra("latitude", 0);

         String[] categories = {"Food","Nightlife", "Arts & Entertainment","Landmarks"};
         final Spinner spinner = (Spinner) findViewById(R.id.spinner);
         ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, categories);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);


         moneyTxt = (EditText) findViewById(R.id.money_editText);
         timeTxt = (EditText) findViewById(R.id.time_editText);


        Button goBtn = (Button) findViewById(R.id.spruch_button);
        goBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {                                                                 //start ListView Activity with Parameters if you click the Button

                if (!moneyTxt.getText().toString().equals("") && !timeTxt.getText().toString().equals("")) {
                     String category = (String) spinner.getSelectedItem();
                     int money = Integer.parseInt(moneyTxt.getText().toString());
                     int time = Integer.parseInt(timeTxt.getText().toString());
                     Intent i = new Intent(getApplicationContext(), SightsActivity.class);
                     i.putExtra("category", category);
                     i.putExtra("money", money);
                     i.putExtra("time", time);
                     i.putExtra("longitude", longitude);
                     i.putExtra("latitude", latitude);
                     startActivity(i);
            }
                else{
                    Log.d("bug","eingabe null ");
                }
            }
        });



    }
}
