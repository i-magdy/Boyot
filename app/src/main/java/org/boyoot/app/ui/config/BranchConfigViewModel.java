package org.boyoot.app.ui.config;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.model.Branch;

import java.util.ArrayList;
import java.util.List;

public class BranchConfigViewModel extends ViewModel {

    private MutableLiveData<List<Branch>> branchesList;

    private static final String BRANCHES_PATH="branches";

    public BranchConfigViewModel(){
        branchesList = new MutableLiveData<>();
    }

    LiveData<List<Branch>> getBranchesList(){
        return branchesList;
    }


    void getBranches(){
        List<Branch> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection(BRANCHES_PATH).orderBy("branchId");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot documentSnapshot = change.getDocument();
                        if (documentSnapshot.exists()) {
                            Branch branch = documentSnapshot.toObject(Branch.class);
                            list.add(branch);
                            branchesList.setValue(list);
                        }
                    }
                }

            }

        });
    }
}
