package org.boyoot.app.ui.appointment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.boyoot.app.R;
import org.boyoot.app.model.direction.Steps;
import org.boyoot.app.model.job.Directions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DirectionsPreviewMap extends SupportMapFragment implements OnMapReadyCallback ,OnDirectionChange{



    private DirectionsMapViewModel viewModel;
    private LatLng latLng;
    private LatLng endPoint;
    private   ArrayList<LatLng> arrayList;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        viewModel = new ViewModelProvider(this).get(DirectionsMapViewModel.class);

        arrayList = new ArrayList<>();
        viewModel.getDirections().observe(getViewLifecycleOwner(), new Observer<Directions>() {
            @Override
            public void onChanged(Directions directions) {
                latLng = new LatLng(directions.getSteps().get(0).getStart_location().getLat(),directions.getSteps().get(0).getStart_location().getLng());
                for (Steps steps : directions.getSteps()){
                    arrayList.add(new LatLng(steps.getStart_location().getLat(),steps.getStart_location().getLng()));
                }
                getMapAsync(DirectionsPreviewMap.this::onMapReady);
            }
        });
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       // LatLng latLng = new LatLng(31.242975, 29.962038);

        Bitmap bitmap = BitmapFactory.decodeResource(requireActivity().getResources(),R.drawable.ic_worker_home_marker);

        googleMap.addMarker(new MarkerOptions().position(latLng)
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .icon(bitmap));
        googleMap.addMarker(new MarkerOptions().position(endPoint));
        arrayList.add(endPoint);
        Polyline polyline =googleMap.addPolyline(new PolylineOptions().addAll(arrayList));
        polyline.setColor(requireActivity().getColor(R.color.colorFirstOption));
        polyline.setWidth(4);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
    }

    @Override
    public void onDirectionChanged(Directions directions) {
        if (directions != null){
            Log.i("TEST_MAP_FRAGMENT",directions.getDuration().getText());
            viewModel.setDirections(directions);
        }
    }

    @Override
    public void onSetDestinationPoints(double lat, double lng) {
        endPoint = new LatLng(lat,lng);
    }
}
