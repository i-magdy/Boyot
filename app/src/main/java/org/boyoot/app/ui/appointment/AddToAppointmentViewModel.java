package org.boyoot.app.ui.appointment;

import android.util.Log;
import android.util.TimeUtils;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.boyoot.app.data.DirectionClient;
import org.boyoot.app.model.Branch;
import org.boyoot.app.model.Car;
import org.boyoot.app.model.CurrentCalenderDate;
import org.boyoot.app.model.MapConfig;
import org.boyoot.app.model.direction.Direction;
import org.boyoot.app.model.job.Appointment;
import org.boyoot.app.model.job.CurrentWork;
import org.boyoot.app.model.job.Directions;
import org.boyoot.app.model.job.Duration;
import org.boyoot.app.model.job.FinishTime;
import org.boyoot.app.model.job.Job;

import org.boyoot.app.utilities.JobUtility;
import org.boyoot.app.utilities.TimeUtility;
import org.boyoot.app.utilities.WorkUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.boyoot.app.utilities.TimeUtility.calculateAppointmentFromOriginalDayStart;
import static org.boyoot.app.utilities.TimeUtility.calculateFinishTime;
import static org.boyoot.app.utilities.TimeUtility.getMonthOfYear;

public class AddToAppointmentViewModel extends ViewModel {

    public MutableLiveData<String> aPhone;
    public MutableLiveData<String> aId;
    public MutableLiveData<String> aDay;
    public MutableLiveData<String> aDate;
    public MutableLiveData<String> workers;
    public MutableLiveData<String> ACsTotal;
    public MutableLiveData<String> currentDuration;
    private MutableLiveData<Job> mainJob;
    private String directionsKey;
    private Job currentJob;
    private CurrentCalenderDate currentCalenderDate;
    private Branch currentBranch;
    private int workersCount;
    private int originalWorkersCount;
    private CurrentWork currentWork;

    private static final String JOBS_PATH = "jobs";
    private static final String BRANCHES_PATH="branches";

    public AddToAppointmentViewModel() {
        aPhone = new MutableLiveData<>();
        aId = new MutableLiveData<>();
        aDay = new MutableLiveData<>();
        aDate = new MutableLiveData<>();
        workers = new MutableLiveData<>();
        ACsTotal = new MutableLiveData<>();
        currentDuration = new MutableLiveData<>();
        mainJob = new MutableLiveData<>();

    }

    LiveData<Job> getMainJob(){
        return mainJob;
    }

    private void getJob(String jobId,int pathNo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(JOBS_PATH).document(jobId);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Job job = documentSnapshot.toObject(Job.class);
                            currentJob = job;
                            currentJob.setJobId(documentSnapshot.getId());
                            aPhone.setValue(job.getPhone());
                            aId.setValue(job.getId());
                            ACsTotal.setValue(WorkUtility.getTextTotalNumberOfWork(job.getCurrentWork()));
                            currentWork = job.getCurrentWork();
                            getBranch(job.getBranch(),pathNo);
                           // mainJob.setValue(currentJob);

                        }
                    }
                });
    }

    private void getBranch(String branch,int path){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(BRANCHES_PATH).document(branch);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Branch branch = documentSnapshot.toObject(Branch.class);
                    currentBranch = branch;
                    getList(branch.getBranchId(), currentCalenderDate.getPathNo(),
                            currentCalenderDate.getYear(), currentCalenderDate.getMonth(),
                            currentCalenderDate.getDay());
                    for (Car c :branch.getCars()){
                        if (c.getPathNo() == path){
                            workers.setValue(String.valueOf(c.getWorker()));
                            originalWorkersCount=c.getWorker();
                            workersCount = c.getWorker();
                            currentDuration.setValue(WorkUtility.getDurationTextOfJob(currentWork,originalWorkersCount));
                        }
                    }
                }
            }
        });
    }

    private void getList(String branch,int pathNo,int year,int month,int day){
        List<Job> jobs = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(JOBS_PATH);
        currentJob.setSort(JobUtility.getSortedId(branch,pathNo,year,month,day));
        Log.i("input",branch+" "+pathNo+" "+year+" " +month+" "+day);
       ref.whereEqualTo("monthOfYear",currentJob.getSort())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Job job = snapshot.toObject(Job.class);
                                Log.i("TEST_SORTED_LIST", job.getPhone() + " | " + job.getBranch() + " | >> " + snapshot.getId());
                            }
                        }else {
                            if (currentJob.getCurrentWork().getInterval().equals("Morning")) {
                                setCurrentJobAtDayStart();
                            }else{

                            }
                        }
                    }

                });

    }


    public void increaseWorkers(){

        if (workersCount < originalWorkersCount){
            ++workersCount;
            workers.setValue(String.valueOf(workersCount));
            currentDuration.setValue(WorkUtility.getDurationTextOfJob(currentWork,workersCount));
        }

    }

    public void decreaseWorkers(){
        if (workersCount > 1){
            --workersCount;
            workers.setValue(String.valueOf(workersCount));
            currentDuration.setValue(WorkUtility.getDurationTextOfJob(currentWork,workersCount));
        }
    }
    void setCurrentCalender(CurrentCalenderDate currentCalender){
        currentCalenderDate = currentCalender;
        getJob(currentCalender.getJobIdKey(),currentCalender.getPathNo());



        aDay.setValue(TimeUtility.getDayName(currentCalender));
        aDate.setValue(TimeUtility.getDateFormat(currentCalender));

    }


    private void setCurrentJobAtDayStart(){
        Appointment appointment = new Appointment(calculateAppointmentFromOriginalDayStart(currentCalenderDate,currentBranch.getStart()).getTimeInMillis());
        Duration duration = new Duration(WorkUtility.getDurationValueOfJob(currentJob.getCurrentWork(),workersCount));
        currentJob.setAppointment(appointment);
        currentJob.setDuration(duration);
        FinishTime finishTime = new FinishTime(calculateFinishTime(currentJob.getAppointment().getValue(),
                currentJob.getDuration().getValue()));

        currentJob.setFinishTime(finishTime);

        getDirectionFromOriginal(currentJob.getMapConfig().getPlaceId());
    }

    void getDirectionFromOriginal(String destinationPlaceId){
        DirectionClient.getINSTANCE().getDirection("place_id:"+currentBranch.getMapConfig().getPlaceId(),
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
                mainJob.setValue(currentJob);
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

                Log.i("DIRECTION_API",t.getMessage());
            }
        });
    }

    public void setDirectionsKey(String directionsKey) {
        this.directionsKey = directionsKey;
    }
}
