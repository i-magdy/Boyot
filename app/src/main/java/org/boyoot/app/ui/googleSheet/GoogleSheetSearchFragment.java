package org.boyoot.app.ui.googleSheet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.model.Work;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.boyoot.app.utilities.CityUtility.getCityCode;
import static org.boyoot.app.utilities.CityUtility.getInterval;
import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;

public class GoogleSheetSearchFragment extends Fragment implements CardView.OnClickListener{

    private SearchViewModel viewModel;
    private GoogleSheet contact;
    private ProgressBar progressBar;
    FirebaseFirestore db;
    FirebaseFirestore dbRoot;
    private long count =0;
    private String year;
    Calendar calendar;
    private boolean searching = false;
    Map<String,Object> map;
    private static final String contactIdKey = "contactId";

    private Intent contactActivityIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new SearchViewModel(Objects.requireNonNull(getActivity()).getApplication());
        //viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        dbRoot = FirebaseFirestore.getInstance();
        map = new HashMap<>();
        calendar = Calendar.getInstance();
        contactActivityIntent = new Intent(getContext(), ContactActivity.class);
        int calenderYear = calendar.get(Calendar.YEAR);
        year = String.valueOf(calenderYear).substring(2);
        View root = inflater.inflate(R.layout.fragment_google_search,container,false);
        SearchView search = root.findViewById(R.id.search_view_bar);
        CardView card = root.findViewById(R.id.card_item);
        card.setVisibility(View.GONE);
        final TextView dateTv = root.findViewById(R.id.contact_tag_view);
        final TextView phoneTv = root.findViewById(R.id.textView);
        final TextView idTv = root.findViewById(R.id.contact_id_tv);
        final TextView cityTv = root.findViewById(R.id.location_tv);
        final TextView intervalTv = root.findViewById(R.id.date_tv);
        final ImageView locationIv = root.findViewById(R.id.location_label_iv);
        progressBar = root.findViewById(R.id.check_out_progress);
        progressBar.setVisibility(View.GONE);
        search.setFocusable(true);
        search.setIconified(false);
        search.requestFocusFromTouch();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.getContact(getValidPhoneNumber(query)).observe(getViewLifecycleOwner(), new Observer<GoogleSheet>() {
                    @Override
                    public void onChanged(GoogleSheet googleSheet) {
                        if (googleSheet != null){
                            contact = googleSheet;
                            card.setVisibility(View.VISIBLE);
                            Log.i("searchFragment",googleSheet.getCity());
                            phoneTv.setText(googleSheet.getPhone());
                            dateTv.setText(googleSheet.getTimeStamp());
                            idTv.setText(googleSheet.getContactId());
                            cityTv.setText(googleSheet.getCity());
                            if (TextUtils.equals(googleSheet.getDate(),"الفترة الأولى من :10 صباحاً إلى: 2 ظهراً")){
                                intervalTv.setText("صباحاً");
                            }else {
                                intervalTv.setText("مساءً");
                            }


                        }else {
                            card.setVisibility(View.GONE);
                        }
                    }
                });
                return false;


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getContact(getValidPhoneNumber(newText)).observe(getViewLifecycleOwner(), new Observer<GoogleSheet>() {
                    @Override
                    public void onChanged(GoogleSheet googleSheet) {

                       if (googleSheet != null){
                            contact = googleSheet;
                            card.setVisibility(View.VISIBLE);
                            Log.i("searchFragment",googleSheet.getCity());
                            phoneTv.setText(googleSheet.getPhone());
                            dateTv.setText(googleSheet.getTimeStamp());
                            idTv.setText(googleSheet.getContactId());
                            cityTv.setText(googleSheet.getCity());
                            if (TextUtils.equals(googleSheet.getDate(),"الفترة الأولى من :10 صباحاً إلى: 2 ظهراً")){
                                intervalTv.setText("صباحاً");
                            }else {
                                intervalTv.setText("مساءً");
                            }
                        }else {
                            card.setVisibility(View.GONE);
                        }
                    }
                });
                return false;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Objects.requireNonNull(getActivity()).finish();
                return false;
            }
        });


        card.setOnClickListener(this);


        return root;
    }


    @Override
    public void onClick(View v) {
        Log.i("card","Clicked");
        if (isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            checkIfContactExist(contact);
        }
    }

    private void checkIfContactExist(GoogleSheet request){
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
                    if (request.getLocationLink() != null) {

                        if (contact.getCity().getLocationLink() == null) {
                            updateLocationLink(contactId, request.getLocationLink());
                        }
                    }
                    if (contactId != null) {
                        contactActivityIntent.putExtra(contactIdKey, contactId);
                        startActivity(contactActivityIntent);
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
            }else{
                Toast.makeText(getContext(),"check again",Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getContactId(GoogleSheet sheet,boolean increase){
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
        String note = googleSheet.getNote();
        String city = googleSheet.getCity();
        String locationLink = googleSheet.getLocationLink();
        String offers = googleSheet.getOffers();
        Work work = new Work(getInterval(date), split, window, cover, stand, offers);
        if (locationLink == null) {
            City cityWithoutLink = new City(city, getCityCode(city), null, null, null, null);
            priority = "1";
            contact = new Contact(contactId, phone, Timestamp.now(), registrationDate, priority, note, work, cityWithoutLink);
        } else {
            City cityWithLink = new City(city, getCityCode(city), locationLink, null, null, null);
            priority = "3";
            contact = new Contact(contactId, phone, Timestamp.now(), registrationDate, priority, note, work, cityWithLink);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contacts")
                .add(contact)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    // editContactActivityIntent.putExtra("contact",contact);

                    getContactId(null, true);
                    Log.i("pushData",documentReference.getId());
                    String contactCloudId = documentReference.getId();
                    contactActivityIntent.putExtra(contactIdKey,contactCloudId);
                    startActivity(contactActivityIntent);
                    Objects.requireNonNull(getActivity()).finish();
                });

    }

    private void updateLocationLink(String contactId,String link){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contacts").document(contactId)
                .update("priority","3","timeStamp", FieldValue.serverTimestamp(),"city.locationLink" , link )
                .addOnSuccessListener(aVoid -> {

                });

    }

    private String getContactCode(String y , String c,long n){
        return y+getCityCode(c)+n;
    }

}