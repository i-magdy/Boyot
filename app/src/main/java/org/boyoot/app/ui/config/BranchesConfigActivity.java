package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityBranchesConfigBinding;

public class BranchesConfigActivity extends AppCompatActivity {

    ActivityBranchesConfigBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branches_config);

    }
}
