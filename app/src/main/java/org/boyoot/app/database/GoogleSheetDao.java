package org.boyoot.app.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GoogleSheetDao {

    @Query("SELECT * FROM sheet_table ORDER BY state ASC")
    LiveData<List<GoogleSheet>> getContacts();

    @Query("DELETE  FROM sheet_table WHERE phone = :phone")
    void deleteContact(String phone);

    @Query("SELECT * FROM sheet_table WHERE phone = :phone")
    LiveData<GoogleSheet> getContact(String phone);
// @Query("SELECT * FROM sheet_table WHERE state = 2 ORDER BY timeStamp DESC")
    @Query("SELECT * FROM sheet_table WHERE state = 2 ORDER BY cloudId ASC")
    LiveData<List<GoogleSheet>> filterContacts();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void saveContact(GoogleSheet contact);

    @Query("UPDATE sheet_table SET plusCode = :plusCode , state = :state WHERE phone = :phone")
    void updateLocationCode(String phone,String plusCode,String state);

    @Query("UPDATE sheet_table SET cloudId = :cloudId WHERE phone = :phone")
    void updateCloudId(String phone ,String cloudId);

    @Query("UPDATE sheet_table SET contactId = :contactId WHERE phone = :phone")
    void updateContactId(String phone,String contactId);

}
