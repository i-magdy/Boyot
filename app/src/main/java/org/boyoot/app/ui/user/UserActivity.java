package org.boyoot.app.ui.user;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.boyoot.app.MainActivity;
import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityUserBinding;
import org.boyoot.app.model.UserProfileModel;
import org.boyoot.app.ui.login.LoginActivity;
import org.boyoot.app.ui.moderator.ModeratorActivity;

public class UserActivity extends AppCompatActivity {

    ActivityUserBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private UserViewModel viewModel;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null){
            viewModel.checkCurrentUser(user.getUid());
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        loginMotion();
        binding.logoIv.animate().scaleX(80f).scaleY(80f).setDuration(700).start();
        binding.logoIv.animate().translationY(-400).setDuration(1000).start();
        binding.makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WebActivity.class));
            }
        });
        viewModel.getUser().observe(this, new Observer<UserProfileModel>() {
            @Override
            public void onChanged(UserProfileModel userProfileModel) {
                if (userProfileModel != null){
                    editor.putString(getString(R.string.saved_role_value_key), userProfileModel.getRole());
                    editor.apply();
                    if (userProfileModel.getRole().equals("Admin")){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        viewModel.checkCurrentUser(null);
                        finish();
                    } else if (userProfileModel.getRole().equals("Moderator")){
                        startActivity(new Intent(getApplicationContext(), ModeratorActivity.class));
                        viewModel.checkCurrentUser(null);
                        finish();
                    }


                }
            }
        });


        binding.employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    void loginMotion(){
        binding.beginningCoverIv.animate()
                .scaleY(500f)
                .scaleX(500f)
                .setDuration(700)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.makeRequestButton.setVisibility(View.VISIBLE);
                        binding.introTv.setVisibility(View.VISIBLE);
                        binding.employeeButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

    }




}
