package org.boyoot.app.ui.jobs;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.job.Job;

public class JobSettingsViewModel extends ViewModel {

    private MutableLiveData<Integer> priority;
    private MutableLiveData<String> author;

    private static final String JOBS_PATH = "jobs";

    public JobSettingsViewModel() {
        priority = new MutableLiveData<>();
        author = new MutableLiveData<>();
    }

    LiveData<Integer> getPriority(){
        return priority;
    }

    LiveData<String> getAuthor(){
        return author;
    }

    void getJob(@NonNull String jobId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(JOBS_PATH).document(jobId);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Job job = documentSnapshot.toObject(Job.class);
                            priority.setValue(job.getPriority());
                        }
                    }
                });
    }
}
