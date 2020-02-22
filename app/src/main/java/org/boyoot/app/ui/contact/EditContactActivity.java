package org.boyoot.app.ui.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.boyoot.app.R;

public class EditContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Spinner spinner = findViewById(R.id.location_spinner);
        Spinner s = findViewById(R.id.date_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.planets_array,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this,R.array.planets_array,android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adap);
        s.setOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(parent.getSelectedItem().toString(),"choose")) {
                    Toast.makeText(getApplicationContext(), "should be value", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }
                parent.setSelection(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Toast.makeText(getApplicationContext(),parent.getSelectedItem().toString(),Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
