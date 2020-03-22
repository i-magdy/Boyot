package org.boyoot.app.data;

import org.boyoot.app.model.GoogleSheetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleSheetClient {

    private static final String BASE_URL = "https://script.google.com/";

    private GoogleSheetInterface googleSheetInterface;
    private static GoogleSheetClient googleSheetClient;

    public GoogleSheetClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        googleSheetInterface = retrofit.create(GoogleSheetInterface.class);
    }

    public static GoogleSheetClient getGoogleSheetClient() {

        if (null == googleSheetClient){
            googleSheetClient = new GoogleSheetClient();
        }
        return googleSheetClient;
    }

    public Call<List<GoogleSheetModel>> getData(){
        return googleSheetInterface.getData();
    }
}
