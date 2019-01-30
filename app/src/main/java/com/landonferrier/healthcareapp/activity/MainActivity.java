package com.landonferrier.healthcareapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.adapter.HomeViewPagerAdapter;
import com.landonferrier.healthcareapp.utils.NonSwipeableViewPager;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_bar)
    public BottomNavigationView bnve;

    @BindView(R.id.view_pager)
    public NonSwipeableViewPager viewPager;

    public HomeViewPagerAdapter homeViewPagerAdapter;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpViewPager();

    }

    private void setUpViewPager() {
        homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homeViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bnve.getMenu().getItem(0).setChecked(false);

                bnve.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bnve.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Utils.dismissKeyboard(getBaseContext(), rootView);
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager.setCurrentItem(0, false);
//                        EventBus.getDefault().post(new EventPush("refresh", "give"));
                        break;
                    case R.id.action_info:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.action_calendar:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.action_alarm:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.action_meds:
                        viewPager.setCurrentItem(2, false);
                        break;
                    default:
                        viewPager.setCurrentItem(0, false);
                        break;
                }
                return true;
            }
        });
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.nav_profile:
//                viewPager.setCurrentItem(3, false);
//                if (prevMenuItem != null)
//                    prevMenuItem.setChecked(false);
//                else
//                    bnve.getMenu().getItem(0).setChecked(false);
//
//                bnve.getMenu().getItem(3).setChecked(true);
//                prevMenuItem = bnve.getMenu().getItem(3);
//                break;
//        }
//    }
//
}
