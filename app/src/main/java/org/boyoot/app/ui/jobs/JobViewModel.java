package org.boyoot.app.ui.jobs;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.job.Job;

import static org.boyoot.app.utilities.WorkUtility.totalNumberOfWork;
import static org.boyoot.app.utilities.WorkUtility.getDurationText;

public class JobViewModel extends ViewModel {

    public MutableLiveData<String> phone;
    public MutableLiveData<String> id;
    public MutableLiveData<String> city;
    public MutableLiveData<String> interval;
    public MutableLiveData<String> split;
    public MutableLiveData<String> splitPrice;
    public MutableLiveData<String> stand;
    public MutableLiveData<String> standPrice;
    public MutableLiveData<String> window;
    public MutableLiveData<String> windowPrice;
    public MutableLiveData<String> concealed;
    public MutableLiveData<String> concealedPrice;
    public MutableLiveData<String> cover;
    public MutableLiveData<String> coverPrice;
    public MutableLiveData<Boolean> offer;
    public MutableLiveData<String> offerPrice;
    public MutableLiveData<String> cost;
    public MutableLiveData<String> totalNumber;
    public MutableLiveData<String> duration;
    public MutableLiveData<Integer> divide;
    private MutableLiveData<Job> job;


    private static final String JOBS_PATH = "jobs";


    public JobViewModel() {
        phone = new MutableLiveData<>();
        id = new MutableLiveData<>();
        city = new MutableLiveData<>();
        interval = new MutableLiveData<>();
        split = new MutableLiveData<>();
        splitPrice = new MutableLiveData<>();
        stand = new MutableLiveData<>();
        standPrice = new MutableLiveData<>();
        window = new MutableLiveData<>();
        windowPrice = new MutableLiveData<>();
        concealed = new MutableLiveData<>();
        concealedPrice = new MutableLiveData<>();
        cover = new MutableLiveData<>();
        coverPrice = new MutableLiveData<>();
        offer = new MutableLiveData<>();
        offerPrice = new MutableLiveData<>();
        cost = new MutableLiveData<>();
        totalNumber = new MutableLiveData<>();
        duration = new MutableLiveData<>();
        divide = new MutableLiveData<>();
        job = new MutableLiveData<>();
    }

    LiveData<Job> getJob(){
        return job;
    }

    public void jobContent(@NonNull String jobId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(JOBS_PATH).document(jobId);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Job j = documentSnapshot.toObject(Job.class);
                            j.setJobId(documentSnapshot.getId());
                            phone.setValue(j.getPhone());
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
                            job.setValue(j);
                        }
                    }
                });
    }
}
