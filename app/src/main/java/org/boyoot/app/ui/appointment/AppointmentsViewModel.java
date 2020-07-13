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
import org.boyoot.app.model.job.Job;


import  static org.boyoot.app.utilities.JobUtility.getSortedId;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsViewModel extends ViewModel {


    public MutableLiveData<String> branchTitle;
    public MutableLiveData<String> workers;
    MutableLiveData<Job> jobMutableLiveData;
    MutableLiveData<List<Job>> jobsListMutableLiveData;
    MutableLiveData<List<Car>> cars;
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
                            getBranch(j.getBranch());
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
    public LiveData<List<Car>> getCarsList(){
        return cars;
    }


    void getList(String branch,int pathNo,int year,int month,int day){

        Log.i("TEST_APPOINTMENT_LIST",branch+" | "+pathNo+" | "+year+" | "+day);
        List<Job> jobs = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(JOBS_PATH);

        Query query = ref.whereEqualTo("sort",getSortedId(branch,pathNo,year,month,day));

                //.orderBy("appointment.value");
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
                    if (branch.getCars().size() == 1){
                        workers.setValue(String.valueOf(branch.getCars().get(0).getWorker()));
                    }
                }
            }
        });
    }

    void getSelectedCar(int i){
        if (carList != null){
            if (carList.size() > 0){
                workers.setValue(String.valueOf(carList.get(i).getWorker()));
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

        return carList.get(position).getPathNo();
    }

}
