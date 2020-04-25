package org.boyoot.app.ui.map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.boyoot.app.R;

import java.util.Objects;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener  {

    private GoogleMap map;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {


        Objects.requireNonNull(getActivity()).findViewById(R.id.main_search_view).setVisibility(View.GONE);
        getMapAsync(this);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(24.411458, 46.831473),
                        new LatLng(24.628834, 46.427069),
                        new LatLng(24.708714, 46.561565),
                        new LatLng(24.882110, 46.523090),
                        new LatLng(24.974232, 46.809358),
                        new LatLng(24.513419, 46.975734)));
        polygon1.setStrokeColor(Color.RED);
        polygon1.setStrokeWidth(3);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.293,45.619), 5));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }


    @Override
    public void onPolygonClick(Polygon polygon) {


    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}