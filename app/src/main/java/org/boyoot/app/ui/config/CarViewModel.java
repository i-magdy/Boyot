package org.boyoot.app.ui.config;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.model.UserProfileModel;

import java.util.ArrayList;
import java.util.List;

public class CarViewModel extends ViewModel {

    private MutableLiveData<List<UserProfileModel>> users;
    private static final String USERS_PATH="users";

    public CarViewModel() {
        users = new MutableLiveData<>();
    }

    LiveData<List<UserProfileModel>> getUsers(){
        return users;
    }

    void fetchUsers(int carNumber,String branch){
        List<UserProfileModel> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection(USERS_PATH).whereEqualTo("branchId",branch).whereEqualTo("car.pathNo",carNumber);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot documentSnapshot = change.getDocument();
                        if (documentSnapshot.exists()) {
                            UserProfileModel user = documentSnapshot.toObject(UserProfileModel.class);
                            user.setId(documentSnapshot.getId());
                            list.add(user);
                            users.setValue(list);
                            Log.i("sync_contacts","true"+"  "+user.getPhone());
                        }
                    }
                }
            }
        });
    }
}
