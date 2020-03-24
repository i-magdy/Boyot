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
import org.boyoot.app.model.Contact;


public class MainRepo {

    private ContactsDoa contactsDoa;

    public MainRepo(Application app) {
        AppRoomDatabase db = AppRoomDatabase.getContactsDatabase(app);
        contactsDoa = db.contactsDoa();
    }

    public void saveContacts(Contacts contact){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> contactsDoa.saveContacts(contact));
    }


    void getContactsFromCloud(){
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
                            Contacts dbContact = new Contacts(documentSnapshot.getId(),contact.getPhone(),contact.getPriority(),contact.getId(),contact.getWork().getInterval(),contact.getCity().getCity(),contact.getCity().getCityCode(),contact.getCity().getLocationCode(),contact.getRegistrationDate());
                            saveContacts(dbContact);
                            Log.i("sync_contacts","true"+"  "+contact.getPhone());
                        }
                    }
                }
            }
        });
    }
}
