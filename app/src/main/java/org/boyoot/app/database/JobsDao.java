package org.boyoot.app.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobsDao {

    @Query("SELECT * FROM jobs_table WHERE priority =:priority ORDER BY timeStamp ASC")
    LiveData<List<Jobs>> getJobs(int priority);

    @Query("SELECT * FROM jobs_table WHERE priority =:priority AND branch=:branch ORDER BY timeStamp ASC")
    LiveData<List<Jobs>> getJobsByBranch(int priority, String branch);

    @Query("SELECT * FROM jobs_table WHERE priority =:priority AND branch=:branch AND interval=:interval ORDER BY timeStamp ASC")
    LiveData<List<Jobs>> getJobsByBranchByInterval(int priority, String branch,String interval);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveJob(Jobs job);
}
