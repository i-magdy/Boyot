package org.boyoot.app.ui.jobs;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentJobSettingsBinding;
import org.boyoot.app.ui.appointment.AvailableAppointmentsActivity;

public class JobSettingsBottomSheetFragment extends Fragment {

    private FragmentJobSettingsBinding binding;
    private BottomSheetBehavior sheetBehavior;

    private static final String JOB_ID_KEY = "job id key";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_settings,container,false);
        FragmentContainerView bottomSheet = requireActivity().findViewById(R.id.job_settings_bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);


        binding.toAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AvailableAppointmentsActivity.class);
                i.putExtra(JOB_ID_KEY,requireActivity().getIntent().getStringExtra(JOB_ID_KEY));
                startActivity(i);
            }
        });


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

    /*private void changeCancelState(boolean b){
        String p = "3";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p="9";
        }
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
                    binding.sheetOptions.workCanceledRadio.setChecked(b);
                    author(contactId,currentUser.getEmail());
        });
    }

    private void changeDelayedState(boolean b){
        String p = "3";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p="6";
        }
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
            binding.sheetOptions.workDelayRadio.setChecked(b);
            author(contactId,currentUser.getEmail());
        });
    }
    private void changeDoneState(boolean b){
        String p = "3";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p="7";
        }
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
            binding.sheetOptions.workDoneCheckBox.setChecked(b);
            author(contactId,currentUser.getEmail());
        });
    }

    private void changeOfferState(boolean b){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update("work.offer",b).addOnSuccessListener(aVoid -> {
            binding.sheetOptions.offerCheckBox.setChecked(b);
            author(contactId,currentUser.getEmail());
        });
    }
    private void fillAppointmentField(String s){
        binding.sheetOptions.appointmentTv.setText(s);
    }

    void fillCostField(String s){
        binding.sheetOptions.costTv.setText(s);
    }*/

}
