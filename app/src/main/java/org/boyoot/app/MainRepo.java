package org.boyoot.app;

import android.app.Application;
import android.util.Log;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.database.ContactsDoa;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.database.JobsDao;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.job.Job;


public class MainRepo {

    private ContactsDoa contactsDoa;
    private JobsDao jobsDao;

    private static final String JOBS_PATH = "jobs";

    public MainRepo(Application app) {
        AppRoomDatabase db = AppRoomDatabase.getContactsDatabase(app);
        contactsDoa = db.contactsDoa();
        AppRoomDatabase jobsDatabase = AppRoomDatabase.getJobsDatabase(app);
        jobsDao = jobsDatabase.jobsDao();
    }

    public void saveContacts(Contacts contact){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> contactsDoa.saveContacts(contact));
    }

    public void saveJobs(Jobs job){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> jobsDao.saveJob(job));
    }

    public void getContactsFromCloud(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("contacts").orderBy("timeStamp").limitToLast(100);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot documentSnapshot = change.getDocument();
                        if (documentSnapshot.exists()) {
                            Contact contact = documentSnapshot.toObject(Contact.class);
                            Contacts dbContact = new Contacts(documentSnapshot.getId(),
                                    contact.getPhone(),contact.getPriority(),
                                    contact.getId(),contact.getWork().getInterval(),
                                    contact.getCity().getCity(),contact.getCity().getCityCode(),
                                    contact.getCity().getLocationCode(),contact.getRegistrationDate());
                            saveContacts(dbContact);
                            //Log.i("sync_contacts","true"+"  "+contact.getTimeStamp().toDate().getTime());
                        }
                    }
                }
            }
        });
    }


    public void getJobs(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection(JOBS_PATH).whereEqualTo("priority",0);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                                DocumentSnapshot documentSnapshot = change.getDocument();
                                if (documentSnapshot.exists()) {
                                    Job j = documentSnapshot.toObject(Job.class);
                                    Jobs job = new Jobs(documentSnapshot.getId(),
                                            j.getPhone(),
                                            j.getPriority(),
                                            j.getId(),
                                            j.isDivided(),
                                            null,
                                            null,
                                            j.getCity(),
                                            j.getBranch(),
                                            j.getTimeStamp().getTime());
                                    saveJobs(job);
                                    Log.i("sync_NEW_JOBS","true"+"  "+j.getTimeStamp().getTime());
                                }
                            }
                        }

                    }
                });
    }
}
