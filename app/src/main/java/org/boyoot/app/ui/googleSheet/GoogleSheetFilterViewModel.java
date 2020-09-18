package org.boyoot.app.ui.googleSheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.boyoot.app.database.GoogleSheet;

import java.util.List;

public class GoogleSheetFilterViewModel extends AndroidViewModel {
    private GoogleSheetRepo sheetRepo;
    private LiveData<List<GoogleSheet>> filterContacts;

    public GoogleSheetFilterViewModel(@NonNull Application application) {
        super(application);
        sheetRepo = new GoogleSheetRepo(application);
        filterContacts = sheetRepo.filterContacts();

    }


    LiveData<List<GoogleSheet>> getFilteredContacts(){
        return filterContacts;
    }
}
