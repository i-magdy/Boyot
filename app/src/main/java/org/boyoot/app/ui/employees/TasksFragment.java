package org.boyoot.app.ui.employees;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentProfileTasksBinding;
import org.boyoot.app.model.Tasks;

import java.util.List;

public class TasksFragment extends Fragment implements TasksAdapter.ItemClickListener{

    private FragmentProfileTasksBinding binding;
    private TasksViewModel viewModel;
    private static final String PROFILE_EMAIL_KEY = "profile email";
    private static final String PROFILE_ID_KEY = "profile id";
    private static final String TASK_ID_KEY = "task id";
    private String profileId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_tasks,container,false);
        viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        viewModel.getId(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
        TasksAdapter adapter = new TasksAdapter(this,requireActivity());
        binding.tasksRecyclerView.setAdapter(adapter);
        binding.tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void onCheckClicked(int position) {


    }

    @Override
    public void onItemClicked(int position) {

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
