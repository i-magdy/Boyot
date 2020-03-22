package org.boyoot.app.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.boyoot.app.R;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.Geocode;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {


    private GoogleMap googleMap;
    private MapViewModel viewModel;
    private LatLng latLng;
    private Activity context;
    private static final String contactIdKey = "contactId";
    private String id;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        if(context.getIntent().hasExtra(contactIdKey)){
            //viewModel.fetchContactFromCloud(context.getIntent().getStringExtra(contactIdKey));
        }

        viewModel.getContact().observe(getViewLifecycleOwner(), contact -> {
            Log.i("test_map","  "+contact.getId());
            if (contact.getCity().getLocationCode() != null) {
                Log.i("test_map","  "+contact.getId());
                id = contact.getId();
                if (!contact.getMapConfig().isSaved()) {
                    viewModel.getGeocodeData(context, getValidLocationCode(contact.getCity().getLocationCode()), context.getString(R.string.google_geocode_key));
                } else {
                    latLng = new LatLng(Double.parseDouble(contact.getMapConfig().getLat()), Double.parseDouble(contact.getMapConfig().getLng()));
                    getMapAsync(MapFragment.this::onMapReady);
                }
            }
        });

        viewModel.getGeocodeData().observe(getViewLifecycleOwner(), new Observer<Geocode>() {
            @Override
            public void onChanged(Geocode geocode) {
                //TODO update contact with location data
                latLng = new LatLng(Double.parseDouble(geocode.getResults().getGeometry().getLocation().getLat()), Double.parseDouble(geocode.getResults().getGeometry().getLocation().getLng()));
                getMapAsync(MapFragment.this::onMapReady);
            }
        });
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
       googleMap.addMarker(new MarkerOptions().position(latLng).title(id));
       googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    private String getValidLocationCode(String s){
        return s.replace("+","%2B");
    }
}
