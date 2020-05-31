package org.boyoot.app.ui.jobs;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.boyoot.app.model.Contact;
import org.boyoot.app.model.Price;
import org.boyoot.app.model.job.Job;

import java.util.ArrayList;
import java.util.List;

public class JobsListViewModel extends ViewModel {

    private MutableLiveData<Contact> contact;
    private MutableLiveData<Boolean> isJobFound;
    private MutableLiveData<List<Job>> jobs;
    private MutableLiveData<Price> price;
    private static final String CONTACTS_PATH = "contacts";
    private static final String JOBS_PATH = "jobs";
    private static final String CONFIG_PATH = "config";
    private static final String PRICE_PATH = "price";

    public JobsListViewModel() {
        contact = new MutableLiveData<>();
        isJobFound = new MutableLiveData<>();
        jobs = new MutableLiveData<>();
        price = new MutableLiveData<>();
    }

    LiveData<Contact> getContact(){
        return contact;
    }
    LiveData<Boolean> isJobExist(){
        return isJobFound;
    }
    LiveData<Price> getPrice(){
        return price;
    }
    LiveData<List<Job>> getJobsList(){
        return jobs;
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
                        contact.setValue(contactObj);
                        getJobs(contactObj);
                        price();
                    }
                }
            }
        });

    }

    private void getJobs(Contact c){

        List<Job> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection(JOBS_PATH);
        query= query.whereEqualTo("phone",c.getPhone());//.orderBy("timeStamp", Query.Direction.ASCENDING);
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        boolean found = false;
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = change.getDocument();
                            if (documentSnapshot.exists()) {
                                Job j = documentSnapshot.toObject(Job.class);
                                j.setJobId(documentSnapshot.getId());
                                list.add(j);
                                jobs.setValue(list);
                                if (c.getRegistrationDate().equals(j.getRegisterTime())){
                                    found = true;
                                }

                            }
                        }

                        if (!found){
                            isJobFound.setValue(false);
                        }else{
                            isJobFound.setValue(true);
                        }
                    }else {
                        isJobFound.setValue(false);
                    }
                });
    }


    private void price(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(CONFIG_PATH).document(PRICE_PATH);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Price priceObject = documentSnapshot.toObject(Price.class);
                    price.setValue(priceObject);

                }
            }
        });
    }
    void clearIsJobFound(){
        isJobFound.setValue(true);
    }
}
