package org.boyoot.app.ui.contact;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.boyoot.app.MainRepo;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.model.Contact;

import static org.boyoot.app.utilities.WorkTimeUtility.calculateTime;

public class ContactViewModel extends AndroidViewModel {

    public MutableLiveData<String> id;
    public MutableLiveData<String> phone;
    public MutableLiveData<String> city;
    public MutableLiveData<String> interval;
    public MutableLiveData<String> split;
    public MutableLiveData<String> window;
    public MutableLiveData<String> stand;
    public MutableLiveData<String> cover;
    public MutableLiveData<String> concealed;
    private MutableLiveData<String> priority;
    public MutableLiveData<String> registrationDate;
    public MutableLiveData<String> note;
    public MutableLiveData<String> time;
    private MainRepo repo;


    public ContactViewModel(@NonNull Application application) {
        super(application);
        repo = new MainRepo(application);
        id = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        city = new MutableLiveData<>();
        interval = new MutableLiveData<>();
        split = new MutableLiveData<>();
        window = new MutableLiveData<>();
        stand = new MutableLiveData<>();
        cover = new MutableLiveData<>();
        concealed = new MutableLiveData<>();
        priority = new MutableLiveData<>();
        registrationDate = new MutableLiveData<>();
        note = new MutableLiveData<>();
        time = new MutableLiveData<>();


    }



    public LiveData<String> getPhone(){
        return phone;
    }
    LiveData<String> getPriority() {
        return priority;
    }
   /* public LiveData<String> getCity() {
        return city;
    }
 public LiveData<String> getId(){
        return id;
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

    public LiveData<String> getConcealed(){
        return concealed;
    }



    public LiveData<String> getRegistrationDate() {
        return registrationDate;
    }

    public LiveData<String> getNote(){
        return note;
    }
    public LiveData<String> getTime(){
        return time;
    }

*/

    public void fetchContact(String contactId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("contacts").document(contactId);
        docRef.get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Contact contact = doc .toObject(Contact.class);
                            if (contact != null){
                                Contacts dbContact = new Contacts(doc.getId(),contact.getPhone(),contact.getPriority(),contact.getId(),contact.getWork().getInterval(),contact.getCity().getCity(),contact.getCity().getCityCode(),contact.getCity().getLocationCode(),contact.getRegistrationDate());
                                repo.saveContacts(dbContact);
                                id.setValue(contact.getId());
                                phone.setValue(contact.getPhone());
                                priority.setValue(contact.getPriority());
                                city.setValue(contact.getCity().getCity());
                                interval.setValue(contact.getWork().getInterval());
                                window.setValue(contact.getWork().getWindow());
                                stand.setValue(contact.getWork().getStand());
                                cover.setValue(contact.getWork().getCover());
                                split.setValue(contact.getWork().getSplit());
                                concealed.setValue(contact.getWork().getConcealed());
                                registrationDate.setValue(contact.getRegistrationDate());
                                note.setValue(contact.getNote());

                                time.setValue(calculateTime(contact.getWork()));

                            }
                        }
                    }
                });

        /*docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e == null) {
                    if (documentSnapshot.exists()) {
                        Contact contact = documentSnapshot .toObject(Contact.class);
                        if (contact != null){
                            Contacts dbContact = new Contacts(documentSnapshot.getId(),contact.getPhone(),contact.getPriority(),contact.getId(),contact.getWork().getInterval(),contact.getCity().getCity(),contact.getCity().getCityCode(),contact.getCity().getLocationCode(),contact.getRegistrationDate());
                            repo.saveContacts(dbContact);
                            id.setValue(contact.getId());
                            phone.setValue(contact.getPhone());
                            priority.setValue(contact.getPriority());
                            city.setValue(contact.getCity().getCity());
                            interval.setValue(contact.getWork().getInterval());
                            window.setValue(contact.getWork().getWindow());
                            stand.setValue(contact.getWork().getStand());
                            cover.setValue(contact.getWork().getCover());
                            split.setValue(contact.getWork().getSplit());
                            concealed.setValue(contact.getWork().getConcealed());
                            registrationDate.setValue(contact.getRegistrationDate());
                            note.setValue(contact.getNote());

                            time.setValue(calculateTime(contact.getWork().getWindow(),contact.getWork().getSplit(),
                                    contact.getWork().getStand(),contact.getWork().getCover(),contact.getWork().getConcealed()));

                        }
                    }
                }

            }
        });*/
    }





}
