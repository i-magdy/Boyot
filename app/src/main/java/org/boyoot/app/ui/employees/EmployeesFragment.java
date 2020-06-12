package org.boyoot.app.ui.employees;

import android.content.Context;
import android.content.Intent;
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
    private List<UserProfileModel> profiles;
    private static final String PROFILE_EMAIL_KEY = "profile email";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // employeesViewModel.removeUsers();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.main_search_view).setVisibility(View.GONE);
        employeesViewModel =
                new ViewModelProvider(this).get(EmployeesViewModel.class);
        employeesViewModel.removeUsers();
        employeesViewModel.fetchUsers();
        View root = inflater.inflate(R.layout.fragment_employees, container, false);
        adapter = new EmployeesAdapter(getContext(),this);
        RecyclerView recyclerView = root.findViewById(R.id.employees_recycler);
        recyclerView.setAdapter(adapter);

        employeesViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<UserProfileModel>>() {
            @Override
            public void onChanged(List<UserProfileModel> userProfileModels) {
                if (userProfileModels.size() > 0) {
                    adapter.setUsers(userProfileModels);
                    //TODO handle when activity rotate
                    profiles = userProfileModels;
                }


            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onListItemClickListener(String email) {
        Intent i = new Intent(getActivity(),ProfileActivity.class);
        i.putExtra(PROFILE_EMAIL_KEY,email);
        startActivity(i);
    }






}