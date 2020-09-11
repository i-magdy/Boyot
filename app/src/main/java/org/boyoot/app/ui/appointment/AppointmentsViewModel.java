package org.boyoot.app.ui.appointment;

import android.util.Log;
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

import org.boyoot.app.model.Branch;
import org.boyoot.app.model.Car;
import org.boyoot.app.model.CurrentCalenderDate;
import org.boyoot.app.model.job.CurrentWork;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.utilities.TimeUtility;
import org.boyoot.app.utilities.WorkUtility;


import  static org.boyoot.app.utilities.JobUtility.getSortedId;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsViewModel extends ViewModel {


    public MutableLiveData<String> branchTitle;
    public MutableLiveData<String> workers;
    public MutableLiveData<String> aDay;
    public MutableLiveData<String> aDate;
    public MutableLiveData<String> totalWork;
    public MutableLiveData<String> duration;
    MutableLiveData<Job> jobMutableLiveData;
    MutableLiveData<List<Job>> jobsListMutableLiveData;
    MutableLiveData<List<Car>> cars;
    private CurrentWork currentWork;
    private String jobIdKey;
    private List<Car> carList;
    private MutableLiveData<CurrentCalenderDate> calenderDateMutableLiveData;

    private static final String JOBS_PATH = "jobs";
    private static final String BRANCHES_PATH="branches";


    public AppointmentsViewModel() {
        jobMutableLiveData = new MutableLiveData<>();
        jobsListMutableLiveData = new MutableLiveData<>();
        branchTitle = new MutableLiveData<>();
        workers = new MutableLiveData<>();
        cars = new MutableLiveData<>();
        calenderDateMutableLiveData = new MutableLiveData<>();
        aDate = new MutableLiveData<>();
        aDay = new MutableLiveData<>();
        totalWork = new MutableLiveData<>();
        duration = new MutableLiveData<>();
    }


    LiveData<Job> getJob(){
        return jobMutableLiveData;
    }
    LiveData<List<Job>> getJobs(){
        return jobsListMutableLiveData;
    }
    LiveData<CurrentCalenderDate> getCurrentCalender(){
        return calenderDateMutableLiveData;
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
                            jobIdKey = documentSnapshot.getId();
                            jobMutableLiveData.setValue(j);
                            currentWork = j.getCurrentWork();
                            totalWork.setValue(WorkUtility.getTextTotalNumberOfWork(j.getCurrentWork()));
                            getBranch(j.getBranch());

                        }
                    }
                });
    }
    public LiveData<List<Car>> getCarsList(){
        return cars;
    }


    void getList(String branch,int pathNo,int year,int month,int day){

        Log.i("TEST_APPOINTMENT_LIST",branch+" | "+pathNo+" | "+year+" | "+day);
        List<Job> jobs = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(JOBS_PATH);
        Query query = ref.whereEqualTo("sort",getSortedId(branch,pathNo,year,month,day));
                query.orderBy("appointment.value", Query.Direction.ASCENDING);
                query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                               //TODO handle list here
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot snapshot : task.getResult()){
                                        Job job = snapshot.toObject(Job.class);
                                        jobs.add(job);
                                        jobsListMutableLiveData.setValue(jobs);
                                    }

                                }
                            }else {
                                //TODO
                                // no list yet
                            }
                        }
                    }
                });
    }

    private void getBranch(String branch){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(BRANCHES_PATH).document(branch);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Branch branch = documentSnapshot.toObject(Branch.class);
                    //dayStart.setValue(branch.getDayStart());
                    //dayEnd.setValue(branch.getDayEnd());
                    branchTitle.setValue(branch.getTitle());
                    cars.setValue(branch.getCars());
                    carList = branch.getCars();
                    if (branch.getCars() != null) {
                        if (branch.getCars().size() >= 1) {
                            workers.setValue(String.valueOf(branch.getCars().get(0).getWorker()));
                            duration.setValue(WorkUtility.getDurationTextOfJob(currentWork, carList.get(0).getWorker()));
                        }
                    }
                }
            }
        });
    }

    void setCurrentCalender(CurrentCalenderDate currentCalender){
        aDay.setValue(TimeUtility.getDayName(currentCalender));
        aDate.setValue(TimeUtility.getDateFormat(currentCalender));

    }

    void getSelectedCar(int i){
        if (carList != null){
            if (carList.size() > 0){

                workers.setValue(String.valueOf(carList.get(i).getWorker()));
                duration.setValue(WorkUtility.getDurationTextOfJob(currentWork,carList.get(i).getWorker()));
            }
        }
    }


    void setCurrentCalender(CurrentCalenderDate c,int path,String b){
        if (c != null) {
            c.setJobIdKey(jobIdKey);
            c.setPathNo(path);
            c.setBranch(b);
            calenderDateMutableLiveData.setValue(c);
        }

    }
    int getSelectedPath(int position){

        if (carList!= null){
            return carList.get(position).getPathNo();
        }else {
            return 0;
        }

    }

    void clearJobList(){
        jobsListMutableLiveData.setValue(null);
    }
}
