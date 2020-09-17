package org.boyoot.app.ui.newJobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.boyoot.app.R;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.databinding.ActivityNewJobsBinding;
import org.boyoot.app.ui.jobs.JobActivity;
import org.boyoot.app.utilities.CityUtility;

import java.util.List;

public class NewJobsActivity extends AppCompatActivity implements NewJobsAdapter.OnItemClickListener , OnNewJobsFiltered{

    ActivityNewJobsBinding binding;
    NewJobsViewModel viewModel;
    private BottomSheetBehavior filterBottomSheet;

    private static final String JOB_ID_KEY = "job id key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_new_jobs);
        setSupportActionBar(binding.newJobsToolbar);
        viewModel = new ViewModelProvider(this).get(NewJobsViewModel.class);
        NewJobsAdapter adapter = new NewJobsAdapter(this,this);
        binding.newJobsRecycler.setAdapter(adapter);
        binding.newJobsRecycler.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getJobs().observe(this,viewModel::setNewJobs);
        viewModel.getNewJobs().observe(this,adapter::setJobs);


    }



    @Override
    public void onItemClicked(String jobId) {
        Intent i = new Intent(getApplicationContext(), JobActivity.class);
        i.putExtra(JOB_ID_KEY,jobId);
        startActivity(i);
    }


    @Override
    public void onJobsFiltered(List<Jobs> jobs) {
        viewModel.setNewJobs(jobs);
    }

    @Override
    public void onResetFilterSelect(List<Jobs> jobs) {
        viewModel.setNewJobs(jobs);
    }
}