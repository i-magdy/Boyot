package org.boyoot.app.ui.user;

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
import org.boyoot.app.ui.login.UserProfileViewModel;

public class UserViewModel extends ViewModel {

    MutableLiveData<UserProfileModel> user;
    public UserViewModel(){
        user = new MutableLiveData<>();
    }

    LiveData<UserProfileModel> getUser(){
        return user;
    }

    void checkCurrentUser(String id){
        if (id != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("users").whereEqualTo("userId", id);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot doc = change.getDocument();
                            if (doc.exists()) {
                                UserProfileModel userProfile = doc.toObject(UserProfileModel.class);
                                user.setValue(userProfile);
                            }
                        }
                    }
                }
            });
        }else{
            user.setValue(null);
        }
    }
}
