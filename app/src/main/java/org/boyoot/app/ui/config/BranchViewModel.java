package org.boyoot.app.ui.config;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.Branch;
import org.boyoot.app.model.Car;

import java.util.List;

public class BranchViewModel extends ViewModel {

    public MutableLiveData<String> title;
    public MutableLiveData<String> code;
    public MutableLiveData<String> dayStart;
    public MutableLiveData<String> dayEnd;
    MutableLiveData<List<Car>> cars;

    private static final String BRANCHES_PATH="branches";

    public BranchViewModel() {
        title = new MutableLiveData<>();
        code = new MutableLiveData<>();
        dayStart = new MutableLiveData<>();
        dayEnd = new MutableLiveData<>();
        cars = new MutableLiveData<>();
    }

    public LiveData<List<Car>> getCarsList(){
        return cars;
    }

    public void getBranch(@NonNull String branch){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection(BRANCHES_PATH).document(branch);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Branch branch = documentSnapshot.toObject(Branch.class);
                        title.setValue(branch.getTitle());
                        code.setValue(branch.getBranchId());
                        dayStart.setValue(branch.getDayStart());
                        dayEnd.setValue(branch.getDayEnd());
                        cars.setValue(branch.getCars());
                    }
                }
            });

    }
}
