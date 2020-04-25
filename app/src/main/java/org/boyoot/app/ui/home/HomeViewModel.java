package org.boyoot.app.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.MainRepo;
import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.database.ContactsDoa;
import org.boyoot.app.model.Contact;

import java.util.List;
import java.util.Objects;

public class HomeViewModel extends AndroidViewModel {

    private MainRepo repo;
    private MutableLiveData<String> contact;
    public HomeViewModel(Application app) {
        super(app);
        repo = new MainRepo(app);
        contact = new MutableLiveData<>();


    }


    void syncContacts(){
        repo.getContactsFromCloud();
    }
    LiveData<String> getContactFromSearch(){
        return contact;
    }
    void getContactById(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("contacts");
        Query query = ref.whereEqualTo("id",id);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (Objects.requireNonNull(task.getResult()).isEmpty()){
                        contact.setValue(null);
                    }else {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            contact.setValue(document.getId());
                        }
                    }
                }
            }
        });
    }

    void getContactByPhoneNumber(String phone){
        if (!phone.equals("invalid")){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference ref = db.collection("contacts");
            Query query = ref.whereEqualTo("phone",phone);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        if (Objects.requireNonNull(task.getResult()).isEmpty()){
                            contact.setValue(null);
                        }else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                contact.setValue(document.getId());
                            }
                        }
                    }
                }
            });

        }
    }


}