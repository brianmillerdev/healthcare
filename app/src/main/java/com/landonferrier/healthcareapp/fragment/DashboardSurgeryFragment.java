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

import com.kaopiz.kprogresshud.KProgressHUD;
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
import com.parse.ParseGeoPoint;
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

public class DashboardSurgeryFragment extends BaseFragment implements TaskAdapter.OnItemSelectedListener {
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
    KProgressHUD hud;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_dashboard_sugery, container, false);
        ButterKnife.bind(this, view);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        viewCurrentSurgery.setOnClickListener(this);
        rcTasks.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcTasks.addItemDecoration(dividerItemDecoration);

        taskAdapter = new TaskAdapter(getContext(), taskModels, this);
        rcTasks.setAdapter(taskAdapter);

        tvTaskTitle.setText(Utils.getFormattedDate(getContext(), showDate.getTime()));
        getMedications();
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("DefaultLocale")
    public void initView() {
        JSONArray ids = ParseUser.getCurrentUser().getJSONArray("surgeryIds");
        if (ids.length() > 0) {
            String surgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            ParseObject surgery = ParseObject.createWithoutData("Surgery", surgeryId);
            if (hud != null) {
                if (!hud.isShowing()) {
                    hud.show();
                }
            }
            surgery.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject o, ParseException e) {
                    if (hud != null) {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                    }
                    if (e == null) {
                        String name = o.getString("name");
                        tvSurgeryName.setText(name);
                        if (ParseUser.getCurrentUser().get("surgeryDates") != null) {
                            JSONObject object = ParseUser.getCurrentUser().getJSONObject("surgeryDates");
                            if (object.has(o.getObjectId())) {
                                try {
                                    String dateStr = object.getString(o.getObjectId());
                                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                                    Date date = format.parse(dateStr);
                                    SimpleDateFormat format1 = new SimpleDateFormat("MMMM d, yyyy");
                                    String dateString = format1.format(date);
                                    tvSurgeryDate.setText(String.format("%s>", dateString));
                                    tvSurgeryDate.setVisibility(View.VISIBLE);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (java.text.ParseException e2) {
                                    e2.printStackTrace();
                                }
                            }else{
                                tvSurgeryDate.setVisibility(View.GONE);
                            }
                        }else{
                            tvSurgeryDate.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
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
//                break;
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
        if (!hud.isShowing()) {
            hud.show();
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
                        getMedications();
                    }
                } else {
                    isFetching = false;
                    Log.d("score", "Error: " + e.getMessage());
                    getMedications();
                }
            }
        });

    }

    public void fintNextDate() {
        if (isFetching) {
            return;
        }
        if (!hud.isShowing()) {
            hud.show();
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
                        getMedications();
                    }
                } else {
                    isFetching = false;
                    Log.d("score", "Error: " + e.getMessage());
                    getMedications();
                }
            }
        });


    }

    public void getMedications() {
        ParseQuery<ParseObject> reminderQuery = ParseQuery.getQuery("Reminder");
        ParseQuery<ParseObject> pastReminderQuery = ParseQuery.getQuery("Reminder");
        pastReminderQuery.whereLessThan("time", new Date());

        if (ParseUser.getCurrentUser().get("completedDict") != null) {
            JSONObject completedDict = ParseUser.getCurrentUser().getJSONObject("completedDict");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String dateString = dateFormat.format(showDate);
            try {
                if (completedDict.has(dateString) ) {
                    JSONArray completedIds = completedDict.getJSONArray(dateString);
                    ArrayList<String> ids = new ArrayList<>();
                    for (int i = 0; i < completedIds.length(); i++) {
                        ids.add(completedIds.getString(i));
                    }
                    pastReminderQuery.whereNotContainedIn("objectId", ids);
                }
            } catch (JSONException e) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                e.printStackTrace();
            }
        }

        ArrayList<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(reminderQuery);
        queries.add(pastReminderQuery);

        ParseQuery<ParseObject> combinedReminderQuery = ParseQuery.or(queries);

        if (ParseUser.getCurrentUser().get("currentSurgeryId") != null) {
            String surgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            combinedReminderQuery.whereEqualTo("surgeryId", surgeryId);
        }
        combinedReminderQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
        combinedReminderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " scores");
                    taskModels.clear();
                    if (objects.size() > 0) {
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
                                try {
                                    if (ParseUser.getCurrentUser().get("completedDict") != null) {
                                        JSONObject completedDict = ParseUser.getCurrentUser().getJSONObject("completedDict");
                                        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                                        String ds = df.format(showDate);
                                        if (completedDict.has(ds)) {
                                            JSONArray completedIds = null;
                                            completedIds = completedDict.getJSONArray(ds);
                                            ArrayList<String> ids = new ArrayList<>();
                                            for (int i = 0; i < completedIds.length(); i++) {
                                                ids.add(completedIds.getString(i));
                                            }
                                            if (ids.contains(taskId)) {
                                                taskModel.setCompleted(true);
                                            }

                                        }
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    if (hud.isShowing()) {
                                        hud.dismiss();
                                    }
                                }

                                taskModels.add(taskModel);
                            }
                        }
                    }else{
                        isFetching = false;
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
                                                        SimpleDateFormat apm = new SimpleDateFormat("a");
                                                        String apmString = apm.format(timeDate);
                                                        Calendar calendar = Calendar.getInstance();
                                                        calendar.set(Calendar.HOUR, Integer.parseInt(hourString));
                                                        calendar.set(Calendar.MINUTE, Integer.parseInt(minString));
                                                        calendar.set(Calendar.AM_PM, (apmString.equals("AM") ? Calendar.AM : Calendar.PM));
                                                        Date newDate = calendar.getTime();
                                                        TaskModel taskModel = new TaskModel();
                                                        taskModel.setCompleted(false);
                                                        taskModel.setDate(newDate);
                                                        taskModel.setId(medication.getObjectId());
                                                        taskModel.setReminder(true);
                                                        taskModel.setName(string);
                                                        taskModel.setIndex(index);
                                                        String taskId = medication.getObjectId();

                                                        if (ParseUser.getCurrentUser().get("completedDict") != null) {
                                                            JSONObject completedDict = ParseUser.getCurrentUser().getJSONObject("completedDict");
                                                            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                                                            String ds = df.format(showDate);
                                                            if (completedDict.has(ds)) {
                                                                JSONArray completedIds = completedDict.getJSONArray(ds);
                                                                ArrayList<String> ids = new ArrayList<>();
                                                                for (int j = 0; j < completedIds.length(); j++) {
                                                                    ids.add(completedIds.getString(j));
                                                                }
                                                                int ii = taskModel.getIndex();
                                                                if (ids.contains(String.format("%s-%s", taskId, ii))) {
                                                                    taskModel.setCompleted(true);
                                                                }

                                                            }
                                                        }

                                                        taskModels.add(taskModel);

                                                    } catch (java.text.ParseException e1) {
                                                        if (hud.isShowing()) {
                                                            hud.dismiss();
                                                        }
                                                        e1.printStackTrace();
                                                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("h:mm a");
                                                        try {
                                                            Date timeDate = dateFormat1.parse(time);
                                                            String string = String.format("%s - %s", time, medicationName);
                                                            SimpleDateFormat datehourFormat = new SimpleDateFormat("h");
                                                            String hourString = datehourFormat.format(timeDate);
                                                            SimpleDateFormat dateminFormat = new SimpleDateFormat("mm");
                                                            String minString = dateminFormat.format(timeDate);
                                                            SimpleDateFormat apm = new SimpleDateFormat("a");
                                                            String apmString = apm.format(timeDate);
                                                            Calendar calendar = Calendar.getInstance();
                                                            calendar.set(Calendar.HOUR, Integer.parseInt(hourString));
                                                            calendar.set(Calendar.MINUTE, Integer.parseInt(minString));
                                                            calendar.set(Calendar.AM_PM, (apmString.equals("AM") ? Calendar.AM : Calendar.PM));
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
                                                            if (hud.isShowing()) {
                                                                hud.dismiss();
                                                            }
                                                            e2.printStackTrace();

                                                        }

                                                    }

                                                } catch (JSONException e1) {
                                                    if (hud.isShowing()) {
                                                        hud.dismiss();
                                                    }
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
                                if (hud.isShowing()) {
                                    hud.dismiss();
                                }

                            }
                        });

                    }else{
                        isFetching = false;
                        Collections.sort(taskModels, new Utils.CustomComparator());
                        taskAdapter.setmItems(taskModels);
                    }
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }

                } else {
                    isFetching = false;
                    Log.d("score", "Error: " + e.getMessage());
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
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
        if (event.getMessage().equals("updateCurrentSurgery")) {
            initView();
        }
    }


    @Override
    public void onEdit(TaskModel object, int position) {

    }

    @Override
    public void onDelete(TaskModel object, int position) {

    }

    @Override
    public void onSelect(TaskModel object, int position) {
        if (ParseUser.getCurrentUser().get("completedDict") != null) {
            String taskId = object.getId();
            if (!object.isReminder()) {
                taskId = String.format("%s-%s", taskId, object.getIndex());
            }
            JSONObject completedDict = ParseUser.getCurrentUser().getJSONObject("completedDict");
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            String ds = df.format(showDate);
            try {
                if (completedDict.has(ds)) {
                    JSONArray completedIds = null;
                    completedIds = completedDict.getJSONArray(ds);
                    ArrayList<String> ids = new ArrayList<>();
                    for (int j = 0; j < completedIds.length(); j++) {
                        ids.add(completedIds.getString(j));
                    }
                    if (ids.contains(taskId)) {
                        ids.remove(taskId);
                    }else{
                        ids.add(taskId);
                    }
                    JSONArray newArr = new JSONArray();
                    for (String str : ids) {
                        newArr.put(str);
                    }
                    completedDict.put(ds, newArr);
                }else{
                    JSONArray newArr = new JSONArray();
                    newArr.put(taskId);
                    completedDict.put(ds, newArr);
                }
                ParseUser.getCurrentUser().put("completedDict", completedDict);
                ParseUser.getCurrentUser().saveInBackground();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}