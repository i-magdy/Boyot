package org.boyoot.app.ui.newJobs;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.database.JobsDao;
import org.boyoot.app.model.job.Job;

import java.util.List;


public class NewJobsViewModel extends AndroidViewModel {

    private JobsDao dao;
    private LiveData<List<Jobs>> jobs;
    private MutableLiveData<List<Jobs>> newJobs;


    public NewJobsViewModel(@NonNull Application application) {
        super(application);
        AppRoomDatabase db = AppRoomDatabase.getJobsDatabase(application);
        dao = db.jobsDao();
        jobs = dao.getJobs(0);
        newJobs = new MutableLiveData<>();


    }

    LiveData<List<Jobs>> getJobs(){
        return jobs;
    }


    LiveData<List<Jobs>> getNewJobs(){
            return newJobs;
    }


    void setNewJobs(List<Jobs> jobsList){
        if (jobsList != null) {
            newJobs.setValue(jobsList);
        }
    }
}
