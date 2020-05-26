package org.boyoot.app.ui.appointment;

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
import org.boyoot.app.model.job.Job;

public class AppointmentListViewModel extends ViewModel {

    private MutableLiveData<Contact> contact;
    private MutableLiveData<Boolean> isJobFound;
    private static final String CONTACTS_PATH = "contacts";
    private static final String JOBS_PATH = "jobs";


    public AppointmentListViewModel() {
        contact = new MutableLiveData<>();
        isJobFound = new MutableLiveData<>();
    }

    LiveData<Contact> getContact(){
        return contact;
    }
    LiveData<Boolean> isJobExist(){
        return isJobFound;
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
                    }
                }
            }
        });

    }

    void getJobs(Contact c){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection(JOBS_PATH);
        query= query.whereEqualTo("phone",c.getPhone());
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        boolean found = false;
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = change.getDocument();
                            if (documentSnapshot.exists()) {
                                Log.i("JOBS_TEST",documentSnapshot.getId());
                                Job j = documentSnapshot.toObject(Job.class);
                                if (c.getRegistrationDate().equals(j.getRegisterTime())){
                                    found = true;
                                }

                            }
                        }

                        if (!found){
                            Log.i("JOBS_TEST","not found");
                            isJobFound.setValue(false);
                        }else{
                            Log.i("JOBS_TEST","found");
                            isJobFound.setValue(true);
                        }
                    }else {
                        Log.i("JOBS_TEST","didnt exsits");
                        isJobFound.setValue(false);
                    }
                });
    }

}
