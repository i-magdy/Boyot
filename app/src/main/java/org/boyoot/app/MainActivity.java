package org.boyoot.app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.boyoot.app.databinding.ActivityMainBinding;
import org.boyoot.app.model.UserProfileModel;
import org.boyoot.app.ui.user.UserActivity;
import org.boyoot.app.ui.user.UserViewModel;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ActivityMainBinding binding;
    private TextView userNameTv;
    private TextView userEmailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        NavigationView navigationView = binding.navView;
        userNameTv = navigationView.getHeaderView(0).findViewById(R.id.user_name_tv);
        userEmailTv = navigationView.getHeaderView(0).findViewById(R.id.user_email_tv);
        setSupportActionBar(binding.appBarContent.toolbar);
        SearchView searchView = findViewById(R.id.search_view_bar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.checkCurrentUser(user.getUid());
        userViewModel.getUser().observe(this, new Observer<UserProfileModel>() {
            @Override
            public void onChanged(UserProfileModel userProfileModel) {
                if (userProfileModel != null){
                    userEmailTv.setText(userProfileModel.getEmail());
                    userNameTv.setText(userProfileModel.getUserName());
                    Log.i("check_user",userProfileModel.getEmail());
                }
            }
        });


        DrawerLayout drawer = binding.drawerLayout;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_employees, R.id.nav_reports,
                R.id.nav_config, R.id.nav_map, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.syncContacts();
        binding.appBarContent.mainSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.syncContacts();
                binding.appBarContent.mainSwipeRefresh.setRefreshing(false);
            }
        });
        //startActivity(new Intent(this, GoogleSheetActivity.class));
        //TODO work manger
        /*Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UpdateContactsWorker.class)
                .setConstraints(constraints)
                .build();
        //WorkManager.getInstance(getApplicationContext()).enqueue(request);
        WorkManager.getInstance(getApplicationContext()).enqueue(request);
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
            finish();
            return true;

        }else{
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
