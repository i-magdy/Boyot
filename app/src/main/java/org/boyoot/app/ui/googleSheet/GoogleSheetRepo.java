package org.boyoot.app.ui.googleSheet;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;

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

    void saveContact(GoogleSheet contact){

        AppRoomDatabase.databaseWriteExecutor.execute(() -> sheetDao.saveContact(contact));
    }

    void updateCloudId(String phone,String cloudId){
        AppRoomDatabase.databaseWriteExecutor.execute(()-> sheetDao.updateCloudId(phone,cloudId));
    }

    void deleteContact(String phone){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> sheetDao.deleteContact(phone));
    }

    void getSheetApis(){
        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {

                Log.i("googleApi","worked");


                for (GoogleSheetModel data : cleanUpApiList(response.body())){
                    saveContact(new GoogleSheet(data.getPhone(),data.getState(),data.getCity(),data.getDate(),data.getCode()));
                }

            }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {

            }
        });

    }

    private List<GoogleSheetModel> cleanUpApiList(List<GoogleSheetModel> list){
        List<GoogleSheetModel> newList = new ArrayList<>();

        if (list != null){
            newList = new ArrayList<>(list.size());

            for (int i = list.size()-1;i >=0;i--){
                if (!TextUtils.equals(PhoneUtility.getValidPhoneNumber(list.get(i).getPhone()),"invalid")){

                    newList.add(list.get(i));


                }
            }
        }

        return newList;

    }
}
