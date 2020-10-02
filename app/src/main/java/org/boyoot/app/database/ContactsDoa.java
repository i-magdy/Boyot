package org.boyoot.app.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactsDoa {

    @Query("SELECT * FROM contacts_table")
    LiveData<List<Contacts>> getContactsFromCloud();

    @Query("SELECT * FROM contacts_table WHERE priority = :newContactPriority ORDER BY timeStamp DESC")
    LiveData<List<Contacts>> getNewContacts(String newContactPriority);

    @Query("DELETE  FROM contacts_table WHERE priority= :priority")
    void deleteContacts(int priority);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveContacts(Contacts contact);
}
