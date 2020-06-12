package org.boyoot.app.ui.preparedContacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.database.ContactsDoa;

import java.util.List;

public class PreparedContactsViewModel extends AndroidViewModel {


    private ContactsDoa doa;
    private LiveData<List<Contacts>> contacts;

    public PreparedContactsViewModel(@NonNull Application application) {
        super(application);
        AppRoomDatabase db = AppRoomDatabase.getContactsDatabase(application);
        doa = db.contactsDoa();
        contacts = doa.getNewContacts("3");
    }

    LiveData<List<Contacts>> getContacts(){
        return contacts;
    }

    void deleteAllContacts(){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> doa.deleteContacts());
    }
}
