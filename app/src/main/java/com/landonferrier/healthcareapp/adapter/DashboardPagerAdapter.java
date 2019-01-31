package com.landonferrier.healthcareapp.adapter;


import com.landonferrier.healthcareapp.fragment.DashboardFragment;
import com.landonferrier.healthcareapp.fragment.DashboardSurgeryFragment;
import com.landonferrier.healthcareapp.fragment.JournalsFragment;
import com.landonferrier.healthcareapp.fragment.MedicationsFragment;
import com.landonferrier.healthcareapp.fragment.RemindersFragment;
import com.landonferrier.healthcareapp.fragment.SurgeryInfoFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Vishal Sharma on 29-Jul-17.
 */

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {


    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new DashboardSurgeryFragment();
                break;
            case 1:
                fragment = new RemindersFragment();
                break;
            case 2:
                fragment = new JournalsFragment();
                break;
            case 3:
                fragment = new MedicationsFragment();
                break;
            case 4:
                fragment = new SurgeryInfoFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }
}


