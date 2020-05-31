package org.boyoot.app.ui.googleSheet;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.AppRoomDatabase;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.database.GoogleSheetDao;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.utilities.PhoneUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.boyoot.app.utilities.WorkUtility.getStringSplit;
import static org.boyoot.app.utilities.WorkUtility.getStringWindow;
import static org.boyoot.app.utilities.WorkUtility.getStringStand;
import static org.boyoot.app.utilities.WorkUtility.getStringConcealed;
import static org.boyoot.app.utilities.WorkUtility.getStringCover;

public class GoogleSheetRepo {

    private GoogleSheetDao sheetDao;
    private LiveData<List<GoogleSheet>> contacts;


    GoogleSheetRepo(Application app){
        AppRoomDatabase db = AppRoomDatabase.getDatabase(app);
        sheetDao = db.googleSheetDao();
        contacts = sheetDao.getContacts();

    }


    LiveData<List<GoogleSheet>> getContacts(){
        return contacts;
    }

    LiveData<List<GoogleSheet>> filterContacts(){
        return sheetDao.filterContacts();
    }



    void saveContact(GoogleSheet contact){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> sheetDao.saveContact(contact));
    }
    void updateLocationLink(String phone,String link,String state){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> sheetDao.updateLocationCode(phone,link,state));
    }

    void updateCloudId(String phone,String cloudId){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> sheetDao.updateCloudId(phone,cloudId));
    }

    void updateContactId(String phone,String contactId){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> sheetDao.updateContactId(phone,contactId));
    }

    void deleteContact(String phone){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> sheetDao.deleteContact(phone));
    }

    void getSheetApis(){
        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {
                List<GoogleSheetModel> originList = response.body();
                assert originList != null;
                for (GoogleSheetModel data : originList ){
                    if (!data.getPhone().equals("invalid")){
                        saveContact(new GoogleSheet(data.getPhone(),data.getTime_stamp(),data.getDate(),
                                getStringSplit(data.getSplit()),getStringWindow(data.getWindow()),
                                getStringCover(data.getCover()),getStringStand(data.getStand()),
                                getStringConcealed(data.getConcealed()),data.getCity(),data.getNote(),data.getOffers(),"0",null));
                        if (!data.getPlus_code().equals("") && data.getPlus_code().contains("+")){
                            updateLocationLink(data.getPhone(),data.getPlus_code(),"2");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {
                getSheetApis();
            }
        });

    }

}
