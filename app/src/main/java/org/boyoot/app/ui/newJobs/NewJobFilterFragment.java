package org.boyoot.app.ui.newJobs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.boyoot.app.R;
import org.boyoot.app.databinding.JobFilterOptionContentBinding;
import org.boyoot.app.utilities.CityUtility;

public class NewJobFilterFragment extends Fragment {

    private JobFilterOptionContentBinding binding;
    private BottomSheetBehavior sheetBehavior;
    private ArrayAdapter<CharSequence> cityAdapter;
    ArrayAdapter<CharSequence> intervalAdapter;
    private NewJobsFilterViewModel viewModel;

    OnNewJobsFiltered onNewJobsFiltered;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.onNewJobsFiltered = (OnNewJobsFiltered) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.job_filter_option_content,container,false);
        viewModel = new ViewModelProvider(this).get(NewJobsFilterViewModel.class);
        FragmentContainerView bottomSheet = requireActivity().findViewById(R.id.new_job_filter_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        cityAdapter= ArrayAdapter.createFromResource(requireContext(),R.array.cities_array,android.R.layout.simple_spinner_dropdown_item);
        intervalAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.date_array,android.R.layout.simple_spinner_dropdown_item);
        binding.newJobFilterLocationSpinner.setAdapter(cityAdapter);
        binding.newJobFilterIntervalSpinner.setAdapter(intervalAdapter);
        binding.newJobFilterLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (!parent.getSelectedItem().equals("Choose City")) {
                    viewModel.getSortedJobsByBranch(CityUtility.getBranchCode(parent.getSelectedItem().toString())).observe(getViewLifecycleOwner(), onNewJobsFiltered::onJobsFiltered);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.newJobFilterIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!binding.newJobFilterLocationSpinner.getSelectedItem().equals("Choose City")
                && !parent.getSelectedItem().equals("Choose interval")){
                    viewModel.getSortedJobsByBranchByInterval(CityUtility.getBranchCode(binding.newJobFilterLocationSpinner.getSelectedItem().toString()),
                            parent.getSelectedItem().toString())
                            .observe(getViewLifecycleOwner(),onNewJobsFiltered::onJobsFiltered);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.resetFilteredNewJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getJobs().observe(getViewLifecycleOwner(),onNewJobsFiltered::onResetFilterSelect);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                binding.newJobFilterLocationSpinner.setSelection(0);
                binding.newJobFilterIntervalSpinner.setSelection(0);
            }
        });

        return binding.getRoot();
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.job_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_job_filter_option){
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}
