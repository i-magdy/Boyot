package org.boyoot.app;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.widget.ImageView;
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
    private MainViewModel viewModel;

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
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home/*
                R.id.nav_home, R.id.nav_employees, R.id.nav_reports,
                R.id.nav_config, R.id.nav_map, R.id.nav_send*/)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.syncContacts();
       /* NavigationView view =  binding.navView;
        Menu menu = view.getMenu();
        menu.findItem(R.id.nav_employees).setVisible(false);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            binding.appBarContent.syncProgressBar.animate().setDuration(1500)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    binding.appBarContent.syncProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.appBarContent.syncProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            viewModel.syncContacts();
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
