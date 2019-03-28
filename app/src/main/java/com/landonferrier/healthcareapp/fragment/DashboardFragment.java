package com.landonferrier.healthcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.activity.HelpActivity;
import com.landonferrier.healthcareapp.activity.ProfileActivity;
import com.landonferrier.healthcareapp.activity.SurgeryActivity;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends BaseFragment {
//    @BindView(R.id.btn_info)
//    ImageView btnInfo;
//    @BindView(R.id.btn_profile)
//    ImageView btnProfile;
    @BindView(R.id.btn_find_surgery)
    CustomFontTextView btnFindSurgery;

//    SimpleAdapter mAdapter;

    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
        btnFindSurgery.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.btn_info:
//                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), HelpActivity.class));
//                break;
//            case R.id.btn_profile:
//                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), ProfileActivity.class));
//
//                break;
            case R.id.btn_find_surgery:
                Intent intent = new Intent(getActivity(), SurgeryActivity.class);
                intent.putExtra("type", "new");
                Objects.requireNonNull(getActivity()).startActivity(intent);
                break;

        }
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