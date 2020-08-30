package org.boyoot.app.ui.appointment;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boyoot.app.data.DirectionClient;
import org.boyoot.app.model.CurrentCalenderDate;
import org.boyoot.app.model.direction.Direction;
import org.boyoot.app.model.job.Appointment;
import org.boyoot.app.model.job.Directions;
import org.boyoot.app.model.job.FinishTime;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.model.job.TimePickerModel;
import org.boyoot.app.utilities.TimeUtility;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondOptionViewModel extends ViewModel {

    private Job currentJob;
    private CurrentCalenderDate date;
    private String directionsKey;
    private String homePlaceId;
    private MutableLiveData<Job> jobMutableLiveData;
    public SecondOptionViewModel() {
        jobMutableLiveData = new MutableLiveData<>();

    }

    LiveData<Job> getJob(){
        return jobMutableLiveData;
    }
    public void getPickedTie(TimePickerModel time){
        Calendar calendar = TimeUtility.calculateAppointmentFromOriginalAtPickedTime(date,time);
        Appointment appointment = new Appointment(calendar.getTimeInMillis());
        currentJob.setAppointment(appointment);
        FinishTime finishTime = new FinishTime(TimeUtility.calculateFinishTime(appointment.getValue(),currentJob.getDuration().getValue()));
        currentJob.setFinishTime(finishTime);
        getDirectionFromOriginal(currentJob.getMapConfig().getPlaceId());
    }

    private void getDirectionFromOriginal(String destinationPlaceId){
        DirectionClient.getINSTANCE().getDirection("place_id:"+homePlaceId,
                "place_id:"+destinationPlaceId,directionsKey).enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                Log.i("DIRECTION_API",response.toString());
                Direction direction = response.body();
                Directions directions = new Directions();
                directions.setDistance(direction.getRoutes().getLegs().getDistance());
                directions.setDuration(direction.getRoutes().getLegs().getDuration());
                directions.setSteps(direction.getRoutes().getLegs().getSteps());
                currentJob.setDirections(directions);
                currentJob.setPriority(1);
                jobMutableLiveData.setValue(currentJob);

            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

                Log.i("DIRECTION_API",t.getMessage());
            }
        });
    }


    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public void setDirectionsKey(String directionsKey) {
        this.directionsKey = directionsKey;
    }

    public void setHomePlaceId(String homePlaceId) {
        this.homePlaceId = homePlaceId;
    }

    public void setDate(CurrentCalenderDate date) {
        this.date = date;
    }
}

