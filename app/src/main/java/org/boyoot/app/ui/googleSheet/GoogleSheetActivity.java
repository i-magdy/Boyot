package org.boyoot.app.ui.googleSheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.model.Work;
import org.boyoot.app.ui.contact.ContactActivity;
import org.boyoot.app.ui.contact.EditContactActivity;
import org.boyoot.app.utilities.PhoneUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.boyoot.app.utilities.CityUtility.getCityCode;
import static org.boyoot.app.utilities.CityUtility.getInterval;

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
                Log.i("testing",googleSheets.get(i).getPhone()+"  ||  "+googleSheets.get(i).getState()+"  ||  "+googleSheets.get(i).getLocationLink());
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.sync());


        updateLocationLink("9JsH2CB37L1SGTAK699z",null);
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
       Log.i("click","clicked"+data.get(clickedItemIndex).getLocationLink());
        /*Intent i = new Intent(getApplicationContext(), EditContactActivity.class);
        i.putExtra("contact",data.get(clickedItemIndex));
        startActivity(i);*/
        progressBar.setVisibility(View.VISIBLE);
        if (isNetworkAvailable()) {
            checkIfContactExist(data.get(clickedItemIndex));
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"check your connection",Toast.LENGTH_LONG).show();
        }
    }

    void checkIfContactExist(GoogleSheet request){
        String phone =request.getPhone();

        CollectionReference yourCollRef = dbRoot.collection("contacts");
        Query query = yourCollRef.whereEqualTo("phone", phone);
        query.get().addOnCompleteListener(task -> {
           Contact contact = new Contact();
           String contactId = null;
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                    getContactId(request,false);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("testFirestore", document.getId() + " => " + document.getData()+document.get("timeStamp").toString());
                        contact = document.toObject(Contact.class);
                        contactId = document.getId();

                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    viewModel.updateContactId(phone,contact.getId());
                    if (request.getLocationLink() != null) {

                        viewModel.updateCloudId(request.getPhone(), "3");
                        viewModel.updateLocationLink(phone,request.getLocationLink(),"3");
                        if (contact.getCity().getLocationLink() == null) {
                            updateLocationLink(contactId, request.getLocationLink());
                        }
                    }else{
                        viewModel.updateLocationLink(phone,request.getLocationLink(),"1");
                    }
                    startActivity(contactActivityIntent);
                }
            }else{
                Toast.makeText(getApplicationContext(),"check again",Toast.LENGTH_LONG).show();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void getContactId(GoogleSheet sheet,boolean increase){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("config").document("counter");
        doc.get().addOnCompleteListener(task -> {
            if (!increase) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        count = document.getLong("count");
                        if (count != 0) {
                            pushContactToCloud(sheet, getContactCode(year, sheet.getCity(), count));
                        }
                    }
                }
            }else{
                map.put("count",++count);
                doc.set(map).addOnSuccessListener(aVoid -> { });

            }
        });
    }
   void pushContactToCloud(GoogleSheet googleSheet,String contactId) {
       Contact contact;
       String priority;
       String phone = googleSheet.getPhone();
       String registerationDate = googleSheet.getTimeStamp();
       String date = googleSheet.getDate();
       String window = googleSheet.getWindow();
       String split = googleSheet.getSplit();
       String cover = googleSheet.getCover();
       String stand = googleSheet.getStand();
       String note = googleSheet.getNote();
       String city = googleSheet.getCity();
       String locationLink = googleSheet.getLocationLink();
       String offers = googleSheet.getOffers();
       Work work = new Work(getInterval(date), split, window, cover, stand, offers);
       if (locationLink == null) {
           City cityWithoutLink = new City(city, getCityCode(city), null, null, null, null);
           priority = "1";
           contact = new Contact(contactId, phone, Timestamp.now(), registerationDate, priority, note, work, cityWithoutLink);
       } else {
           City cityWithLink = new City(city, getCityCode(city), locationLink, null, null, null);
           priority = "3";
           contact = new Contact(contactId, phone, Timestamp.now(), registerationDate, priority, note, work, cityWithLink);
       }
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("contacts")
               .add(contact)
               .addOnSuccessListener(documentReference -> {
                   progressBar.setVisibility(View.INVISIBLE);
                   // editContactActivityIntent.putExtra("contact",contact);
                   viewModel.updateLocationLink(phone, locationLink, priority);
                   viewModel.updateContactId(phone, contactId);
                   getContactId(null, true);
                   startActivity(editContactActivityIntent);
               });

    }

    void updateLocationLink(String contactId,String link){

       FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("contacts").document(contactId)
               .update("priority","3","timeStamp",FieldValue.serverTimestamp(),"city.locationLink" , link )
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {

                   }

               });

    }

    String getContactCode(String y , String c,long n){
        return y+getCityCode(c)+n;
    }

}
