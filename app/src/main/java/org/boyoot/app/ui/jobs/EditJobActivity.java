package org.boyoot.app.ui.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityEditJobBinding;
import org.boyoot.app.model.job.Job;

import java.util.Objects;

public class EditJobActivity extends AppCompatActivity {

    private ActivityEditJobBinding binding;
    private EditJobViewModel viewModel;


    private static final String JOB_ID_KEY = "job id key";
    private static final String JOB_DIVIDE_VALUE_KEY = "job divide value";
    private static final String JOBS_PATH = "jobs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_job);
        viewModel = new ViewModelProvider(this).get(EditJobViewModel.class);
        binding.editJobToolbar.setTitle(getString(R.string.create_jobe_title));
        final boolean divide = getIntent().getBooleanExtra(JOB_DIVIDE_VALUE_KEY,false);
        if (getIntent().hasExtra(JOB_ID_KEY)){
            viewModel.getJobContent(Objects.requireNonNull(getIntent().getStringExtra(JOB_ID_KEY)));
            binding.editJobToolbar.setTitle(getString(R.string.edit_job_title));
        }
        ArrayAdapter<CharSequence> intervalAdapter = ArrayAdapter.createFromResource(this,R.array.date_array,android.R.layout.simple_spinner_dropdown_item);
        binding.ejWorkEditCard.dateSpinner.setAdapter(intervalAdapter);

        viewModel.getJob().observe(this, new Observer<Job>() {
            @Override
            public void onChanged(Job job) {
                if (job != null){
                    if (divide){
                        job.setDivided(true);
                    }
                    fillFields(job);
                }
            }
        });

        binding.ejWorkEditCard.dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.ejWorkEditCard.dateSpinnerErrorTv.setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillFields(Job job){
        binding.ejContactIdTv.setText(job.getId());
        binding.ejPhoneTv.setText(job.getPhone());
        binding.ejCityTv.setText(job.getCity());
        //CARD
        binding.ejWorkEditCard.splitEditText.setText(String.valueOf(job.getCurrentWork().getSplit()));
        binding.ejWorkEditCard.windowEditText.setText(String.valueOf(job.getCurrentWork().getWindow()));
        binding.ejWorkEditCard.coverEditText.setText(String.valueOf(job.getCurrentWork().getCover()));
        binding.ejWorkEditCard.standEditText.setText(String.valueOf(job.getCurrentWork().getStand()));
        binding.ejWorkEditCard.concealedEditText.setText(String.valueOf(job.getCurrentWork().getConcealed()));
        binding.ejWorkEditCard.discountEditText.setText(String.valueOf(job.getCurrentWork().getDiscount()));
        // interval
        String interval = job.getCurrentWork().getInterval();
        if (interval.equals("Morning")) {
            binding.ejWorkEditCard.dateSpinner.setSelection(1);
        }else if (interval.equals("Evening")){
            binding.ejWorkEditCard.dateSpinner.setSelection(2);
        }
        binding.ejWorkEditCard.ejDivideJobSwitch.setChecked(job.isDivided());


    }




    private boolean isDateValid(String date){
        return !TextUtils.equals(date,getString(R.string.date_error_message));
    }
    /*
    if (interval.equals(getString(R.string.morning))){
        interval = "Morning";
    }else if (interval.equals(getString(R.string.evening))){
        interval = "Evening";
    }*/
}
