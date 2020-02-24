package org.boyoot.app.ui.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.Contact;

public class EditContactActivity extends AppCompatActivity {

    private EditContactViewModel viewModel;
    private TextView mContactIdTv;
    private EditText mPhoneEditText;
    private EditText mLocationLinkEditText;
    private Spinner mCitySpinner;
    private Spinner mDateSpinner;
    private EditText mWindowEditText;
    private EditText mSpilitEditText;
    private EditText mStandEditText;
    private EditText mCoverEditText;
    private TextView mRegistrationDate;
    private EditText mNoteEditText;
    private ArrayAdapter<CharSequence> dateAdapter;
    private ArrayAdapter<CharSequence> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        mContactIdTv = findViewById(R.id.contact_id_tv);
        mCitySpinner = findViewById(R.id.location_spinner);
        mDateSpinner = findViewById(R.id.date_spinner);
        mPhoneEditText = findViewById(R.id.phone_edit_text);
        mLocationLinkEditText = findViewById(R.id.location_edit_text);
        mWindowEditText = findViewById(R.id.window_edit_text);
        mSpilitEditText = findViewById(R.id.split_edit_text);
        mStandEditText = findViewById(R.id.stand_edit_text);
        mCoverEditText = findViewById(R.id.cover_edit_text);
        mRegistrationDate = findViewById(R.id.registration_date_tv);
        mNoteEditText = findViewById(R.id.note_edit_text);

        viewModel = new ViewModelProvider(this).get(EditContactViewModel.class);
        if (getIntent().hasExtra("contact")){
            Contact contact = (Contact) getIntent().getSerializableExtra("contact");
            viewModel.setData(contact);
        }

        viewModel.getContact().observe(this, this::fillContactData);
        dateAdapter = ArrayAdapter.createFromResource(this,R.array.date_array,android.R.layout.simple_spinner_dropdown_item);
        mDateSpinner.setAdapter(dateAdapter);
        cityAdapter= ArrayAdapter.createFromResource(this,R.array.cities_array,android.R.layout.simple_spinner_dropdown_item);
        mCitySpinner.setAdapter(cityAdapter);

    }

    void fillContactData(Contact contact){
        mContactIdTv.setText(contact.getId());
        mPhoneEditText.setText(contact.getPhone());
        mLocationLinkEditText.setText(contact.getPhone());
        mWindowEditText.setText(contact.getWork().getWindow());
        mCoverEditText.setText(contact.getWork().getCover());
        mSpilitEditText.setText(contact.getWork().getSplit());
        mStandEditText.setText(contact.getWork().getStand());
        mRegistrationDate.setText(contact.getRegistrationDate());
        mNoteEditText.setText(contact.getNote());

    }
}
