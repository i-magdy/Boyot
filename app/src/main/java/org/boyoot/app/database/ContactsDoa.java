package org.boyoot.app.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactsDoa {

    @Query("SELECT * FROM contacts_table")
    LiveData<List<Contacts>> getContactsFromCloud();

    @Query("SELECT * FROM contacts_table WHERE priority = :newContactPriority ORDER BY contactId DESC")
    LiveData<List<Contacts>> getNewContacts(String newContactPriority);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveContacts(Contacts contact);
}
