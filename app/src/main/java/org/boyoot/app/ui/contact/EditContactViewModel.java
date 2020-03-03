package org.boyoot.app.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.Contact;

public class EditContactViewModel extends ViewModel {

    MutableLiveData<Contact> data;

    public EditContactViewModel() {

        data = new MutableLiveData<>();
    }



    LiveData<Contact> getContact(){
        return data;
    }


  public void getContactFromCloud(String contactCloudId){
      FirebaseFirestore db = FirebaseFirestore.getInstance();
      DocumentReference docRef = db.collection("contacts").document(contactCloudId);
      docRef.get()
              .addOnCompleteListener(task -> {

                  if (task.isSuccessful()){
                      DocumentSnapshot doc = task.getResult();
                      if (doc.exists()) {
                          Contact contact = doc .toObject(Contact.class);
                          if (contact != null){
                              data.setValue(contact);
                          }
                      }
                  }
              });
  }


}
