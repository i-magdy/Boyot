package org.boyoot.app.ui.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.DialogAddTaskBinding;
import org.boyoot.app.model.Tasks;

public class AddTaskDialog extends AppCompatActivity {

    DialogAddTaskBinding binding;
    TasksViewModel viewModel;
    private static final String PROFILE_ID_KEY = "profile id";
    private static final String TASK_ID_KEY = "task id";
    private String profileId;
    private String taskId;
    private Tasks currentTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_add_task);
        setSupportActionBar(binding.addTaskTb);
        setFinishOnTouchOutside(false);

        viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        if (getIntent().hasExtra(PROFILE_ID_KEY)){
            profileId = getIntent().getStringExtra(PROFILE_ID_KEY);
            if (getIntent().hasExtra(TASK_ID_KEY)){
                taskId = getIntent().getStringExtra(TASK_ID_KEY);
                viewModel.getTask(profileId,taskId);
            }else {
                taskId = "NULL";
            }
        }else {
            finish();
        }

        viewModel.showTask().observe(this, new Observer<Tasks>() {
            @Override
            public void onChanged(Tasks tasks) {
                if (tasks != null){
                    binding.taskTitleEt.setText(tasks.getTitle());
                    binding.taskContentEt.setText(tasks.getContent());
                    currentTask = tasks;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_contact_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_contact) {
            if (taskId.equals("NULL")){
                //add
                saveTask();
            }else{

                updateTask();
            }
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void saveTask(){
        String title = binding.taskTitleEt.getEditableText().toString();
        String content = binding.taskContentEt.getEditableText().toString();
        if (title.isEmpty()){
            binding.taskTitleEt.setError(getString(R.string.empty_message));
        }

        if (content.isEmpty()){
            binding.taskContentEt.setError(getString(R.string.empty_message));
        }
        if (!title.isEmpty() && ! content.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(profileId).collection("tasks")
                    .add(new Tasks(title, content))
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                finish();
                            }
                        }
                    });
        }
    }

    private void updateTask(){
        Tasks taskOb = new Tasks();
        String title = binding.taskTitleEt.getEditableText().toString();
        String content = binding.taskContentEt.getEditableText().toString();
        if (title.isEmpty()){
           binding.taskTitleEt.setError(getString(R.string.empty_message));
        }

        if (content.isEmpty()){
            binding.taskContentEt.setError(getString(R.string.empty_message));
        }
        if (!title.isEmpty() && ! content.isEmpty()) {
            taskOb.setTitle(title);
            taskOb.setContent(content);
            if (title.equals(currentTask.getTitle()) && content.equals(currentTask.getContent())){
                taskOb = currentTask;
                Log.i("task_update",title+"...."+currentTask.getTitle());
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(profileId).collection("tasks").document(taskId)
                    .set(taskOb)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    });

        }
    }
}
