package org.boyoot.app.ui.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.animation.Animator;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.databinding.ActivityContactBinding;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements OnMapReadyCallback {

    String contactCloudId;
    private static final String contactIdKey = "contactId";
    private ContactViewModel viewModel;
    private  SupportMapFragment mapFragment;
    private String phone;
    private ActivityContactBinding binding;
    private  Intent call;
    private String id;
    private boolean changed = false;
    private boolean isBottomExpended = false;
    private static final  int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);
        viewModel = new ContactViewModel(getApplication());
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        if (getIntent().hasExtra(contactIdKey)){
            contactCloudId = getIntent().getStringExtra(contactIdKey);
            viewModel.fetchContact(contactCloudId);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);
            viewModel.getPriority().observe(this, this::setPriorityState);
        }
        binding.editContactFab.setOnClickListener(v -> editContact());
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(binding.frameLayoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        binding.contactBottomSheet.expendBottomSheet.setBackground(getDrawable(R.drawable.arrowbottom));
        binding.contactBottomSheet.expendBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!isBottomExpended) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        isBottomExpended = true;
                    } else {
                        isBottomExpended = false;
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    }

            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                    if (i == 3){
                        isBottomExpended = true;
                    }else if (i == 4){
                        isBottomExpended = false;
                    }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v >0.5){
                    binding.contactBottomSheet.expendBottomSheet.setBackground(getDrawable(R.drawable.close));
                    binding.contactBottomSheet.expendBottomSheet.animate().rotation(90*v).start();
                }else{
                    binding.contactBottomSheet.expendBottomSheet.setBackground(getDrawable(R.drawable.arrowbottom));
                    binding.contactBottomSheet.expendBottomSheet.animate().rotation(180*v).start();
                    binding.contactBottomSheet.expendBottomSheet.animate().rotation(180).start();
                }
            }
        });


        viewModel.getPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()){
                    phone=s;
                }
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contact_map_view);

        viewModel.getId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    id = s;
                    mapFragment.getMapAsync(ContactActivity.this::onMapReady);
                }
            }
        });


        binding.contactBottomSheet.sendWhatsApp.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://wa.me/"+"966"+phone);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(sendIntent,"Choose App"));

        });

        binding.contactBottomSheet.makeCall.setOnClickListener(v -> {
          call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+966"+ phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {

                    ActivityCompat.requestPermissions(ContactActivity.this,new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);


                }
                return;
            }
            startActivity(call);
        });


       /* String placeId = "EisxMyBNYXJrZXQgU3RyZWV0LCBXaWxtaW5ndG9uLCBOQyAyODQwMSwgVVNB";
        Places.initialize(getApplicationContext(), getString(R.string.google_place_key));

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);
// Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i("TAG", "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });*/
        /*RequestQueue requestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        //  String url ="https://script.googleusercontent.com/macros/echo?user_content_key=g0RDsSYxb1jj4-8cR99DdjzlHDjGup3qQKrBlL7fLugz9wjpTmkUmaPieV_0zguUCz1L8-yCgvC1EK-x7IR2yh4XD1-dN9RRm5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnEK29S8VPbzlIqohAe5JY5iml3WhGABCneVzE3xJU3_yKUyBcMZ8Mgwp0wSG0N8Cv2nk4I7jbIL8&lib=MXrjKRtnBFuuONreZiSXIUq5ff9lVmTvD";
        String url ="https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyAnR-tuIFYzC8WHKcAQ4yI-PV_7aPZF5DA";
// Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        Log.i("APITEST",response);
                        //text.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.i("APITEST",error.toString());
                    }
                });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);*/


    }

    private void setPriorityState(String state){
        binding.contactProgressBar.setVisibility(View.INVISIBLE);
        switch (state){
            case "1":
                binding.priorityTagTv.setText(getString(R.string.state_location_needed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.location_needed_tag));
                break;
            case "3":
                binding.priorityTagTv.setText(getString(R.string.state_prepared_contact));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.prepared_contact_tag));
                break;
            case "4":
                binding.priorityTagTv.setText(getString(R.string.state_date_picked));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.date_picked_tag));
                break;
            case "5":
                binding.priorityTagTv.setText(getString(R.string.state_date_approved));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.date_approved_tag));
                break;
            case "6":
                binding.priorityTagTv.setText(getString(R.string.state_work_delayed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.work_delayed_tag));
                break;
            case "7":
                binding.priorityTagTv.setText(getString(R.string.state_work_done));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.work_done_tag));
                break;
            case "8":
                binding.priorityTagTv.setText(getString(R.string.state_reviewed));
                binding.priorityTagTv.setBackground(getDrawable(R.drawable.reviewed_tag));
                break;

        }
    }

    private void editContact(){
        Intent i = new Intent(getApplicationContext(),EditContactActivity.class);
        i.putExtra(contactIdKey,contactCloudId);
        startActivity(i);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(24.632444, 46.563361);
        googleMap.addMarker(new MarkerOptions().position(sydney).title(id));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivity(call);
                } else {

                }
                return;
            }


        }
    }
}
