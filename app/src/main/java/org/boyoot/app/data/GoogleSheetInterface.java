package org.boyoot.app.data;

import org.boyoot.app.model.GoogleSheetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
//https://script.google.com/macros/s/AKfycbwIfVeiBL09Ndor06DAuLeEBRgu6ivTvw7Ex-c3auMTeyAyBdg/exec
public interface GoogleSheetInterface {



    @GET("macros/s/AKfycbwIfVeiBL09Ndor06DAuLeEBRgu6ivTvw7Ex-c3auMTeyAyBdg/exec")
    Call<List<GoogleSheetModel>> getData();

}
