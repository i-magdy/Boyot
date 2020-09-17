package org.boyoot.app.ui.newJobs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.database.JobsDao;

import java.util.List;

public class NewJobsFilterViewModel extends AndroidViewModel {

    private JobsDao dao;
    LiveData<List<Jobs>> sortedLiveData;
    LiveData<List<Jobs>> sortedJobsByInterval;
    LiveData<List<Jobs>> jobs;

    public NewJobsFilterViewModel(@NonNull Application application) {
        super(application);
        AppRoomDatabase db = AppRoomDatabase.getJobsDatabase(application);
        dao = db.jobsDao();
    }

    LiveData<List<Jobs>> getJobs(){
        jobs = dao.getJobs(0);
        return jobs;
    }

    LiveData<List<Jobs>> getSortedJobsByBranch(String branch){
            sortedLiveData = dao.getJobsByBranch(0, branch);
            return sortedLiveData;
    }

    LiveData<List<Jobs>> getSortedJobsByBranchByInterval(String branch,String interval){
        sortedJobsByInterval = dao.getJobsByBranchByInterval(0,branch,interval);
        return sortedJobsByInterval;
    }
}
