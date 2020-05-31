package org.boyoot.app.ui.googleSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.JobAdded;
import org.boyoot.app.model.MapConfig;
import org.boyoot.app.model.Work;
import org.boyoot.app.ui.contact.ContactActivity;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.boyoot.app.utilities.CityUtility.getCityCode;
import static org.boyoot.app.utilities.CityUtility.getInterval;

public class UpdateContactDialog extends AppCompatActivity {


    private UpdateContactViewModel viewModel;

    private long count =0;
    private String year;
    Calendar calendar;
    Map<String,Object> map;
    private Intent contactActivityIntent;
    private FirebaseUser currentUser;

    private static final String contactIdKey = "contactId";
    private static final String REQUEST_DATA_KEY="requestKey";
    private static final String CONTACTS_PATH = "contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_contact_dialog);
        setFinishOnTouchOutside(false);
        getWindow().setBackgroundDrawable(null);
        viewModel = new ViewModelProvider(this).get(UpdateContactViewModel.class);
        map = new HashMap<>();
        calendar = Calendar.getInstance();
        int calenderYear = calendar.get(Calendar.YEAR);
        year = String.valueOf(calenderYear).substring(2);

        contactActivityIntent = new Intent(this, ContactActivity.class);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if (getIntent().hasExtra(REQUEST_DATA_KEY)){
            if (isNetworkAvailable()){
                //TODO
                GoogleSheet request = (GoogleSheet) getIntent().getSerializableExtra(REQUEST_DATA_KEY);
                checkIfContactExist(request);
                Log.i("TEST",request.getPhone());

            }else {
                Toast.makeText(this, "check your connection", Toast.LENGTH_LONG).show();
                finish();
            }
        }else {
            finish();
        }
    }

    private void checkIfContactExist(@NotNull GoogleSheet request){
        String phone =request.getPhone();
        FirebaseFirestore dbRoot = FirebaseFirestore.getInstance();
        CollectionReference yourCollRef = dbRoot.collection(CONTACTS_PATH);
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
                        updateContact(contactId,request);
                        contactActivityIntent.putExtra(contactIdKey, contactId);
                        startActivity(contactActivityIntent);
                        finish();
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(),"check again",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void getContactId(GoogleSheet sheet,boolean increase){
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
    }


    private void pushContactToCloud(GoogleSheet googleSheet,String contactId) {
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
        MapConfig mapConfig = new MapConfig(null,null,null,null,null,false);
        JobAdded jobAdded = new JobAdded(null,false,null);
        if (locationCode == null) {
            City cityWithoutCode = new City(city, getCityCode(city), null, lat, null);
            priority = "1";
            contact = new Contact(contactId, phone, Timestamp.now(), registrationDate, priority, note, work, cityWithoutCode,mapConfig,jobAdded);
        } else {
            City cityWithCode= new City(city, getCityCode(city), locationCode, lat, null);
            priority = "3";
            contact = new Contact(contactId, phone, Timestamp.now(), registrationDate, priority, note, work, cityWithCode, mapConfig,jobAdded);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH)
                .add(contact)
                .addOnSuccessListener(documentReference -> {
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
                    startActivity(contactActivityIntent);
                    finish();
                });

    }


    private void updateLocationCode(String phone,String contactId,String locationCode,String user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority","3",
                        "auth",user,
                        "city.locationCode" , locationCode)
                .addOnSuccessListener(aVoid -> viewModel.updateCloudId(phone, "3"));

    }

    private void updateContact(String contactId,GoogleSheet request){
        boolean offer = false;
        if (request.getOffers().equals("نعم")){
            offer = true;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update(
                        "registrationDate",request.getTimeStamp(),
                        "note",request.getNote(),
                        "work.interval",getInterval(request.getDate()),
                        "work.split", request.getSplit(),
                        "work.window",request.getWindow(),
                        "work.stand",request.getStand(),
                        "work.cover",request.getCover(),
                        "work.concealed",request.getConcealed(),
                        "work.offer",offer)
                .addOnSuccessListener(aVoid -> {

                });
    }

    private String getContactCode(String y , String c,long n){
        return y+getCityCode(c)+n;
    }

    private void author(String contactId,String user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update("auth",user).addOnSuccessListener(aVoid -> {

        });
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
