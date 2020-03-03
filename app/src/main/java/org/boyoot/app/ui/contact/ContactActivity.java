package org.boyoot.app.ui.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.databinding.ActivityContactBinding;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    String contactCloudId;
    private static final String contactIdKey = "contactId";
    private ContactViewModel viewModel;
    private ActivityContactBinding binding;

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


    }

    private void setPriorityState(String state){
        binding.contactProgressBar.setVisibility(View.INVISIBLE);
        switch (state){
            case "1":
                binding.priorityTagTv.setText(getString(R.string.state_location_needed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.location_needed_tag));
                break;
            case "3":
                binding.priorityTagTv.setText(getString(R.string.state_prepared_contact));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.prepared_contact_tag));
                break;
            case "4":
                binding.priorityTagTv.setText(getString(R.string.state_date_picked));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.date_picked_tag));
                break;
            case "5":
                binding.priorityTagTv.setText(getString(R.string.state_date_approved));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.date_approved_tag));
                break;
            case "6":
                binding.priorityTagTv.setText(getString(R.string.state_work_delayed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.work_delayed_tag));
                break;
            case "7":
                binding.priorityTagTv.setText(getString(R.string.state_work_done));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.work_done_tag));
                break;
            case "8":
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
