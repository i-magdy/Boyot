package org.boyoot.app.ui.appointment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boyoot.app.model.job.Job;
import org.boyoot.app.utilities.TimeUtility;

public class MainJobViewModel extends ViewModel {

    public MutableLiveData<Integer> aVisibility;
    public MutableLiveData<String> aAppointment;
    public MutableLiveData<String> aDuration;
    public MutableLiveData<String> aFinishTime;
    public MutableLiveData<String> aRoute;
    public MutableLiveData<String> aRouteDuration;

    public MainJobViewModel() {
        aVisibility = new MutableLiveData<>();
        aAppointment = new MutableLiveData<>();
        aDuration = new MutableLiveData<>();
        aFinishTime = new MutableLiveData<>();
        aRoute = new MutableLiveData<>();
        aRouteDuration = new MutableLiveData<>();


        aVisibility.setValue(View.GONE);
    }


    void setCurrentJob(Job job){
        if (job.getAppointment()!= null){
            aVisibility.setValue(View.VISIBLE);
            aAppointment.setValue(TimeUtility.dateFormatter(job.getAppointment().getValue()));
            aDuration.setValue(TimeUtility.getDurationText(job.getDuration().getValue()));
            aFinishTime.setValue(TimeUtility.getFinishTimeText(job.getFinishTime().getValue()));
            aRoute.setValue(job.getDirections().getDistance().getText());
            aRouteDuration.setValue(job.getDirections().getDuration().getText());
            Log.i("TESTING"," SHOW");
        }else {
            Log.i("TESTING","NO SHOW");
        }

    }
}
