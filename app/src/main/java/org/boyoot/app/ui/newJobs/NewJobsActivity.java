package org.boyoot.app.ui.newJobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import org.boyoot.app.R;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.databinding.ActivityNewJobsBinding;
import org.boyoot.app.ui.jobs.JobActivity;

import java.util.List;

public class NewJobsActivity extends AppCompatActivity implements NewJobsAdapter.OnItemClickListener{

    ActivityNewJobsBinding binding;
    NewJobsViewModel viewModel;
    private static final String JOB_ID_KEY = "job id key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_new_jobs);
        setSupportActionBar(binding.newJobsToolbar);
        viewModel = new ViewModelProvider(this).get(NewJobsViewModel.class);
        NewJobsAdapter adapter = new NewJobsAdapter(this);
        binding.newJobsRecycler.setAdapter(adapter);
        binding.newJobsRecycler.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getJobs().observe(this, adapter::setJobs);

    }

    @Override
    public void onItemClicked(String jobId) {
        Intent i = new Intent(getApplicationContext(), JobActivity.class);
        i.putExtra(JOB_ID_KEY,jobId);
        startActivity(i);
    }
}