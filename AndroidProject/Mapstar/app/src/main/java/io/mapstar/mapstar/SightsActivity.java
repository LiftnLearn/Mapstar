package io.mapstar.mapstar;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SightsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sights);

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
