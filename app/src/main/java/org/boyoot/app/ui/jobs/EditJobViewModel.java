package org.boyoot.app.ui.jobs;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.job.Job;

public class EditJobViewModel extends ViewModel {


    private MutableLiveData<Job> jobMutableLiveData;
    private static final String JOBS_PATH = "jobs";


    public EditJobViewModel() {
        jobMutableLiveData = new MutableLiveData<>();
    }


    LiveData<Job> getJob(){
        return jobMutableLiveData;
    }


    void getJobContent(@NonNull String jobId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(JOBS_PATH).document(jobId);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Job j = documentSnapshot.toObject(Job.class);
                            j.setJobId(documentSnapshot.getId());
                            Log.i("TEST_JOB","CLOUD  ||  "+j.getJobId());
                            jobMutableLiveData.setValue(j);
                        }
                    }
                });
    }
}
