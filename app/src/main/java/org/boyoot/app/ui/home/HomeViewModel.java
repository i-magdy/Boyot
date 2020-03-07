package org.boyoot.app.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.database.ContactsDoa;
import org.boyoot.app.model.Contact;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<Contacts>> contacts;
    private ContactsDoa doa;

    public HomeViewModel(Application app) {
        super(app);

        AppRoomDatabase db = AppRoomDatabase.getContactsDatabase(app);
        doa = db.contactsDoa();
        contacts = doa.getContactsFromCloud();

    }

    public LiveData<List<Contacts>> getContacts() {
        return contacts;
    }

    void saveContact(Contacts contact){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> doa.saveContacts(contact));
    }
}