package org.boyoot.app.ui.config;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.model.Price;

import java.text.NumberFormat;

public class PriceConfigViewModel extends ViewModel {

    private MutableLiveData<Price> price;
    public MutableLiveData<String > windowCurrency;
    public MutableLiveData<String> concealed;
    public MutableLiveData<String> stand ;
    public MutableLiveData<String> split;
    public MutableLiveData<String> offers;
    public MutableLiveData<String> cover;
    private static final String CONFIG_PATH = "config";
    private static final String PRICE_PATH = "price";

    public PriceConfigViewModel(){
        price = new MutableLiveData<>();
        windowCurrency = new MutableLiveData<>();
        concealed= new MutableLiveData<>();
        stand= new MutableLiveData<>();
        split= new MutableLiveData<>();
        offers= new MutableLiveData<>();
        cover= new MutableLiveData<>();
    }

    LiveData<Price> getCurrentPrice(){
        return price;
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
                    char s = '$';
                    windowCurrency.setValue(s+" "+priceObject.getWindow());
                    stand.setValue(s+" "+priceObject.getStand());
                    concealed.setValue(s+" "+priceObject.getConcealed());
                    split.setValue(s+" "+priceObject.getSplit());
                    offers.setValue(s+" "+priceObject.getOffers());
                    cover.setValue(s+" "+priceObject.getCover());
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
