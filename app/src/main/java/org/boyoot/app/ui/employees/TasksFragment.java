package org.boyoot.app.ui.employees;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentProfileTasksBinding;
import org.boyoot.app.model.Tasks;
import org.boyoot.app.ui.jobs.EditJobActivity;
import org.boyoot.app.ui.jobs.JobActivity;

import java.util.List;

public class TasksFragment extends Fragment implements TasksAdapter.ItemClickListener{

    private FragmentProfileTasksBinding binding;
    private TasksViewModel viewModel;
    private static final String PROFILE_EMAIL_KEY = "profile email";
    private static final String PROFILE_ID_KEY = "profile id";
    private static final String TASK_ID_KEY = "task id";
    private static final String USERS_PATH="users";
    private static final String TASKS_PATH = "tasks";
    private String profileId;
    private String role;
    private  FirebaseUser user;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_tasks,container,false);
        viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        if (requireActivity().getIntent().hasExtra(PROFILE_EMAIL_KEY)) {
            viewModel.getId(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
        }else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            viewModel.getId(user.getEmail());
        }
        TasksAdapter adapter = new TasksAdapter(this,requireActivity());
        binding.tasksRecyclerView.setAdapter(adapter);
        binding.tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        role = sharedPref.getString(getString(R.string.saved_role_value_key), "user");

        adapter.setAdmin(role.equals("Admin"));
        if (!role.equals("Admin")){
            binding.addTaskFab.hide();
        }
        viewModel.getListTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                if (tasks.size() > 0){
                    adapter.setTasks(tasks);
                    binding.tasksLoadingProgress.setMax(tasks.size());
                    showProgress(tasks);
                }
            }
        });
        viewModel.getProfileId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                profileId = s;
                adapter.setProfileId(s);
            }
        });
        binding.addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AddTaskDialog.class);
                i.putExtra(PROFILE_ID_KEY,profileId);
                startActivity(i);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCheckClicked(Tasks task) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_PATH).document(profileId).collection(TASKS_PATH).document(task.getId())
                .update("done",!task.isDone())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (requireActivity().getIntent().hasExtra(PROFILE_EMAIL_KEY)){
                            viewModel.getId(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
                        }else{
                            viewModel.getId(user.getEmail());
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity().getIntent().hasExtra(PROFILE_EMAIL_KEY)){
            viewModel.getId(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
        }else{
            viewModel.getId(user.getEmail());
        }
    }


    @Override
    public void onItemClicked(Tasks task) {
        if (role.equals("Admin")) {
            Intent i = new Intent(getContext(), AddTaskDialog.class);
            i.putExtra(PROFILE_ID_KEY, profileId);
            i.putExtra(TASK_ID_KEY, task.getId());
            startActivity(i);
        }else {
           showTask(task);
        }
    }

    @Override
    public void syncTasks() {
        viewModel.getId(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
    }

    void showTask(Tasks task){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(task.getContent())
                .setTitle(task.getTitle());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(USERS_PATH).document(profileId).collection(TASKS_PATH).document(task.getId());
        ref.update("seen",true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }


    private void showProgress(List<Tasks> list){
        int progress = 0;
        binding.tasksLoadingProgress.setProgress(progress);
        for (int i=0;i<list.size();++i){
            if (list.get(i).isDone()){
                ++progress;
                binding.tasksLoadingProgress.setProgress(progress);
            }
        }
    }
}
