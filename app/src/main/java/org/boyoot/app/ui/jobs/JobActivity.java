package org.boyoot.app.ui.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityJobBinding;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.Objects;

import static org.boyoot.app.R.color.colorTagWorkDone;

public class JobActivity extends AppCompatActivity {

    private ActivityJobBinding binding;
    private JobViewModel viewModel;
    private Job currentJob;

    private static final String CONTACT_APPOINTMENT_KEY="contact appointment key";
    private static final String contactIdKey = "contactId";
    private static final String JOB_ID_KEY = "job id key";
    private static final String JOB_DIVIDE_VALUE_KEY = "job divide value";
    private static final String JOBS_PATH = "jobs";
    private static final String CONTACTS_PATH = "contacts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_job);
        viewModel = new ViewModelProvider(this).get(JobViewModel.class);
        setSupportActionBar(binding.jobToolbar);
        if (getIntent().hasExtra(JOB_ID_KEY)){
           viewModel.jobContent(Objects.requireNonNull(getIntent().getStringExtra(JOB_ID_KEY)));
        }else {
            finish();
        }
        binding.jobMainLayout.setVisibility(View.INVISIBLE);
        binding.jobContentCard.setVm(viewModel);
        binding.jobContentCard.setLifecycleOwner(this);
        viewModel.getJob().observe(this, new Observer<Job>() {
            @Override
            public void onChanged(Job job) {
                if (job != null) {
                    binding.jobMainLayout.setVisibility(View.VISIBLE);
                    currentJob = job;
                }
            }
        });
        binding.jobContentCard.idContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ContactActivity.class);
                i.putExtra(contactIdKey,currentJob.getContactId());
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.work_done_menu, menu);
        MenuItem item = menu.findItem(R.id.action_work_done_switch);
        item.setActionView(R.layout.work_done_switch);
        Switch view = (Switch)  item.getActionView();

        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("work_done_menu","clicked  "+isChecked);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_work_done_switch){

            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}
