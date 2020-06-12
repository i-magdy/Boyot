package org.boyoot.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MainViewModel extends AndroidViewModel {

    private MainRepo repo;
    public MainViewModel(@NonNull Application application) {
        super(application);
        repo = new MainRepo(application);

    }


    public void syncContacts(){
        repo.getContactsFromCloud();
    }
}
