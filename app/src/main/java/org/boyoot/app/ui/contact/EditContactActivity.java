package org.boyoot.app.ui.contact;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.databinding.ActivityEditContactBinding;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.JobAdded;
import org.boyoot.app.model.MapConfig;
import org.boyoot.app.model.Work;
import org.boyoot.app.model.job.Job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static org.boyoot.app.utilities.CityUtility.getBranchCode;

public class EditContactActivity extends AppCompatActivity {

    private EditContactViewModel viewModel;
    private ActivityEditContactBinding mBinding;

   /* private TextView mContactIdTv;
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
    private ProgressBar progressBar;*/
    private static final String contactIdKey = "contactId";
    private static final String CONTACTS_PATH = "contacts";
    private String existContactCloudId;
    private String existCity="";
    private String existInterval;
    ArrayAdapter<CharSequence> dateAdapter;
    ArrayAdapter<CharSequence> cityAdapter;
    private long count =0;
    private String year;
    private String currentDate;
    private Timestamp timestamp;
    private Calendar calendar;
    private Map<String,Object> map;
    private boolean contactChanged = false;
    private String currentLocationCode;
    private FirebaseUser currentUser;
    private Contact currentContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_contact);
        setSupportActionBar(mBinding.editContactToolbar);
        //setContentView(R.layout.activity_edit_contact);

/*
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
       // Toolbar toolbar = findViewById(R.id.edit_contact_toolbar);*/


        viewModel = new ViewModelProvider(this).get(EditContactViewModel.class);
        if (getIntent().hasExtra(contactIdKey)){
            String contactCloudId =  getIntent().getStringExtra(contactIdKey);
            existContactCloudId = contactCloudId;
            viewModel.getContactFromCloud(contactCloudId);
        }else{
            mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
            existContactCloudId = "new";
        }
        map = new HashMap<>();
        calendar = Calendar.getInstance();
        int calenderYear = calendar.get(Calendar.YEAR);
        year = String.valueOf(calenderYear).substring(2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("en"));
        currentDate = dateFormat.format(calendar.getTime());
       // mBinding.editContactInclude.registrationDateTv.setText(currentDate);
        viewModel.getContact().observe(this, this::fillContactData);
        dateAdapter = ArrayAdapter.createFromResource(this,R.array.date_array,android.R.layout.simple_spinner_dropdown_item);
       // mBinding.editContactInclude.dateSpinner.setAdapter(dateAdapter);
        cityAdapter= ArrayAdapter.createFromResource(this,R.array.cities_array,android.R.layout.simple_spinner_dropdown_item);
        mBinding.editContactInclude.locationSpinner.setAdapter(cityAdapter);
        mBinding.editContactInclude.locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.editContactInclude.citySpinnerErrorTv.setText(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*mBinding.editContactInclude.dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.editContactInclude.dateSpinnerErrorTv.setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        mBinding.editContactInclude.phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(mBinding.editContactInclude.phoneNumberEditLayout, null);
                }else{
                    mBinding.editContactInclude.phoneEditText.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.editContactInclude.locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hideErrorMessage(mBinding.editContactInclude.locationEditLayout, null);
                }else{
                    mBinding.editContactInclude.locationEditText.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
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
        currentContact = contact;
        mBinding.editContactInclude.contactIdTv.setText(contact.getId());
        mBinding.editContactInclude.phoneEditText.setText(contact.getPhone());
        mBinding.editContactInclude.locationEditText.setText(contact.getCity().getLocationCode());
       // mBinding.editContactInclude.windowEditText.setText(contact.getWork().getWindow());
       // mBinding.editContactInclude.coverEditText.setText(contact.getWork().getCover());
       // mBinding.editContactInclude.splitEditText.setText(contact.getWork().getSplit());
       // mBinding.editContactInclude.standEditText.setText(contact.getWork().getStand());
       // mBinding.editContactInclude.concealedEditText.setText(contact.getWork().getConcealed());
       // mBinding.editContactInclude.registrationDateTv.setText(contact.getRegistrationDate());
        mBinding.editContactInclude.noteEditText.setText(contact.getNote());
        timestamp = contact.getTimeStamp();
        String cityCode = contact.getCity().getCityCode();
        //String interval = contact.getWork().getInterval();
        existCity = contact.getCity().getCity();
        switch (cityCode) {
            case "D":
                mBinding.editContactInclude.locationSpinner.setSelection(1);
                break;
            case "H":
                mBinding.editContactInclude.locationSpinner.setSelection(2);
                break;
            case "J":
                mBinding.editContactInclude.locationSpinner.setSelection(3);
                break;
            case "K":
                mBinding.editContactInclude.locationSpinner.setSelection(4);
                break;
            case "L":
                mBinding.editContactInclude.locationSpinner.setSelection(5);
                break;
            case "M":
                mBinding.editContactInclude.locationSpinner.setSelection(6);
                break;
            case "N":
                mBinding.editContactInclude.locationSpinner.setSelection(7);
                break;
            case "Q":
                mBinding.editContactInclude.locationSpinner.setSelection(8);
                break;
            case "R":
                mBinding.editContactInclude.locationSpinner.setSelection(9);
                break;
            case "W":
                mBinding.editContactInclude.locationSpinner.setSelection(10);
                break;
            case "Y":
                mBinding.editContactInclude.locationSpinner.setSelection(11);
                break;
            case "Z":
                mBinding.editContactInclude.locationSpinner.setSelection(12);
                break;
        }
        /*if (interval.equals("Morning")) {
            mBinding.editContactInclude.dateSpinner.setSelection(1);
        }else if (interval.equals("Evening")){
            mBinding.editContactInclude.dateSpinner.setSelection(2);
        }*/
        mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
        //TODO handle mapConfig

    }

    private void updateContact(Contact contact,String contactId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .set(contact)
                .addOnSuccessListener(aVoid -> {
                    mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(),ContactActivity.class);
                    i.putExtra(contactIdKey,contactId);
                    startActivity(i);
                    finish();
                });



    }

    private void checkIfContactExist(Contact contact){
        String phone =contact.getPhone();
        FirebaseFirestore dbRoot = FirebaseFirestore.getInstance();
        CollectionReference yourCollRef = dbRoot.collection(CONTACTS_PATH);
        Query query = yourCollRef.whereEqualTo("phone", phone);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                    getContactId(contact,false);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        existContactCloudId = document.getId();
                        Intent i = new Intent(getApplicationContext(),ContactActivity.class);
                        i.putExtra(contactIdKey,existContactCloudId);
                        startActivity(i);
                        finish();
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
                            pushContactToCloud(contact, getContactCode(year, contact.getCity().getCityCode(), count));
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
        if (contact.getWork().getInterval() == null){
            contact.getWork().setInterval("Morning");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH)
                .add(contact)
                .addOnSuccessListener(documentReference -> {
                    getContactId(null, true);
                    Log.i("pushData",documentReference.getId());
                    String contactCloudId = documentReference.getId();
                    author(contactCloudId,currentUser.getEmail());
                    mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(),ContactActivity.class);
                    i.putExtra(contactIdKey,contactCloudId);
                    startActivity(i);
                    finish();
                });

    }

    String getContactCode(String y , String c,long n){
        return y+c+n;
    }

    private void attemptCreateContact(){
        mBinding.editContactProgressBar.setVisibility(View.VISIBLE);
        String contactId = mBinding.editContactInclude.contactIdTv.getText().toString();
        String phone = mBinding.editContactInclude.phoneEditText.getEditableText().toString();
        String locationCode = mBinding.editContactInclude.locationEditText.getEditableText().toString();
        String branch = mBinding.editContactInclude.locationSpinner.getSelectedItem().toString();
       // String interval = mBinding.editContactInclude.dateSpinner.getSelectedItem().toString();
       // String window = mBinding.editContactInclude.windowEditText.getEditableText().toString();
       // String cover = mBinding.editContactInclude.coverEditText.getEditableText().toString();
        //String split = mBinding.editContactInclude.splitEditText.getEditableText().toString();
       // String stand = mBinding.editContactInclude.standEditText.getEditableText().toString();
       // String concealed = mBinding.editContactInclude.concealedEditText.getEditableText().toString();
        String note = mBinding.editContactInclude.noteEditText.getEditableText().toString();
        //String discount = mBinding.editContactInclude.discountEditText.getEditableText().toString();
       // String registerDate = mBinding.editContactInclude.registrationDateTv.getText().toString();

        // check fields

        if (phone.isEmpty()){
            mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
           if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               showErrorMessage(mBinding.editContactInclude.phoneNumberEditLayout,getString(R.string.empty_message));
           }else{
               mBinding.editContactInclude.phoneEditText.setError(getString(R.string.empty_message));
           }
        }
        if (!phone.isEmpty() && !isPhoneValid(phone)){
            mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
           if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               showErrorMessage(mBinding.editContactInclude.phoneNumberEditLayout,getString(R.string.phone_error_message));
           }else{
               mBinding.editContactInclude.phoneEditText.setError(getString(R.string.phone_error_message));
           }
        }

        if (locationCode.isEmpty()){
            mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                showErrorMessage(mBinding.editContactInclude.locationEditLayout,getString(R.string.empty_message));
            }else{
                mBinding.editContactInclude.locationEditText.setError(getString(R.string.empty_message));
            }
        }
        if (!isCityValid(branch)){
            mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
            mBinding.editContactInclude.citySpinnerErrorTv.setText(getString(R.string.city_error_message));
        }
        /*if (!isDateValid(interval)){
            mBinding.editContactProgressBar.setVisibility(View.INVISIBLE);
            mBinding.editContactInclude.dateSpinnerErrorTv.setText(getString(R.string.date_error_message));
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
        if (concealed.isEmpty()){
            concealed = "0";
        }
        if (discount.isEmpty()){
            discount = "0";
        }
        if (interval.equals(getString(R.string.morning))){
            interval = "Morning";
        }else if (interval.equals(getString(R.string.evening))){
            interval = "Evening";
        }
*/

        MapConfig mapConfig = new MapConfig(null,null,null,null,null,false);
        JobAdded jobAdded = new JobAdded(null,false,null);
        if (!phone.isEmpty() && isPhoneValid(phone) && isCityValid(branch)){
            if (existContactCloudId.equals("new")) {

                if (locationCode.isEmpty() && !isLocationValid(locationCode)) {
                    Work work = new Work(null, "0", "0", "0", "0","0",false,"0");
                    City cityObj = new City(branch, getBranchCode(branch), null, null, null);
                    Contact contact = new Contact(contactId, phone, Timestamp.now(), currentDate, "1", note, work, cityObj,mapConfig,jobAdded);
                    checkIfContactExist(contact);
                } else  {
                    Toast.makeText(getApplicationContext(), "save with link", Toast.LENGTH_SHORT).show();
                    Work work = new Work(null, "0", "0", "0", "0","0",false,"0");
                    City cityObj = new City(branch, getBranchCode(branch), locationCode, null, null);
                    Contact contact = new Contact(contactId, phone, Timestamp.now(), currentDate, "3", note, work, cityObj,mapConfig,jobAdded);
                    checkIfContactExist(contact);
                }
            }else{

                if (!isLocationValid(locationCode)) {
                    City cityObj = new City(branch,getBranchCode(branch), null, null, null);
                    StringBuilder id = new StringBuilder(contactId);
                    id.setCharAt(2,getBranchCode(branch).charAt(0));
                    Contact contact = new Contact(id.toString(), changePhone(phone), currentContact.getTimeStamp(), currentContact.getRegistrationDate(), "1",
                            changeNote(note), currentContact.getWork(), cityObj,mapConfig,currentContact.getJobAdded());
                    if (contactChanged){
                       contact.setAuth(currentUser.getEmail());
                    }else{
                        contact.setAuth(currentContact.getAuth());
                    }
                    updateContact(contact,existContactCloudId);
                }else{
                    City cityObj = new City(changeCity(branch), getBranchCode(branch), changeLocation(locationCode), null, null);
                    StringBuilder id = new StringBuilder(contactId);
                    id.setCharAt(2,getBranchCode(branch).charAt(0));
                    String p = currentContact.getPriority();
                    if (p.equals("1")){
                        p = "3";
                    }
                    Contact contact = new Contact(id.toString(), changePhone(phone),currentContact.getTimeStamp(), currentContact.getRegistrationDate(), p, changeNote(note),
                            currentContact.getWork(), cityObj,currentContact.getMapConfig(),currentContact.getJobAdded());
                    if (contactChanged){
                        contact.setAuth(currentUser.getEmail());
                    }else{
                        contact.setAuth(currentContact.getAuth());
                    }
                    updateContact(contact,existContactCloudId);
                }
            }
       }
    }

    private boolean isLocationValid(String s){
        return s.contains("+") && s.length() > 10;
    }

    private boolean isPhoneValid(String phone){
        return phone.length() == 9 && phone.charAt(0) == '5';
    }

    private boolean isCityValid(String city){
        return !TextUtils.equals(city,getString(R.string.city_error_message));
    }

    private boolean isDateValid(String date){
        return !TextUtils.equals(date,getString(R.string.date_error_message));
    }

    private String changeCity(String c){
        if (currentContact.getCity().getCityCode().equals(c)){
            return currentContact.getCity().getCityCode();
        }else {
            contactChanged = true;
            //author(existContactCloudId,currentUser.getEmail());
            return c;
        }
    }

    private String changePhone(String phone){
        if (currentContact.getPhone().equals(phone)){
            return currentContact.getPhone();
        }else {
            contactChanged = true;
            //author(existContactCloudId,currentUser.getEmail());
            return phone;
        }
    }

    private String changeLocation(String location){
        if (currentContact.getCity().getLocationCode() != null) {
            if (currentContact.getCity().getLocationCode().equals(location)) {
                return currentContact.getCity().getLocationCode();
            } else {
                contactChanged = true;
                //author(existContactCloudId, currentUser.getEmail());
                currentContact.getMapConfig().setSaved(false);
                return location;
            }
        }else {
            return location;
        }
    }

    private String changeNote(String note){
        if (currentContact.getNote().equals(note)){
            return currentContact.getNote();
        }else{
            contactChanged = true;
           // author(existContactCloudId,currentUser.getEmail());
            return note;
        }

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
    /*private String getCity(String s){
       switch (s){
           case "D":
               return "????????????";
           case "H":
               return "??????";
           case "J":
               return "??????";
           case "K":
               return "??????????";
           case "L":
               return "?????????????? ??????????????";
           case "M":
               return "??????????";
           case "N":
               return "????????";
           case "Q":
               return "????????????";
           case "R":
               return "????????????";
           case "W":
               return "??????????????";
           case "Z":
               return "??????????????";
       }
       return "";
    }*/

    private void author(String contactId,String user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(CONTACTS_PATH).document(contactId)
                .update("auth",user).addOnSuccessListener(aVoid -> {

        });
    }

}
