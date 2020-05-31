package org.boyoot.app.ui.googleSheet;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.model.MapConfig;
import org.boyoot.app.model.Work;
import org.boyoot.app.ui.contact.ContactActivity;
import org.boyoot.app.ui.contact.EditContactActivity;
import org.boyoot.app.utilities.PhoneUtility;
import org.jetbrains.annotations.NotNull;

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
import static org.boyoot.app.utilities.CityUtility.getInterval;

public class GoogleSheetActivity extends AppCompatActivity implements GoogleSheetListAdapter.ListItemOnClickListener {

    List<GoogleSheet> data;
    List<GoogleSheetModel> apiData;
    private GoogleSheetViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView searchView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    private boolean isCleaning = true;
   /* FirebaseFirestore db;
    FirebaseFirestore dbRoot;
    private long count =0;
    private String year;
    Calendar calendar;
    Map<String,Object> map;
    private static final String contactIdKey = "contactId";
    private Intent contactActivityIntent;
    private FirebaseUser currentUser;*/
   private GoogleSheetListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sheet);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_sheet);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        progressBar = findViewById(R.id.check_out_progress);
        progressBar.setVisibility(View.INVISIBLE);
        searchView = findViewById(R.id.search_view_bar);
        Toolbar toolbar = findViewById(R.id.google_sheet_toolbar);
        frameLayout = findViewById(R.id.fragment_container_search);
        fab = findViewById(R.id.create_contact_fab);
        swipeRefreshLayout.setRefreshing(true);
        apiData = new ArrayList<>();
        //db = FirebaseFirestore.getInstance();
        //dbRoot = FirebaseFirestore.getInstance();
       /* map = new HashMap<>();
        calendar = Calendar.getInstance();
        int calenderYear = calendar.get(Calendar.YEAR);
        year = String.valueOf(calenderYear).substring(2);*/
       //TODO clear this
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //contactActivityIntent = new Intent(this,ContactActivity.class);
        recyclerView = findViewById(R.id.google_sheet_recycler);
        adapter = new GoogleSheetListAdapter(getApplicationContext(),this);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);
        viewModel.sync();
        viewModel.getContacts().observe(this, googleSheets -> {
            adapter.setDataList(googleSheets);
            data = googleSheets;

            if (isCleaning){
                cleanUpContacts(googleSheets);
            }
            swipeRefreshLayout.setRefreshing(false);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.sync());
        toolbar.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),GoogleSearchActivity.class)));
        fab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),EditContactActivity.class)));
        viewModel.getMainContacts().observe(this, new Observer<List<GoogleSheet>>() {
            @Override
            public void onChanged(List<GoogleSheet> googleSheets) {
                //viewModel.setContactList(googleSheets);
                //viewModel.syncContacts();

            }
        });

        //viewModel.filterContacts().observe(this, googleSheets -> viewModel.setFilterContactList(googleSheets));
       // FirebaseAuth auth = FirebaseAuth.getInstance();
       // currentUser = auth.getCurrentUser();


    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
       if (data.size() > 0) {
           //progressBar.setVisibility(View.VISIBLE);
           if (isNetworkAvailable()) {
               //adapter.isClicked = true;
              // checkIfContactExist(data.get(clickedItemIndex));
               Intent i = new Intent(getApplicationContext(),UpdateContactDialog.class);
               i.putExtra("requestKey",data.get(clickedItemIndex));
               startActivity(i);
           } else {
               //progressBar.setVisibility(View.INVISIBLE);
               Toast.makeText(this, "check your connection", Toast.LENGTH_LONG).show();
           }
       }else {
           swipeRefreshLayout.setRefreshing(true);
       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.google_sheet_filter, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter_sheet) {
           // viewModel.getContacts().removeObservers(this);
            //viewModel.getFilterContact();

            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

   /* private void checkIfContactExist(@NotNull GoogleSheet request){
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
                        contact = document.toObject(Contact.class);
                        contactId = document.getId();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    viewModel.updateContactId(phone,contact.getId());
                    if (request.getPlusCode() != null) {
                        //TODO
                        if (contact.getCity().getLocationCode() == null) {
                            updateLocationCode(request.getPhone(),contactId,request.getPlusCode(),currentUser.getEmail());
                        }else{
                            viewModel.updateCloudId(request.getPhone(),"3");
                        }
                    }else{
                        viewModel.updateLocationCode(phone,request.getPlusCode(),"1");
                    }
                    if (contactId != null) {
                        updateContact(contactId,request.getTimeStamp(),request.getSplit(),request.getWindow(),request.getStand(),request.getCover(),request.getConcealed(),request.getOffers());
                        contactActivityIntent.putExtra(contactIdKey, contactId);
                        adapter.isClicked = false;
                        startActivity(contactActivityIntent);
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(),"check again",Toast.LENGTH_LONG).show();
            }
        });

    }*/


    void cleanUpContacts(@NonNull List<GoogleSheet> db){
        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {
                apiData = cleanUpApiList(response.body());
                isCleaning = false;
                boolean found = false;
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
/*
    public void getContactId(GoogleSheet sheet,boolean increase){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("config").document("counter");
        doc.get().addOnCompleteListener(task -> {
            if (!increase) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {
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
    }*/
   /*public void pushContactToCloud(GoogleSheet googleSheet,String contactId) {
       Contact contact;
       String priority;
       String phone = googleSheet.getPhone();
       String registrationDate = googleSheet.getTimeStamp();
       String date = googleSheet.getDate();
       String window = googleSheet.getWindow();
       String split = googleSheet.getSplit();
       String cover = googleSheet.getCover();
       String stand = googleSheet.getStand();
       String concealed = googleSheet.getConcealed();
       String note = googleSheet.getNote();
       String city = googleSheet.getCity();
       String locationCode = googleSheet.getPlusCode();
       String offers = googleSheet.getOffers();
       boolean offer = false;
       String lat = googleSheet.getLat();
       if (offers.equals("نعم")){
           offer = true;
       }
       Work work = new Work(getInterval(date), split, window, cover, stand,concealed, offer,null);
       if (locationCode == null) {
           City cityWithoutCode = new City(city, getCityCode(city), null, lat, null);
           priority = "1";
           contact = new Contact(contactId, phone, Timestamp.now(), registrationDate, priority, note, work, cityWithoutCode,new MapConfig(null,null,null,null,null,false));
       } else {
           City cityWithCode= new City(city, getCityCode(city), locationCode, lat, null);
           priority = "3";
           contact = new Contact(contactId, phone, Timestamp.now(), registrationDate, priority, note, work, cityWithCode,new MapConfig(null,null,null,null,null,false));
       }
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("contacts")
               .add(contact)
               .addOnSuccessListener(documentReference -> {
                   progressBar.setVisibility(View.INVISIBLE);
                   // editContactActivityIntent.putExtra("contact",contact);
                   viewModel.updateLocationCode(phone, locationCode, priority);
                   viewModel.updateContactId(phone, contactId);
                   if (priority.equals("3")) {
                       viewModel.updateCloudId(phone, "3");
                   }
                   getContactId(null, true);
                   Log.i("pushData",documentReference.getId());
                   String contactCloudId = documentReference.getId();
                   author(contactCloudId,currentUser.getEmail());
                   contactActivityIntent.putExtra(contactIdKey,contactCloudId);
                   adapter.isClicked = false;
                   startActivity(contactActivityIntent);
               });

    }*/


    //TODO modify UPDATE
   /* public void updateLocationCode(String phone,String contactId,String locationCode,String user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("contacts").document(contactId)
               .update("priority","3",
                       "auth",user,
                       "city.locationCode" , locationCode)
               .addOnSuccessListener(aVoid -> viewModel.updateCloudId(phone, "3"));

    }

    void updateContact(String contactId,String registrationDate,String split,String window,String stand,String cover,String concealed,String offers){
        boolean offer = false;
        if (offers.equals("نعم")){
            offer = true;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contacts").document(contactId)
                .update(
                        "registrationDate",registrationDate,
                        "work.split", split,
                        "work.window",window,
                        "work.stand",stand,
                        "work.cover",cover,
                        "work.concealed",concealed,
                        "work.offer",offer)
                .addOnSuccessListener(aVoid -> {

                });
    }
    String getContactCode(String y , String c,long n){
        return y+getCityCode(c)+n;
    }

    private void author(String contactId,String user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contacts").document(contactId)
                .update("auth",user).addOnSuccessListener(aVoid -> {

                });
    }*/
}
