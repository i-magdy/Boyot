package org.boyoot.app.ui.contact;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.boyoot.app.database.Contacts;
import org.boyoot.app.model.Contact;

import static org.boyoot.app.utilities.WorkTimeUtility.calculateTime;

public class ContactBottomSheetViewModel extends ViewModel {

    private MutableLiveData<Contact> contact;
    public ContactBottomSheetViewModel(){
        contact = new MutableLiveData<>();
    }

    LiveData<Contact> getContact(){
        return contact;
    }

    void fetchContact(String contactId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("contacts").document(contactId);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e == null) {
                    if (documentSnapshot.exists()) {
                        Contact contactObj = documentSnapshot .toObject(Contact.class);
                        if (contactObj != null){
                          contact.setValue(contactObj);
                        }
                    }
                }

            }
        });

    }

}
