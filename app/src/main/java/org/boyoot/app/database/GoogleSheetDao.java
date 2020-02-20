package org.boyoot.app.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GoogleSheetDao {

    @Query("SELECT * FROM google_sheet ORDER BY state ASC")
    LiveData<List<GoogleSheet>> getContacts();

    @Query("DELETE  FROM google_sheet WHERE phone = :phone")
    void deleteContact(String phone);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void saveContact(GoogleSheet contact);

    @Query("UPDATE google_sheet SET cloudId = :cloudId WHERE phone = :phone")
    void updateCloudId(String phone ,String cloudId);



}
