package org.boyoot.app.ui.employees;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.model.Tasks;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksViewModel extends ViewModel {

    private MutableLiveData<List<Tasks>> tasks;
    private MutableLiveData<String> accountId;
    private MutableLiveData<Tasks> taskMLData;
    private MutableLiveData<String> role;

    private static final String USERS_PATH="users";
    private static final String TASKS_PATH = "tasks";

    public TasksViewModel() {
        tasks = new MutableLiveData<>();
        accountId = new MutableLiveData<>();
        taskMLData = new MutableLiveData<>();
        role = new MutableLiveData<>();
    }

    public LiveData<List<Tasks>> getListTasks(){
        return tasks;
    }

    public LiveData<String> getProfileId(){
        return accountId;
    }
    public LiveData<Tasks> showTask(){
        return taskMLData;
    }

    public LiveData<String> getRole(){
        return role;
    }

    void getId(final String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(USERS_PATH);
        Query query = ref.whereEqualTo("email",email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!Objects.requireNonNull(task.getResult()).isEmpty()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            getTasks(documentSnapshot.getId());
                            accountId.setValue(documentSnapshot.getId());
                            role.setValue(documentSnapshot.getString("role"));

                        }
                    }
                }
            }
        });
    }
    private void getTasks(final String id){
        List<Tasks> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users").document(id).collection(TASKS_PATH).orderBy("done");//.orderBy("date", Query.Direction.DESCENDING);
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = change.getDocument();
                            if (documentSnapshot.exists()) {
                                Tasks task = documentSnapshot.toObject(Tasks.class);
                                task.setId(documentSnapshot.getId());
                                list.add(task);
                                tasks.setValue(list);
                            }
                        }
                    }
                });
    }

    void getTask(final String profileId,final String taskId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("users").document(profileId).collection("tasks").document(taskId);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null){
                        Tasks t = snapshot.toObject(Tasks.class);
                        taskMLData.setValue(t);

                    }
                }
            }
        });
    }
}
