package org.boyoot.app.ui.appconfigs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConfigsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is configurations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}