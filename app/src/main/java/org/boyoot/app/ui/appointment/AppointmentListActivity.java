package org.boyoot.app.ui.appointment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.job.Job;

import static org.boyoot.app.utilities.WorkTimeUtility.parseWork;

public class AppointmentListActivity extends AppCompatActivity {

    private AppointmentListViewModel viewModel;
    private String contactId;
    private Contact contact;

    private static final String CONTACT_APPOINTMENT_KEY="contact appointment key";
    private static final String contactIdKey = "contactId";
    private static final String JOBS_PATH = "jobs";
    private static final String CONTACTS_PATH = "contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        viewModel = new ViewModelProvider(this).get(AppointmentListViewModel.class);
        if (getIntent().hasExtra(contactIdKey)){
            contactId = getIntent().getStringExtra(contactIdKey);
            viewModel.fetchContact(contactId);
            Toast.makeText(this,contactId,Toast.LENGTH_SHORT).show();
        }else {
            finish();
        }
        viewModel.isJobExist().observe(this, this::showAddDialog);
        viewModel.getContact().observe(this, c -> contact = c);
    }

    private void showAddDialog(boolean b){
        if (!b) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("heyyy")
                    .setTitle("R.string.dialog_title");

            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*Intent i = new Intent(getApplicationContext(),CheckingAppointmentDialog.class);
                    i.putExtra(contactIdKey,contactId);
                    startActivity(i);*/
                    pushNewJob(contact);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("cancele", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void pushNewJob(Contact c){
        Job job = new Job(c.getId(),c.getContactId(),c.getPhone(),0,
                c.getCity().getCityCode(),c.getRegistrationDate(),false,parseWork(c.getWork()),null,c.getMapConfig());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(JOBS_PATH)
                .add(job).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                updateContactForNewJob(c.getRegistrationDate());
            }
        });
    }

    private void updateContactForNewJob(String date){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update ("priority","4",
                        "jobAdded.date",date,
                        "jobAdded.added",true).addOnSuccessListener(aVoid -> {

        });
    }
}
