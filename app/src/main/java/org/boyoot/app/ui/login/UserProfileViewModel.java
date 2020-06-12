package org.boyoot.app.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.UserProfileModel;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<List<UserProfileModel>> userMutableLiveData;


    private List<UserProfileModel> users;
    private MutableLiveData<UserProfileModel> user;

    public UserProfileViewModel(){
        user = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
    }

    LiveData<List<UserProfileModel>> getUsers(){
        userMutableLiveData.setValue(users);
        return userMutableLiveData;
    }
    LiveData<UserProfileModel> getUser(){
        return user;
    }

    LiveData<UserProfileModel> userUpdated(){
        user.setValue(null);
        return user;
    }

    void pushNewUser(String name,String email,String phone,String userId,String password,String role){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .add(new UserProfileModel(name,email,phone,userId,password,role))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("firestore_users","added");
                      documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                          @Override
                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                              if (documentSnapshot.exists()) {
                                  UserProfileModel model = documentSnapshot.toObject(UserProfileModel.class);
                                  user.setValue(model);

                              }
                          }

                      });

                    }
                });
    }
}
