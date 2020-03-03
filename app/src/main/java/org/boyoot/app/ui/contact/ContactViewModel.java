package org.boyoot.app.ui.contact;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.Contact;

public class ContactViewModel extends AndroidViewModel {

    private MutableLiveData<String> id;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> city;
    private MutableLiveData<String> interval;
    private MutableLiveData<String> split;
    private MutableLiveData<String> window;
    private MutableLiveData<String> stand;
    private MutableLiveData<String> cover;
    private MutableLiveData<String> priority;
    private MutableLiveData<String> registrationDate;
    private MutableLiveData<String> note;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        id = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        city = new MutableLiveData<>();
        interval = new MutableLiveData<>();
        split = new MutableLiveData<>();
        window = new MutableLiveData<>();
        stand = new MutableLiveData<>();
        cover = new MutableLiveData<>();
        priority = new MutableLiveData<>();
        registrationDate = new MutableLiveData<>();
        note = new MutableLiveData<>();

    }


    public LiveData<String> getId(){
        return id;
    }
    public LiveData<String> getPhone(){
        return phone;
    }

    public LiveData<String> getCity() {
        return city;
    }

    public LiveData<String> getInterval() {
        return interval;
    }

    public LiveData<String> getSplit() {
        return split;
    }

    public LiveData<String> getWindow() {
        return window;
    }

    public LiveData<String> getStand() {
        return stand;
    }

    public LiveData<String> getCover() {
        return cover;
    }

    public LiveData<String> getPriority() {
        return priority;
    }

    public LiveData<String> getRegistrationDate() {
        return registrationDate;
    }

    public LiveData<String> getNote(){
        return note;
    }

    public void fetchContact(String ContactId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("contacts").document(ContactId);
        docRef.get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Contact contact = doc .toObject(Contact.class);
                            if (contact != null){
                                id.setValue(contact.getId());
                                phone.setValue(contact.getPhone());
                                priority.setValue(contact.getPriority());
                                city.setValue(contact.getCity().getCity());
                                interval.setValue(contact.getWork().getInterval());
                                window.setValue(contact.getWork().getWindow());
                                stand.setValue(contact.getWork().getStand());
                                cover.setValue(contact.getWork().getCover());
                                split.setValue(contact.getWork().getSplit());
                                registrationDate.setValue(contact.getRegistrationDate());
                                note.setValue(contact.getNote());

                            }
                        }
                    }
                });
    }
}
