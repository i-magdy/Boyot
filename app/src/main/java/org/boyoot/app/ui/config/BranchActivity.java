package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityBranchBinding;
import org.boyoot.app.model.Car;

import java.util.List;
import java.util.Objects;

public class BranchActivity extends AppCompatActivity implements CarsAdapter.OnItemClickListener {

    ActivityBranchBinding binding;
    BranchViewModel viewModel;
    private List<Car> list;
    private static final String BRANCH_ID_KEY = "branch id key";
    private static final String BRANCHES_PATH="branches";
    private static final String PATH_NUMBER = "pathNumber";

    private String BRANCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_branch);
        viewModel = new ViewModelProvider(this).get(BranchViewModel.class);
        if (getIntent().hasExtra(BRANCH_ID_KEY)){
            BRANCH =Objects.requireNonNull(getIntent().getStringExtra(BRANCH_ID_KEY));
            viewModel.getBranch(BRANCH);
        }else {
            finish();
        }
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        CarsAdapter adapter = new CarsAdapter(this,this);
        adapter.setBranch(BRANCH);
        binding.branchRecycler.setAdapter(adapter);
        viewModel.getCarsList().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                binding.mainBranchLayout.setVisibility(View.VISIBLE);
                if (cars != null) {
                    adapter.setList(cars);
                    list = cars;
                }
            }
        });

        binding.addNewCarFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddCarDialog.class);
                i.putExtra(BRANCH_ID_KEY,BRANCH);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemClickListener(int index) {
        Intent i  = new Intent(getApplicationContext(),CarActivity.class);
        i.putExtra(PATH_NUMBER,list.get(index).getPathNo());
        i.putExtra(BRANCH_ID_KEY,BRANCH);
        startActivity(i);
    }

    @Override
    public void updateCarList() {
        viewModel.getBranch(BRANCH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getBranch(BRANCH);
    }
}