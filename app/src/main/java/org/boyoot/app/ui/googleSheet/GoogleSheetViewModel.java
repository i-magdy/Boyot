package org.boyoot.app.ui.googleSheet;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private LiveData<List<GoogleSheet>> contacts;



    public GoogleSheetViewModel(Application app) {
        super(app);
        sheetRepo = new GoogleSheetRepo(app);
        contacts = sheetRepo.getContacts();

    }



    LiveData<List<GoogleSheet>> getContacts(){
        return contacts;
    }

    void saveContact(GoogleSheet contact){
        sheetRepo.saveContact(contact);
    }


    void sync(){
        sheetRepo.getSheetApis();
    }

    void updateLocationLink(String phone,String link,String state){
        sheetRepo.updateLocationLink(phone,link,state);
    }
    void updateCloudId(String phone,String cloudId){
        sheetRepo.updateCloudId(phone,cloudId);
    }

    void deleteContact(String phone){
        sheetRepo.deleteContact(phone);
    }


    /*LiveData<List<GoogleSheetModel>> getData(){
     return getDataApis();
    }

    private void getDataApis(){


        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {

                Log.i("googleApi","worked");
             //   data.setValue(cleanUpApiList(response.body()));

            }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {

            }
        });

       // return data;
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

    }*/
}
