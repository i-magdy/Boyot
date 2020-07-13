package org.boyoot.app.ui.config;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.boyoot.app.data.GeocodeSingleton;
import org.boyoot.app.model.Branch;
import org.boyoot.app.model.Car;
import org.boyoot.app.model.geocode.Geocode;

import java.util.List;

public class BranchViewModel extends ViewModel {

    public MutableLiveData<String> title;
    public MutableLiveData<String> code;
    public MutableLiveData<String> dayStart;
    public MutableLiveData<String> dayEnd;
    MutableLiveData<List<Car>> cars;
    private MutableLiveData<Geocode> geocodeMutableLiveData;

    private static final String BRANCHES_PATH="branches";

    public BranchViewModel() {
        title = new MutableLiveData<>();
        code = new MutableLiveData<>();
        dayStart = new MutableLiveData<>();
        dayEnd = new MutableLiveData<>();
        cars = new MutableLiveData<>();
        geocodeMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Car>> getCarsList(){
        return cars;
    }
    LiveData<Geocode> getGeocodeData(){
        return geocodeMutableLiveData;
    }
    public void getBranch(Activity context,@NonNull String branch,String key){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection(BRANCHES_PATH).document(branch);
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Branch branch = documentSnapshot.toObject(Branch.class);
                        title.setValue(branch.getTitle());
                        code.setValue(branch.getBranchId());
                        dayStart.setValue(branch.getDayStart());
                        dayEnd.setValue(branch.getDayEnd());
                        cars.setValue(branch.getCars());
                        if (key != null && branch.getLocation() != null) {
                            getGeocodeData(context, getValidLocationCode(branch.getLocation()), key);
                        }
                    }
                }
            });

    }

    void getGeocodeData(Activity context, String address, String key){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String url ="https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key="+key;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                response -> {
                    Geocode geocode = gson.fromJson(response.toString(),Geocode.class);
                    // Log.i("APITEST",geocode.getStatus()+"\n"+response);
                    if (geocode.getStatus().equals("OK")){ geocodeMutableLiveData.setValue(geocode);}},
                error ->  Log.i("APITEST",error.toString()));
        RequestQueue queue = GeocodeSingleton.getInstance(context).
                getRequestQueue();
        GeocodeSingleton.getInstance(context).addToRequestQueue(stringRequest);
        //queue.start();
    }

    void updateMap(String branch,String compound_code,String globalCode,String placeId,String lat,String lng ) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(BRANCHES_PATH).document(branch)
                .update(
                        "mapConfig.compound_code", compound_code,
                        "mapConfig.globalCode", globalCode,
                        "mapConfig.placeId", placeId,
                        "mapConfig.lat", lat,
                        "mapConfig.lng", lng,
                        "mapConfig.saved", true
                )
                .addOnSuccessListener(aVoid -> {

                });
    }

    private String getValidLocationCode(String s){
        return s.replace("+","%2B");
    }
}
