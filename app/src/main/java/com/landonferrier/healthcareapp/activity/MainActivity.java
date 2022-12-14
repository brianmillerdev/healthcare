package com.landonferrier.healthcareapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.adapter.DashboardPagerAdapter;
import com.landonferrier.healthcareapp.adapter.HomeViewPagerAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.utils.NonSwipeableViewPager;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_bar)
    public BottomNavigationView bnve;

    @BindView(R.id.view_pager)
    public NonSwipeableViewPager viewPager;

    public HomeViewPagerAdapter homeViewPagerAdapter;
    public DashboardPagerAdapter dashboardPagerAdapter;
    MenuItem prevMenuItem;
    public static int previousCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateViewPager();
        setUpViewPager();
        EventBus.getDefault().register(this);

    }

    private void updateViewPager() {
        if (Objects.requireNonNull(ParseUser.getCurrentUser().getJSONArray("surgeryIds")).length() > 0) {
            if (previousCount != 5) {
                bnve.getMenu().clear();
                bnve.inflateMenu(R.menu.navigation);
                dashboardPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(dashboardPagerAdapter);
                viewPager.setOffscreenPageLimit(5);
                previousCount = 5;
            }
        }else{
            if (previousCount != 3) {
                bnve.getMenu().clear();
                bnve.inflateMenu(R.menu.navigation_init);
                homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(homeViewPagerAdapter);
                viewPager.setOffscreenPageLimit(3);
                previousCount = 3;
            }
        }

        viewPager.setCurrentItem(0, false);
        if (prevMenuItem != null)
            prevMenuItem.setChecked(false);
        else
            bnve.getMenu().getItem(0).setChecked(false);

        bnve.getMenu().getItem(0).setChecked(true);
        prevMenuItem = bnve.getMenu().getItem(0);
    }
    private void setUpViewPager() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bnve.getMenu().getItem(position).setChecked(false);

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
                        EventBus.getDefault().post(new EventPush("updateCurrentSurgery", "fetchSurgery"));
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.action_info:
                        EventBus.getDefault().post(new EventPush("fetchSurgeryInfo", "fetchSurgeryInfo"));
                        viewPager.setCurrentItem(4, false);
                        break;
                    case R.id.action_journal:
                        EventBus.getDefault().post(new EventPush("updateJournals", "Journals"));
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.action_alarm:
                        EventBus.getDefault().post(new EventPush("updateReminders", "Reminders"));
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.action_meds:
                        EventBus.getDefault().post(new EventPush("updateMedications", "Medications"));
                        if (Objects.requireNonNull(ParseUser.getCurrentUser().getJSONArray("surgeryIds")).length() > 0) {
                            viewPager.setCurrentItem(3, false);
                        }else{
                            viewPager.setCurrentItem(2, false);
                        }
                        break;
                    default:
                        viewPager.setCurrentItem(0, false);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventPush event) {
        if (event.getMessage().equals("SurgeryInfo")) {
            viewPager.setCurrentItem(4, false);
            if (prevMenuItem != null)
                prevMenuItem.setChecked(false);
            else
                bnve.getMenu().getItem(4).setChecked(false);

            bnve.getMenu().getItem(4).setChecked(true);
            prevMenuItem = bnve.getMenu().getItem(4);
            EventBus.getDefault().post(new EventPush("fetchSurgeryInfo", "fetchSurgeryInfo"));
        }else if (event.getMessage().equals("updateCurrentSurgery")) {
            updateViewPager();
        }
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
