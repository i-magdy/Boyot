package org.boyoot.app.ui.employees;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.database.Contacts;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.UserProfileModel;
import org.boyoot.app.ui.user.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeesViewModel extends ViewModel {

    private MutableLiveData<List<UserProfileModel>> users;
    private List<UserProfileModel> list = new ArrayList<>();

    public EmployeesViewModel() {
        users = new MutableLiveData<>();

    }

    LiveData<List<UserProfileModel>> getUsers(){
        return users;
    }


    void fetchUsers(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot documentSnapshot = change.getDocument();
                        if (documentSnapshot.exists()) {
                            UserProfileModel user = documentSnapshot.toObject(UserProfileModel.class);
                            list.add(user);
                            users.setValue(list);
                            Log.i("sync_contacts","true"+"  "+user.getPhone());
                        }
                    }
                }
            }
        });
    }

    void removeUsers(){
        list = new ArrayList<>();
        Log.i("users",list.size()+" ");
        users.setValue(list);
    }

}