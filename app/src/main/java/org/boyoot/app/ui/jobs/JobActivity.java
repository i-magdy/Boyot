package org.boyoot.app.ui.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityJobBinding;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.Objects;

public class JobActivity extends AppCompatActivity implements OnStateChanged {

    private ActivityJobBinding binding;
    private JobViewModel viewModel;
    private Job currentJob;
    private String JOB_KEY;
    private int priority;



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
            JOB_KEY = Objects.requireNonNull(getIntent().getStringExtra(JOB_ID_KEY));
            viewModel.jobContent(JOB_KEY);
        }else {
            finish();
        }

        binding.jobMainLayout.setVisibility(View.INVISIBLE);
        binding.jobContentCard.setVm(viewModel);
        binding.jobContentCard.setLifecycleOwner(this);
        viewModel.getJob().observe(this, this::fillTagState);
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
        Switch switchOption = (Switch)  item.getActionView();

        /*if (priority != 4){
            switchOption.setClickable(false);
        }*/

        if (priority == 5){
            switchOption.setChecked(true);
        }
        switchOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked /*&& /priority == 4*/){
                    startActivity(new Intent(getApplicationContext(),PaymentActivity.class));

                }else {
                    switchOption.setChecked(false);
                }
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

    @Override
    public void onStateChanged() {
        viewModel.jobContent(Objects.requireNonNull(getIntent().getStringExtra(JOB_ID_KEY)));
    }

    void fillTagState(Job job){
        if (job != null) {

            binding.jobMainLayout.setVisibility(View.VISIBLE);

            currentJob = job;

            priority = job.getPriority();

            switch (priority) {
            case 0:
                binding.jobContentCard.jobTagIv.setBackground(getDrawable(R.drawable.ic_new_job_tag));
                break;
            case 1:
                //TODO-sendWhatsApp
                break;
            case 2:
                binding.jobContentCard.jobTagIv.setBackground(getDrawable(R.drawable.ic_date_picked_tag));
                break;
            case 3:
                //TODO-changeDate
                break;
            case 4:
                binding.jobContentCard.jobTagIv.setBackground(getDrawable(R.drawable.ic_date_approved_tag));
                break;
            case 5:
                binding.jobContentCard.jobTagIv.setBackground(getDrawable(R.drawable.ic_job_done_tag));
                break;
            case 6:
                binding.jobContentCard.jobTagIv.setBackground(getDrawable(R.drawable.ic_job_delay_tag));
                break;
            case 7:
                binding.jobContentCard.jobTagIv.setBackground(getDrawable(R.drawable.ic_job_cancel_tag));
                break;
            default:
                break;
            }
        }
    }
}
