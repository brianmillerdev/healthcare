package com.landonferrier.healthcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.activity.HelpActivity;
import com.landonferrier.healthcareapp.activity.ProfileActivity;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardSurgeryFragment extends BaseFragment {
    @BindView(R.id.btn_info)
    ImageView btnInfo;
    @BindView(R.id.btn_profile)
    ImageView btnProfile;
    @BindView(R.id.tv_surgery_name)
    CustomFontTextView tvSurgeryName;
    @BindView(R.id.tv_surgery_date)
    CustomFontTextView tvSurgeryDate;
    @BindView(R.id.tv_task_title)
    CustomFontTextView tvTaskTitle;
    @BindView(R.id.btn_left_arrow)
    ImageView btnLeft;
    @BindView(R.id.btn_right_arrow)
    ImageView btnRight;

    @BindView(R.id.view_current_surgery)
    RelativeLayout viewCurrentSurgery;

    Date showDate = new Date();
    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_dashboard_sugery, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("DefaultLocale")
    public void initView() {
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        viewCurrentSurgery.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnProfile.setOnClickListener(this);

        JSONArray ids = ParseUser.getCurrentUser().getJSONArray("surgeryIds");
        if (ids.length() > 0) {
            try {
                ParseObject surgery = ParseObject.createWithoutData("Surgery", String.valueOf(ids.get(0)));
                surgery.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            tvSurgeryName.setText(ParseUser.getCurrentUser().getString("surgeryName"));
                            Date date = ParseUser.getCurrentUser().getDate("surgeryDate");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
                            String dateString = simpleDateFormat.format(date);
                            tvSurgeryDate.setText(String.format("%s >", dateString));
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_info:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.btn_profile:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.btn_left_arrow:
                fintPreviousDate();
                break;
            case R.id.btn_right_arrow:
                fintNextDate();
                break;
            case R.id.view_current_surgery:
                EventBus.getDefault().post(new EventPush("SurgeryInfo", "Surgery"));
                break;

        }
    }

    public void fintPreviousDate() {
        ParseQuery reminderQuery = PFQuery(className: "Reminder")
        reminderQuery.whereKey("creatorId", equalTo: PFUser.current()?.objectId!)

        if forward {

            reminderQuery.whereKey("time", greaterThan: self.showDate)
            reminderQuery.order(byAscending: "time")
        } else {

            reminderQuery.whereKey("time", lessThan: self.showDate)
            reminderQuery.order(byDescending: "time")
        }



    }

    public void fintNextDate() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (subdivisionArr.size() == 0) {
//            getInitialData();
//        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventPush event) {
        if (event.getMessage().equals("updatedBadge")) {
            initView();
        }
    }



}