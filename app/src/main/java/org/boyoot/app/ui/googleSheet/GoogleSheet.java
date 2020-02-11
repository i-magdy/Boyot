package org.boyoot.app.ui.googleSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleSheet extends AppCompatActivity {

    List<GoogleSheetModel> data;

    GoogleSheetViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sheet);

        final Button b = findViewById(R.id.b);
        b.setVisibility(View.GONE);
        data = new ArrayList<>();

        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);
        viewModel.getData().observe(this, new Observer<List<GoogleSheetModel>>() {
            @Override
            public void onChanged(List<GoogleSheetModel> googleSheetModels) {
                data=googleSheetModels;
                Log.i("apiRetroView",data.get(15).getPhone());
                b.setVisibility(View.VISIBLE);
            }
        });

b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (data.size() != 0) {
            for (int i=0;i<data.size();++i){
                GoogleSheetModel d = data.get(i);
                System.out.println(d.getNumber()+" "+d.getPhone()+" "+d.getCode()+" "+d.getCity()+" "+d.getCover()+" "+d.getOffers());
            }

        }

    }
});


    }
}
