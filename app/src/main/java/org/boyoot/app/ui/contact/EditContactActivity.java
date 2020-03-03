package org.boyoot.app.ui.contact;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
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
import org.boyoot.app.model.Work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static org.boyoot.app.utilities.CityUtility.getCityCode;
import static org.boyoot.app.utilities.CityUtility.getInterval;

public class EditContactActivity extends AppCompatActivity {

    private EditContactViewModel viewModel;
    private TextView mContactIdTv;
    private TextInputLayout mPhoneTextLayout;
    private EditText mPhoneEditText;
    private TextInputLayout mLocationTextLayout;
    private EditText mLocationLinkEditText;
    private Spinner mCitySpinner;
    private TextView citySpinnerErrorTv;
    private Spinner mDateSpinner;
    private TextView dateSpinnerErrorTv;
    private EditText mWindowEditText;
    private EditText mSplitEditText;
    private EditText mStandEditText;
    private EditText mCoverEditText;
    private TextView mRegistrationDate;
    private EditText mNoteEditText;
    private ProgressBar progressBar;
    private static final String contactIdKey = "contactId";
    private boolean isContactExist = false;
    private String existContactCloudId;
    private String existCity="";
    private String existInterval;
    ArrayAdapter<CharSequence> dateAdapter;
    ArrayAdapter<CharSequence> cityAdapter;
    private FirebaseFirestore dbRoot;
    private long count =0;
    private String year;
    private String currentDate;
    private Timestamp timestamp;
    Calendar calendar;
    Map<String,Object> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        mContactIdTv = findViewById(R.id.contact_id_tv);
        mCitySpinner = findViewById(R.id.location_spinner);
        citySpinnerErrorTv = findViewById(R.id.city_spinner_error_tv);
        mDateSpinner = findViewById(R.id.date_spinner);
        dateSpinnerErrorTv = findViewById(R.id.date_spinner_error_tv);
        mPhoneTextLayout = findViewById(R.id.phone_number_edit_layout);
        mPhoneEditText = findViewById(R.id.phone_edit_text);
        mLocationTextLayout = findViewById(R.id.location_edit_layout);
        mLocationLinkEditText = findViewById(R.id.location_edit_text);
        mWindowEditText = findViewById(R.id.window_edit_text);
        mSplitEditText = findViewById(R.id.split_edit_text);
        mStandEditText = findViewById(R.id.stand_edit_text);
        mCoverEditText = findViewById(R.id.cover_edit_text);
        mRegistrationDate = findViewById(R.id.registration_date_tv);
        mNoteEditText = findViewById(R.id.note_edit_text);
        progressBar = findViewById(R.id.edit_contact_progress_bar);
        Toolbar toolbar = findViewById(R.id.edit_contact_toolbar);
        setSupportActionBar(toolbar);


        viewModel = new ViewModelProvider(this).get(EditContactViewModel.class);
        if (getIntent().hasExtra(contactIdKey)){
            String contactCloudId =  getIntent().getStringExtra(contactIdKey);
            existContactCloudId = contactCloudId;
            isContactExist = true;
            viewModel.getContactFromCloud(contactCloudId);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
        dbRoot = FirebaseFirestore.getInstance();
        map = new HashMap<>();
        calendar = Calendar.getInstance();
        int calenderYear = calendar.get(Calendar.YEAR);
        year = String.valueOf(calenderYear).substring(2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("en"));
        currentDate = dateFormat.format(calendar.getTime());
        mRegistrationDate.setText(currentDate);
        viewModel.getContact().observe(this, this::fillContactData);
        dateAdapter = ArrayAdapter.createFromResource(this,R.array.date_array,android.R.layout.simple_spinner_dropdown_item);
        mDateSpinner.setAdapter(dateAdapter);
        cityAdapter= ArrayAdapter.createFromResource(this,R.array.cities_array,android.R.layout.simple_spinner_dropdown_item);
        mCitySpinner.setAdapter(cityAdapter);

        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySpinnerErrorTv.setText(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateSpinnerErrorTv.setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(mPhoneTextLayout, null);
                }else{
                    mPhoneEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationLinkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(mLocationTextLayout, null);
                }else{
                    mLocationLinkEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_contact_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_contact) {
            Log.i("settings menu","clicked");
            attemptCreateContact();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
    private void fillContactData(Contact contact){
        mContactIdTv.setText(contact.getId());
        mPhoneEditText.setText(contact.getPhone());
        mLocationLinkEditText.setText(contact.getCity().getLocationLink());
        mWindowEditText.setText(contact.getWork().getWindow());
        mCoverEditText.setText(contact.getWork().getCover());
        mSplitEditText.setText(contact.getWork().getSplit());
        mStandEditText.setText(contact.getWork().getStand());
        mRegistrationDate.setText(contact.getRegistrationDate());
        mNoteEditText.setText(contact.getNote());
        timestamp = contact.getTimeStamp();
        String cityCode = contact.getCity().getCityCode();
        String interval = contact.getWork().getInterval();
        Log.i("cityCode",cityCode);
        existCity = contact.getCity().getCity();
        switch (cityCode) {
            case "D":
                mCitySpinner.setSelection(1);
                break;
            case "H":
                mCitySpinner.setSelection(2);
                break;
            case "J":
                mCitySpinner.setSelection(3);
                break;
            case "K":
                mCitySpinner.setSelection(4);
                break;
            case "L":
                mCitySpinner.setSelection(5);
                break;
            case "M":
                mCitySpinner.setSelection(6);
                break;
            case "N":
                mCitySpinner.setSelection(7);
                break;
            case "Q":
                mCitySpinner.setSelection(8);
                break;
            case "R":
                mCitySpinner.setSelection(9);
                break;
            case "W":
                mCitySpinner.setSelection(10);
                break;
            case "Z":
                mCitySpinner.setSelection(11);
                break;
        }

         if (interval.equals("Morning")) {
             mDateSpinner.setSelection(1);
         }else if (interval.equals("Evening")){
                mDateSpinner.setSelection(2);

        }

        progressBar.setVisibility(View.INVISIBLE);

    }

    private void updateContact(Contact contact,String contactId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contacts").document(contactId)
                .set(contact)
                .addOnSuccessListener(aVoid -> {
                    isContactExist = false;
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(),ContactActivity.class);
                    i.putExtra(contactIdKey,contactId);
                    startActivity(i);
                    finish();
                });



    }

    private void checkIfContactExist(Contact contact){
        String phone =contact.getPhone();
        CollectionReference yourCollRef = dbRoot.collection("contacts");
        Query query = yourCollRef.whereEqualTo("phone", phone);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                    getContactId(contact,false);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        existContactCloudId = document.getId();
                        //TODO
                        isContactExist = true;
                        Contact c = document.toObject(Contact.class);
                        fillContactData(c);

                    }
                }
            }else{
                Toast.makeText(getApplicationContext(),"check again",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getContactId(Contact contact,boolean increase){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("config").document("counter");
        doc.get().addOnCompleteListener(task -> {
            if (!increase) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        count = document.getLong("count");
                        if (count != 0) {
                            pushContactToCloud(contact, getContactCode(year, contact.getCity().getCity(), count));
                        }
                    }
                }
            }else{
                map.put("count",++count);
                doc.set(map).addOnSuccessListener(aVoid -> { });

            }
        });
    }
    private void pushContactToCloud(Contact contact,String contactId) {
        contact.setId(contactId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contacts")
                .add(contact)
                .addOnSuccessListener(documentReference -> {
                    getContactId(null, true);
                    Log.i("pushData",documentReference.getId());
                    String contactCloudId = documentReference.getId();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(),ContactActivity.class);
                    i.putExtra(contactIdKey,contactCloudId);
                    startActivity(i);
                    finish();
                });

    }

    String getContactCode(String y , String c,long n){
        return y+getCityCode(c)+n;
    }

    private void attemptCreateContact(){
        progressBar.setVisibility(View.VISIBLE);
        String contactId = mContactIdTv.getText().toString();
        String phone = mPhoneEditText.getEditableText().toString();
        String locationLink = mLocationLinkEditText.getEditableText().toString();
        String cityCode = mCitySpinner.getSelectedItem().toString();
        String interval = mDateSpinner.getSelectedItem().toString();
        String window = mWindowEditText.getEditableText().toString();
        String cover = mCoverEditText.getEditableText().toString();
        String split = mSplitEditText.getEditableText().toString();
        String stand = mStandEditText.getEditableText().toString();
        String note = mNoteEditText.getEditableText().toString();
        String registerDate = mRegistrationDate.getText().toString();

        if (phone.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
           if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               showErrorMessage(mPhoneTextLayout,getString(R.string.empty_message));
           }else{
               mPhoneEditText.setError(getString(R.string.empty_message));
           }
        }
        if (!phone.isEmpty() && !isPhoneValid(phone)){
            progressBar.setVisibility(View.INVISIBLE);
           if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               showErrorMessage(mPhoneTextLayout,getString(R.string.phone_error_message));
           }else{
               mPhoneEditText.setError(getString(R.string.phone_error_message));
           }
        }

        if (!locationLink.isEmpty() &&!isLocationValid(locationLink)){
            progressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                showErrorMessage(mLocationTextLayout,getString(R.string.link_error_message));
            }else{
                mLocationLinkEditText.setError(getString(R.string.link_error_message));
            }
        }
        if (!isCityValid(cityCode)){
            progressBar.setVisibility(View.INVISIBLE);
            citySpinnerErrorTv.setText(getString(R.string.city_error_message));
        }
        if (!isDateValid(interval)){
            progressBar.setVisibility(View.INVISIBLE);
            dateSpinnerErrorTv.setText(getString(R.string.date_error_message));
        }
        if (split.isEmpty()){
            split = "0";
        }
        if (window.isEmpty()){
            window = "0";
        }
        if (stand.isEmpty()){
            stand = "0";
        }
        if (cover.isEmpty()){
            cover = "0";
        }
        if (!phone.isEmpty() && isPhoneValid(phone) && isCityValid(cityCode) && isDateValid(interval)){
            if (!isContactExist) {
                if (locationLink.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT).show();
                    Work work = new Work(interval, split, window, cover, stand, "");
                    City cityObj = new City(getCity(cityCode), cityCode, null, null, null, null);
                    Contact contact = new Contact(contactId, phone, Timestamp.now(), currentDate, "1", note, work, cityObj);
                    checkIfContactExist(contact);
                } else if (isLocationValid(locationLink)) {
                    Toast.makeText(getApplicationContext(), "save with link", Toast.LENGTH_SHORT).show();
                    Work work = new Work(interval, split, window, cover, stand, "");
                    City cityObj = new City(getCity(cityCode), cityCode, locationLink, null, null, null);
                    Contact contact = new Contact(contactId, phone, Timestamp.now(), currentDate, "3", note, work, cityObj);
                    checkIfContactExist(contact);
                }
            }else{
                if (locationLink.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    Work work = new Work(interval, split, window, cover, stand, "");
                    City cityObj = new City(existCity,cityCode, null, null, null, null);
                    Contact contact = new Contact(contactId, phone, timestamp, registerDate, "1", note, work, cityObj);
                    updateContact(contact,existContactCloudId);
                } else if (isLocationValid(locationLink)) {
                    Toast.makeText(getApplicationContext(), "updated with link", Toast.LENGTH_SHORT).show();
                    Work work = new Work(interval, split, window, cover, stand, "");
                    City cityObj = new City(existCity, cityCode, locationLink, null, null, null);
                    Contact contact = new Contact(contactId, phone, timestamp, registerDate, "3", note, work, cityObj);
                    updateContact(contact,existContactCloudId);
                }
            }
       }
    }

    private boolean isPhoneValid(String phone){
        return phone.length() == 9 && phone.charAt(0) == '5';
    }

    private boolean isLocationValid(String link){
        return link.contains("https://") && link.contains("goo.gl");
    }

    private boolean isCityValid(String city){
        return !TextUtils.equals(city,"Choose City");
    }

    private boolean isDateValid(String date){
        return !TextUtils.equals(date,"Choose Date");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showErrorMessage(TextInputLayout layout, String message){
        layout.setBoxStrokeColor(Color.RED);
        layout.setDefaultHintTextColor(getColorStateList(R.color.colorSecondary));
        layout.setHelperText(message);
        layout.setHelperTextColor(getColorStateList(R.color.colorSecondary));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideErrorMessage(TextInputLayout layout, String message){
        layout.setBoxStrokeColor(getColor(R.color.colorAccent));
        layout.setDefaultHintTextColor(getColorStateList(R.color.colorAccent));
        layout.setHelperTextColor(getColorStateList(R.color.colorBackGround));
        layout.setHelperText(message);
    }

    private String getCity(String s){
       switch (s){
           case "D":
               return "الدمام";
           case "H":
               return "مكه";
           case "J":
               return "جده";
           case "K":
               return "الخرج";
           case "L":
               return "المدينة المنورة";
           case "M":
               return "شقراء";
           case "N":
               return "جديد";
           case "Q":
               return "القصيم";
           case "R":
               return "الرياض";
           case "W":
               return "الأحساء";
           case "Z":
               return "الظهران";
       }
       return "";
    }

}
