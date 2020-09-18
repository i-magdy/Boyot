package org.boyoot.app.ui.googleSheet;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.utilities.PhoneUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleSheetViewModel extends AndroidViewModel {


    private GoogleSheetRepo sheetRepo;
    private LiveData<List<GoogleSheet>> mainContacts;
    private MutableLiveData<Boolean> refreshingStateMutableLiveData;
    private MutableLiveData<List<GoogleSheet>> contacts;



    public GoogleSheetViewModel(Application app) {
        super(app);
        contacts = new MutableLiveData<>();
        sheetRepo = new GoogleSheetRepo(app);
        mainContacts = sheetRepo.getContacts();
        refreshingStateMutableLiveData = new MutableLiveData<>();
    }

    LiveData<List<GoogleSheet>> getMainContacts(){
        return mainContacts;
    }
    LiveData<List<GoogleSheet>> getContacts(){
        return contacts;
    }


    void sync(){
        sheetRepo.getSheetApis();
        refreshingStateMutableLiveData.setValue(true);
    }
    public void setContacts(List<GoogleSheet> contactList) {
        if (contactList != null){
            contacts.setValue(contactList);
            sheetRepo.cleanUpContacts(contactList);
            refreshingStateMutableLiveData.setValue(false);
        }

    }

    LiveData<Boolean> setRefreshing(){
        return refreshingStateMutableLiveData;
    }

}
