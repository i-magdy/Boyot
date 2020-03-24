package org.boyoot.app.ui.employees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import org.boyoot.app.R;

import java.util.Objects;

public class EmployeesFragment extends Fragment {

    private EmployeesViewModel employeesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.search_view_bar).setVisibility(View.GONE);
        employeesViewModel =
                new ViewModelProvider(this).get(EmployeesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_employees, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        employeesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }
}