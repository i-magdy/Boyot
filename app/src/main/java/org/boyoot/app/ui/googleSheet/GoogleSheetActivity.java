package org.boyoot.app.ui.googleSheet;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.databinding.ActivityGoogleSheetBinding;
import org.boyoot.app.model.City;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.GoogleSheetModel;
import org.boyoot.app.model.MapConfig;
import org.boyoot.app.model.Work;
import org.boyoot.app.ui.contact.ContactActivity;
import org.boyoot.app.ui.contact.EditContactActivity;
import org.boyoot.app.utilities.PhoneUtility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.boyoot.app.utilities.CityUtility.getCityCode;
import static org.boyoot.app.utilities.CityUtility.getInterval;

public class GoogleSheetActivity extends AppCompatActivity implements GoogleSheetListAdapter.ListItemOnClickListener , OnGoogleSheetFiltered {

    private ActivityGoogleSheetBinding binding;
    private GoogleSheetViewModel viewModel;
    private GoogleSheetListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_google_sheet);
        binding.googleSheetToolbar.setTitle("");
        binding.swipeRefreshSheet.setColorSchemeResources(R.color.colorAccent);
        binding.swipeRefreshSheet.setRefreshing(true);
        setSupportActionBar(binding.googleSheetToolbar);
        adapter = new GoogleSheetListAdapter(getApplicationContext(), this);
        binding.googleSheetRecycler.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);
        viewModel.sync();
        viewModel.getMainContacts().observe(this,viewModel::setContacts);
        binding.googleSheetRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeRefreshSheet.setOnRefreshListener(() -> viewModel.sync());
        binding.googleSheetToolbar.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), GoogleSearchActivity.class)));
        binding.createContactFab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EditContactActivity.class)));
        viewModel.getContacts().observe(this,adapter::setDataList);
        viewModel.setRefreshing().observe(this,binding.swipeRefreshSheet::setRefreshing);

    }

    @Override
    public void onListItemClicked(GoogleSheet request) {
        if (isNetworkAvailable()) {
            Intent i = new Intent(getApplicationContext(), UpdateContactDialog.class);
            i.putExtra("requestKey", request);
            startActivity(i);
        } else {
            Toast.makeText(this, "check your connection", Toast.LENGTH_LONG).show();
        }

    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onGoogleSheetFiltered(List<GoogleSheet> filteredSheetList) {
        viewModel.setContacts(filteredSheetList);
    }
}

