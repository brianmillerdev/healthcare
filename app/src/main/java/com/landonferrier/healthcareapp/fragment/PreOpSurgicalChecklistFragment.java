package com.landonferrier.healthcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.EventPush;
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

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PreOpSurgicalChecklistFragment extends BaseFragment {

    @BindView(R.id.edt_scheduler_name)
    CustomFontEditText edtSchedulerName;

    @BindView(R.id.edt_scheduler_number)
    CustomFontEditText edtSchedulerNumber;

    @BindView(R.id.edt_hospital_name)
    CustomFontEditText edtHospitalName;

    @BindView(R.id.edt_hospital_address)
    CustomFontEditText edtHospitalAddress;

    @BindView(R.id.edt_hospital_number)
    CustomFontEditText edtHospitalNumber;

    @BindView(R.id.edt_pre_registration_number)
    CustomFontEditText edtPreRegistrationNumber;

    @BindView(R.id.edt_pcp_name)
    CustomFontEditText edtPCPName;

    @BindView(R.id.edt_pcp_number)
    CustomFontEditText edtPCPNumber;


    @BindView(R.id.cb_completed_surgery)
    ImageView cbCompletedSurgery;

    @BindView(R.id.tv_completed_surgery)
    CustomFontTextView tvCompletedSurgery;

    @BindView(R.id.cb_date_yes)
    ImageView cbDateYes;

    @BindView(R.id.tv_date_yes)
    CustomFontTextView tvDateYes;

    @BindView(R.id.cb_date_no)
    ImageView cbDateNo;

    @BindView(R.id.tv_date_no)
    CustomFontTextView tvDateNo;

    @BindView(R.id.cb_pcp_yes)
    ImageView cbPCPYes;

    @BindView(R.id.tv_pcp_yes)
    CustomFontTextView tvPCPYes;

    @BindView(R.id.cb_pcp_no)
    ImageView cbPCPNo;

    @BindView(R.id.tv_pcp_no)
    CustomFontTextView tvPCPNo;

    @BindView(R.id.cb_pcp_cardiologist)
    ImageView cbPCPCardiologist;

    @BindView(R.id.tv_pcp_cardiologist)
    CustomFontTextView tvPCPCardiologist;

    @BindView(R.id.cb_found_yes)
    ImageView cbFoundYes;

    @BindView(R.id.tv_found_yes)
    CustomFontTextView tvFoundYes;

    @BindView(R.id.cb_found_no)
    ImageView cbFoundNo;

    @BindView(R.id.tv_found_no)
    CustomFontTextView tvFoundNo;

    @BindView(R.id.cb_eat_light_meal)
    ImageView cbEatLightMeal;

    @BindView(R.id.tv_eat_light_meal)
    CustomFontTextView tvEatLightMeal;

    @BindView(R.id.cb_drink_lots_fluids)
    ImageView cbDrinkLotsFluids;

    @BindView(R.id.tv_drink_lots_fluids)
    CustomFontTextView tvDrinkLotsFluids;

    @BindView(R.id.cb_dont_midnight)
    ImageView cbDontMidnight;

    @BindView(R.id.tv_dont_midnight)
    CustomFontTextView tvDontMidnight;




    public KProgressHUD hud;

    ParseObject surgery;
    ParseObject checklist;
    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_pre_op_surveys_checklist, container, false);
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


    @SuppressLint("DefaultLocale")
    public void initView() {
        cbDrinkLotsFluids.setSelected(false);
        cbDontMidnight.setSelected(false);
        cbEatLightMeal.setSelected(false);
        cbCompletedSurgery.setSelected(false);
        cbFoundYes.setSelected(false);
        cbDateYes.setSelected(false);
        cbPCPYes.setSelected(false);
        cbPCPCardiologist.setSelected(false);
        fetchSurveys();

        cbCompletedSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cbCompletedSurgery.setSelected(!cbCompletedSurgery.isSelected());
                tvCompletedSurgery.setSelected(cbCompletedSurgery.isSelected());
            }
        });

        cbDateYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDateYes.setSelected(true);
                tvDateYes.setSelected(true);
                cbDateNo.setSelected(false);
                tvDateNo.setSelected(false);
            }
        });

        cbDateNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDateYes.setSelected(false);
                tvDateYes.setSelected(false);
                cbDateNo.setSelected(true);
                tvDateNo.setSelected(true);
            }
        });

        cbPCPYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbPCPYes.setSelected(true);
                tvPCPYes.setSelected(true);
                cbPCPNo.setSelected(false);
                tvPCPNo.setSelected(false);
            }
        });

        cbPCPNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbPCPYes.setSelected(false);
                tvPCPYes.setSelected(false);
                cbPCPNo.setSelected(true);
                tvPCPNo.setSelected(true);
            }
        });

        cbPCPCardiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbPCPCardiologist.setSelected(!cbPCPCardiologist.isSelected());
                tvPCPCardiologist.setSelected(cbPCPCardiologist.isSelected());
            }
        });

        cbFoundYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFoundYes.setSelected(true);
                tvFoundYes.setSelected(true);
                cbFoundNo.setSelected(false);
                tvFoundNo.setSelected(false);
            }
        });

        cbFoundNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFoundYes.setSelected(false);
                tvFoundYes.setSelected(false);
                cbFoundNo.setSelected(true);
                tvFoundNo.setSelected(true);
            }
        });

        cbEatLightMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbEatLightMeal.setSelected(!cbEatLightMeal.isSelected());
                tvEatLightMeal.setSelected(cbEatLightMeal.isSelected());
            }
        });

        cbDrinkLotsFluids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDrinkLotsFluids.setSelected(!cbDrinkLotsFluids.isSelected());
                tvDrinkLotsFluids.setSelected(cbDrinkLotsFluids.isSelected());
            }
        });

        cbDontMidnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDontMidnight.setSelected(!cbDontMidnight.isSelected());
                tvDontMidnight.setSelected(cbDontMidnight.isSelected());
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

        ParseQuery<ParseObject> cQuery = ParseQuery.getQuery("Checklist");
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

                        checklist = new ParseObject("Checklist");
                        checklist.put("ownerId", ParseUser.getCurrentUser().getObjectId());
                        checklist.put("surgeryId", surgery.getObjectId());
                        checklist.put("hasDrankFluids", false);
                        checklist.put("dontDrink", false);
                        checklist.put("hasEatenLightMeal", false);
                        checklist.put("hasCompletedCall", false);
                        checklist.put("schedulerName", "");
                        checklist.put("schedulerNumber", "");
                        checklist.put("hasFinalizedDate", false);
                        checklist.put("hasScheduledVisit", false);
                        checklist.put("hospitalName", "");
                        checklist.put("hospitalAddress", "");
                        checklist.put("hospitalPhone", "");
                        checklist.put("preregistrationNumber", "");
                        checklist.put("preopApproval", false);
                        checklist.put("pcpNumber", "");
                        checklist.put("pcpName", "");
                        checklist.put("haveFoundPlace", false);

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
        String schedulerName = checklist.getString("schedulerName");
        String schedulerNumber = checklist.getString("schedulerNumber");

        edtSchedulerName.setText(schedulerName);
        edtSchedulerNumber.setText(schedulerNumber);

        boolean hasCompletedCall = checklist.getBoolean("hasCompletedCall");

        cbCompletedSurgery.setSelected(hasCompletedCall);
        tvCompletedSurgery.setSelected(hasCompletedCall);

        boolean hasFinalizedDate = checklist.getBoolean("hasFinalizedDate");

        if (hasFinalizedDate) {
            cbDateYes.setSelected(true);
            tvDateYes.setSelected(true);
            cbDateNo.setSelected(false);
            tvDateNo.setSelected(false);
        }else{
            cbDateYes.setSelected(false);
            tvDateYes.setSelected(false);
            cbDateNo.setSelected(true);
            tvDateNo.setSelected(true);
        }

        boolean hasScheduledVisit = checklist.getBoolean("hasScheduledVisit");

        if (hasScheduledVisit) {
            cbPCPYes.setSelected(true);
            tvPCPYes.setSelected(true);
            cbPCPNo.setSelected(false);
            tvPCPNo.setSelected(false);
        }else{
            cbPCPYes.setSelected(false);
            tvPCPYes.setSelected(false);
            cbPCPNo.setSelected(true);
            tvPCPNo.setSelected(true);
        }


        String hospitalName = checklist.getString("hospitalName");
        String hospitalAddress = checklist.getString("hospitalAddress");
        String hospitalPhone = checklist.getString("hospitalPhone");
        String preregistrationNumber = checklist.getString("preregistrationNumber");
        edtHospitalName.setText(hospitalName);
        edtHospitalAddress.setText(hospitalAddress);
        edtHospitalNumber.setText(hospitalPhone);
        edtPreRegistrationNumber.setText(preregistrationNumber);

        boolean preopApproval = checklist.getBoolean("preopApproval");

        cbPCPCardiologist.setSelected(preopApproval);
        tvPCPCardiologist.setSelected(preopApproval);


        String pcpName = checklist.getString("pcpName");
        String pcpNumber = checklist.getString("pcpNumber");
        edtPCPName.setText(pcpName);
        edtPCPNumber.setText(pcpNumber);

        boolean haveFoundPlace = checklist.getBoolean("haveFoundPlace");

        if (haveFoundPlace) {
            cbFoundYes.setSelected(true);
            tvFoundYes.setSelected(true);
            cbFoundNo.setSelected(false);
            tvFoundNo.setSelected(false);
        }else{
            cbFoundYes.setSelected(false);
            tvFoundYes.setSelected(false);
            cbFoundNo.setSelected(true);
            tvFoundNo.setSelected(true);
        }

        boolean hasEatenLightMeal = checklist.getBoolean("hasEatenLightMeal");

        cbEatLightMeal.setSelected(hasEatenLightMeal);
        tvEatLightMeal.setSelected(hasEatenLightMeal);

        boolean hasDrankFluids = checklist.getBoolean("hasDrankFluids");

        cbDrinkLotsFluids.setSelected(hasDrankFluids);
        tvDrinkLotsFluids.setSelected(hasDrankFluids);

        boolean dontDrink = checklist.getBoolean("dontDrink");

        cbDontMidnight.setSelected(dontDrink);
        tvDontMidnight.setSelected(dontDrink);


    }

    public void saveChecklist(){

        checklist.put("hasDrankFluids", cbDrinkLotsFluids.isSelected());
        checklist.put("dontDrink", cbDontMidnight.isSelected());
        checklist.put("hasEatenLightMeal", cbEatLightMeal.isSelected());
        checklist.put("hasCompletedCall", cbCompletedSurgery.isSelected());
        checklist.put("haveFoundPlace", cbFoundYes.isSelected());
        checklist.put("hasFinalizedDate", cbDateYes.isSelected());
        checklist.put("hasScheduledVisit", cbPCPYes.isSelected());
        checklist.put("preopApproval", cbPCPCardiologist.isSelected());

        checklist.put("schedulerName", edtSchedulerName.getText().toString());
        checklist.put("schedulerNumber", edtSchedulerNumber.getText().toString());
        checklist.put("hospitalName", edtHospitalName.getText().toString());
        checklist.put("hospitalAddress", edtHospitalAddress.getText().toString());
        checklist.put("hospitalPhone", edtHospitalNumber.getText().toString());
        checklist.put("preregistrationNumber", edtPreRegistrationNumber.getText().toString());
        checklist.put("pcpName", edtPCPName.getText().toString());
        checklist.put("pcpNumber", edtPCPNumber.getText().toString());

        checklist.saveInBackground();

    }


}