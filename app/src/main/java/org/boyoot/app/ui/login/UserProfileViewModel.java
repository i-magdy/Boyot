package org.boyoot.app.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.boyoot.app.model.UserProfileModel;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<List<UserProfileModel>> userMutableLiveData;
    FirebaseDatabase data ;
    DatabaseReference reference ;
    ChildEventListener childEventListener;
    private List<UserProfileModel> users;

    public UserProfileViewModel(){
       data = FirebaseDatabase.getInstance();
       reference = data.getReference().child("users");
        users = new ArrayList<>();
        users = getUsersList();
        userMutableLiveData = new MutableLiveData<>();
    }

    LiveData<List<UserProfileModel>> getUsers(){
        userMutableLiveData.setValue(users);
        return userMutableLiveData;
    }


    private List<UserProfileModel> getUsersList(){
        final List<UserProfileModel> mUsers = new ArrayList<>();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    UserProfileModel user = dataSnapshot.getValue(UserProfileModel.class);
                    mUsers.add(user);
                    Log.i("TEST_DATABASE",user.getEmail());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.addChildEventListener(childEventListener);

        return mUsers ;
    }
}
