package org.boyoot.app.ui.employees;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.model.UserProfileModel;

import java.util.Objects;

public class ProfileViewModel extends ViewModel {

    public MutableLiveData<String> name;
    public MutableLiveData<String> email;
    private MutableLiveData<String> role;
    public MutableLiveData<String> branch;
    private MutableLiveData<UserProfileModel> profileModelMutableLiveData;

    private static final String USERS_PATH="users";

    public ProfileViewModel() {
        name = new MutableLiveData<>();
        email = new MutableLiveData<>();
        role = new MutableLiveData<>();
        branch = new MutableLiveData<>();
        profileModelMutableLiveData = new MutableLiveData<>();

    }

    public LiveData<String> getRole(){
        return role;
    }
    public LiveData<UserProfileModel> getProfile(){
        return profileModelMutableLiveData;
    }

    void getProfile(final String accEmail){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(USERS_PATH);
        Query query = ref.whereEqualTo("email",accEmail);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!Objects.requireNonNull(task.getResult()).isEmpty()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                            UserProfileModel profile = documentSnapshot.toObject(UserProfileModel.class);
                            profile.setId(documentSnapshot.getId());
                            profileModelMutableLiveData.setValue(profile);
                            name.setValue(profile.getUserName());
                            email.setValue(profile.getEmail());
                            role.setValue(profile.getRole());
                            branch.setValue(profile.getBranch());

                        }
                    }
                }
            }
        });
    }


}
