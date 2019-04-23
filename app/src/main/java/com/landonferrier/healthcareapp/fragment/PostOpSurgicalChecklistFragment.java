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
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostOpSurgicalChecklistFragment extends BaseFragment {


    @BindView(R.id.cb_checked_docter_yes)
    ImageView cbCheckedDocterYes;

    @BindView(R.id.tv_checked_docter_yes)
    CustomFontTextView tvCheckedDocterYes;

    @BindView(R.id.cb_checked_docter_no)
    ImageView cbCheckedDocterNo;

    @BindView(R.id.tv_checked_docter_no)
    CustomFontTextView tvCheckedDocterNo;

    @BindView(R.id.cb_following_direction_yes)
    ImageView cbFollowingDirectionYes;

    @BindView(R.id.tv_following_direction_yes)
    CustomFontTextView tvFollowingDirectionYes;

    @BindView(R.id.cb_following_direction_no)
    ImageView cbFollowingDirectionNo;

    @BindView(R.id.tv_following_direction_no)
    CustomFontTextView tvFollowingDirectionNo;

    @BindView(R.id.cb_scheduled_first_yes)
    ImageView cbScheduledFirstYes;

    @BindView(R.id.tv_scheduled_first_yes)
    CustomFontTextView tvScheduledFirstYes;

    @BindView(R.id.cb_scheduled_first_no)
    ImageView cbScheduledFirstNo;

    @BindView(R.id.tv_scheduled_first_no)
    CustomFontTextView tvScheduledFirstNo;

    @BindView(R.id.cb_first_appointment_yes)
    ImageView cbFirstAppointmentYes;

    @BindView(R.id.tv_first_appointment_yes)
    CustomFontTextView tvFirstAppointmentYes;

    @BindView(R.id.cb_first_appointment_no)
    ImageView cbFirstAppointmentNo;

    @BindView(R.id.tv_first_appointment_no)
    CustomFontTextView tvFirstAppointmentNo;

    @BindView(R.id.tv_date)
    CustomFontTextView tvDate;

    @BindView(R.id.edt_schedule_post_op)
    CustomFontEditText edtSchedulePostop;

    @BindView(R.id.tv_email)
    CustomFontTextView tvEmail;

    @BindView(R.id.ll_email)
    LinearLayout llEmail;

    @BindView(R.id.cb_email_yes)
    ImageView cbEmailYes;

    @BindView(R.id.tv_email_yes)
    CustomFontTextView tvEmailYes;

    @BindView(R.id.cb_email_no)
    ImageView cbEmailNo;

    @BindView(R.id.tv_email_no)
    CustomFontTextView tvEmailNo;

    @BindView(R.id.tv_address)
    CustomFontTextView tvAddress;

    @BindView(R.id.edt_address)
    CustomFontEditText edtAddress;

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
                edtSchedulePostop.setText(current);
                edtSchedulePostop.setSelection(sel < current.length() ? sel : current.length());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @SuppressLint("DefaultLocale")
    public void initView() {
        cbCheckedDocterNo.setSelected(false);
        cbCheckedDocterYes.setSelected(false);
        cbScheduledFirstNo.setSelected(false);
        cbScheduledFirstYes.setSelected(false);
        cbFirstAppointmentYes.setSelected(false);
        cbFirstAppointmentNo.setSelected(false);
        cbFollowingDirectionNo.setSelected(false);
        cbFollowingDirectionYes.setSelected(false);
        cbEmailYes.setSelected(false);
        cbEmailNo.setSelected(false);
        fetchSurveys();

        edtSchedulePostop.addTextChangedListener(tw);

        cbCheckedDocterYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedDocterYes.setSelected(true);
                tvCheckedDocterYes.setSelected(true);
                cbCheckedDocterNo.setSelected(false);
                tvCheckedDocterNo.setSelected(false);
            }
        });

        cbCheckedDocterNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckedDocterYes.setSelected(false);
                tvCheckedDocterYes.setSelected(false);
                cbCheckedDocterNo.setSelected(true);
                tvCheckedDocterNo.setSelected(true);
            }
        });

        cbFollowingDirectionYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFollowingDirectionYes.setSelected(true);
                tvFollowingDirectionYes.setSelected(true);
                cbFollowingDirectionNo.setSelected(false);
                tvFollowingDirectionNo.setSelected(false);
            }
        });

        cbFollowingDirectionNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFollowingDirectionYes.setSelected(false);
                tvFollowingDirectionYes.setSelected(false);
                cbFollowingDirectionNo.setSelected(true);
                tvFollowingDirectionNo.setSelected(true);
            }
        });

        cbScheduledFirstYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledFirstYes.setSelected(true);
                tvScheduledFirstYes.setSelected(true);
                cbScheduledFirstNo.setSelected(false);
                tvScheduledFirstNo.setSelected(false);
            }
        });

        cbScheduledFirstNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbScheduledFirstYes.setSelected(false);
                tvScheduledFirstYes.setSelected(false);
                cbScheduledFirstNo.setSelected(true);
                tvScheduledFirstNo.setSelected(true);
            }
        });

        cbFirstAppointmentYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFirstAppointmentYes.setSelected(true);
                tvFirstAppointmentYes.setSelected(true);
                cbFirstAppointmentNo.setSelected(false);
                tvFirstAppointmentNo.setSelected(false);
            }
        });

        cbFirstAppointmentNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFirstAppointmentYes.setSelected(false);
                tvFirstAppointmentYes.setSelected(false);
                cbFirstAppointmentNo.setSelected(true);
                tvFirstAppointmentNo.setSelected(true);
            }
        });

        cbEmailYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbEmailYes.setSelected(true);
                tvEmailYes.setSelected(true);
                cbEmailNo.setSelected(false);
                tvEmailNo.setSelected(false);
            }
        });

        cbEmailNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbEmailYes.setSelected(false);
                tvEmailYes.setSelected(false);
                cbEmailNo.setSelected(true);
                tvEmailNo.setSelected(true);
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
        boolean hasCheckedIn = checklist.getBoolean("hasCheckedIn");

        if (hasCheckedIn) {
            cbCheckedDocterYes.setSelected(true);
            tvCheckedDocterYes.setSelected(true);
            cbCheckedDocterNo.setSelected(false);
            tvCheckedDocterNo.setSelected(false);
        }else{
            cbCheckedDocterYes.setSelected(false);
            tvCheckedDocterYes.setSelected(false);
            cbCheckedDocterNo.setSelected(true);
            tvCheckedDocterNo.setSelected(true);
        }

        boolean hasFollowedDirections = checklist.getBoolean("hasFollowedDirections");

        if (hasFollowedDirections) {
            cbFollowingDirectionYes.setSelected(true);
            tvFollowingDirectionYes.setSelected(true);
            cbFollowingDirectionNo.setSelected(false);
            tvFollowingDirectionNo.setSelected(false);
        }else{
            cbFollowingDirectionYes.setSelected(false);
            tvFollowingDirectionYes.setSelected(false);
            cbFollowingDirectionNo.setSelected(true);
            tvFollowingDirectionNo.setSelected(true);
        }

        boolean hasScheduledVisit = checklist.getBoolean("hasScheduledVisit");

        if (hasScheduledVisit) {
            cbScheduledFirstYes.setSelected(true);
            tvScheduledFirstYes.setSelected(true);
            cbScheduledFirstNo.setSelected(false);
            tvScheduledFirstNo.setSelected(false);
        }else{
            cbScheduledFirstYes.setSelected(false);
            tvScheduledFirstYes.setSelected(false);
            cbScheduledFirstNo.setSelected(true);
            tvScheduledFirstNo.setSelected(true);
        }

        if (surgeryType.equals(SurgeryType.LumbarFusion) || surgeryType.equals(SurgeryType.ACDF)) {

            boolean hasScheduledDate = checklist.has("scheduledPostopDate");
            if (hasScheduledDate) {
                tvDate.setVisibility(View.VISIBLE);
                edtSchedulePostop.setVisibility(View.VISIBLE);
                edtSchedulePostop.setText(checklist.getString("scheduledPostopDate"));
            }

            if (checklist.has("hasEmail")) {
                tvEmail.setVisibility(View.VISIBLE);
                llEmail.setVisibility(View.VISIBLE);
                boolean hasEmail = checklist.getBoolean("hasEmail");
                if (hasEmail) {
                    cbEmailYes.setSelected(true);
                    tvEmailYes.setSelected(true);
                    cbEmailNo.setSelected(false);
                    tvEmailNo.setSelected(false);
                }else{
                    cbEmailYes.setSelected(false);
                    tvEmailYes.setSelected(false);
                    cbEmailNo.setSelected(true);
                    tvEmailNo.setSelected(true);
                }

            }

            boolean hasFacilityAddress = checklist.has("facilityAddress");
            if (hasFacilityAddress) {
                tvAddress.setVisibility(View.VISIBLE);
                edtAddress.setVisibility(View.VISIBLE);
                edtAddress.setText(checklist.getString("facilityAddress"));
            }

        } else {
            tvDate.setVisibility(View.GONE);
            edtSchedulePostop.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            llEmail.setVisibility(View.GONE);
            edtAddress.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
        }

        boolean hasHadFirstPostAppt = checklist.getBoolean("hasHadFirstPostAppt");

        if (hasHadFirstPostAppt) {
            cbFirstAppointmentYes.setSelected(true);
            tvFirstAppointmentYes.setSelected(true);
            cbFirstAppointmentNo.setSelected(false);
            tvFirstAppointmentNo.setSelected(false);
        }else{
            cbFirstAppointmentYes.setSelected(false);
            tvFirstAppointmentYes.setSelected(false);
            cbFirstAppointmentNo.setSelected(true);
            tvFirstAppointmentNo.setSelected(true);
        }

    }

    public void saveChecklist(){

        checklist.put("hasCheckedIn", cbCheckedDocterYes.isSelected());
        checklist.put("hasFollowedDirections", cbFollowingDirectionYes.isSelected());
        checklist.put("hasScheduledVisit", cbScheduledFirstYes.isSelected());
        checklist.put("hasHadFirstPostAppt", cbFirstAppointmentYes.isSelected());
        if (surgeryType.equals(SurgeryType.LumbarFusion) || surgeryType.equals(SurgeryType.ACDF)) {
            checklist.put("hasEmail", cbEmailYes.isSelected());
            checklist.put("facilityAddress", edtAddress.getText().toString());
            checklist.put("scheduledPostopDate", edtSchedulePostop.getText().toString());
        }


        checklist.saveInBackground();

    }


}