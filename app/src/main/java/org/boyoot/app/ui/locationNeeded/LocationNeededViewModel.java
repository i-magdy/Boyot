package org.boyoot.app.ui.locationNeeded;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.database.ContactsDoa;


import java.util.List;

public class LocationNeededViewModel extends AndroidViewModel {

    private ContactsDoa doa;
    private LiveData<List<Contacts>> contacts;

    public LocationNeededViewModel(@NonNull Application application) {
        super(application);
        AppRoomDatabase db = AppRoomDatabase.getContactsDatabase(application);
        doa = db.contactsDoa();
        contacts = doa.getNewContacts("1");
    }

    LiveData<List<Contacts>> getContacts(){
        return contacts;
    }

    void deleteAllContacts(){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> doa.deleteContacts());
    }
}
