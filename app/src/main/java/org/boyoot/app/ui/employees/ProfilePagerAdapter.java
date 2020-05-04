package org.boyoot.app.ui.employees;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    public ProfilePagerAdapter(@NonNull FragmentManager fm, int behavior,List<Fragment> fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
       if (position == 0){
          return "Profile";
       }else{
           return "Tasks";
       }
    }


    @Override
    public int getCount() {
        return fragments.size();
    }



}
