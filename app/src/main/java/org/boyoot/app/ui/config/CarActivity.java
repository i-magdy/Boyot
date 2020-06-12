package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityCarBinding;
import org.boyoot.app.model.UserProfileModel;
import org.boyoot.app.ui.employees.EmployeesAdapter;
import org.boyoot.app.ui.employees.ProfileActivity;

import java.util.List;
import java.util.Objects;

public class CarActivity extends AppCompatActivity implements EmployeesAdapter.ListItemClickListener {

    ActivityCarBinding binding;
    private CarViewModel viewModel;

    private static final String PATH_NUMBER = "pathNumber";
    private static final String BRANCH_ID_KEY = "branch id key";
    private static final String PROFILE_EMAIL_KEY = "profile email";
    private String BRANCH;
    private int pathNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_car);
        viewModel = new ViewModelProvider(this).get(CarViewModel.class);
        if (getIntent().hasExtra(PATH_NUMBER)) {
            pathNo = Objects.requireNonNull(getIntent().getIntExtra(PATH_NUMBER,0));
            BRANCH = getIntent().getStringExtra(BRANCH_ID_KEY);
            viewModel.fetchUsers(pathNo,BRANCH);
        }
        EmployeesAdapter adapter = new EmployeesAdapter(getApplicationContext(),this);
        binding.employeesRecycler.setAdapter(adapter);
        viewModel.getUsers().observe(this, new Observer<List<UserProfileModel>>() {
            @Override
            public void onChanged(List<UserProfileModel> userProfileModels) {
                adapter.setUsers(userProfileModels);
            }
        });

        binding.carEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddCarDialog.class);
                i.putExtra(PATH_NUMBER,pathNo);
                i.putExtra(BRANCH_ID_KEY,BRANCH);
                startActivity(i);
            }
        });
    }

    @Override
    public void onListItemClickListener(String email) {
        Intent i = new Intent(getApplicationContext() , ProfileActivity.class);
        i.putExtra(PROFILE_EMAIL_KEY,email);
        startActivity(i);
    }
}
