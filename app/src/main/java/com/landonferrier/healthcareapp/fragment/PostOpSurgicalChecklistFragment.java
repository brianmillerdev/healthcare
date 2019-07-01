package com.landonferrier.healthcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.utils.Helper;
import com.landonferrier.healthcareapp.utils.SurgeryType;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostOpSurgicalChecklistFragment extends BaseFragment {


    @BindView(R.id.cb_checked_in_one_week)
    ImageView cbCheckedInOneWeek;

    @BindView(R.id.tv_checked_in_one_week)
    CustomFontTextView tvCheckedInOneWeek;

    @BindView(R.id.view_checked_in_one_week)
    LinearLayout viewCheckedInOneWeek;

    @BindView(R.id.cb_checked_in_3_week)
    ImageView cbCheckedInThreeWeek;

    @BindView(R.id.tv_checked_in_3_week)
    CustomFontTextView tvCheckedInThreeWeek;

    @BindView(R.id.view_checked_in_3_week)
    LinearLayout viewCheckedInThreeWeek;

    @BindView(R.id.cb_checked_in_6_week)
    ImageView cbCheckedInSixWeek;

    @BindView(R.id.tv_checked_in_6_week)
    CustomFontTextView tvCheckedInSixWeek;

    @BindView(R.id.view_checked_in_6_week)
    LinearLayout viewCheckedInSixWeek;

    @BindView(R.id.cb_checked_in_3_month)
    ImageView cbCheckedInThreeMonth;

    @BindView(R.id.tv_checked_in_3_month)
    CustomFontTextView tvCheckedInThreeMonth;

    @BindView(R.id.view_checked_in_3_month)
    LinearLayout viewCheckedInThreeMonth;

    @BindView(R.id.cb_checked_in_6_month)
    ImageView cbCheckedInSixMonth;

    @BindView(R.id.tv_checked_in_6_month)
    CustomFontTextView tvCheckedInSixMonth;

    @BindView(R.id.view_checked_in_6_month)
    LinearLayout viewCheckedInSixMonth;


    @BindView(R.id.cb_confirmed_order_one_week)
    ImageView cbConfirmedOrderOneWeek;

    @BindView(R.id.tv_confirmed_order_one_week)
    CustomFontTextView tvConfirmedOrderOneWeek;

    @BindView(R.id.view_confirmed_order_one_week)
    LinearLayout viewConfirmedOrderOneWeek;

    @BindView(R.id.cb_confirmed_order_3_week)
    ImageView cbConfirmedOrderThreeWeek;

    @BindView(R.id.tv_confirmed_order_3_week)
    CustomFontTextView tvConfirmedOrderThreeWeek;

    @BindView(R.id.view_confirmed_order_3_week)
    LinearLayout viewConfirmedOrderThreeWeek;

    @BindView(R.id.cb_confirmed_order_6_week)
    ImageView cbConfirmedOrderSixWeek;

    @BindView(R.id.tv_confirmed_order_6_week)
    CustomFontTextView tvConfirmedOrderSixWeek;

    @BindView(R.id.view_confirmed_order_6_week)
    LinearLayout viewConfirmedOrderSixWeek;

    @BindView(R.id.cb_confirmed_order_3_month)
    ImageView cbConfirmedOrderThreeMonth;

    @BindView(R.id.tv_confirmed_order_3_month)
    CustomFontTextView tvConfirmedOrderThreeMonth;

    @BindView(R.id.view_confirmed_order_3_month)
    LinearLayout viewConfirmedOrderThreeMonth;

    @BindView(R.id.cb_confirmed_order_6_month)
    ImageView cbConfirmedOrderSixMonth;

    @BindView(R.id.tv_confirmed_order_6_month)
    CustomFontTextView tvConfirmedOrderSixMonth;

    @BindView(R.id.view_confirmed_order_6_month)
    LinearLayout viewConfirmedOrderSixMonth;



    @BindView(R.id.cb_scheduled_order_one_week)
    ImageView cbScheduledOrderOneWeek;

    @BindView(R.id.tv_scheduled_order_one_week)
    CustomFontTextView tvScheduledOrderOneWeek;

    @BindView(R.id.view_scheduled_order_one_week)
    LinearLayout viewScheduledOrderOneWeek;

    @BindView(R.id.cb_scheduled_order_3_week)
    ImageView cbScheduledOrderThreeWeek;

    @BindView(R.id.tv_scheduled_order_3_week)
    CustomFontTextView tvScheduledOrderThreeWeek;

    @BindView(R.id.view_scheduled_order_3_week)
    LinearLayout viewScheduledOrderThreeWeek;

    @BindView(R.id.cb_scheduled_order_6_week)
    ImageView cbScheduledOrderSixWeek;

    @BindView(R.id.tv_scheduled_order_6_week)
    CustomFontTextView tvScheduledOrderSixWeek;

    @BindView(R.id.view_scheduled_order_6_week)
    LinearLayout viewScheduledOrderSixWeek;

    @BindView(R.id.cb_scheduled_order_3_month)
    ImageView cbScheduledOrderThreeMonth;

    @BindView(R.id.tv_scheduled_order_3_month)
    CustomFontTextView tvScheduledOrderThreeMonth;

    @BindView(R.id.view_scheduled_order_3_month)
    LinearLayout viewScheduledOrderThreeMonth;

    @BindView(R.id.cb_scheduled_order_6_month)
    ImageView cbScheduledOrderSixMonth;

    @BindView(R.id.tv_scheduled_order_6_month)
    CustomFontTextView tvScheduledOrderSixMonth;

    @BindView(R.id.view_scheduled_order_6_month)
    LinearLayout viewScheduledOrderSixMonth;





    public String surgeryType = "";


    public KProgressHUD hud;

    ParseObject surgery;
    ParseObject checklist;
    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_post_op_surveys_checklist, container, false);
        ButterKnife.bind(this, view);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @SuppressLint("DefaultLocale")
    public void initView() {
        cbCheckedInOneWeek.setSelected(false);
        cbCheckedInThreeWeek.setSelected(false);
        cbCheckedInSixWeek.setSelected(false);
        cbCheckedInThreeMonth.setSelected(false);
        cbCheckedInSixMonth.setSelected(false);

        cbConfirmedOrderOneWeek.setSelected(false);
        cbConfirmedOrderThreeWeek.setSelected(false);
        cbConfirmedOrderSixWeek.setSelected(false);
        cbConfirmedOrderThreeMonth.setSelected(false);
        cbConfirmedOrderSixMonth.setSelected(false);

        cbScheduledOrderOneWeek.setSelected(false);
        cbScheduledOrderThreeWeek.setSelected(false);
        cbScheduledOrderSixWeek.setSelected(false);
        cbScheduledOrderThreeMonth.setSelected(false);
        cbScheduledOrderSixMonth.setSelected(false);

        fetchSurveys();

        cbCheckedInOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedInOneWeek.setSelected(!cbCheckedInOneWeek.isSelected());
                tvCheckedInOneWeek.setSelected(cbCheckedInOneWeek.isSelected());
            }
        });

        cbCheckedInThreeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedInThreeWeek.setSelected(!cbCheckedInThreeWeek.isSelected());
                tvCheckedInThreeWeek.setSelected(cbCheckedInThreeWeek.isSelected());
            }
        });

        cbCheckedInSixWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedInSixWeek.setSelected(!cbCheckedInSixWeek.isSelected());
                tvCheckedInSixWeek.setSelected(cbCheckedInSixWeek.isSelected());
            }
        });

        cbCheckedInThreeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedInThreeMonth.setSelected(!cbCheckedInThreeMonth.isSelected());
                tvCheckedInThreeMonth.setSelected(cbCheckedInThreeMonth.isSelected());
            }
        });

        cbCheckedInSixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedInSixMonth.setSelected(!cbCheckedInSixMonth.isSelected());
                tvCheckedInSixMonth.setSelected(cbCheckedInSixMonth.isSelected());
            }
        });

        cbConfirmedOrderOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbConfirmedOrderOneWeek.setSelected(!cbConfirmedOrderOneWeek.isSelected());
                tvConfirmedOrderOneWeek.setSelected(cbConfirmedOrderOneWeek.isSelected());
            }
        });

        cbConfirmedOrderThreeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbConfirmedOrderThreeWeek.setSelected(!cbConfirmedOrderThreeWeek.isSelected());
                tvConfirmedOrderThreeWeek.setSelected(cbConfirmedOrderThreeWeek.isSelected());
            }
        });

        cbConfirmedOrderSixWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbConfirmedOrderSixWeek.setSelected(!cbConfirmedOrderSixWeek.isSelected());
                tvConfirmedOrderSixWeek.setSelected(cbConfirmedOrderSixWeek.isSelected());
            }
        });

        cbConfirmedOrderThreeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbConfirmedOrderThreeMonth.setSelected(!cbConfirmedOrderThreeMonth.isSelected());
                tvConfirmedOrderThreeMonth.setSelected(cbConfirmedOrderThreeMonth.isSelected());
            }
        });

        cbConfirmedOrderSixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbConfirmedOrderSixMonth.setSelected(!cbConfirmedOrderSixMonth.isSelected());
                tvConfirmedOrderSixMonth.setSelected(cbConfirmedOrderSixMonth.isSelected());
            }
        });

        cbScheduledOrderOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledOrderOneWeek.setSelected(!cbScheduledOrderOneWeek.isSelected());
                tvScheduledOrderOneWeek.setSelected(cbScheduledOrderOneWeek.isSelected());
            }
        });

        cbScheduledOrderThreeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledOrderThreeWeek.setSelected(!cbScheduledOrderThreeWeek.isSelected());
                tvScheduledOrderThreeWeek.setSelected(cbScheduledOrderThreeWeek.isSelected());
            }
        });

        cbScheduledOrderSixWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledOrderSixWeek.setSelected(!cbScheduledOrderSixWeek.isSelected());
                tvScheduledOrderSixWeek.setSelected(cbScheduledOrderSixWeek.isSelected());
            }
        });

        cbScheduledOrderThreeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledOrderThreeMonth.setSelected(!cbScheduledOrderThreeMonth.isSelected());
                tvScheduledOrderThreeMonth.setSelected(cbScheduledOrderThreeMonth.isSelected());
            }
        });

        cbScheduledOrderSixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledOrderSixMonth.setSelected(!cbScheduledOrderSixMonth.isSelected());
                tvScheduledOrderSixMonth.setSelected(cbScheduledOrderSixMonth.isSelected());
            }
        });


    }

    public void fetchSurveys() {
        if (hud != null) {
            if (!hud.isShowing()) {
                hud.show();
            }
        }
        JSONArray ids = ParseUser.getCurrentUser().getJSONArray("surgeryIds");
        if (ids != null && ids.length() > 0) {
            String surgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            if (Helper.surgeryTypeFor(surgeryId) != null) {

                surgeryType = Helper.surgeryTypeFor(surgeryId);
            }

            surgery = ParseObject.createWithoutData("Surgery", surgeryId);
            if (hud != null) {
                if (!hud.isShowing()) {
                    hud.show();
                }
            }
            surgery.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject o, ParseException e) {
                    if (e == null) {
                        fetchChecklist();
                    } else {
                        if (hud != null) {
                            if (hud.isShowing()) {
                                hud.dismiss();
                            }
                        }
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveChecklist();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventPush event) {
        if (event.getMessage().equals("updateSurveys")) {
            fetchSurveys();
        }
    }

    public void fetchChecklist() {

        ParseQuery<ParseObject> cQuery = ParseQuery.getQuery("ChecklistPostOp");
        cQuery.whereEqualTo("ownerId", ParseUser.getCurrentUser().getObjectId());
        cQuery.whereEqualTo("surgeryId", surgery.getObjectId());
        cQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        checklist = objects.get(0);
                        showChecklist();
                    }else{

                        checklist = new ParseObject("ChecklistPostOp");
                        checklist.put("ownerId", ParseUser.getCurrentUser().getObjectId());
                        checklist.put("surgeryId", surgery.getObjectId());
                        checklist.put("hasCheckedIn", false);
                        checklist.put("hasFollowedDirections", false);
                        checklist.put("hasScheduledVisit", false);
                        checklist.put("hasHadFirstPostAppt", false);
                        checklist.put("scheduledPostopDate", "");
                        checklist.put("facilityAddress", "");
                        checklist.put("hasEmail", false);

                        Map<String, Map<String, Boolean>> newData = new HashMap<>();
                        Map<String, Boolean> checkedInData = new HashMap<>();
                        checkedInData.put("oneWeekAfter", false);
                        checkedInData.put("threeWeeksAfter", false);
                        checkedInData.put("sixWeeksAfter", false);
                        checkedInData.put("threeMonthsAfter", false);
                        checkedInData.put("sixMonthsAfter", false);

                        Map<String, Boolean> confirmedOrderData = new HashMap<>();
                        confirmedOrderData.put("oneWeekAfter", false);
                        confirmedOrderData.put("threeWeeksAfter", false);
                        confirmedOrderData.put("sixWeeksAfter", false);
                        confirmedOrderData.put("threeMonthsAfter", false);
                        confirmedOrderData.put("sixMonthsAfter", false);

                        Map<String, Boolean> scheduledOrderData = new HashMap<>();
                        scheduledOrderData.put("oneWeekAfter", false);
                        scheduledOrderData.put("threeWeeksAfter", false);
                        scheduledOrderData.put("sixWeeksAfter", false);
                        scheduledOrderData.put("threeMonthsAfter", false);
                        scheduledOrderData.put("sixMonthsAfter", false);

                        newData.put("hasCheckedIn", checkedInData);
                        newData.put("hasConfirmedOrder", confirmedOrderData);
                        newData.put("hasScheduledOrder", scheduledOrderData);

                        checklist.put("data", newData);
                        checklist.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    showChecklist();
                                }
                            }
                        });

                    }
                }
            }
        });

    }


    public void showChecklist() {
        if (hud != null) {
            if (hud.isShowing()) {
                hud.dismiss();
            }
        }

        Map<String, Map<String, Boolean>> data = new HashMap<>();
        if (checklist.has("data")) {
            data = checklist.getMap("data");
            if (data != null) {
                Map<String, Boolean> hasCheckedIn = data.get("hasCheckedIn");
                if (hasCheckedIn != null ) {
                    if (hasCheckedIn.containsKey("oneWeekAfter")) {
                        cbCheckedInOneWeek.setSelected(hasCheckedIn.get("oneWeekAfter"));
                        tvCheckedInOneWeek.setSelected(hasCheckedIn.get("oneWeekAfter"));
                        viewCheckedInOneWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewCheckedInOneWeek.setVisibility(View.GONE);
                    }
                    if (hasCheckedIn.containsKey("threeWeeksAfter")) {
                        cbCheckedInThreeWeek.setSelected(hasCheckedIn.get("threeWeeksAfter"));
                        tvCheckedInThreeWeek.setSelected(hasCheckedIn.get("threeWeeksAfter"));
                        viewCheckedInThreeWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewCheckedInThreeWeek.setVisibility(View.GONE);
                    }
                    if (hasCheckedIn.containsKey("sixWeeksAfter")) {
                        cbCheckedInSixWeek.setSelected(hasCheckedIn.get("sixWeeksAfter"));
                        tvCheckedInSixWeek.setSelected(hasCheckedIn.get("sixWeeksAfter"));
                        viewCheckedInSixWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewCheckedInSixWeek.setVisibility(View.GONE);
                    }
                    if (hasCheckedIn.containsKey("threeMonthsAfter")) {
                        cbCheckedInThreeMonth.setSelected(hasCheckedIn.get("threeMonthsAfter"));
                        tvCheckedInThreeMonth.setSelected(hasCheckedIn.get("threeMonthsAfter"));
                        viewCheckedInThreeMonth.setVisibility(View.VISIBLE);
                    } else {
                        viewCheckedInThreeMonth.setVisibility(View.GONE);
                    }
                    if (hasCheckedIn.containsKey("sixMonthsAfter")) {
                        cbCheckedInSixMonth.setSelected(hasCheckedIn.get("sixMonthsAfter"));
                        tvCheckedInSixMonth.setSelected(hasCheckedIn.get("sixMonthsAfter"));
                        viewCheckedInSixMonth.setVisibility(View.VISIBLE);
                    } else {
                        viewCheckedInSixMonth.setVisibility(View.GONE);
                    }
                }

                Map<String, Boolean> hasConfirmedOrder = data.get("hasConfirmedOrder");
                if (hasConfirmedOrder != null ) {
                    if (hasConfirmedOrder.containsKey("oneWeekAfter")) {
                        cbConfirmedOrderOneWeek.setSelected(hasConfirmedOrder.get("oneWeekAfter"));
                        tvConfirmedOrderOneWeek.setSelected(hasConfirmedOrder.get("oneWeekAfter"));
                        viewConfirmedOrderOneWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewConfirmedOrderOneWeek.setVisibility(View.GONE);
                    }
                    if (hasConfirmedOrder.containsKey("threeWeeksAfter")) {
                        cbConfirmedOrderThreeWeek.setSelected(hasConfirmedOrder.get("threeWeeksAfter"));
                        tvConfirmedOrderThreeWeek.setSelected(hasConfirmedOrder.get("threeWeeksAfter"));
                        viewConfirmedOrderThreeWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewConfirmedOrderThreeWeek.setVisibility(View.GONE);
                    }
                    if (hasConfirmedOrder.containsKey("sixWeeksAfter")) {
                        cbConfirmedOrderSixWeek.setSelected(hasConfirmedOrder.get("sixWeeksAfter"));
                        tvConfirmedOrderSixWeek.setSelected(hasConfirmedOrder.get("sixWeeksAfter"));
                        viewConfirmedOrderSixWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewConfirmedOrderSixWeek.setVisibility(View.GONE);
                    }
                    if (hasConfirmedOrder.containsKey("threeMonthsAfter")) {
                        cbConfirmedOrderThreeMonth.setSelected(hasConfirmedOrder.get("threeMonthsAfter"));
                        tvConfirmedOrderThreeMonth.setSelected(hasConfirmedOrder.get("threeMonthsAfter"));
                        viewConfirmedOrderThreeMonth.setVisibility(View.VISIBLE);
                    } else {
                        viewConfirmedOrderThreeMonth.setVisibility(View.GONE);
                    }
                    if (hasConfirmedOrder.containsKey("sixMonthsAfter")) {
                        cbConfirmedOrderSixMonth.setSelected(hasConfirmedOrder.get("sixMonthsAfter"));
                        tvConfirmedOrderSixMonth.setSelected(hasConfirmedOrder.get("sixMonthsAfter"));
                        viewConfirmedOrderSixMonth.setVisibility(View.VISIBLE);
                    } else {
                        viewConfirmedOrderSixMonth.setVisibility(View.GONE);
                    }
                }

                Map<String, Boolean> hasScheduledOrder = data.get("hasScheduledOrder");
                if (hasScheduledOrder != null ) {
                    if (hasScheduledOrder.containsKey("oneWeekAfter")) {
                        cbScheduledOrderOneWeek.setSelected(hasScheduledOrder.get("oneWeekAfter"));
                        tvScheduledOrderOneWeek.setSelected(hasScheduledOrder.get("oneWeekAfter"));
                        viewScheduledOrderOneWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewScheduledOrderOneWeek.setVisibility(View.GONE);
                    }
                    if (hasScheduledOrder.containsKey("threeWeeksAfter")) {
                        cbScheduledOrderThreeWeek.setSelected(hasScheduledOrder.get("threeWeeksAfter"));
                        tvScheduledOrderThreeWeek.setSelected(hasScheduledOrder.get("threeWeeksAfter"));
                        viewScheduledOrderThreeWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewScheduledOrderThreeWeek.setVisibility(View.GONE);
                    }
                    if (hasScheduledOrder.containsKey("sixWeeksAfter")) {
                        cbScheduledOrderSixWeek.setSelected(hasScheduledOrder.get("sixWeeksAfter"));
                        tvScheduledOrderSixWeek.setSelected(hasScheduledOrder.get("sixWeeksAfter"));
                        viewScheduledOrderSixWeek.setVisibility(View.VISIBLE);
                    } else {
                        viewScheduledOrderSixWeek.setVisibility(View.GONE);
                    }
                    if (hasScheduledOrder.containsKey("threeMonthsAfter")) {
                        cbScheduledOrderThreeMonth.setSelected(hasScheduledOrder.get("threeMonthsAfter"));
                        tvScheduledOrderThreeMonth.setSelected(hasScheduledOrder.get("threeMonthsAfter"));
                        viewScheduledOrderThreeMonth.setVisibility(View.VISIBLE);
                    } else {
                        viewScheduledOrderThreeMonth.setVisibility(View.GONE);
                    }
                    if (hasScheduledOrder.containsKey("sixMonthsAfter")) {
                        cbScheduledOrderSixMonth.setSelected(hasScheduledOrder.get("sixMonthsAfter"));
                        tvScheduledOrderSixMonth.setSelected(hasScheduledOrder.get("sixMonthsAfter"));
                        viewScheduledOrderSixMonth.setVisibility(View.VISIBLE);
                    } else {
                        viewScheduledOrderSixMonth.setVisibility(View.GONE);
                    }
                }

            }
        }

    }

    public void saveChecklist(){

//        checklist.put("hasCheckedIn", cbCheckedDocterYes.isSelected());
//        checklist.put("hasFollowedDirections", cbFollowingDirectionYes.isSelected());
//        checklist.put("hasScheduledVisit", cbScheduledFirstYes.isSelected());
//        checklist.put("hasHadFirstPostAppt", cbFirstAppointmentYes.isSelected());
//        if (surgeryType.equals(SurgeryType.LumbarFusion) || surgeryType.equals(SurgeryType.ACDF)) {
//            checklist.put("hasEmail", cbEmailYes.isSelected());
//            checklist.put("facilityAddress", edtAddress.getText().toString());
//            checklist.put("scheduledPostopDate", edtSchedulePostop.getText().toString());
//        }

        Map<String, Map<String, Boolean>> newData = new HashMap<>();
        Map<String, Boolean> checkedInData = new HashMap<>();
        checkedInData.put("oneWeekAfter", cbCheckedInOneWeek.isSelected());
        checkedInData.put("threeWeeksAfter", cbCheckedInThreeWeek.isSelected());
        checkedInData.put("sixWeeksAfter", cbCheckedInSixWeek.isSelected());
        checkedInData.put("threeMonthsAfter", cbCheckedInThreeMonth.isSelected());
        checkedInData.put("sixMonthsAfter", cbCheckedInSixMonth.isSelected());

        Map<String, Boolean> confirmedOrderData = new HashMap<>();
        confirmedOrderData.put("oneWeekAfter", cbConfirmedOrderOneWeek.isSelected());
        confirmedOrderData.put("threeWeeksAfter", cbConfirmedOrderThreeWeek.isSelected());
        confirmedOrderData.put("sixWeeksAfter", cbConfirmedOrderSixWeek.isSelected());
        confirmedOrderData.put("threeMonthsAfter", cbConfirmedOrderThreeMonth.isSelected());
        confirmedOrderData.put("sixMonthsAfter", cbConfirmedOrderSixMonth.isSelected());

        Map<String, Boolean> scheduledOrderData = new HashMap<>();
        scheduledOrderData.put("oneWeekAfter", cbScheduledOrderOneWeek.isSelected());
        scheduledOrderData.put("threeWeeksAfter", cbScheduledOrderThreeWeek.isSelected());
        scheduledOrderData.put("sixWeeksAfter", cbScheduledOrderSixWeek.isSelected());
        scheduledOrderData.put("threeMonthsAfter", cbScheduledOrderThreeMonth.isSelected());
        scheduledOrderData.put("sixMonthsAfter", cbScheduledOrderSixMonth.isSelected());

        newData.put("hasCheckedIn", checkedInData);
        newData.put("hasConfirmedOrder", confirmedOrderData);
        newData.put("hasScheduledOrder", scheduledOrderData);

        checklist.put("data", newData);

        checklist.saveInBackground();

    }


}