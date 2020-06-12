package org.boyoot.app.ui.home;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.boyoot.app.R;
import org.boyoot.app.model.Contact;
import org.boyoot.app.ui.contact.ContactActivity;
import org.boyoot.app.ui.googleSheet.GoogleSheetActivity;
import org.boyoot.app.ui.locationNeeded.LocationNeededActivity;
import org.boyoot.app.ui.newJobs.NewJobsActivity;
import org.boyoot.app.ui.preparedContacts.PreparedContactsActivity;


import java.util.Objects;
import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;

public class HomeFragment extends Fragment implements MaterialButton.OnClickListener {

    private HomeViewModel homeViewModel;
    private String searchText;
    private String contactId;
    private static final String contactIdKey = "contactId";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        requireActivity().findViewById(R.id.main_search_view).setVisibility(View.VISIBLE);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView contactNotFoundTv = root.findViewById(R.id.contact_not_found_message_tv);
        final TextView contactTV = root.findViewById(R.id.contact_search_result_tv);
        final MaterialCardView cv = root.findViewById(R.id.contact_search_cv);
        SearchView search = requireActivity().findViewById(R.id.main_search_view);
        FrameLayout bottomSheet = root.findViewById(R.id.search_frame_layout);
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() > 1){
                    searchText = query;
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (query.matches("[0-9]+")){
                        homeViewModel.getContactByPhoneNumber(getValidPhoneNumber(query));
                    }else{
                        homeViewModel.getContactById(query);
                    }
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1){
                    searchText = newText;
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (newText.matches("[0-9]+")){
                        homeViewModel.getContactByPhoneNumber(getValidPhoneNumber(newText));
                    }else{
                        homeViewModel.getContactById(newText);
                    }
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                return false;
            }
        });

        homeViewModel.getContactFromSearch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String id) {
                if (id != null){
                    contactNotFoundTv.setVisibility(View.INVISIBLE);
                    contactTV.setText(searchText);
                    cv.setVisibility(View.VISIBLE);
                    contactId = id;
                }else {
                    contactNotFoundTv.setVisibility(View.VISIBLE);
                    cv.setVisibility(View.INVISIBLE);
                }
            }
        });
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ContactActivity.class);
                i.putExtra(contactIdKey,contactId);
                cv.setVisibility(View.INVISIBLE);
                startActivity(i);
            }
        });
        MaterialButton mNewContactButton = root.findViewById(R.id.new_contacts_button);
        MaterialButton mLocationNeededButton = root.findViewById(R.id.location_needed_button);
        MaterialButton mPreparedContactButton = root.findViewById(R.id.prepared_contact_button);
        MaterialButton mNewJobs = root.findViewById(R.id.new_jobs_button);
        MaterialButton mDatePickedButton = root.findViewById(R.id.date_picked_button);
        MaterialButton mDateApprovedButton = root.findViewById(R.id.date_approved_button);
        MaterialButton mWorkDelayButton = root.findViewById(R.id.work_delay_button);
        MaterialButton mWorkDoneButton = root.findViewById(R.id.work_done_button);
        mNewContactButton.setOnClickListener(this);
        mLocationNeededButton.setOnClickListener(this);
        mPreparedContactButton.setOnClickListener(this);
        mNewJobs.setOnClickListener(this);
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
                break;
            case R.id.prepared_contact_button :
                startActivity(new Intent(getContext(), PreparedContactsActivity.class));
                break;
            case R.id.new_jobs_button:
                startActivity(new Intent(getContext(), NewJobsActivity.class));
                break;
            case R.id.date_picked_button :
            case R.id.date_approved_button :
            case R.id.work_delay_button :
            case R.id.work_done_button:
            default:
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setActionView(R.layout.sync);
        ImageView view= (ImageView) item.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // viewModel.syncContacts();
                //Toast.makeText(getContext(),"hey",Toast.LENGTH_SHORT).show();
                //binding.appBarContent.syncProgressBar.setVisibility(View.VISIBLE);
                homeViewModel.syncContacts();
                homeViewModel.syncJobs();
                view.animate().rotation(-360).setDuration(1500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setRotation(360);
                       // binding.appBarContent.syncProgressBar.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();

            }
        });

    }
}