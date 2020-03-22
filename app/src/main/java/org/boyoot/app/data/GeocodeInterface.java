package org.boyoot.app.data;



import org.boyoot.app.model.Geocode;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodeInterface {

    //address={address}&key={geoKey}

    @GET("maps/api/geocode/json")
    Call<Geocode> getGeocodeData(@Query(value = "address") String address, @Query("key")String geoKey);


}
