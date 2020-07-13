package org.boyoot.app.ui.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityAddJobToAppointmentListBinding;
import org.boyoot.app.model.CurrentCalenderDate;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.model.job.TimePickerModel;

public class AddJobToAppointmentListActivity extends AppCompatActivity {

    private static final String CURRENT_CALENDER_KEY="current_calender";
    private AddToAppointmentViewModel viewModel;
    private ActivityAddJobToAppointmentListBinding binding;
    private static final int TIME_PICKER_CODE = 2;
    private static final String TIME_PICKER_KEY="time picked";
    private static final String JOBS_PATH = "jobs";

private OnDirectionChange onDirectionChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_job_to_appointment_list);
        viewModel = new ViewModelProvider(this).get(AddToAppointmentViewModel.class);
        if (getIntent().hasExtra(CURRENT_CALENDER_KEY)){
            CurrentCalenderDate calenderDate = (CurrentCalenderDate) getIntent().getSerializableExtra(CURRENT_CALENDER_KEY);
            viewModel.setCurrentCalender(calenderDate);
            viewModel.setDirectionsKey(getString(R.string.google_directions_key));
        }
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

       // DirectionsPreviewMap map = new DirectionsPreviewMap();
        viewModel.getMainJob().observe(this, new Observer<Job>() {
            @Override
            public void onChanged(Job job) {
           //     Log.i("TEST_APPOINTMENT",job.getDirections().getDuration().getText());
                //updateJob(job);
               //map.setDirections(job.getDirections());
                onDirectionChange.onSetDestinationPoints(Double.parseDouble(job.getMapConfig().getLat()),Double.parseDouble(job.getMapConfig().getLng()));
                onDirectionChange.onDirectionChanged(job.getDirections());
            }
        });

    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        this.onDirectionChange = (OnDirectionChange) fragment;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TIME_PICKER_CODE && resultCode == RESULT_OK){
            TimePickerModel time = (TimePickerModel) data.getSerializableExtra(TIME_PICKER_KEY);
            if (time != null){
                Log.i("TIME_PICKER",time.getHourOfDay()+" : "+time.getMin());
            }
        }
    }

    public void pickTime(View v){
        Intent i = new Intent(getApplicationContext(),TimePickerDialog.class);
        startActivityForResult(i,TIME_PICKER_CODE);
    }

    void updateJob(Job jj){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(JOBS_PATH).document(jj.getJobId())
                .set(jj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }
}