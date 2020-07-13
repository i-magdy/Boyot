package org.boyoot.app.ui.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityEditJobBinding;

import org.boyoot.app.model.job.CurrentWork;
import org.boyoot.app.model.job.Job;


import java.util.Date;

import java.util.Objects;


import  org.boyoot.app.utilities.WorkUtility;


public class EditJobActivity extends AppCompatActivity {

    private ActivityEditJobBinding binding;
    private EditJobViewModel viewModel;
    private Job currentJob;
    private CurrentWork newWork;
    private CurrentWork attemptedWork;
   @ServerTimestamp
    Date date;
    private static final String JOB_ID_KEY = "job id key";
    private static final String JOB_DIVIDE_VALUE_KEY = "job divide value";
    private static final String JOBS_PATH = "jobs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_job);
        setSupportActionBar(binding.editJobToolbar);
        viewModel = new ViewModelProvider(this).get(EditJobViewModel.class);
        binding.editJobToolbar.setTitle(getString(R.string.create_jobe_title));
        binding.editJobMainLayout.setVisibility(View.INVISIBLE);
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
                    binding.editJobMainLayout.setVisibility(View.VISIBLE);

                    if (divide){
                        job.setDivided(true);
                    }
                    fillFields(job);
                    currentJob = job;
                    Log.i("TEST_JOB","Observer  || "+job.getJobId());
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
        // in=-terval
        String interval = job.getCurrentWork().getInterval();
        if (interval.equals("Morning")) {
            binding.ejWorkEditCard.dateSpinner.setSelection(1);
        }else if (interval.equals("Evening")){
            binding.ejWorkEditCard.dateSpinner.setSelection(2);
        }
        binding.ejWorkEditCard.ejDivideJobSwitch.setChecked(job.isDivided());


    }

    void attemptedToSaveJob(){
        String interval = binding.ejWorkEditCard.dateSpinner.getSelectedItem().toString();
        boolean divide = binding.ejWorkEditCard.ejDivideJobSwitch.isChecked();
        String window = binding.ejWorkEditCard.windowEditText.getEditableText().toString();
        String cover = binding.ejWorkEditCard.coverEditText.getEditableText().toString();
        String split = binding.ejWorkEditCard.splitEditText.getEditableText().toString();
        String stand = binding.ejWorkEditCard.standEditText.getEditableText().toString();
        String concealed = binding.ejWorkEditCard.concealedEditText.getEditableText().toString();
        String discount = binding.ejWorkEditCard.discountEditText.getEditableText().toString();

        if (interval.equals(getString(R.string.morning))){
            interval = "Morning";
        }else if (interval.equals(getString(R.string.evening))){
            interval = "Evening";
        }
        attemptedWork = new CurrentWork(interval,
                WorkUtility.getIntSplit(split),
                WorkUtility.getIntWindow(window),
                WorkUtility.getIntCover(cover),
                WorkUtility.getIntStand(stand),
                WorkUtility.getIntConcealed(concealed),
                currentJob.getCurrentWork().isOffer(),
                WorkUtility.getDiscount(discount));

        /*attemptedWork.setWindow(WorkUtility.getIntWindow(window));
        attemptedWork.setCover(WorkUtility.getIntCover(cover));
        attemptedWork.setSplit(WorkUtility.getIntSplit(split));
        attemptedWork.setStand(WorkUtility.getIntStand(stand));
        attemptedWork.setConcealed(WorkUtility.getIntConcealed(concealed));
        attemptedWork.setDiscount(WorkUtility.getDiscount(discount));*/
        newWork= compareCurrentWork(attemptedWork);
        if (isDateValid(interval)){
            if (divide && !currentJob.isConfirmDivide()) {
                if (newWork != null) {
                    //currentJob.setCurrentWork(attemptedWork);
                    currentJob.setConfirmDivide(true);
                    //newJob.setJobId(null);
                    //newJob.setTimeStamp(date);
                    currentJob.setCurrentWork(newWork);
                    pushNewJob(currentJob);
                } else {
                    currentJob.setCurrentWork(attemptedWork);
                    if (!currentJob.isConfirmDivide()){
                        currentJob.setDivided(false);
                    }
                    updateCurrentJob(currentJob);
                }
            }else {
                currentJob.setCurrentWork(attemptedWork);
                updateCurrentJob(currentJob);
            }
        }


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
            attemptedToSaveJob();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean isDateValid(String date){
        return !TextUtils.equals(date,getString(R.string.date_error_message));
    }


    CurrentWork compareCurrentWork(CurrentWork attemptedWork){
        int aWindow = attemptedWork.getWindow();
        int aCover = attemptedWork.getCover();
        int aSplit = attemptedWork.getSplit();
        int aStand = attemptedWork.getStand();
        int aConcealed = attemptedWork.getConcealed();

        CurrentWork currentWorkR = currentJob.getCurrentWork();
        CurrentWork newWork = new CurrentWork();

        int window = currentWorkR.getWindow();
        int cover = currentWorkR.getCover();
        int split = currentWorkR.getSplit();
        int stand = currentWorkR.getStand();
        int concealed = currentWorkR.getConcealed();
        int newWindow = window - aWindow;
        int newCover = cover - aCover;
        int newSplit = split -aSplit;
        int newStand = stand - aStand;
        int newConcealed = concealed - aConcealed;
        if (newWindow > 0 || newCover > 0 || newSplit > 0 || newStand > 0 || newConcealed > 0){
            newWork.setInterval(attemptedWork.getInterval());
            newWork.setWindow(newWindow);
            newWork.setCover(newCover);
            newWork.setSplit(newSplit);
            newWork.setStand(newStand);
            newWork.setConcealed(newConcealed);
            newWork.setDiscount(0);
            Log.i("TEST_JOB","TOTAL NEW || "+WorkUtility.getTextTotalNumberOfWork(newWork));
            if (WorkUtility.getIntTotalNumberOfWork(newWork) > 0) {
                return newWork;
            }
        }



        return null;
    }

    private void pushNewJob(Job job){
        //job.setJobId(null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(JOBS_PATH)
                .add(job).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                currentJob.setCurrentWork(attemptedWork);
                updateCurrentJob(currentJob);
            }
        });
    }

    private void updateCurrentJob(Job j){
        Log.i("TEST_JOB",j.getJobId());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(JOBS_PATH).document(j.getJobId())
                .set(j)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(getApplicationContext(),JobActivity.class);
                        i.putExtra(JOB_ID_KEY,j.getJobId());
                        startActivity(i);
                        finish();
                    }
                });
    }
}
