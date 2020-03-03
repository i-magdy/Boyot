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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void saveContact(GoogleSheet contact);

    @Query("UPDATE sheet_table SET locationLink = :locationLink , state = :state WHERE phone = :phone")
    void updateLocationLink(String phone,String locationLink,String state);

    @Query("UPDATE sheet_table SET cloudId = :cloudId WHERE phone = :phone")
    void updateCloudId(String phone ,String cloudId);

    @Query("UPDATE sheet_table SET contactId = :contactId WHERE phone = :phone")
    void updateContactId(String phone,String contactId);

}
