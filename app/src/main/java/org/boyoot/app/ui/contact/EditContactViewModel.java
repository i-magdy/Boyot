package org.boyoot.app.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import org.boyoot.app.model.Contact;

public class EditContactViewModel extends ViewModel {

    MutableLiveData<Contact> data;

    public EditContactViewModel() {

        data = new MutableLiveData<>();
    }



    LiveData<Contact> getContact(){
        return data;
    }


    void setData(Contact contact){

        data.setValue(contact);
    }


}
