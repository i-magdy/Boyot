package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityBranchesConfigBinding;
import org.boyoot.app.model.Branch;

import java.util.List;

public class BranchesConfigActivity extends AppCompatActivity implements BranchesAdapter.OnListItemClicked {

    ActivityBranchesConfigBinding binding;
    private static final String BRANCH_ID_KEY = "branch id key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branches_config);
        BranchConfigViewModel viewModel = new ViewModelProvider(this).get(BranchConfigViewModel.class);
        viewModel.getBranches();
        BranchesAdapter adapter = new BranchesAdapter(this);
        binding.branchRecycler.setAdapter(adapter);
        viewModel.getBranchesList().observe(this, adapter::setBranchesList);
    }

    @Override
    public void onItemClickedListener(String branchId) {
        Intent i = new Intent(this,BranchActivity.class);
        i.putExtra(BRANCH_ID_KEY,branchId);
        startActivity(i);
    }
}
