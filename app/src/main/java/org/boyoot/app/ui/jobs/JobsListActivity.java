package org.boyoot.app.ui.jobs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.Price;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.model.job.Payment;

import java.util.List;

import static org.boyoot.app.utilities.WorkUtility.parseWork;
import static org.boyoot.app.utilities.WorkUtility.getTotalPrice;
import static org.boyoot.app.utilities.WorkUtility.getTotalPriceText;

public class JobsListActivity extends AppCompatActivity implements JobsListAdapter.ListItemOnClickListener{

    private JobsListViewModel viewModel;
    private String contactId;
    private Contact contact;
    private List<Job> jobsList;
    private Price price;

    private static final String CONTACT_APPOINTMENT_KEY="contact appointment key";
    private static final String contactIdKey = "contactId";
    private static final String JOB_ID_KEY = "job id key";
    private static final String JOB_DIVIDE_VALUE_KEY = "job divide value";
    private static final String JOBS_PATH = "jobs";
    private static final String CONTACTS_PATH = "contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);
        viewModel = new ViewModelProvider(this).get(JobsListViewModel.class);
        if (getIntent().hasExtra(contactIdKey)){
            contactId = getIntent().getStringExtra(contactIdKey);
            viewModel.fetchContact(contactId);
            Toast.makeText(this,contactId,Toast.LENGTH_SHORT).show();
        }else {
            finish();
        }

        RecyclerView recycler = findViewById(R.id.appointment_list_recycler);
        JobsListAdapter adapter = new JobsListAdapter(this);
        recycler.setAdapter(adapter);
        viewModel.getContact().observe(this, c -> contact = c);
        viewModel.getPrice().observe(this, p -> price = p);
        viewModel.isJobExist().observe(this, this::showAddDialog);

        viewModel.getJobsList().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                adapter.setJobs(jobs);
                jobsList = jobs;
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showAddDialog(boolean b){
        if (!b) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.message_add_job_dialog))
                    .setTitle(getString(R.string.title_add_job_dialog));

            builder.setPositiveButton(getString(R.string.confirm_add_job_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*Intent i = new Intent(getApplicationContext(),CheckingAppointmentDialog.class);
                    i.putExtra(contactIdKey,contactId);
                    startActivity(i);*/
                    pushNewJob(contact);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(getString(R.string.canecle_add_job_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        viewModel.isJobExist().removeObservers(this);
    }

    private void pushNewJob(Contact c){
        Job job = new Job(c.getId(),c.getContactId(),c.getPhone(),0,
                c.getCity().getCityCode(),c.getCity().getCity(),c.getRegistrationDate(),false,parseWork(c.getWork()),null,c.getMapConfig(),price);
        Payment payment = new Payment(getTotalPrice(job.getPrice(),job.getCurrentWork().getDiscount()),getTotalPriceText(job.getPrice(),job.getCurrentWork().getDiscount()),null,null);
        job.setPayment(payment);
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
                            viewModel.fetchContact(contactId);
        });
    }

    @Override
    public void onItemClickListener(int position) {
        Job job =jobsList.get(position);
        if (job.getPriority() == 0){
            showDivideDialog(job);
        }else {

        }
    }

    private void showDivideDialog(Job job){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_divide_job_dialog))
                .setTitle(getString(R.string.title_divide_job_dialog));

        builder.setPositiveButton(getString(R.string.confirm_divide_job_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(),EditJobActivity.class);
                    i.putExtra(JOB_DIVIDE_VALUE_KEY,true);
                    i.putExtra(JOB_ID_KEY,job.getJobId());
                    startActivity(i);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.canecle_divide_job_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),JobActivity.class);
                i.putExtra(JOB_ID_KEY,job.getJobId());
                startActivity(i);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
