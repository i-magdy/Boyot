package org.boyoot.app.ui.config;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigsViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public ConfigsViewModel() {
        mText = new MutableLiveData<>();

        mText.setValue("hey");
    }

    public LiveData<String> getText() {
        return mText;
    }
}