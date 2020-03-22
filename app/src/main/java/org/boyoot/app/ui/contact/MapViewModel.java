package org.boyoot.app.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.boyoot.app.data.GeocodeSingleton;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.Geocode;



public class MapViewModel extends ViewModel {

    private MutableLiveData<Contact> contactMutableLiveData;
    private MutableLiveData<Geocode> geocodeMutableLiveData;

    public MapViewModel(){
        contactMutableLiveData = new MutableLiveData<>();
        geocodeMutableLiveData = new MutableLiveData<>();
    }

    LiveData<Contact> getContact(){
        return contactMutableLiveData;
    }
    LiveData<Geocode> getGeocodeData(){
        return geocodeMutableLiveData;
    }

    void fetchContactFromCloud(String contactId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("contacts").document(contactId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    Contact contact = doc .toObject(Contact.class);
                    contactMutableLiveData.setValue(contact);
                }
            }
        });
    }

    void getGeocodeData(Activity context,String address, String key){
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
                    if (geocode.getStatus().equals("OK")){ geocodeMutableLiveData.setValue(geocode);}},
                error ->  Log.i("APITEST",error.toString()));
        RequestQueue queue = GeocodeSingleton.getInstance(context).
                getRequestQueue();
        GeocodeSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


}
