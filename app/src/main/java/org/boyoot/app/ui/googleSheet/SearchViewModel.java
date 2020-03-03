package org.boyoot.app.ui.googleSheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.database.GoogleSheetDao;

public class SearchViewModel extends AndroidViewModel {

    private GoogleSheetDao dao;
    public SearchViewModel(@NonNull Application application) {
        super(application);

        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        dao = db.googleSheetDao();

    }

    LiveData<GoogleSheet> getContact(String phone){
        return dao.getContact(phone);
    }
}
