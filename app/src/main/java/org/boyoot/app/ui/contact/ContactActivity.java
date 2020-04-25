package org.boyoot.app.ui.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityContactBinding;
import org.boyoot.app.model.Geocode;


public class ContactActivity extends AppCompatActivity {

    String contactCloudId;
    private static final String contactIdKey = "contactId";
    private ContactViewModel viewModel;
   // private String phone;
    private ActivityContactBinding binding;
    //private  Intent call;

   // private boolean isBottomExpended = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);
        viewModel = new ContactViewModel(getApplication());
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        if (getIntent().hasExtra(contactIdKey)){
            contactCloudId = getIntent().getStringExtra(contactIdKey);
            viewModel.fetchContact(contactCloudId);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);
            viewModel.getPriority().observe(this, this::setPriorityState);
        }
        binding.editContactFab.setOnClickListener(v -> editContact());

       /* binding.contactBottomSheet.expendBottomSheet.setBackground(getDrawable(R.drawable.arrowbottom));
        binding.contactBottomSheet.expendBottomSheet.setOnClickListener(v -> {
                if (!isBottomExpended) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    isBottomExpended = true;
                } else {
                    isBottomExpended = false;
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }

        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                    if (i == 3){
                        isBottomExpended = true;
                    }else if (i == 4){
                        isBottomExpended = false;
                    }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v >0.5){
                    binding.contactBottomSheet.expendBottomSheet.setBackground(getDrawable(R.drawable.close));
                    binding.contactBottomSheet.expendBottomSheet.animate().rotation(90*v).start();
                }else{
                    binding.contactBottomSheet.expendBottomSheet.setBackground(getDrawable(R.drawable.arrowbottom));
                    binding.contactBottomSheet.expendBottomSheet.animate().rotation(180*v).start();
                    binding.contactBottomSheet.expendBottomSheet.animate().rotation(180).start();
                }
            }
        });

        viewModel.getPhone().observe(this, s -> {
            if (!s.isEmpty()){
                phone=s;
            }
        });

*/





    }


    private void setPriorityState(String state){
        binding.contactProgressBar.setVisibility(View.INVISIBLE);
        switch (state){
            case "1":

                binding.priorityTagTv.setText(getString(R.string.state_location_needed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.location_needed_tag));
                break;
            case "3":
                binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
                binding.priorityTagTv.setText(getString(R.string.state_prepared_contact));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.prepared_contact_tag));
                break;
            case "4":
                binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
                binding.priorityTagTv.setText(getString(R.string.state_date_picked));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.date_picked_tag));
                break;
            case "5":
                binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
                binding.priorityTagTv.setText(getString(R.string.state_date_approved));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.date_approved_tag));
                break;
            case "6":
                binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
                binding.priorityTagTv.setText(getString(R.string.state_work_delayed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.work_delayed_tag));
                break;
            case "7":
                binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
                binding.priorityTagTv.setText(getString(R.string.state_work_done));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.work_done_tag));
                break;
            case "8":
                binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
                binding.priorityTagTv.setText(getString(R.string.state_reviewed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.reviewed_tag));
                break;

        }
    }
    private void editContact(){
        Intent i = new Intent(getApplicationContext(),EditContactActivity.class);
        i.putExtra(contactIdKey,contactCloudId);
        startActivity(i);
        finish();
    }




}
