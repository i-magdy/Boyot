package org.boyoot.app.ui.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.DialogAddTaskBinding;
import org.boyoot.app.model.Tasks;

public class AddTaskDialog extends AppCompatActivity {

    DialogAddTaskBinding binding;
    private static final String PROFILE_ID_KEY = "profile id";
    private static final String TASK_ID_KEY = "task id";
    private String profileId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_add_task);
       setSupportActionBar(binding.addTaskTb);
        setFinishOnTouchOutside(false);
        if (getIntent().hasExtra(PROFILE_ID_KEY)){
            profileId = getIntent().getStringExtra(PROFILE_ID_KEY);
        }else {
            finish();
        }
/*
         FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(profileId).collection("tasks")
                        .add(new Tasks("new Task 6","task content",1,"date"))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                            }
                        });
                        
 */

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
            Log.i("settings menu","clicked");
            //attemptCreateContact();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
