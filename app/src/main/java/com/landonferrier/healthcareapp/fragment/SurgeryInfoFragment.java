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
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.activity.HelpActivity;
import com.landonferrier.healthcareapp.activity.SurgeryInfoDetailActivity;
import com.landonferrier.healthcareapp.adapter.RemindersAdapter;
import com.landonferrier.healthcareapp.adapter.SurgeryInfoAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
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

public class SurgeryInfoFragment extends BaseFragment implements SurgeryInfoAdapter.OnItemSelectedListener {
    @BindView(R.id.rc_info)
    RecyclerView rcInfo;

    @BindView(R.id.tv_surgery_name)
    CustomFontTextView tvName;

    @BindView(R.id.tv_surgery_date)
    CustomFontTextView tvDate;

    SurgeryInfoAdapter mAdapter;
    public KProgressHUD hud;
    ArrayList<String> infoItems = new ArrayList<>();
    View view;

    ParseObject surgery;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        rcInfo.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcInfo.addItemDecoration(dividerItemDecoration);
        infoItems.add("Description");
        infoItems.add("Video");
        infoItems.add("Pre-op Information");
        infoItems.add("Post-op Information");
        infoItems.add("FAQ");
        infoItems.add("Red Flags");
        mAdapter = new SurgeryInfoAdapter(getContext(), infoItems, this);


        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @SuppressLint("DefaultLocale")
    public void initView() {
        tvDate.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_surgery_date:
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
        if (event.getMessage().equals("fetchSurgeryInfo")) {
            fetchSurgeryInfo();
        }
    }

    public void fetchSurgeryInfo() {
        if (hud != null) {
            if (!hud.isShowing()) {
                hud.show();
            }
        }
        tvName.setText("");
        tvDate.setText("");
        JSONArray ids = ParseUser.getCurrentUser().getJSONArray("surgeryIds");
        if (ids.length() > 0){
            String surgeryId = "";
            if (ParseUser.getCurrentUser().has("currentSurgeryId")) {
                surgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            }
            if (surgeryId.equals("")) {
                return;
            }
            surgery = ParseObject.createWithoutData("Surgery", surgeryId);
            surgery.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (hud != null) {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                    }
                    if (e == null) {
                        rcInfo.setAdapter(mAdapter);
                        updateHeader();
                    }else{
                        Log.e("error", e.getLocalizedMessage());
                    }
                }
            });
        }
    }

    public void updateHeader() {
        String name = surgery.getString("name");
        tvName.setText(name);
        if (ParseUser.getCurrentUser().get("surgeryDates") != null) {
            JSONObject object = ParseUser.getCurrentUser().getJSONObject("surgeryDates");
            if (object.has(surgery.getObjectId())) {
                try {
                    String dateStr = object.getString(surgery.getObjectId());
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                    Date date = format.parse(dateStr);
                    SimpleDateFormat format1 = new SimpleDateFormat("MMMM d, yyyy");
                    String dateString = format1.format(date);
                    tvDate.setText(String.format("Date: %s", dateString));
                    tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorText));
                    tvDate.setClickable(false);
                    tvDate.setFocusable(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }else{
                tvDate.setText("Select Surgery Date");
                tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                tvDate.setClickable(true);
                tvDate.setFocusable(true);
            }
        }else{
            tvDate.setText("Select Surgery Date");
            tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            tvDate.setClickable(true);
            tvDate.setFocusable(true);
        }
    }


    @Override
    public void onSelect(int position) {
        String detail = "";
        if (position == 0) {
            detail = surgery.getString("description");
        }else if (position == 1) {
            detail = surgery.getString("videoLink");
        }else if (position == 2) {
            detail = surgery.getString("preopInformation");
        }else if (position == 3) {
            detail = surgery.getString("postopInformation");
        }else if (position == 4) {
            detail = surgery.getString("FAQ");
        }else if (position == 5) {
            detail = surgery.getString("redFlags");
        }

        Intent intent = new Intent(getActivity(), SurgeryInfoDetailActivity.class);
        intent.putExtra("type", position);
        intent.putExtra("detail", detail);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }
}