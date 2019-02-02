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
import com.landonferrier.healthcareapp.activity.AddMedicationActivity;
import com.landonferrier.healthcareapp.activity.AddReminderActivity;
import com.landonferrier.healthcareapp.activity.HelpActivity;
import com.landonferrier.healthcareapp.adapter.MedicationAdapter;
import com.landonferrier.healthcareapp.adapter.TaskAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
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

public class MedicationsFragment extends BaseFragment implements MedicationAdapter.OnItemSelectedListener {
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.rc_medications)
    SlidingItemMenuRecyclerView rcMedications;

    MedicationAdapter mAdapter;
    public KProgressHUD hud;

    ArrayList<ParseObject> medications = new ArrayList<>();

    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_medications, container, false);
        ButterKnife.bind(this, view);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        rcMedications.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcMedications.addItemDecoration(dividerItemDecoration);

        mAdapter = new MedicationAdapter(getContext(), medications, this);
        rcMedications.setAdapter(mAdapter);

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

    public void fetchMedications() {
        if (hud != null) {
            if (!hud.isShowing()) {
                hud.show();
            }
        }
        ParseQuery<ParseObject> medicationsQuery = ParseQuery.getQuery("Medication");
        medicationsQuery.whereEqualTo("creatorId", ParseUser.getCurrentUser().getObjectId());
        medicationsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (hud != null) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                }
                if (e == null) {
                    medications.clear();
                    medications.addAll(objects);
                    mAdapter.setmItems(medications);
                }else{
                    Log.e("error", e.getLocalizedMessage());
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_add:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AddMedicationActivity.class));
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
        if (event.getMessage().equals("updateMedications")) {
            fetchMedications();
        }
    }


    @Override
    public void onDelete(ParseObject object, int position) {
        ParseObject medication = medications.get(position);
        medication.deleteEventually(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    medications.remove(position);
                    mAdapter.setmItems(medications);
                }
            }
        });
    }

    @Override
    public void onSelect(ParseObject object, int position) {
        Intent intent = new Intent(getActivity(), AddMedicationActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("medication", medications.get(position).getObjectId());
        startActivity(intent);
    }
}