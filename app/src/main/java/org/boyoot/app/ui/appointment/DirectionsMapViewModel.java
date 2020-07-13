package org.boyoot.app.ui.appointment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boyoot.app.model.job.Directions;

public class DirectionsMapViewModel extends ViewModel {

    private MutableLiveData<Directions> directionsMutableLiveData;
    public DirectionsMapViewModel() {

        directionsMutableLiveData = new MutableLiveData<>();
    }

    LiveData<Directions> getDirections(){
        return directionsMutableLiveData;
    }

    void setDirections(Directions directions){
        directionsMutableLiveData.setValue(directions);
    }
}
