package org.boyoot.app.ui.googleSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.ui.contact.ContactActivity;
import org.boyoot.app.utilities.PhoneUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class GoogleSheetActivity extends AppCompatActivity implements GoogleSheetListAdapter.ListItemOnClickListener {

    List<GoogleSheet> data;
    List<GoogleSheetModel> apiData;
    private GoogleSheetViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sheet);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_sheet);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setRefreshing(true);
        data = new ArrayList<>();
        apiData = new ArrayList<>();
       //TODO clear this

        RecyclerView recyclerView = findViewById(R.id.google_sheet_recycler);
        GoogleSheetListAdapter adapter = new GoogleSheetListAdapter(getApplicationContext(),this);

        recyclerView.setAdapter(adapter);

        viewModel = new GoogleSheetViewModel(getApplication());

        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);


        viewModel.sync();
        viewModel.getContacts().observe(this, new Observer<List<GoogleSheet>>() {
            @Override
            public void onChanged(List<GoogleSheet> googleSheets) {
                adapter.setDataList(googleSheets);
                data = googleSheets;
                cleanUpContacts(googleSheets);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        /*viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);
        viewModel.getData().observe(this, new Observer<List<GoogleSheetModel>>() {
            @Override
            public void onChanged(List<GoogleSheetModel> googleSheetModels) {
                data=googleSheetModels;
                adapter.setDataList(googleSheetModels);
                swipeRefreshLayout.setRefreshing(false);
                Log.i("apiRetroView",data.get(15).getPhone());
              //  b.setVisibility(View.VISIBLE);
            }
        });*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.sync());

        //viewModel.updateCloudId("506920623","cloud id has been set");





       //cleanUpContacts(data);
/*b.setOnClickListener(v -> {
    if (data.size() != 0) {
        for (int i=0;i<data.size();++i){
            GoogleSheetModel d = data.get(i);

            System.out.println(d.getNumber()+" "+getValidPhoneNumber(d.getPhone())+" "+d.getCode()+" "+d.getCity()+" "+d.getCover()+" "+d.getOffers());
        }

    }

});*/


    }


    void cleanUpContacts(List<GoogleSheet> db){
        Log.i("whyNotWorking", "called");

        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {


                apiData = cleanUpApiList(response.body());

                boolean found = false;

                    for (int i = 0; i < db.size();i++) {
                      String phone = db.get(i).getPhone();
                        for (int j = 0; j <apiData.size();++j){

                            if (TextUtils.equals(phone,apiData.get(j).getPhone())){
                                found = true;
                                break;
                            }else{
                                found = false;
                            }
                        }

                        if (!found){
                            viewModel.deleteContact(phone);
                        }
                    }

                }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
       Log.i("click","clicked"+clickedItemIndex);
        Intent i = new Intent(getApplicationContext(), ContactActivity.class);
        i.putExtra("contact",data.get(clickedItemIndex));
        startActivity(i);
    }

    private List<GoogleSheetModel> cleanUpApiList(List<GoogleSheetModel> list){
        List<GoogleSheetModel> newList = new ArrayList<>();

        if (list != null){
            newList = new ArrayList<>(list.size());

            for (int i = list.size()-1;i >=0;i--){
                if (!TextUtils.equals(PhoneUtility.getValidPhoneNumber(list.get(i).getPhone()),"invalid")){

                    newList.add(list.get(i));



                }
            }
        }

        return newList;

    }
}
