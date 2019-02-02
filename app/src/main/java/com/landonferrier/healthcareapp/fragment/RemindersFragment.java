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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.activity.AddReminderActivity;
import com.landonferrier.healthcareapp.activity.HelpActivity;
import com.landonferrier.healthcareapp.adapter.MedicationAdapter;
import com.landonferrier.healthcareapp.adapter.RemindersAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.customRecyclerView.SlidingItemMenuRecyclerView;
import com.parse.DeleteCallback;
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
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RemindersFragment extends BaseFragment implements RemindersAdapter.OnItemSelectedListener {
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.rc_reminders)
    SlidingItemMenuRecyclerView rcReminders;

    RemindersAdapter mAdapter;
    public KProgressHUD hud;

    ArrayList<ParseObject> reminders = new ArrayList<>();


    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_reminders, container, false);
        ButterKnife.bind(this, view);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        rcReminders.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcReminders.addItemDecoration(dividerItemDecoration);

        mAdapter = new RemindersAdapter(getContext(), reminders, this);
        rcReminders.setAdapter(mAdapter);


        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("DefaultLocale")
    public void initView() {
        btnAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_add:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AddReminderActivity.class));
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
        if (event.getMessage().equals("updateReminders")) {
            fetchReminders();
        }
    }


    public void fetchReminders() {
        if (hud != null) {
            if (!hud.isShowing()) {
                hud.show();
            }
        }
        ParseQuery<ParseObject> reminderQuery = ParseQuery.getQuery("Reminder");
        reminderQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
        reminderQuery.orderByDescending("time");
        reminderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (hud != null) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                }
                if (e == null) {
                    reminders.clear();
                    reminders.addAll(objects);
                    mAdapter.setmItems(reminders);
                }else{
                    Log.e("error", e.getLocalizedMessage());
                }
            }
        });
    }


    @Override
    public void onDelete(ParseObject object, int position) {
        ParseObject reminder = reminders.get(position);
        reminder.deleteEventually(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    reminders.remove(position);
                    mAdapter.setmItems(reminders);
                }
            }
        });
    }

    @Override
    public void onSelect(ParseObject object, int position) {
        Intent intent = new Intent(getActivity(), AddReminderActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("reminder", reminders.get(position).getObjectId());
        startActivity(intent);
    }
}