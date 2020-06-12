package org.boyoot.app.ui.newJobs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.database.JobsDao;

import java.util.List;


public class NewJobsViewModel extends AndroidViewModel {

    private JobsDao dao;
    private LiveData<List<Jobs>> jobs;

    public NewJobsViewModel(@NonNull Application application) {
        super(application);
        AppRoomDatabase db = AppRoomDatabase.getJobsDatabase(application);
        dao = db.jobsDao();
        jobs = dao.getJobs(0);
    }

    LiveData<List<Jobs>> getJobs(){
        return jobs;
    }
}
