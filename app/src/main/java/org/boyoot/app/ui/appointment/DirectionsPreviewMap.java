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
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.boyoot.app.R;
import org.boyoot.app.model.direction.Steps;
import org.boyoot.app.model.job.Directions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DirectionsPreviewMap extends SupportMapFragment implements OnMapReadyCallback ,OnDirectionChange, GoogleMap.OnMarkerClickListener {



    private DirectionsMapViewModel viewModel;
    private LatLng homeWorker;
    private LatLng endPoint;
    private ArrayList<LatLng> arrayList;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        viewModel = new ViewModelProvider(this).get(DirectionsMapViewModel.class);

        viewModel.getDirections().observe(getViewLifecycleOwner(), new Observer<Directions>() {
            @Override
            public void onChanged(Directions directions) {
                arrayList = new ArrayList<>();
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

        if (arrayList != null && endPoint != null) {

            arrayList.add(endPoint);
            Polyline polyline = googleMap.addPolyline(new PolylineOptions().addAll(arrayList));
            polyline.setColor(requireActivity().getColor(R.color.colorFirstOption));
            polyline.setWidth(4);
            googleMap.addMarker(new MarkerOptions().position(homeWorker).icon(BitmapDescriptorFactory.fromResource(R.drawable.worker_home))).setTag(0);
            googleMap.addMarker(new MarkerOptions().position(endPoint)).setTag(1);
            googleMap.setOnMarkerClickListener(this);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endPoint, 11));
        }
        if (endPoint != null && homeWorker != null) {
            googleMap.addMarker(new MarkerOptions().position(homeWorker).icon(BitmapDescriptorFactory.fromResource(R.drawable.worker_home))).setTag(0);
            googleMap.addMarker(new MarkerOptions().position(endPoint)).setTag(1);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endPoint, 11));
        }

    }

    @Override
    public void onDirectionChanged(Directions directions) {
        if (directions != null){
            Log.i("TEST_MAP_FRAGMENT",directions.getDuration().getText());
            viewModel.setDirections(directions);
        }
    }

    @Override
    public void setDestinationAndOriginalMarks(double oLat, double oLng, double lat, double lng) {
        homeWorker = new LatLng(oLat,oLng);
        endPoint = new LatLng(lat,lng);
        getMapAsync(this::onMapReady);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null) {
            int tag = (Integer) marker.getTag();
            if (tag == 0) {
                Toast.makeText(getContext(), "mark clicked", Toast.LENGTH_LONG).show();
                Log.i("MARKER", "  :   >> CLICKED");

            }
        }
        return false;
    }
}
