package org.boyoot.app.ui.moderator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import org.boyoot.app.R;
import org.boyoot.app.ui.employees.ProfileFragment;
import org.boyoot.app.ui.employees.ProfilePagerAdapter;
import org.boyoot.app.ui.employees.TasksFragment;
import org.boyoot.app.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class ModeratorActivity extends AppCompatActivity {

    private ViewPager pager;
    private ModeratorPagerAdapter adapter;
    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Fragment> fragments = new ArrayList<>(2);
        pager = findViewById(R.id.moderator_view_pager);
        tabs = findViewById(R.id.moderator_tab_layout);
        SearchView searchView = findViewById(R.id.main_search_view);
        fragments.add(0,new HomeFragment());
        fragments.add(1,new TasksFragment());
        adapter = new ModeratorPagerAdapter(getSupportFragmentManager(),1,fragments);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        tabs.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    searchView.setVisibility(View.VISIBLE);
                }else {
                    searchView.setVisibility(View.INVISIBLE);
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