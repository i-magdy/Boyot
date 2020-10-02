package org.boyoot.app.ui.jobs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentJobSettingsBinding;
import org.boyoot.app.ui.appointment.AvailableAppointmentsActivity;

import java.util.Objects;

import io.grpc.internal.AbstractReadableBuffer;

public class JobSettingsBottomSheetFragment extends Fragment implements View.OnLongClickListener , OnStateChanged{


    private FragmentJobSettingsBinding binding;
    private BottomSheetBehavior sheetBehavior;
    private JobSettingsViewModel viewModel;
    private int priority;
    private String jobId;

    private static final String JOB_ID_KEY = "job id key";
    private static final String JOBS_PATH = "jobs";


    OnStateChanged onStateChanged;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.onStateChanged = (OnStateChanged) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_settings,container,false);
        viewModel = new ViewModelProvider(this).get(JobSettingsViewModel.class);
        jobId = Objects.requireNonNull(requireActivity().getIntent().getStringExtra(JOB_ID_KEY));
        viewModel.getJob(jobId);

        FragmentContainerView bottomSheet = requireActivity().findViewById(R.id.job_settings_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        viewModel.getPriority().observe(getViewLifecycleOwner(), this::fillPriorityState);

        binding.toAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AvailableAppointmentsActivity.class);
                i.putExtra(JOB_ID_KEY,requireActivity().getIntent().getStringExtra(JOB_ID_KEY));
                startActivity(i);
            }
        });

        binding.editJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditJobActivity.class);
                i.putExtra(JOB_ID_KEY,requireActivity().getIntent().getStringExtra(JOB_ID_KEY));
                startActivity(i);
            }
        });

        binding.appointmentApprovedItem.setOnLongClickListener(this);
        binding.workCanceledItem.setOnLongClickListener(this);
        binding.workDelayItem.setOnLongClickListener(this);
        return binding.getRoot();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.job_sheet_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_job_bottom_sheet){

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    void fillPriorityState(int p){
        priority = p ;
        switch (p){
            case 0:
                binding.removeAppointmentButton.setVisibility(View.GONE);
                binding.appointmentApprovedItem.setVisibility(View.GONE);
                binding.appointmentApprovedCheckbox.setChecked(false);
                binding.workDelayRadio.setChecked(false);
                binding.workCanceledRadio.setChecked(false);
                break;
            case 1:
            case 2:
                binding.removeAppointmentButton.setVisibility(View.VISIBLE);
                binding.appointmentApprovedCheckbox.setChecked(false);
                binding.workDelayRadio.setChecked(false);
                binding.workCanceledRadio.setChecked(false);
                break;
            case 3:
                binding.appointmentApprovedCheckbox.setChecked(false);
                binding.workDelayRadio.setChecked(false);
                binding.workCanceledRadio.setChecked(false);
                binding.removeAppointmentButton.setVisibility(View.VISIBLE);
                break;

            case 4:
                binding.removeAppointmentButton.setVisibility(View.VISIBLE);
                binding.appointmentApprovedCheckbox.setChecked(true);
                binding.workDelayRadio.setChecked(false);
                binding.workCanceledRadio.setChecked(false);
                break;
            case 5:
                //TODO-workDone
                binding.removeAppointmentButton.setVisibility(View.GONE);
                binding.appointmentApprovedCheckbox.setChecked(true);
                break;
            case 6:
                binding.workDelayRadio.setChecked(true);
                binding.appointmentApprovedCheckbox.setChecked(false);
                binding.workCanceledRadio.setChecked(false);
                binding.removeAppointmentButton.setVisibility(View.GONE);
                binding.appointmentApprovedItem.setVisibility(View.GONE);
                break;
            case 7:
                binding.workCanceledRadio.setChecked(true);
                binding.appointmentApprovedCheckbox.setChecked(false);
                binding.workDelayRadio.setChecked(false);
                binding.removeAppointmentButton.setVisibility(View.GONE);
                binding.appointmentApprovedItem.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }
    private void changeCancelState(boolean b){
        int p = 7;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p=0;
        }
        db.collection(JOBS_PATH).document(jobId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
                    //binding.workCanceledRadio.setChecked(!b);
                    onStateChanged.onStateChanged();
                    viewModel.getJob(jobId);
                    //author(contactId,currentUser.getEmail());
        });
    }

    private void changeDelayedState(boolean b){
        int p = 6;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p=0;
        }
        db.collection(JOBS_PATH).document(jobId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
           // binding.workDelayRadio.setChecked(!b);
            onStateChanged.onStateChanged();
            viewModel.getJob(jobId);
            //author(contactId,currentUser.getEmail());
        });
    }
    private void changeConfirmAppointmentState(boolean b){
        int p = 4;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p=3;
        }
        db.collection(JOBS_PATH).document(jobId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
            //binding.appointmentApprovedCheckbox.setChecked(!b);
            onStateChanged.onStateChanged();
            viewModel.getJob(jobId);
            //author(contactId,currentUser.getEmail());
        });
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.appointment_approved_item:
                if (priority == 1){
                   // changeConfirmAppointmentState(false);
                    showDialog(getString(R.string.appointment_approved),getString(R.string.confirm_appointment_message),0);
                }

                if (priority == 2) {
                    showDialog(getString(R.string.appointment_approved),getString(R.string.unconfirm_appointment_message),1);
                }
                break;
            case R.id.work_delay_item:
                if (priority != 6){
                    showDialog(getString(R.string.delay_job_title),getString(R.string.delay_job_massege),2);
                }else {
                    showDialog(getString(R.string.delay_job_title),getString(R.string.cancel_delay_massege),3);
                }
                break;
            case R.id.work_canceled_item :
                if (priority != 7){
                    showDialog(getString(R.string.cancel_job_title),getString(R.string.cancel_job_massege),4);
                }else {
                    showDialog(getString(R.string.cancel_job_title),getString(R.string.uncancel_job_massege),5);

                }
                break;
            default:
                break;

        }
        return false;
    }


    void showDialog(String title,String message,int state){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(),R.style.AlertDialog);
        builder.setMessage(message)
                .setTitle(title);

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (state) {
                    case 0:
                        changeConfirmAppointmentState(false);
                        break;
                    case 1:
                        changeConfirmAppointmentState(true);
                        break;
                    case 2:
                        changeDelayedState(false);
                        break;
                    case 3:
                        changeDelayedState(true);
                        break;
                    case 4:
                        changeCancelState(false);
                        break;
                    case 5 :
                        changeCancelState(true);
                        break;
                    default:
                        break;

                }


                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onStateChanged() {


    }
}
