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
    private LiveData<List<GoogleSheet>> filteredContacts;
    private MutableLiveData<List<GoogleSheet>> contacts;
    private List<GoogleSheet> contactList;
    private List<GoogleSheet> filterContactList;


    public GoogleSheetViewModel(Application app) {
        super(app);
        contactList = new ArrayList<>();
        filterContactList = new ArrayList<>();
        contacts = new MutableLiveData<>();
        sheetRepo = new GoogleSheetRepo(app);
        mainContacts = sheetRepo.getContacts();
        filteredContacts = sheetRepo.filterContacts();

    }

   public LiveData<List<GoogleSheet>> getContacts(){
        return contacts;
    }

    LiveData<List<GoogleSheet>> getMainContacts(){
        return mainContacts;
    }

    LiveData<List<GoogleSheet>> filterContacts(){
        return filteredContacts;
    }


    void saveContact(GoogleSheet contact){
        sheetRepo.saveContact(contact);
    }


    void sync(){
        Log.i("refresh","true");
        sheetRepo.getSheetApis();
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

    void deleteContact(String phone){
        sheetRepo.deleteContact(phone);
    }

    void syncContacts(){
        contacts.setValue(contactList);
    }
    void getFilterContact(){
        contacts.setValue(filterContactList);
       // mainContacts = sheetRepo.filterContacts();
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

    public void setContactList(List<GoogleSheet> contactList) {
        this.contactList = contactList;
    }


    public void setContacts(List<GoogleSheet> contacts) {
        this.contactList = contacts;
    }

    public List<GoogleSheet> getContactList() {
        return contactList;
    }

    public void setFilterContactList(List<GoogleSheet> filterContactList) {
        this.filterContactList = filterContactList;
    }

    public void removeContacts(List<GoogleSheet> db){

        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {
                List<GoogleSheetModel> apiData = response.body();
                boolean found = false;
                for (int i = 0; i < db.size(); i++) {
                    String phone = db.get(i).getPhone();
                    for (int j = 0; j < apiData.size(); ++j) {
                        if (TextUtils.equals(phone, apiData.get(j).getPhone())) {
                            found = true;
                            break;
                        } else {
                            found = false;
                        }
                    }
                    if (!found) {
                        deleteContact(phone);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {

            }
        });

    }

}
