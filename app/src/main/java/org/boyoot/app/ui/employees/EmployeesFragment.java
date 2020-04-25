package org.boyoot.app.ui.employees;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.boyoot.app.R;
import org.boyoot.app.model.UserProfileModel;

import java.util.List;
import java.util.Objects;

public class EmployeesFragment extends Fragment implements EmployeesAdapter.ListItemClickListener {

    private EmployeesViewModel employeesViewModel;
    private EmployeesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeesViewModel =
                new ViewModelProvider(this).get(EmployeesViewModel.class);
        employeesViewModel.removeUsers();
        employeesViewModel.fetchUsers();
       // employeesViewModel.removeUsers();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.main_search_view).setVisibility(View.GONE);

        View root = inflater.inflate(R.layout.fragment_employees, container, false);
        adapter = new EmployeesAdapter(getContext(),this);
        RecyclerView recyclerView = root.findViewById(R.id.employees_recycler);
        recyclerView.setAdapter(adapter);

        employeesViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<UserProfileModel>>() {
            @Override
            public void onChanged(List<UserProfileModel> userProfileModels) {
                    adapter.setUsers(userProfileModels);
                    //TODO handle when activity rotate

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onListItemClickListener(int itemIndex) {

    }






}