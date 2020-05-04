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
    public TasksViewModel() {
        tasks = new MutableLiveData<>();
        accountId = new MutableLiveData<>();
    }

    public LiveData<List<Tasks>> getListTasks(){
        return tasks;
    }

    public LiveData<String> getProfileId(){
        return accountId;
    }

    void getId(final String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users");
        Query query = ref.whereEqualTo("email",email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!Objects.requireNonNull(task.getResult()).isEmpty()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            getTasks(documentSnapshot.getId());
                            accountId.setValue(documentSnapshot.getId());

                        }
                    }
                }
            }
        });
    }
    private void getTasks(final String id){
        List<Tasks> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users").document(id).collection("tasks").orderBy("done");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
            }
        });
    }
}
