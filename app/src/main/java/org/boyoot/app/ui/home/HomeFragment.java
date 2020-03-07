package org.boyoot.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;


import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.ui.googleSheet.GoogleSheetActivity;
import org.boyoot.app.ui.locationNeeded.LocationNeededActivity;

import java.util.List;

public class HomeFragment extends Fragment implements MaterialButton.OnClickListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        MaterialButton mNewContactButton = root.findViewById(R.id.new_contacts_button);
        MaterialButton mLocationNeededButton = root.findViewById(R.id.location_needed_button);
        MaterialButton mPreparedContactButton = root.findViewById(R.id.prepared_contact_button);
        MaterialButton mDatePickedButton = root.findViewById(R.id.date_picked_button);
        MaterialButton mDateApprovedButton = root.findViewById(R.id.date_approved_button);
        MaterialButton mWorkDelayButton = root.findViewById(R.id.work_delay_button);
        MaterialButton mWorkDoneButton = root.findViewById(R.id.work_done_button);
        mNewContactButton.setOnClickListener(this);
        mLocationNeededButton.setOnClickListener(this);
        mPreparedContactButton.setOnClickListener(this);
        mDatePickedButton.setOnClickListener(this);
        mDateApprovedButton.setOnClickListener(this);
        mWorkDelayButton.setOnClickListener(this);
        mWorkDoneButton.setOnClickListener(this);
           /* @Override
            public void onClick(View v) {

            }
        });*/
        return root;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.new_contacts_button:
                Intent i = new Intent(getContext(), GoogleSheetActivity.class);
                startActivity(i);
                break;
            case R.id.location_needed_button:
                startActivity(new Intent(getContext(), LocationNeededActivity.class));
            case R.id.prepared_contact_button :
            case R.id.date_picked_button :
            case R.id.date_approved_button :
            case R.id.work_delay_button :
            case R.id.work_done_button:
            default:
                break;
        }

    }
}