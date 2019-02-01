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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.activity.HelpActivity;
import com.landonferrier.healthcareapp.activity.ProfileActivity;
import com.landonferrier.healthcareapp.adapter.TaskAdapter;
import com.landonferrier.healthcareapp.models.TaskModel;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.utils.Utils;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.FindCallback;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    @BindView(R.id.rc_tasks)
    RecyclerView rcTasks;

    @BindView(R.id.view_current_surgery)
    RelativeLayout viewCurrentSurgery;

    Date showDate = new Date();
    ArrayList<TaskModel> taskModels = new ArrayList<>();
    TaskAdapter taskAdapter;

    boolean isFetching = false;
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
        rcTasks.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcTasks.addItemDecoration(dividerItemDecoration);

        taskAdapter = new TaskAdapter(getContext(), taskModels);
        rcTasks.setAdapter(taskAdapter);

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
        tvTaskTitle.setText(Utils.getFormattedDate(getContext(), showDate.getTime()));
        getMedications();
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
        if (isFetching) {
            return;
        }
        isFetching = true;
        ParseQuery<ParseObject> reminderQuery = ParseQuery.getQuery("Reminder");
        reminderQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
        reminderQuery.whereLessThan("time", showDate).orderByDescending("time");
        reminderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    if (objects.size() > 0) {
                        Date newDate = objects.get(0).getDate("time");
                        if (new Date().after(newDate) && showDate.after(new Date()) && !Utils.isSameDay(showDate, new Date())) {
                            showDate = new Date();
                        }else{
                            showDate = newDate;
                        }
                        tvTaskTitle.setText(Utils.getFormattedDate(getContext(), showDate.getTime()));
                        getMedications();
                    }else{
                        isFetching = false;

                    }
                } else {
                    isFetching = false;
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        getMedications();

    }

    public void fintNextDate() {
        if (isFetching) {
            return;
        }
        isFetching = true;
        ParseQuery<ParseObject> reminderQuery = ParseQuery.getQuery("Reminder");
        reminderQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
        reminderQuery.whereGreaterThan("time", showDate).orderByAscending("time");
        reminderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    if (objects.size() > 0) {
                        Date newDate = objects.get(0).getDate("time");
                        if (new Date().after(showDate) && newDate.after(new Date()) && !Utils.isSameDay(showDate, new Date())) {
                            showDate = new Date();
                        }else{
                            showDate = newDate;
                        }
                        tvTaskTitle.setText(Utils.getFormattedDate(getContext(), showDate.getTime()));
                        getMedications();
                    }else{
                        isFetching = false;

                    }
                } else {
                    isFetching = false;
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }

    public void getMedications() {
        ParseQuery<ParseObject> reminderQuery = ParseQuery.getQuery("Reminder");
        reminderQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
        reminderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    if (objects.size() > 0) {
                        taskModels.clear();
                        for (ParseObject reminder : objects) {
                            Date remindDate = reminder.getDate("time");
                            boolean complted  = reminder.getBoolean("complete");
                            String name = reminder.getString("name");
                            if (Utils.isSameDay(remindDate, showDate)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
                                String dateString = dateFormat.format(remindDate);
                                if (name == null) {
                                    continue;
                                }
                                String string = dateString.replace(" ", "");
                                string = String.format("%s - %s", string, name);
                                TaskModel taskModel = new TaskModel();
                                taskModel.setCompleted(complted);
                                taskModel.setDate(remindDate);
                                taskModel.setId(reminder.getObjectId());
                                taskModel.setReminder(true);
                                taskModel.setName(string);
                                taskModel.setIndex(0);
                                String taskId = reminder.getObjectId();

                                if (ParseUser.getCurrentUser().get("completedIds") != null) {
                                    JSONArray ids = ParseUser.getCurrentUser().getJSONArray("completedIds");
                                    int ii = taskModel.getIndex();
                                    for (int j = 0; j < ids.length(); j++) {
                                        try {
                                            if (ids.getString(j).equals(taskId)) {
                                                taskModel.setCompleted(true);
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }


                                }
                                taskModels.add(taskModel);
                            }
                        }
                        if (Utils.isSameDay(showDate, new Date())){
                            ParseQuery<ParseObject> medicationQuery = ParseQuery.getQuery("Medication");
                            medicationQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
                            medicationQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        Log.d("score", "Retrieved " + objects.size() + " scores");
                                        if (objects.size() > 0) {
                                            for (ParseObject medication : objects) {
                                                JSONArray medicationTimes = medication.getJSONArray("times");
                                                String medicationName = medication.getString("name");
                                                int index = 0;
                                                for (int i = 0; i < medicationTimes.length(); i++) {
                                                    index = index + 1;
                                                    try {
                                                        String time = medicationTimes.getString(i);
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mma");
                                                        try {
                                                            Date timeDate = dateFormat.parse(time);
                                                            String string = String.format("%s - %s", time, medicationName);
                                                            SimpleDateFormat datehourFormat = new SimpleDateFormat("h");
                                                            String hourString = datehourFormat.format(timeDate);
                                                            SimpleDateFormat dateminFormat = new SimpleDateFormat("mm");
                                                            String minString = dateminFormat.format(timeDate);
                                                            Calendar calendar = Calendar.getInstance();
                                                            calendar.set(Calendar.HOUR, Integer.parseInt(hourString));
                                                            calendar.set(Calendar.MINUTE, Integer.parseInt(minString));
                                                            Date newDate = calendar.getTime();
                                                            TaskModel taskModel = new TaskModel();
                                                            taskModel.setCompleted(false);
                                                            taskModel.setDate(newDate);
                                                            taskModel.setId(medication.getObjectId());
                                                            taskModel.setReminder(true);
                                                            taskModel.setName(string);
                                                            taskModel.setIndex(index);
                                                            String taskId = medication.getObjectId();

                                                            if (ParseUser.getCurrentUser().get("completedIds") != null) {
                                                                JSONArray ids = ParseUser.getCurrentUser().getJSONArray("completedIds");
                                                                int ii = taskModel.getIndex();
                                                                for (int j = 0; j < ids.length(); j++) {
                                                                    if (ids.getString(j).equals(String.format("%s-%s", taskId, ii))) {
                                                                        taskModel.setCompleted(true);
                                                                    }
                                                                }


                                                            }
                                                            taskModels.add(taskModel);

                                                        } catch (java.text.ParseException e1) {
                                                            e1.printStackTrace();
                                                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("h:mm a");
                                                            try {
                                                                Date timeDate = dateFormat1.parse(time);
                                                                String string = String.format("%s - %s", time, medicationName);
                                                                SimpleDateFormat datehourFormat = new SimpleDateFormat("h");
                                                                String hourString = datehourFormat.format(timeDate);
                                                                SimpleDateFormat dateminFormat = new SimpleDateFormat("mm");
                                                                String minString = dateminFormat.format(timeDate);
                                                                Calendar calendar = Calendar.getInstance();
                                                                calendar.set(Calendar.HOUR, Integer.parseInt(hourString));
                                                                calendar.set(Calendar.MINUTE, Integer.parseInt(minString));
                                                                Date newDate = calendar.getTime();
                                                                TaskModel taskModel = new TaskModel();
                                                                taskModel.setCompleted(false);
                                                                taskModel.setDate(newDate);
                                                                taskModel.setId(medication.getObjectId());
                                                                taskModel.setReminder(true);
                                                                taskModel.setName(string);
                                                                taskModel.setIndex(0);
                                                                taskModels.add(taskModel);
                                                            } catch (java.text.ParseException e2) {
                                                                e2.printStackTrace();

                                                            }

                                                        }

                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            }
                                            isFetching = false;
                                            Collections.sort(taskModels, new Utils.CustomComparator());
                                            taskAdapter.setmItems(taskModels);

                                        }else{
                                            isFetching = false;
                                            Collections.sort(taskModels, new Utils.CustomComparator());
                                            taskAdapter.setmItems(taskModels);
                                        }

                                    } else {
                                        isFetching = false;
                                        Log.d("score", "Error: " + e.getMessage());
                                    }
                                }
                            });

                        }else{
                            isFetching = false;
                            Collections.sort(taskModels, new Utils.CustomComparator());
                            taskAdapter.setmItems(taskModels);
                        }

                    }else{
                        isFetching = false;
                    }
                } else {
                    isFetching = false;
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
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