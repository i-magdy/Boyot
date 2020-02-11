package org.boyoot.app.ui.googleSheet;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleSheetViewModel extends ViewModel {

    private MutableLiveData<List<GoogleSheetModel>> data;

    public GoogleSheetViewModel() {
        data = new MutableLiveData<>();
    }



    public LiveData<List<GoogleSheetModel>> getData(){
        GoogleSheetClient.getGoogleSheetClient().getData().enqueue(new Callback<List<GoogleSheetModel>>() {
            @Override
            public void onResponse(Call<List<GoogleSheetModel>> call, Response<List<GoogleSheetModel>> response) {
                //Log.i("apiRetro",response.body().get(15).getPhone());
                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<List<GoogleSheetModel>> call, Throwable t) {

            }
        });

        return data;
    }
}
