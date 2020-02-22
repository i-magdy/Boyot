package org.boyoot.app.ui.googleSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.ui.contact.ContactActivity;
import org.boyoot.app.ui.contact.EditContactActivity;
import org.boyoot.app.utilities.PhoneUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static org.boyoot.app.utilities.CityUtility.getCityCode;



public class GoogleSheetActivity extends AppCompatActivity implements GoogleSheetListAdapter.ListItemOnClickListener {

    List<GoogleSheet> data;
    List<GoogleSheetModel> apiData;
    private GoogleSheetViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    FirebaseFirestore db;
    FirebaseFirestore dbRoot;
    private DocumentReference mConfigDocRef;
    private long count =0;
    private String year;
    Calendar calendar;
    Map<String,Object> map;
    private String code;

    private Intent contactActivityIntent;
    private Intent editContactActivityIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sheet);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_sheet);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        progressBar = findViewById(R.id.check_out_progress);
        progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        data = new ArrayList<>();
        apiData = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        dbRoot = FirebaseFirestore.getInstance();
        map = new HashMap<>();
        calendar = Calendar.getInstance();
        int calenderYear = calendar.get(Calendar.YEAR);
        year = String.valueOf(calenderYear).substring(2);
       //TODO clear this

        contactActivityIntent = new Intent(this,ContactActivity.class);
        editContactActivityIntent = new Intent(this,EditContactActivity.class);
        RecyclerView recyclerView = findViewById(R.id.google_sheet_recycler);
        GoogleSheetListAdapter adapter = new GoogleSheetListAdapter(getApplicationContext(),this);

        recyclerView.setAdapter(adapter);

        viewModel = new GoogleSheetViewModel(getApplication());
        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);


        viewModel.sync();

        viewModel.getContacts().observe(this, googleSheets -> {
            adapter.setDataList(googleSheets);
            data = googleSheets;
                cleanUpContacts(googleSheets);
            for (int i =0 ; i <googleSheets.size();i++){
                Log.i("testing",googleSheets.get(i).getPhone()+"  ||  "+googleSheets.get(i).getState());
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.sync());




    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
       Log.i("click","clicked"+clickedItemIndex);
        /*Intent i = new Intent(getApplicationContext(), EditContactActivity.class);
        i.putExtra("contact",data.get(clickedItemIndex));
        startActivity(i);*/
        progressBar.setVisibility(View.VISIBLE);
        checkIfContactExist(data.get(clickedItemIndex));
    }

    void checkIfContactExist(GoogleSheet request){
        String phone =request.getPhone();
        CollectionReference yourCollRef = dbRoot.collection("contacts");
        Query query = yourCollRef.whereEqualTo("phone", phone);
        query.get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                //TODO PUSH
                Log.i("testFirestore",request.getPhone()+" || "+"pushed"+"  ||   "+code);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(editContactActivityIntent);
            } else {
                //TODO UPDATE
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("testFirestore", document.getId() + " => " + document.getData());
                }
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(contactActivityIntent);
            }

        });
    }

    void cleanUpContacts(List<GoogleSheet> db){
        Log.i("whyNotWorking", "called");
        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {
                apiData = cleanUpApiList(response.body());
                boolean found = false;
                if (db != null) {
                    for (int i = 0; i < db.size(); i++) {
                        String phone = db.get(i).getPhone();
                        for (int j = 0; j < apiData.size(); ++j) {
                            if (TextUtils.equals(phone, apiData.get(j).getPhone())) {
                                found = true;
                                break;
                            } else {
                                found = false;
                            }
                        }
                        if (!found) {
                            viewModel.deleteContact(phone);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {

            }
        });
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
