package com.landonferrier.healthcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.activity.DoctorSurveyActivity;
import com.landonferrier.healthcareapp.activity.HospitalStaySurveyActivity;
import com.landonferrier.healthcareapp.adapter.SurveysAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveysFragment extends BaseFragment implements SurveysAdapter.OnItemSelectedListener {
    @BindView(R.id.rc_surveys)
    RecyclerView rcSurveys;

    SurveysAdapter mAdapter;
    public KProgressHUD hud;

    ArrayList<ParseObject> surveys = new ArrayList<>();

    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_surveys, container, false);
        ButterKnife.bind(this, view);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        rcSurveys.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcSurveys.addItemDecoration(dividerItemDecoration);

        mAdapter = new SurveysAdapter(getContext(), surveys, this);
        rcSurveys.setAdapter(mAdapter);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("DefaultLocale")
    public void initView() {
        fetchSurveys();
    }

    public void fetchSurveys() {
        if (hud != null) {
            if (!hud.isShowing()) {
                hud.show();
            }
        }
        surveys.clear();
        ParseQuery<ParseObject> hospitalQuery = ParseQuery.getQuery("SurveyHospital");
        hospitalQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        hospitalQuery.whereEqualTo("completed", false);
        hospitalQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    surveys.addAll(objects);
                }else{
                    Log.e("error", e.getLocalizedMessage());
                }

                ParseQuery<ParseObject> doctorQuery = ParseQuery.getQuery("SurveyDoctor");
                doctorQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                doctorQuery.whereEqualTo("completed", false);
                doctorQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (hud != null) {
                            if (hud.isShowing()) {
                                hud.dismiss();
                            }
                        }
                        if (e == null) {
                            surveys.addAll(objects);
                            mAdapter.setmItems(surveys);
                        }else{
                            Log.e("error", e.getLocalizedMessage());
                        }
                    }
                });

            }
        });
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
//        switch (v.getId()) {
//            case R.id.btn_add:
//                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AddMedicationActivity.class));
//                break;
//
//        }
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
        if (event.getMessage().equals("updateSurveys")) {
            fetchSurveys();
        }
    }


    @Override
    public void onSelect(ParseObject object, int position) {

        if (object.getClassName().equals("SurveyHospital")) {
            Intent intent = new Intent(getActivity(), HospitalStaySurveyActivity.class);
            intent.putExtra("survey", surveys.get(position).getObjectId());
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), DoctorSurveyActivity.class);
            intent.putExtra("survey", surveys.get(position).getObjectId());
            startActivity(intent);
        }
    }
}