package org.boyoot.app.services;

import android.content.Context;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.work.ListenableWorker;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.model.Work;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;

public class UpdateContactsWorker extends Worker {

    private List<GoogleSheetModel> mDataList;
    private boolean isExist;

    public UpdateContactsWorker(@NonNull Context context,
                                @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        isExist=false;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("checkContact","working"+"   ||");
        getGoogleSheetContacts();
        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }

    private void getGoogleSheetContacts(){

        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {
                //Log.i("apiRetro",response.body().get(15).getPhone());
               mDataList =  response.body();

               update(response.body());
                   Log.i("googleWorker","   ||  "+ getValidPhoneNumber(mDataList.get(2).getPhone()));

                  // checkIfContactExist(getValidPhoneNumber(data.get(i).getPhone()));
                //checkIfContactExist("558845632a");

            }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {
                Log.w("googleWorker","   ||  "+"fail"+"  "+t.getMessage());
            }
        });
    }

    private boolean checkIfContactExist(String contact,GoogleSheetModel d){

        final boolean[] b = new boolean[1];
        if (TextUtils.isEmpty(contact) || TextUtils.equals(contact , "invalid")){
            return false;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query contactRef = db.collection("contacts").whereEqualTo("phone",contact);

        Log.i("checkContact",contactRef.get().isSuccessful()+"   ||");

        contactRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc:task.getResult()){
                        Log.i("checkContact",doc.getId()+"  "+isExist);
                        //TODO update data
                       // pushUpdateToContact(d);
                        b[0] = false;

                    }
                }
            }


        });
        return b[0];

    }

    private void update(List<GoogleSheetModel> data){
        boolean b = true;

        for (int i = 0;i < data.size();++i) {
            GoogleSheetModel d =data.get(i);
            checkIfContactExist(getValidPhoneNumber(d.getPhone()),d);

        }
    }

    private void pushUpdateToContact(GoogleSheetModel d){

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("contacts")
                .add(new Contact("Z07","558845632", FieldValue.serverTimestamp(),"1","test",new Work("am","10","2","5","7","no"),new City("رياض","R","35.4874","21.148541888")))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("firestore", "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                });



    }



}
