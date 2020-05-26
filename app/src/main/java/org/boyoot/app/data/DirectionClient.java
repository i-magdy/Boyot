package org.boyoot.app.data;

import org.boyoot.app.model.direction.Direction;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionClient {

    private static final String BASE_URL = "https://maps.googleapis.com/";
    private DirectionInterface directionInterface;
    private static DirectionClient INSTANCE;
    public DirectionClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        directionInterface = retrofit.create(DirectionInterface.class);

    }

    public static DirectionClient getINSTANCE(){
        if (null == INSTANCE){
            INSTANCE = new DirectionClient();
        }
        return INSTANCE;
    }

    public Call<Direction> getDirection(String origin,String destination,String key){
        return directionInterface.getDirection(origin,destination,key);
    }
}
