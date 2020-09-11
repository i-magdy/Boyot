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

import org.boyoot.app.model.Contact;
import org.boyoot.app.model.Price;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.utilities.WorkUtility;

public class EditJobViewModel extends ViewModel {



    private static final String JOBS_PATH = "jobs";
    private static final String CONTACTS_PATH = "contacts";
    private static final String CONFIG_PATH = "config";
    private static final String PRICE_PATH = "price";

    private MutableLiveData<Job> jobMutableLiveData;

    public EditJobViewModel() {

        jobMutableLiveData = new MutableLiveData<>();

    }


    LiveData<Job> getJob(){
        return jobMutableLiveData;
    }


    void fetchContact(String contactId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(CONTACTS_PATH).document(contactId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Contact contactObj = documentSnapshot.toObject(Contact.class);
                    if (contactObj != null) {
                        contactObj.setContactId(documentSnapshot.getId());
                        price(contactObj);
                    }
                }
            }
        });
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


    private void price(Contact c){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(CONFIG_PATH).document(PRICE_PATH);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Price priceObject = documentSnapshot.toObject(Price.class);
                    Job job = new Job(c.getId(),c.getContactId(),c.getPhone(),0,
                            c.getCity().getCityCode(),c.getCity().getCity(),c.getRegistrationDate(),false, WorkUtility.parseZeroWorkCounts(c.getWork().getInterval()),
                            null,c.getMapConfig(),priceObject);
                    jobMutableLiveData.setValue(job);
                }
            }
        });
    }
}
