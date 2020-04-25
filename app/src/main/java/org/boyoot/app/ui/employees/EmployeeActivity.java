package org.boyoot.app.ui.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityEmployeeBinding;

public class EmployeeActivity extends AppCompatActivity {

    ActivityEmployeeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);

    }
}
