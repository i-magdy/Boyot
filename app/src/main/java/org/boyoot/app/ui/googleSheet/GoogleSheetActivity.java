package org.boyoot.app.ui.googleSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.boyoot.app.R;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;
import java.util.List;

import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;


public class GoogleSheetActivity extends AppCompatActivity implements GoogleSheetListAdapter.ListItemOnClickListener {

    List<GoogleSheetModel> data;

    GoogleSheetViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sheet);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_sheet);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setRefreshing(true);
        data = new ArrayList<>();
       //TODO clear this

        RecyclerView recyclerView = findViewById(R.id.google_sheet_recycler);
        GoogleSheetListAdapter adapter = new GoogleSheetListAdapter(getApplicationContext(),this);

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);
        viewModel.getData().observe(this, new Observer<List<GoogleSheetModel>>() {
            @Override
            public void onChanged(List<GoogleSheetModel> googleSheetModels) {
                data=googleSheetModels;
                adapter.setDataList(googleSheetModels);
                swipeRefreshLayout.setRefreshing(false);
                Log.i("apiRetroView",data.get(15).getPhone());
              //  b.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getData();
            }
        });

/*b.setOnClickListener(v -> {
    if (data.size() != 0) {
        for (int i=0;i<data.size();++i){
            GoogleSheetModel d = data.get(i);

            System.out.println(d.getNumber()+" "+getValidPhoneNumber(d.getPhone())+" "+d.getCode()+" "+d.getCity()+" "+d.getCover()+" "+d.getOffers());
        }

    }

});*/


    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
       Log.i("click","clicked"+clickedItemIndex);
    }
}
