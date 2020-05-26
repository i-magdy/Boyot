package org.boyoot.app.data;


import org.boyoot.app.model.geocode.Geocode;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeocodeClient {

    private static final String BASE_URL = "https://maps.googleapis.com/";
    private GeocodeInterface geocodeInterface;
    private static GeocodeClient INSTANCE;

    public GeocodeClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        geocodeInterface = retrofit.create(GeocodeInterface.class);
    }

    public static GeocodeClient getInstance(){
        if (null == INSTANCE){
            INSTANCE = new GeocodeClient();
        }
        return INSTANCE;
    }

    public Call<Geocode> getGeocodeData(String geoKey,String address) {
        return geocodeInterface.getGeocodeData(geoKey,address);
    }
}
