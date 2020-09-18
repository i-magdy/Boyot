package org.boyoot.app.ui.googleSheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentGoogleSheetFilterBinding;

public class GoogleSheetFilterFragment extends Fragment {

    private FragmentGoogleSheetFilterBinding binding;
    private GoogleSheetFilterViewModel viewModel;
    OnGoogleSheetFiltered onGoogleSheetFiltered;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.onGoogleSheetFiltered = (OnGoogleSheetFiltered) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_google_sheet_filter,container,false);
        viewModel = new ViewModelProvider(this).get(GoogleSheetFilterViewModel.class);
        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.google_sheet_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter_sheet){
            viewModel.getFilteredContacts().observe(getViewLifecycleOwner(),onGoogleSheetFiltered::onGoogleSheetFiltered);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}
