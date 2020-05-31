package org.boyoot.app.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.Contact;

public class ContactBottomSheetViewModel extends ViewModel {

    private MutableLiveData<Contact> contact;
    private static final String CONTACTS_PATH = "contacts";

    public ContactBottomSheetViewModel(){
        contact = new MutableLiveData<>();
    }

    LiveData<Contact> getContact(){
        return contact;
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
                    }
                }
            }
        });

    }

}
