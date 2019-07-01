package com.landonferrier.healthcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import java.util.List;
import java.util.SplittableRandom;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplicationsChecklistFragment extends BaseFragment {

    @BindView(R.id.cb_new_numbness)
    ImageView cb_new_numbness;

    @BindView(R.id.tv_new_numbness)
    CustomFontTextView tv_new_numbness;

    @BindView(R.id.ll_new_numbness)
    LinearLayout ll_new_numbness;


    @BindView(R.id.cb_infection)
    ImageView cb_infection;

    @BindView(R.id.tv_infection)
    CustomFontTextView tv_infection;

    @BindView(R.id.ll_infection)
    LinearLayout ll_infection;


    @BindView(R.id.cb_spinal_fluid_leak)
    ImageView cb_spinal_fluid_leak;

    @BindView(R.id.tv_spinal_fluid_leak)
    CustomFontTextView tv_spinal_fluid_leak;

    @BindView(R.id.ll_spinal_fluid_leak)
    LinearLayout ll_spinal_fluid_leak;

    @BindView(R.id.cb_spinal_fluid_leak_requiring_further_surgery)
    ImageView cb_spinal_fluid_leak_requiring_further_surgery;

    @BindView(R.id.tv_spinal_fluid_leak_requiring_further_surgery)
    CustomFontTextView tv_spinal_fluid_leak_requiring_further_surgery;

    @BindView(R.id.ll_spinal_fluid_leak_requiring_further_surgery)
    LinearLayout ll_spinal_fluid_leak_requiring_further_surgery;


    @BindView(R.id.cb_new_weakness)
    ImageView cb_new_weakness;

    @BindView(R.id.tv_new_weakness)
    CustomFontTextView tv_new_weakness;

    @BindView(R.id.ll_new_weakness)
    LinearLayout ll_new_weakness;



    @BindView(R.id.cb_new_balance_problems)
    ImageView cb_new_balance_problems;

    @BindView(R.id.tv_new_balance_problems)
    CustomFontTextView tv_new_balance_problems;

    @BindView(R.id.ll_new_balance_problems)
    LinearLayout ll_new_balance_problems;


    @BindView(R.id.cb_new_bladder_incontinence)
    ImageView cb_new_bladder_incontinence;

    @BindView(R.id.tv_new_bladder_incontinence)
    CustomFontTextView tv_new_bladder_incontinence;

    @BindView(R.id.ll_new_bladder_incontinence)
    LinearLayout ll_new_bladder_incontinence;



    @BindView(R.id.cb_new_bowel_incontinence)
    ImageView cb_new_bowel_incontinence;

    @BindView(R.id.tv_new_bowel_incontinence)
    CustomFontTextView tv_new_bowel_incontinence;

    @BindView(R.id.ll_new_bowel_incontinence)
    LinearLayout ll_new_bowel_incontinence;


    @BindView(R.id.cb_paralysis)
    ImageView cb_paralysis;

    @BindView(R.id.tv_paralysis)
    CustomFontTextView tv_paralysis;

    @BindView(R.id.ll_paralysis)
    LinearLayout ll_paralysis;



    @BindView(R.id.cb_bladder_infection)
    ImageView cb_bladder_infection;

    @BindView(R.id.tv_bladder_infection)
    CustomFontTextView tv_bladder_infection;

    @BindView(R.id.ll_bladder_infection)
    LinearLayout ll_bladder_infection;



    @BindView(R.id.cb_pneumonia)
    ImageView cb_pneumonia;

    @BindView(R.id.tv_pneumonia)
    CustomFontTextView tv_pneumonia;

    @BindView(R.id.ll_pneumonia)
    LinearLayout ll_pneumonia;


    @BindView(R.id.cb_heart_attack)
    ImageView cb_heart_attack;

    @BindView(R.id.tv_heart_attack)
    CustomFontTextView tv_heart_attack;

    @BindView(R.id.ll_heart_attack)
    LinearLayout ll_heart_attack;



    @BindView(R.id.cb_stroke)
    ImageView cb_stroke;

    @BindView(R.id.tv_stroke)
    CustomFontTextView tv_stroke;

    @BindView(R.id.ll_stroke)
    LinearLayout ll_stroke;



    @BindView(R.id.cb_blood_clot_in_leg_or_legs)
    ImageView cb_blood_clot_in_leg_or_legs;

    @BindView(R.id.tv_blood_clot_in_leg_or_legs)
    CustomFontTextView tv_blood_clot_in_leg_or_legs;

    @BindView(R.id.ll_blood_clot_in_leg_or_legs)
    LinearLayout ll_blood_clot_in_leg_or_legs;



    @BindView(R.id.cb_worse_pain_after_surger)
    ImageView cb_worse_pain_after;

    @BindView(R.id.tv_worse_pain_after_surger)
    CustomFontTextView tv_worse_pain_after;

    @BindView(R.id.ll_worse_pain_after_surger)
    LinearLayout ll_worse_pain_after_surger;


    @BindView(R.id.cb_needing_a_subsequent_fusion)
    ImageView cb_needing_a_subsequent_fusion;

    @BindView(R.id.tv_needing_a_subsequent_fusion)
    CustomFontTextView tv_needing_a_subsequent_fusion;

    @BindView(R.id.ll_needing_a_subsequent_fusion)
    LinearLayout ll_needing_a_subsequent_fusion;




    @BindView(R.id.cb_recurrent_or_new_disc_herniation)
    ImageView cb_recurrent_or_new_disc_herniation;

    @BindView(R.id.tv_recurrent_or_new_disc_herniation)
    CustomFontTextView tv_recurrent_or_new_disc_herniation;

    @BindView(R.id.ll_recurrent_or_new_disc_herniation)
    LinearLayout ll_recurrent_or_new_disc_herniation;



    @BindView(R.id.cb_failure_to_fuse_on_x_ray_or_ct_scan)
    ImageView cb_failure_to_fuse_on_x_ray_or_ct_scan;

    @BindView(R.id.tv_failure_to_fuse_on_x_ray_or_ct_scan)
    CustomFontTextView tv_failure_to_fuse_on_x_ray_or_ct_scan;

    @BindView(R.id.ll_failure_to_fuse_on_x_ray_or_ct_scan)
    LinearLayout ll_failure_to_fuse_on_x_ray_or_ct_scan;



    @BindView(R.id.cb_screw_or_hardware_loosening)
    ImageView cb_screw_or_hardware_loosening;

    @BindView(R.id.tv_screw_or_hardware_loosening)
    CustomFontTextView tv_screw_or_hardware_loosening;

    @BindView(R.id.ll_screw_or_hardware_loosening)
    LinearLayout ll_screw_or_hardware_loosening;



    @BindView(R.id.cb_broken_screw_s_or_rod_s)
    ImageView cb_broken_screw_s_or_rod_s;

    @BindView(R.id.tv_broken_screw_s_or_rod_s)
    CustomFontTextView tv_broken_screw_s_or_rod_s;

    @BindView(R.id.ll_broken_screw_s_or_rod_s)
    LinearLayout ll_broken_screw_s_or_rod_s;



    @BindView(R.id.cb_hoarse_voice)
    ImageView cb_hoarse_voice;

    @BindView(R.id.tv_hoarse_voice)
    CustomFontTextView tv_hoarse_voice;

    @BindView(R.id.ll_hoarse_voice)
    LinearLayout ll_hoarse_voice;



    @BindView(R.id.cb_broken_plate_or_screws)
    ImageView cb_broken_plate_or_screws;

    @BindView(R.id.tv_broken_plate_or_screws)
    CustomFontTextView tv_broken_plate_or_screws;

    @BindView(R.id.ll_broken_plate_or_screws)
    LinearLayout ll_broken_plate_or_screws;



    @BindView(R.id.cb_difficulty_swallowing)
    ImageView cb_difficulty_swallowing;

    @BindView(R.id.tv_difficulty_swallowing)
    CustomFontTextView tv_difficulty_swallowing;

    @BindView(R.id.ll_difficulty_swallowing)
    LinearLayout ll_difficulty_swallowing;


    public String surgeryType = null;

    public KProgressHUD hud;

    ParseObject surgery;
    ParseObject checklist;
    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_complications_checklist, container, false);
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
        cb_new_numbness.setSelected(false);

        cb_infection.setSelected(false);

        cb_spinal_fluid_leak.setSelected(false);

        cb_spinal_fluid_leak_requiring_further_surgery.setSelected(false);

        cb_new_weakness.setSelected(false);

        cb_new_balance_problems.setSelected(false);

        cb_new_bladder_incontinence.setSelected(false);

        cb_new_bowel_incontinence.setSelected(false);

        cb_paralysis.setSelected(false);

        cb_bladder_infection.setSelected(false);

        cb_pneumonia.setSelected(false);

        cb_heart_attack.setSelected(false);

        cb_stroke.setSelected(false);

        cb_blood_clot_in_leg_or_legs.setSelected(false);

        cb_worse_pain_after.setSelected(false);

        cb_needing_a_subsequent_fusion.setSelected(false);

        cb_recurrent_or_new_disc_herniation.setSelected(false);

        cb_failure_to_fuse_on_x_ray_or_ct_scan.setSelected(false);

        cb_screw_or_hardware_loosening.setSelected(false);

        cb_broken_screw_s_or_rod_s.setSelected(false);

        cb_hoarse_voice.setSelected(false);

        cb_broken_plate_or_screws.setSelected(false);

        cb_difficulty_swallowing.setSelected(false);


        fetchSurveys();

        cb_new_numbness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_numbness.setSelected(!cb_new_numbness.isSelected());
                tv_new_numbness.setSelected(cb_new_numbness.isSelected());
            }
        });

        cb_infection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_infection.setSelected(!cb_infection.isSelected());
                tv_infection.setSelected(cb_infection.isSelected());
            }
        });


        cb_spinal_fluid_leak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_spinal_fluid_leak.setSelected(!cb_spinal_fluid_leak.isSelected());
                tv_spinal_fluid_leak.setSelected(cb_spinal_fluid_leak.isSelected());
            }
        });


        cb_spinal_fluid_leak_requiring_further_surgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_spinal_fluid_leak_requiring_further_surgery.setSelected(!cb_spinal_fluid_leak_requiring_further_surgery.isSelected());
                tv_spinal_fluid_leak_requiring_further_surgery.setSelected(cb_spinal_fluid_leak_requiring_further_surgery.isSelected());
            }
        });


        cb_new_weakness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_weakness.setSelected(!cb_new_weakness.isSelected());
                tv_new_weakness.setSelected(cb_new_weakness.isSelected());
            }
        });


        cb_new_balance_problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_balance_problems.setSelected(!cb_new_balance_problems.isSelected());
                tv_new_balance_problems.setSelected(cb_new_balance_problems.isSelected());
            }
        });


        cb_new_bladder_incontinence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_bladder_incontinence.setSelected(!cb_new_bladder_incontinence.isSelected());
                tv_new_bladder_incontinence.setSelected(cb_new_bladder_incontinence.isSelected());
            }
        });


        cb_new_bowel_incontinence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_bowel_incontinence.setSelected(!cb_new_bowel_incontinence.isSelected());
                tv_new_bowel_incontinence.setSelected(cb_new_bowel_incontinence.isSelected());
            }
        });


        cb_paralysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_paralysis.setSelected(!cb_paralysis.isSelected());
                tv_paralysis.setSelected(cb_paralysis.isSelected());
            }
        });


        cb_bladder_infection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_bladder_infection.setSelected(!cb_bladder_infection.isSelected());
                tv_bladder_infection.setSelected(cb_bladder_infection.isSelected());
            }
        });


        cb_pneumonia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_pneumonia.setSelected(!cb_pneumonia.isSelected());
                tv_pneumonia.setSelected(cb_pneumonia.isSelected());
            }
        });


        cb_heart_attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_heart_attack.setSelected(!cb_heart_attack.isSelected());
                tv_heart_attack.setSelected(cb_heart_attack.isSelected());
            }
        });


        cb_stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_stroke.setSelected(!cb_stroke.isSelected());
                tv_stroke.setSelected(cb_stroke.isSelected());
            }
        });


        cb_blood_clot_in_leg_or_legs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_blood_clot_in_leg_or_legs.setSelected(!cb_blood_clot_in_leg_or_legs.isSelected());
                tv_blood_clot_in_leg_or_legs.setSelected(cb_blood_clot_in_leg_or_legs.isSelected());
            }
        });


        cb_worse_pain_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_worse_pain_after.setSelected(!cb_worse_pain_after.isSelected());
                tv_worse_pain_after.setSelected(cb_worse_pain_after.isSelected());
            }
        });


        cb_needing_a_subsequent_fusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_needing_a_subsequent_fusion.setSelected(!cb_needing_a_subsequent_fusion.isSelected());
                tv_needing_a_subsequent_fusion.setSelected(cb_needing_a_subsequent_fusion.isSelected());
            }
        });


        cb_recurrent_or_new_disc_herniation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_recurrent_or_new_disc_herniation.setSelected(!cb_recurrent_or_new_disc_herniation.isSelected());
                tv_recurrent_or_new_disc_herniation.setSelected(cb_recurrent_or_new_disc_herniation.isSelected());
            }
        });


        cb_failure_to_fuse_on_x_ray_or_ct_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_failure_to_fuse_on_x_ray_or_ct_scan.setSelected(!cb_failure_to_fuse_on_x_ray_or_ct_scan.isSelected());
                tv_failure_to_fuse_on_x_ray_or_ct_scan.setSelected(cb_failure_to_fuse_on_x_ray_or_ct_scan.isSelected());
            }
        });


        cb_screw_or_hardware_loosening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_screw_or_hardware_loosening.setSelected(!cb_screw_or_hardware_loosening.isSelected());
                tv_screw_or_hardware_loosening.setSelected(cb_screw_or_hardware_loosening.isSelected());
            }
        });


        cb_broken_screw_s_or_rod_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_broken_screw_s_or_rod_s.setSelected(!cb_broken_screw_s_or_rod_s.isSelected());
                tv_broken_screw_s_or_rod_s.setSelected(cb_broken_screw_s_or_rod_s.isSelected());
            }
        });


        cb_hoarse_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_hoarse_voice.setSelected(!cb_hoarse_voice.isSelected());
                tv_hoarse_voice.setSelected(cb_hoarse_voice.isSelected());
            }
        });


        cb_broken_plate_or_screws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_broken_plate_or_screws.setSelected(!cb_broken_plate_or_screws.isSelected());
                tv_broken_plate_or_screws.setSelected(cb_broken_plate_or_screws.isSelected());
            }
        });


        cb_difficulty_swallowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_difficulty_swallowing.setSelected(!cb_difficulty_swallowing.isSelected());
                tv_difficulty_swallowing.setSelected(cb_difficulty_swallowing.isSelected());
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
            if (Helper.surgeryTypeFor(surgeryId) != null) {

                surgeryType = Helper.surgeryTypeFor(surgeryId);
            }

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

        ParseQuery<ParseObject> cQuery = ParseQuery.getQuery("ChecklistComplications");
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

                        checklist = new ParseObject("ChecklistComplications");
                        checklist.put("ownerId", ParseUser.getCurrentUser().getObjectId());
                        checklist.put("surgeryId", surgery.getObjectId());
                        checklist.put("hasInfection", false);
                        checklist.put("hasNewNumbness", false);
                        checklist.put("hasSpinalFluidLeak", false);
                        checklist.put("hasSpinalFluidLeakRequiringSurgery", false);
                        checklist.put("hasNewBladderIncontinence", false);
                        checklist.put("hasNewBowelIncontinence", false);
                        checklist.put("hasParalysis", false);
                        checklist.put("hasBladderInfection", false);
                        checklist.put("hasPneumonia", false);
                        checklist.put("hasHeartAttack", false);
                        checklist.put("hasStroke", false);
                        checklist.put("hasWorsePain", false);
                        checklist.put("needsSubsequentFusion", false);
                        checklist.put("hasIncontinence", false);
                        checklist.put("hasFailureFuseScan", false);
                        checklist.put("hasHardwareLoosening", false);
                        checklist.put("hasBrokenScrewRod", false);
                        checklist.put("hasHoarseVoice", false);
                        checklist.put("hasDifficultySwallowing", false);
                        checklist.put("hasNewNumbers", false);
                        checklist.put("hasBloodClot", false);
                        checklist.put("hasNewWeakness", false);
                        checklist.put("hasNewBalanceProblems", false);
                        checklist.put("hasBrokenPlateScrews", false);
                        checklist.put("hasDiscHerniation", false);

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

        if (surgeryType.equals(SurgeryType.LumbarStenosis)) {
            tv_needing_a_subsequent_fusion.setVisibility(View.VISIBLE);
            ll_needing_a_subsequent_fusion.setVisibility(View.VISIBLE);

            tv_recurrent_or_new_disc_herniation.setVisibility(View.GONE);
            ll_recurrent_or_new_disc_herniation.setVisibility(View.GONE);

            tv_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.GONE);
            ll_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.GONE);

            tv_screw_or_hardware_loosening.setVisibility(View.GONE);
            ll_screw_or_hardware_loosening.setVisibility(View.GONE);

            tv_broken_screw_s_or_rod_s.setVisibility(View.GONE);
            ll_broken_screw_s_or_rod_s.setVisibility(View.GONE);

            tv_hoarse_voice.setVisibility(View.GONE);
            ll_hoarse_voice.setVisibility(View.GONE);

            tv_difficulty_swallowing.setVisibility(View.GONE);
            ll_difficulty_swallowing.setVisibility(View.GONE);

            tv_broken_plate_or_screws.setVisibility(View.GONE);
            ll_broken_plate_or_screws.setVisibility(View.GONE);


        } else if (surgeryType.equals(SurgeryType.LumbarDisectomy)){

            tv_needing_a_subsequent_fusion.setVisibility(View.VISIBLE);
            ll_needing_a_subsequent_fusion.setVisibility(View.VISIBLE);

            tv_recurrent_or_new_disc_herniation.setVisibility(View.VISIBLE);
            ll_recurrent_or_new_disc_herniation.setVisibility(View.VISIBLE);

            tv_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.GONE);
            ll_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.GONE);

            tv_screw_or_hardware_loosening.setVisibility(View.GONE);
            ll_screw_or_hardware_loosening.setVisibility(View.GONE);

            tv_broken_screw_s_or_rod_s.setVisibility(View.GONE);
            ll_broken_screw_s_or_rod_s.setVisibility(View.GONE);

            tv_hoarse_voice.setVisibility(View.GONE);
            ll_hoarse_voice.setVisibility(View.GONE);

            tv_difficulty_swallowing.setVisibility(View.GONE);
            ll_difficulty_swallowing.setVisibility(View.GONE);

            tv_broken_plate_or_screws.setVisibility(View.GONE);
            ll_broken_plate_or_screws.setVisibility(View.GONE);
        } else if (surgeryType.equals(SurgeryType.LumbarFusion)){

            tv_needing_a_subsequent_fusion.setVisibility(View.GONE);
            ll_needing_a_subsequent_fusion.setVisibility(View.GONE);

            tv_recurrent_or_new_disc_herniation.setVisibility(View.GONE);
            ll_recurrent_or_new_disc_herniation.setVisibility(View.GONE);

            tv_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.VISIBLE);
            ll_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.VISIBLE);

            tv_screw_or_hardware_loosening.setVisibility(View.VISIBLE);
            ll_screw_or_hardware_loosening.setVisibility(View.VISIBLE);

            tv_broken_screw_s_or_rod_s.setVisibility(View.VISIBLE);
            ll_broken_screw_s_or_rod_s.setVisibility(View.VISIBLE);

            tv_hoarse_voice.setVisibility(View.GONE);
            ll_hoarse_voice.setVisibility(View.GONE);

            tv_difficulty_swallowing.setVisibility(View.GONE);
            ll_difficulty_swallowing.setVisibility(View.GONE);

            tv_broken_plate_or_screws.setVisibility(View.GONE);
            ll_broken_plate_or_screws.setVisibility(View.GONE);
        } else if (surgeryType.equals(SurgeryType.ACDF)){

            tv_needing_a_subsequent_fusion.setVisibility(View.GONE);
            ll_needing_a_subsequent_fusion.setVisibility(View.GONE);

            tv_recurrent_or_new_disc_herniation.setVisibility(View.GONE);
            ll_recurrent_or_new_disc_herniation.setVisibility(View.GONE);

            tv_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.VISIBLE);
            ll_failure_to_fuse_on_x_ray_or_ct_scan.setVisibility(View.VISIBLE);

            tv_screw_or_hardware_loosening.setVisibility(View.GONE);
            ll_screw_or_hardware_loosening.setVisibility(View.GONE);

            tv_broken_screw_s_or_rod_s.setVisibility(View.GONE);
            ll_broken_screw_s_or_rod_s.setVisibility(View.GONE);

            tv_hoarse_voice.setVisibility(View.VISIBLE);
            ll_hoarse_voice.setVisibility(View.VISIBLE);

            tv_difficulty_swallowing.setVisibility(View.VISIBLE);
            ll_difficulty_swallowing.setVisibility(View.VISIBLE);

            tv_broken_plate_or_screws.setVisibility(View.VISIBLE);
            ll_broken_plate_or_screws.setVisibility(View.VISIBLE);
        } else {

        }

        if (checklist.has("hasNewNumbness")) {
            boolean hasNewNumbness = checklist.getBoolean("hasNewNumbness");

            cb_new_numbness.setSelected(hasNewNumbness);
            tv_new_numbness.setSelected(hasNewNumbness);
        }

        if (checklist.has("hasInfection")) {
            boolean hasInfection = checklist.getBoolean("hasInfection");
            cb_infection.setSelected(hasInfection);
            tv_infection.setSelected(hasInfection);
        }

        if (checklist.has("hasSpinalFluidLeak")) {
            boolean hasSpinalFluidLeak = checklist.getBoolean("hasSpinalFluidLeak");

            cb_spinal_fluid_leak.setSelected(hasSpinalFluidLeak);
            tv_spinal_fluid_leak.setSelected(hasSpinalFluidLeak);
        }

        if (checklist.has("hasSpinalFluidLeakRequiringSurgery")) {
            boolean hasSpinalFluidLeakRequiringSurgery = checklist.getBoolean("hasSpinalFluidLeakRequiringSurgery");

            cb_spinal_fluid_leak_requiring_further_surgery.setSelected(hasSpinalFluidLeakRequiringSurgery);
            tv_spinal_fluid_leak_requiring_further_surgery.setSelected(hasSpinalFluidLeakRequiringSurgery);
        }

        if (checklist.has("hasNewWeakness")) {
            boolean hasNewWeakness = checklist.getBoolean("hasNewWeakness");

            cb_new_weakness.setSelected(hasNewWeakness);
            tv_new_weakness.setSelected(hasNewWeakness);
        }

        if (checklist.has("hasNewBalanceProblems")) {
            boolean hasNewBalanceProblems = checklist.getBoolean("hasNewBalanceProblems");

            cb_new_balance_problems.setSelected(hasNewBalanceProblems);
            tv_new_balance_problems.setSelected(hasNewBalanceProblems);
        }

        if (checklist.has("hasNewBladderIncontinence")) {
            boolean hasNewBladderIncontinence = checklist.getBoolean("hasNewBladderIncontinence");

            cb_new_bladder_incontinence.setSelected(hasNewBladderIncontinence);
            tv_new_bladder_incontinence.setSelected(hasNewBladderIncontinence);
        }

        if (checklist.has("hasNewBowelIncontinence")) {
            boolean hasNewBowelIncontinence = checklist.getBoolean("hasNewBowelIncontinence");

            cb_new_bowel_incontinence.setSelected(hasNewBowelIncontinence);
            tv_new_bowel_incontinence.setSelected(hasNewBowelIncontinence);
        }

        if (checklist.has("hasParalysis")) {
            boolean hasParalysis = checklist.getBoolean("hasParalysis");

            cb_paralysis.setSelected(hasParalysis);
            tv_paralysis.setSelected(hasParalysis);
        }

        if (checklist.has("hasBladderInfection")) {
            boolean hasBladderInfection = checklist.getBoolean("hasBladderInfection");

            cb_bladder_infection.setSelected(hasBladderInfection);
            tv_bladder_infection.setSelected(hasBladderInfection);
        }

        if (checklist.has("hasPneumonia")) {
            boolean hasPneumonia = checklist.getBoolean("hasPneumonia");

            cb_pneumonia.setSelected(hasPneumonia);
            tv_pneumonia.setSelected(hasPneumonia);
        }

        if (checklist.has("hasHeartAttack")) {
            boolean hasHeartAttack = checklist.getBoolean("hasHeartAttack");

            cb_heart_attack.setSelected(hasHeartAttack);
            tv_heart_attack.setSelected(hasHeartAttack);
        }

        if (checklist.has("hasStroke")) {
            boolean hasStroke = checklist.getBoolean("hasStroke");

            cb_stroke.setSelected(hasStroke);
            tv_stroke.setSelected(hasStroke);
        }

        if (checklist.has("hasBloodClot")) {
            boolean hasBloodClot = checklist.getBoolean("hasBloodClot");

            cb_blood_clot_in_leg_or_legs.setSelected(hasBloodClot);
            tv_blood_clot_in_leg_or_legs.setSelected(hasBloodClot);
        }

        if (checklist.has("hasWorsePain")) {
            boolean hasWorsePain = checklist.getBoolean("hasWorsePain");

            cb_worse_pain_after.setSelected(hasWorsePain);
            tv_worse_pain_after.setSelected(hasWorsePain);
        }

        if (checklist.has("needsSubsequentFusion")) {
            boolean needsSubsequentFusion = checklist.getBoolean("needsSubsequentFusion");

            cb_needing_a_subsequent_fusion.setSelected(needsSubsequentFusion);
            tv_needing_a_subsequent_fusion.setSelected(needsSubsequentFusion);
        }

        if (checklist.has("hasDiscHerniation")) {
            boolean hasDiscHerniation = checklist.getBoolean("hasDiscHerniation");

            cb_recurrent_or_new_disc_herniation.setSelected(hasDiscHerniation);
            tv_recurrent_or_new_disc_herniation.setSelected(hasDiscHerniation);
        }

        if (checklist.has("hasFailureFuseScan")) {
            boolean hasFailureFuseScan = checklist.getBoolean("hasFailureFuseScan");

            cb_failure_to_fuse_on_x_ray_or_ct_scan.setSelected(hasFailureFuseScan);
            tv_failure_to_fuse_on_x_ray_or_ct_scan.setSelected(hasFailureFuseScan);
        }

        if (checklist.has("hasHardwareLoosening")) {
            boolean hasHardwareLoosening = checklist.getBoolean("hasHardwareLoosening");

            cb_screw_or_hardware_loosening.setSelected(hasHardwareLoosening);
            tv_screw_or_hardware_loosening.setSelected(hasHardwareLoosening);
        }

        if (checklist.has("hasBrokenScrewRod")) {
            boolean hasBrokenScrewRod = checklist.getBoolean("hasBrokenScrewRod");

            cb_broken_screw_s_or_rod_s.setSelected(hasBrokenScrewRod);
            tv_broken_screw_s_or_rod_s.setSelected(hasBrokenScrewRod);
        }

        if (checklist.has("hasHoarseVoice")) {
            boolean hasHoarseVoice = checklist.getBoolean("hasHoarseVoice");

            cb_hoarse_voice.setSelected(hasHoarseVoice);
            tv_hoarse_voice.setSelected(hasHoarseVoice);
        }

        if (checklist.has("hasBrokenPlateScrews")) {
            boolean hasBrokenPlateScrews = checklist.getBoolean("hasBrokenPlateScrews");

            cb_broken_plate_or_screws.setSelected(hasBrokenPlateScrews);
            tv_broken_plate_or_screws.setSelected(hasBrokenPlateScrews);
        }

        if (checklist.has("hasDifficultySwallowing")) {
            boolean hasDifficultySwallowing = checklist.getBoolean("hasDifficultySwallowing");

            cb_difficulty_swallowing.setSelected(hasDifficultySwallowing);
            tv_difficulty_swallowing.setSelected(hasDifficultySwallowing);
        }


    }

    public void saveChecklist(){

        if (surgeryType.equals(SurgeryType.LumbarStenosis)) {
            checklist.put("hasNewNumbness", cb_new_numbness.isSelected());
            checklist.put("hasInfection", cb_infection.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence.isSelected());
            checklist.put("hasParalysis", cb_paralysis.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack.isSelected());
            checklist.put("hasStroke", cb_stroke.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after.isSelected());
            checklist.put("needsSubsequentFusion", cb_needing_a_subsequent_fusion.isSelected());

        } else if (surgeryType.equals(SurgeryType.LumbarDisectomy)) {

            checklist.put("hasNewNumbness", cb_new_numbness.isSelected());
            checklist.put("hasInfection", cb_infection.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence.isSelected());
            checklist.put("hasParalysis", cb_paralysis.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack.isSelected());
            checklist.put("hasStroke", cb_stroke.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after.isSelected());
            checklist.put("needsSubsequentFusion", cb_needing_a_subsequent_fusion.isSelected());
            checklist.put("hasDiscHerniation", cb_recurrent_or_new_disc_herniation.isSelected());
        } else if (surgeryType.equals(SurgeryType.LumbarFusion)) {
            checklist.put("hasNewNumbness", cb_new_numbness.isSelected());
            checklist.put("hasInfection", cb_infection.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence.isSelected());
            checklist.put("hasParalysis", cb_paralysis.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack.isSelected());
            checklist.put("hasStroke", cb_stroke.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after.isSelected());
            checklist.put("hasFailureFuseScan", cb_failure_to_fuse_on_x_ray_or_ct_scan.isSelected());
            checklist.put("hasHardwareLoosening", cb_screw_or_hardware_loosening.isSelected());
            checklist.put("hasBrokenScrewRod", cb_broken_screw_s_or_rod_s.isSelected());

        } else if (surgeryType.equals(SurgeryType.ACDF)) {
            checklist.put("hasNewNumbness", cb_new_numbness.isSelected());
            checklist.put("hasInfection", cb_infection.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence.isSelected());
            checklist.put("hasParalysis", cb_paralysis.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack.isSelected());
            checklist.put("hasStroke", cb_stroke.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after.isSelected());
            checklist.put("hasFailureFuseScan", cb_failure_to_fuse_on_x_ray_or_ct_scan.isSelected());
            checklist.put("hasBrokenPlateScrews", cb_broken_plate_or_screws.isSelected());
            checklist.put("hasHoarseVoice", cb_hoarse_voice.isSelected());
            checklist.put("hasDifficultySwallowing", cb_difficulty_swallowing.isSelected());

        }


        checklist.saveInBackground();

    }


}