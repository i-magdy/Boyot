package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityBranchesConfigBinding;
import org.boyoot.app.model.Branch;

import java.util.List;

public class BranchesConfigActivity extends AppCompatActivity implements BranchesAdapter.OnListItemClicked {

    ActivityBranchesConfigBinding binding;
    private BranchConfigViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branches_config);
        viewModel = new ViewModelProvider(this).get(BranchConfigViewModel.class);
        viewModel.getBranches();
        BranchesAdapter adapter = new BranchesAdapter(this);
        binding.branchRecycler.setAdapter(adapter);
        viewModel.getBranchesList().observe(this, new Observer<List<Branch>>() {
            @Override
            public void onChanged(List<Branch> branches) {
                adapter.setBranchesList(branches);
            }
        });
    }

    @Override
    public void onItemClickedListener(int index) {


    }
}
