package org.boyoot.app.ui.appointment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.model.job.Job;

import static org.boyoot.app.utilities.WorkUtility.getDurationText;
import static org.boyoot.app.utilities.WorkUtility.totalNumberOfWork;

public class AppointmentsViewModel extends ViewModel {


    MutableLiveData<Job> jobMutableLiveData;

    private static final String JOBS_PATH = "jobs";


    public AppointmentsViewModel() {
        jobMutableLiveData = new MutableLiveData<>();
    }


    LiveData<Job> getJob(){
        return jobMutableLiveData;
    }

    void jobContent(@NonNull String jobId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(JOBS_PATH).document(jobId);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Job j = documentSnapshot.toObject(Job.class);
                            j.setJobId(documentSnapshot.getId());
                            jobMutableLiveData.setValue(j);
                            /*phone.setValue(j.getPhone());
                            id.setValue(j.getId());
                            city.setValue(j.getCity());
                            interval.setValue(j.getCurrentWork().getInterval());
                            split.setValue(j.getCurrentWork().getSplit()+"");
                            splitPrice.setValue(j.getPrice().getSplit()*j.getCurrentWork().getSplit()+"");
                            stand.setValue(j.getCurrentWork().getStand()+"");
                            standPrice.setValue(j.getPrice().getStand()*j.getCurrentWork().getStand()+"");
                            window.setValue(j.getCurrentWork().getWindow()+"");
                            windowPrice.setValue(j.getPrice().getWindow()*j.getCurrentWork().getWindow()+"");
                            cover.setValue(j.getCurrentWork().getCover()+"");
                            coverPrice.setValue(j.getPrice().getCover()*j.getCurrentWork().getCover()+"");
                            concealed.setValue(j.getCurrentWork().getConcealed()+"");
                            concealedPrice.setValue(j.getPrice().getConcealed()*j.getCurrentWork().getConcealed()+"");
                            offer.setValue(j.getCurrentWork().isOffer());
                            if (j.getCurrentWork().isOffer()){
                                offerPrice.setValue(j.getPrice().getOffers()+"");
                            }else{
                                offerPrice.setValue("0");
                            }
                            cost.setValue(j.getPayment().getText());
                            totalNumber.setValue(totalNumberOfWork(j.getCurrentWork()));
                            duration.setValue(getDurationText(j.getCurrentWork()));
                            if (j.isDivided()){
                                divide.setValue(View.VISIBLE);
                            }else {
                                divide.setValue(View.INVISIBLE);
                            }
                            job.setValue(j);*/
                        }
                    }
                });
    }

    void getList(String branch,String car,String monthOfYear,int day){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(JOBS_PATH);
        Query query = ref.whereEqualTo("branch",branch).whereEqualTo("team.car",car)
                .whereEqualTo("appointment.monthOfYear",monthOfYear).whereEqualTo("appointment.day",day)
                .orderBy("appointment.value");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                               //TODO handle list here
                            }else {
                                //TODO
                                // no list yet
                            }
                        }
                    }
                });
    }
}
