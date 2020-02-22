package org.boyoot.app.ui.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    GoogleSheet mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        boolean isTherData = getIntent().hasExtra("contact");
        if (isTherData){
            mData = (GoogleSheet) getIntent().getSerializableExtra("contact");
        }else{
            //finish();
        }



    }
}
