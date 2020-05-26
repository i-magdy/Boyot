package org.boyoot.app.ui.googleSheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UpdateContactViewModel extends AndroidViewModel {

    private GoogleSheetRepo sheetRepo;

    public UpdateContactViewModel(@NonNull Application app) {
        super(app);
        sheetRepo = new GoogleSheetRepo(app);
    }

    void updateLocationCode(String phone,String code,String state){
        sheetRepo.updateLocationLink(phone,code,state);
    }
    void updateCloudId(String phone,String cloudId){
        sheetRepo.updateCloudId(phone,cloudId);
    }

    void updateContactId(String phone,String contactId){
        sheetRepo.updateContactId(phone,contactId);
    }
}
