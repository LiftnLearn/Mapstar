package io.mapstar.mapstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Options_Activity extends AppCompatActivity {

    EditText moneyTxt;
    EditText timeTxt;

    float longitude;
    float latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_);


        setTitle("Preferences");
        setContentView(R.layout.activity_options_);

        Intent inputIntent = getIntent();
        longitude = inputIntent.getFloatExtra("longitude",0);   //0 returned if no longitude found
        latitude = inputIntent.getFloatExtra("latitude",0);


         moneyTxt = (EditText) findViewById(R.id.money_editText);
         timeTxt = (EditText) findViewById(R.id.time_editText);

        Button goBtn = (Button) findViewById(R.id.spruch_button);
        goBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {                                                                 //start ListView Activity with Parameters if you click the Button

                if (!moneyTxt.getText().toString().equals("") && !timeTxt.getText().toString().equals("")) {

                     int money = Integer.parseInt(moneyTxt.getText().toString());
                     int time = Integer.parseInt(timeTxt.getText().toString());
                     Intent i = new Intent(getApplicationContext(), SightsActivity.class);
                     i.putExtra("money", money);
                     i.putExtra("time", time);
                     i.putExtra("longitude", longitude);
                     i.putExtra("latitude", longitude);
                     startActivity(i);
            }
                else{
                    Log.d("bug","eingabe null ");
                }
            }
        });



    }
}
