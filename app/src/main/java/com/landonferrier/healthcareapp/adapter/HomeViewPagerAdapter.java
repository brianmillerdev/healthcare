package com.landonferrier.healthcareapp.adapter;


import com.landonferrier.healthcareapp.fragment.DashboardFragment;
import com.landonferrier.healthcareapp.fragment.MedicationsFragment;
import com.landonferrier.healthcareapp.fragment.RemindersFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Vishal Sharma on 29-Jul-17.
 */

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {


    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                break;
            case 1:
                fragment = new RemindersFragment();
                break;
            case 2:
                fragment = new MedicationsFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}


