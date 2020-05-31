package org.boyoot.app.ui.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import org.boyoot.app.R;
import org.boyoot.app.data.DirectionClient;
import org.boyoot.app.databinding.ActivityContactBinding;
import org.boyoot.app.model.direction.Direction;
import org.boyoot.app.model.job.Directions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactActivity extends AppCompatActivity {

    String contactCloudId;
    private static final String contactIdKey = "contactId";
    private ContactViewModel viewModel;
   // private String phone;
    private ActivityContactBinding binding;
    private String role;
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        role = sharedPref.getString(getString(R.string.saved_role_value_key), "user");
        binding.editContactFab.setOnClickListener(v -> editContact());
        if (role.equals("admin")){
            //binding.editContactFab.hide();
        }
      /*  DirectionClient.getINSTANCE().getDirection("place_id:GhIJ16NwPQqpNUARIbByaJGaQ0A",
                "place_id:GhIJEFg5tMiUNUARaJHtfD-YQ0A",
                "AIzaSyCD1jO0-JySq-LYOZIm69olvESKAexNJrw").enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                Log.i("DIRECTION_API",response.toString());
                Direction direction = response.body();
                Directions directions = new Directions();
                directions.setDistance(direction.getRoutes().getLegs().getDistance());
                directions.setDuration(direction.getRoutes().getLegs().getDuration());
                directions.setSteps(direction.getRoutes().getLegs().getSteps());

            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

                Log.i("DIRECTION_API",t.getMessage());
            }
        });

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
       if(!state.equals("1")){
           binding.contentLayout.materialCardView.setVisibility(View.VISIBLE);
       }
    }
    private void editContact(){
        Intent i = new Intent(getApplicationContext(),EditContactActivity.class);
        i.putExtra(contactIdKey,contactCloudId);
        startActivity(i);
        finish();
    }




}
