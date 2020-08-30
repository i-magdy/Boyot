package org.boyoot.app.ui.appointment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.job.Directions;
import org.boyoot.app.model.job.FinishTime;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.utilities.TimeUtility;

import static org.boyoot.app.utilities.TimeUtility.calculateFinishTime;

public class MainJobViewModel extends ViewModel {

    private MutableLiveData<Directions> directionsMutableLiveData;
    public MutableLiveData<Integer> aVisibility;
    public MutableLiveData<String> aAppointment;
    public MutableLiveData<String> aDuration;
    public MutableLiveData<String> aFinishTime;
    public MutableLiveData<String> aRoute;
    public MutableLiveData<String> aRouteDuration;


    private static final String JOBS_PATH = "jobs";


    public MainJobViewModel() {
        aVisibility = new MutableLiveData<>();
        aAppointment = new MutableLiveData<>();
        aDuration = new MutableLiveData<>();
        aFinishTime = new MutableLiveData<>();
        aRoute = new MutableLiveData<>();
        aRouteDuration = new MutableLiveData<>();
        directionsMutableLiveData = new MutableLiveData<>();

        aVisibility.setValue(View.GONE);
    }

    LiveData<Directions> getDirections(){
        return directionsMutableLiveData;
    }
    void setCurrentJob(Job job){
        if (job.getAppointment()!= null){
            aVisibility.setValue(View.VISIBLE);
             FinishTime finishTime = new FinishTime(calculateFinishTime(job.getAppointment().getValue(),
                    job.getDuration().getValue()));
            job.setFinishTime(finishTime);
            aAppointment.setValue(TimeUtility.dateFormatter(job.getAppointment().getValue()));
            aDuration.setValue(TimeUtility.getDurationText(job.getDuration().getValue()));
            aFinishTime.setValue(TimeUtility.getFinishTimeText(job.getFinishTime().getValue()));
            aRoute.setValue(job.getDirections().getDistance().getText());
            aRouteDuration.setValue(job.getDirections().getDuration().getText());
            directionsMutableLiveData.setValue(job.getDirections());
            Log.i("TESTING_JOB",job.getSort()+" SHOW"+" O : "+job.getTeam().getOriginalWorkerCount()+" | divide ? "+job.getTeam().isDivided()+" >> workers : "+job.getTeam().getWorkers());
        }else {
            Log.i("TESTING","NO SHOW");
        }

    }

    public void updateJob(){
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(JOBS_PATH).document(.getJobId())
                .set(jj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });*/
    }
}
