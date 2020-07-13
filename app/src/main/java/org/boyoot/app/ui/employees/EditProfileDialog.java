package org.boyoot.app.ui.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.DialogEditProfileBinding;
import org.boyoot.app.model.Car;
import org.boyoot.app.model.UserProfileModel;
import org.boyoot.app.ui.config.BranchViewModel;

import java.util.ArrayList;
import java.util.List;

import static org.boyoot.app.utilities.CityUtility.getBranchCode;

public class EditProfileDialog extends AppCompatActivity {

    DialogEditProfileBinding binding;
    private ProfileViewModel viewModel;
    private BranchViewModel branchViewModel;

    private static final String PROFILE_EMAIL_KEY = "profile email";
    private static final String USERS_PATH="users";

    ArrayAdapter<String> carAdapter;
    ArrayAdapter<CharSequence> branchAdapter;
    ArrayAdapter<CharSequence> rolesAdapter;
    private ArrayList<String> carList;
    private List<Car> carsList;
    private UserProfileModel currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_edit_profile);
        setSupportActionBar(binding.editProfileTb);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        branchViewModel = new ViewModelProvider(this).get(BranchViewModel.class);
        viewModel.getProfile(getIntent().getStringExtra(PROFILE_EMAIL_KEY));
        branchAdapter  = ArrayAdapter.createFromResource(this,R.array.cities_array,android.R.layout.simple_spinner_dropdown_item);
        rolesAdapter = ArrayAdapter.createFromResource(this,R.array.roles_array,android.R.layout.simple_spinner_dropdown_item);
        binding.branchSpinner.setAdapter(branchAdapter);
        binding.rolesSpinner.setAdapter(rolesAdapter);
        viewModel.getProfile().observe(this, new Observer<UserProfileModel>() {
            @Override
            public void onChanged(UserProfileModel userProfileModel) {
                fillProfileData(userProfileModel);
                currentProfile = userProfileModel;
            }
        });
        binding.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchViewModel.getBranch(getParent(),getBranchCode(parent.getSelectedItem().toString()),null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        branchViewModel.getCarsList().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                binding.carIv.setVisibility(View.VISIBLE);
                binding.carSpinner.setVisibility(View.VISIBLE);
                    fillCarList(cars);
                    carsList = cars;
                    if (currentProfile.getCar() != null && cars != null){
                        for (int i=0;i < cars.size();++i){
                            if (cars.get(i).getCarNumber().equals(currentProfile.getCar().getCarNumber())){
                                binding.carSpinner.setSelection(i);
                            }
                        }
                    }

            }
        });






    }

    void fillCarList(List<Car> list){
        String s;
        if (list != null) {
            carList = new ArrayList<>();
            for (Car car : list) {
                s = car.getTitle();
                carList.add(s);
            }
        }else {
            carList = new ArrayList<>();
            carList.add("NULL");
        }
        carAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,carList);
        binding.carSpinner.setAdapter(carAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_contact_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_contact) {
            updateProfile(currentProfile);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    void fillProfileData(UserProfileModel profile){
        if ( profile.getBranchId() != null) {
            switch (profile.getBranchId()) {
                case "D":
                    binding.branchSpinner.setSelection(1);
                    break;
                case "H":
                    binding.branchSpinner.setSelection(2);
                    break;
                case "J":
                    binding.branchSpinner.setSelection(3);
                    break;
                case "K":
                    binding.branchSpinner.setSelection(4);
                    break;
                case "L":
                    binding.branchSpinner.setSelection(5);
                    break;
                case "M":
                    binding.branchSpinner.setSelection(6);
                    break;
                case "N":
                    binding.branchSpinner.setSelection(7);
                    break;
                case "Q":
                    binding.branchSpinner.setSelection(8);
                    break;
                case "R":
                    binding.branchSpinner.setSelection(9);
                    break;
                case "W":
                    binding.branchSpinner.setSelection(10);
                    break;
                case "Y":
                    binding.branchSpinner.setSelection(11);
                    break;
                case "Z":
                    binding.branchSpinner.setSelection(12);
                    break;
            }
        }
            switch (profile.getRole()) {
                case "Admin":
                    binding.rolesSpinner.setSelection(0);
                    break;
                case "Manager":
                    binding.rolesSpinner.setSelection(1);
                    break;
                case "Moderator":
                    binding.rolesSpinner.setSelection(2);
                    break;
                case "Worker":
                    binding.rolesSpinner.setSelection(3);
                    break;
                case "User":
                    binding.rolesSpinner.setSelection(4);
                    break;
            }

        if (profile.getBranch() != null) {
            branchViewModel.getBranch(this,profile.getBranchId(),null);
        }
          /*  for (int i = 0; i < carsList.size(); ++i) {
                String s = carsList.get(i).getTitle();
                if (s.equals(profile.getCar().getTitle())) {
                    binding.carSpinner.setSelection(i);
                }
            }*/

    }

    void updateProfile(UserProfileModel profileModel){
        String role = binding.rolesSpinner.getSelectedItem().toString();
        String branch = binding.branchSpinner.getSelectedItem().toString();
        String carNumber = binding.carSpinner.getSelectedItem().toString();
        int i = binding.carSpinner.getSelectedItemPosition();
        profileModel.setRole(role);
        profileModel.setBranch(branch);
        profileModel.setBranchId(getBranchCode(branch));
        if (carsList != null) {
            profileModel.setCar(carsList.get(i));
        }else {
            profileModel.setCar(null);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(USERS_PATH).document(profileModel.getId());
        ref.set(profileModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
    }
}
