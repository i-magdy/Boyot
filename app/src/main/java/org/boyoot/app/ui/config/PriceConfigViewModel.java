package org.boyoot.app.ui.config;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.Price;

public class PriceConfigViewModel extends ViewModel {

    private MutableLiveData<Price> price;
    private MutableLiveData<String > window;
    private MutableLiveData<String> concealed;
    private MutableLiveData<String> stand ;
    private MutableLiveData<String> split;
    private MutableLiveData<String> offers;
    private MutableLiveData<String> cover;

    private static final String CONFIG_PATH = "config";
    private static final String PRICE_PATH = "price";

    public PriceConfigViewModel(){
        price = new MutableLiveData<>();
        window= new MutableLiveData<>();
        concealed= new MutableLiveData<>();
        stand= new MutableLiveData<>();
        split= new MutableLiveData<>();
        offers= new MutableLiveData<>();
        cover= new MutableLiveData<>();
    }

    LiveData<Price> getCurrentPrice(){
        return price;
    }
    public LiveData<String> getWindow(){
        return window;
    }
    public LiveData<String> getStand(){
        return stand;
    }
    public LiveData<String> getSplit(){
        return split;
    }

    public LiveData<String> getCover(){
        return cover;
    }

    public LiveData<String> getConcealed(){
        return concealed;
    }

    public LiveData<String> getOffers(){
        return offers;
    }

    void fetchCurrentPrice(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(CONFIG_PATH).document(PRICE_PATH);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Price priceObject = documentSnapshot.toObject(Price.class);
                    price.setValue(priceObject);
                    window.setValue(priceObject.getWindow()+"");
                    stand.setValue(priceObject.getStand()+"");
                    concealed.setValue(priceObject.getConcealed()+"");
                    split.setValue(priceObject.getSplit()+"");
                    offers.setValue(priceObject.getOffers()+"");
                    cover.setValue(priceObject.getCover()+"");
                }
            }
        });
    }

    void updateNewPrice(Price p){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(CONFIG_PATH).document(PRICE_PATH);
        doc.set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

}
