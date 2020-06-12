package org.boyoot.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobsDao {

    @Query("SELECT * FROM jobs_table WHERE priority =:priority ORDER BY timeStamp ASC")
    LiveData<List<Jobs>> getJobs(int priority);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveJob(Jobs job);
}
