package org.boyoot.app.ui.employees;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityEmployeeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityEmployeeBinding binding;

    private static final String PROFILE_EMAIL_KEY = "profile email";
    private ViewPager pager;
    private ProfilePagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);
        //viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        List<Fragment> fragments = new ArrayList<>(2);
        if (!getIntent().hasExtra(PROFILE_EMAIL_KEY)){
           // viewModel.getProfile(getIntent().getStringExtra(PROFILE_EMAIL_KEY)); n 25
            finish();
        }
        pager = binding.profileViewPager;
        fragments.add(0,new ProfileFragment());
        fragments.add(1,new TasksFragment());
        adapter = new ProfilePagerAdapter(getSupportFragmentManager(),1,fragments);
        pager.setAdapter(adapter);
        binding.profileTabLayout.setupWithViewPager(pager);
        binding.profileTabLayout.getTabAt(0).setIcon(getDrawable(R.drawable.ic_worker));


        binding.profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              if (tab.getPosition() == 0){
                  tab.setIcon(getDrawable(R.drawable.ic_worker));
                  binding.profileTabLayout.getTabAt(1).setIcon(null);
              }else {

                  binding.profileTabLayout.getTabAt(0).setIcon(null);
                  tab.setIcon(getDrawable(R.drawable.ic_tasks));
                  tab.setText("Tasks");
              }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }
}
