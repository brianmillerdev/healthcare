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

    @BindView(R.id.cb_new_numbness_yes)
    ImageView cb_new_numbness_yes;

    @BindView(R.id.tv_new_numbness_yes)
    CustomFontTextView tv_new_numbness_yes;

    @BindView(R.id.cb_new_numbness_no)
    ImageView cb_new_numbness_no;

    @BindView(R.id.tv_new_numbness_no)
    CustomFontTextView tv_new_numbness_no;


    @BindView(R.id.cb_infection_yes)
    ImageView cb_infection_yes;

    @BindView(R.id.tv_infection_yes)
    CustomFontTextView tv_infection_yes;

    @BindView(R.id.cb_infection_no)
    ImageView cb_infection_no;

    @BindView(R.id.tv_infection_no)
    CustomFontTextView tv_infection_no;

    @BindView(R.id.tv_infection)
    CustomFontTextView tv_infection;

    @BindView(R.id.ll_infection)
    LinearLayout ll_infection;


    @BindView(R.id.cb_spinal_fluid_leak_yes)
    ImageView cb_spinal_fluid_leak_yes;

    @BindView(R.id.tv_spinal_fluid_leak_yes)
    CustomFontTextView tv_spinal_fluid_leak_yes;

    @BindView(R.id.cb_spinal_fluid_leak_no)
    ImageView cb_spinal_fluid_leak_no;

    @BindView(R.id.tv_spinal_fluid_leak_no)
    CustomFontTextView tv_spinal_fluid_leak_no;


    @BindView(R.id.cb_spinal_fluid_leak_requiring_further_surgery_yes)
    ImageView cb_spinal_fluid_leak_requiring_further_surgery_yes;

    @BindView(R.id.tv_spinal_fluid_leak_requiring_further_surgery_yes)
    CustomFontTextView tv_spinal_fluid_leak_requiring_further_surgery_yes;

    @BindView(R.id.cb_spinal_fluid_leak_requiring_further_surgery_no)
    ImageView cb_spinal_fluid_leak_requiring_further_surgery_no;

    @BindView(R.id.tv_spinal_fluid_leak_requiring_further_surgery_no)
    CustomFontTextView tv_spinal_fluid_leak_requiring_further_surgery_no;


    @BindView(R.id.cb_new_weakness_yes)
    ImageView cb_new_weakness_yes;

    @BindView(R.id.tv_new_weakness_yes)
    CustomFontTextView tv_new_weakness_yes;

    @BindView(R.id.cb_new_weakness_no)
    ImageView cb_new_weakness_no;

    @BindView(R.id.tv_new_weakness_no)
    CustomFontTextView tv_new_weakness_no;


    @BindView(R.id.cb_new_balance_problems_yes)
    ImageView cb_new_balance_problems_yes;

    @BindView(R.id.tv_new_balance_problems_yes)
    CustomFontTextView tv_new_balance_problems_yes;

    @BindView(R.id.cb_new_balance_problems_no)
    ImageView cb_new_balance_problems_no;

    @BindView(R.id.tv_new_balance_problems_no)
    CustomFontTextView tv_new_balance_problems_no;


    @BindView(R.id.cb_new_bladder_incontinence_yes)
    ImageView cb_new_bladder_incontinence_yes;

    @BindView(R.id.tv_new_bladder_incontinence_yes)
    CustomFontTextView tv_new_bladder_incontinence_yes;

    @BindView(R.id.cb_new_bladder_incontinence_no)
    ImageView cb_new_bladder_incontinence_no;

    @BindView(R.id.tv_new_bladder_incontinence_no)
    CustomFontTextView tv_new_bladder_incontinence_no;


    @BindView(R.id.cb_new_bowel_incontinence_yes)
    ImageView cb_new_bowel_incontinence_yes;

    @BindView(R.id.tv_new_bowel_incontinence_yes)
    CustomFontTextView tv_new_bowel_incontinence_yes;

    @BindView(R.id.cb_new_bowel_incontinence_no)
    ImageView cb_new_bowel_incontinence_no;

    @BindView(R.id.tv_new_bowel_incontinence_no)
    CustomFontTextView tv_new_bowel_incontinence_no;


    @BindView(R.id.cb_paralysis_yes)
    ImageView cb_paralysis_yes;

    @BindView(R.id.tv_paralysis_yes)
    CustomFontTextView tv_paralysis_yes;

    @BindView(R.id.cb_paralysis_no)
    ImageView cb_paralysis_no;

    @BindView(R.id.tv_paralysis_no)
    CustomFontTextView tv_paralysis_no;


    @BindView(R.id.cb_bladder_infection_yes)
    ImageView cb_bladder_infection_yes;

    @BindView(R.id.tv_bladder_infection_yes)
    CustomFontTextView tv_bladder_infection_yes;

    @BindView(R.id.cb_bladder_infection_no)
    ImageView cb_bladder_infection_no;

    @BindView(R.id.tv_bladder_infection_no)
    CustomFontTextView tv_bladder_infection_no;


    @BindView(R.id.cb_pneumonia_yes)
    ImageView cb_pneumonia_yes;

    @BindView(R.id.tv_pneumonia_yes)
    CustomFontTextView tv_pneumonia_yes;

    @BindView(R.id.cb_pneumonia_no)
    ImageView cb_pneumonia_no;

    @BindView(R.id.tv_pneumonia_no)
    CustomFontTextView tv_pneumonia_no;


    @BindView(R.id.cb_heart_attack_yes)
    ImageView cb_heart_attack_yes;

    @BindView(R.id.tv_heart_attack_yes)
    CustomFontTextView tv_heart_attack_yes;

    @BindView(R.id.cb_heart_attack_no)
    ImageView cb_heart_attack_no;

    @BindView(R.id.tv_heart_attack_no)
    CustomFontTextView tv_heart_attack_no;


    @BindView(R.id.cb_stroke_yes)
    ImageView cb_stroke_yes;

    @BindView(R.id.tv_stroke_yes)
    CustomFontTextView tv_stroke_yes;

    @BindView(R.id.cb_stroke_no)
    ImageView cb_stroke_no;

    @BindView(R.id.tv_stroke_no)
    CustomFontTextView tv_stroke_no;


    @BindView(R.id.cb_blood_clot_in_leg_or_legs_yes)
    ImageView cb_blood_clot_in_leg_or_legs_yes;

    @BindView(R.id.tv_blood_clot_in_leg_or_legs_yes)
    CustomFontTextView tv_blood_clot_in_leg_or_legs_yes;

    @BindView(R.id.cb_blood_clot_in_leg_or_legs_no)
    ImageView cb_blood_clot_in_leg_or_legs_no;

    @BindView(R.id.tv_blood_clot_in_leg_or_legs_no)
    CustomFontTextView tv_blood_clot_in_leg_or_legs_no;


    @BindView(R.id.cb_worse_pain_after_yes)
    ImageView cb_worse_pain_after_yes;

    @BindView(R.id.tv_worse_pain_after_yes)
    CustomFontTextView tv_worse_pain_after_yes;

    @BindView(R.id.cb_worse_pain_after_no)
    ImageView cb_worse_pain_after_no;

    @BindView(R.id.tv_worse_pain_after_no)
    CustomFontTextView tv_worse_pain_after_no;


    @BindView(R.id.cb_needing_a_subsequent_fusion_yes)
    ImageView cb_needing_a_subsequent_fusion_yes;

    @BindView(R.id.tv_needing_a_subsequent_fusion_yes)
    CustomFontTextView tv_needing_a_subsequent_fusion_yes;

    @BindView(R.id.cb_needing_a_subsequent_fusion_no)
    ImageView cb_needing_a_subsequent_fusion_no;

    @BindView(R.id.tv_needing_a_subsequent_fusion_no)
    CustomFontTextView tv_needing_a_subsequent_fusion_no;

    @BindView(R.id.tv_needing_a_subsequent_fusion)
    CustomFontTextView tv_needing_a_subsequent_fusion;

    @BindView(R.id.ll_needing_a_subsequent_fusion)
    LinearLayout ll_needing_a_subsequent_fusion;


    @BindView(R.id.cb_recurrent_or_new_disc_herniation_yes)
    ImageView cb_recurrent_or_new_disc_herniation_yes;

    @BindView(R.id.tv_recurrent_or_new_disc_herniation_yes)
    CustomFontTextView tv_recurrent_or_new_disc_herniation_yes;

    @BindView(R.id.cb_recurrent_or_new_disc_herniation_no)
    ImageView cb_recurrent_or_new_disc_herniation_no;

    @BindView(R.id.tv_recurrent_or_new_disc_herniation_no)
    CustomFontTextView tv_recurrent_or_new_disc_herniation_no;

    @BindView(R.id.tv_recurrent_or_new_disc_herniation)
    CustomFontTextView tv_recurrent_or_new_disc_herniation;

    @BindView(R.id.ll_recurrent_or_new_disc_herniation)
    LinearLayout ll_recurrent_or_new_disc_herniation;



    @BindView(R.id.cb_failure_to_fuse_on_x_ray_or_ct_scan_yes)
    ImageView cb_failure_to_fuse_on_x_ray_or_ct_scan_yes;

    @BindView(R.id.tv_failure_to_fuse_on_x_ray_or_ct_scan_yes)
    CustomFontTextView tv_failure_to_fuse_on_x_ray_or_ct_scan_yes;

    @BindView(R.id.cb_failure_to_fuse_on_x_ray_or_ct_scan_no)
    ImageView cb_failure_to_fuse_on_x_ray_or_ct_scan_no;

    @BindView(R.id.tv_failure_to_fuse_on_x_ray_or_ct_scan_no)
    CustomFontTextView tv_failure_to_fuse_on_x_ray_or_ct_scan_no;

    @BindView(R.id.tv_failure_to_fuse_on_x_ray_or_ct_scan)
    CustomFontTextView tv_failure_to_fuse_on_x_ray_or_ct_scan;

    @BindView(R.id.ll_failure_to_fuse_on_x_ray_or_ct_scan)
    LinearLayout ll_failure_to_fuse_on_x_ray_or_ct_scan;



    @BindView(R.id.cb_screw_or_hardware_loosening_yes)
    ImageView cb_screw_or_hardware_loosening_yes;

    @BindView(R.id.tv_screw_or_hardware_loosening_yes)
    CustomFontTextView tv_screw_or_hardware_loosening_yes;

    @BindView(R.id.cb_screw_or_hardware_loosening_no)
    ImageView cb_screw_or_hardware_loosening_no;

    @BindView(R.id.tv_screw_or_hardware_loosening_no)
    CustomFontTextView tv_screw_or_hardware_loosening_no;

    @BindView(R.id.tv_screw_or_hardware_loosening)
    CustomFontTextView tv_screw_or_hardware_loosening;

    @BindView(R.id.ll_screw_or_hardware_loosening)
    LinearLayout ll_screw_or_hardware_loosening;



    @BindView(R.id.cb_broken_screw_s_or_rod_s_yes)
    ImageView cb_broken_screw_s_or_rod_s_yes;

    @BindView(R.id.tv_broken_screw_s_or_rod_s_yes)
    CustomFontTextView tv_broken_screw_s_or_rod_s_yes;

    @BindView(R.id.cb_broken_screw_s_or_rod_s_no)
    ImageView cb_broken_screw_s_or_rod_s_no;

    @BindView(R.id.tv_broken_screw_s_or_rod_s_no)
    CustomFontTextView tv_broken_screw_s_or_rod_s_no;

    @BindView(R.id.tv_broken_screw_s_or_rod_s)
    CustomFontTextView tv_broken_screw_s_or_rod_s;

    @BindView(R.id.ll_broken_screw_s_or_rod_s)
    LinearLayout ll_broken_screw_s_or_rod_s;



    @BindView(R.id.cb_hoarse_voice_yes)
    ImageView cb_hoarse_voice_yes;

    @BindView(R.id.tv_hoarse_voice_yes)
    CustomFontTextView tv_hoarse_voice_yes;

    @BindView(R.id.cb_hoarse_voice_no)
    ImageView cb_hoarse_voice_no;

    @BindView(R.id.tv_hoarse_voice_no)
    CustomFontTextView tv_hoarse_voice_no;

    @BindView(R.id.tv_hoarse_voice)
    CustomFontTextView tv_hoarse_voice;

    @BindView(R.id.ll_hoarse_voice)
    LinearLayout ll_hoarse_voice;



    @BindView(R.id.cb_broken_plate_or_screws_yes)
    ImageView cb_broken_plate_or_screws_yes;

    @BindView(R.id.tv_broken_plate_or_screws_yes)
    CustomFontTextView tv_broken_plate_or_screws_yes;

    @BindView(R.id.cb_broken_plate_or_screws_no)
    ImageView cb_broken_plate_or_screws_no;

    @BindView(R.id.tv_broken_plate_or_screws_no)
    CustomFontTextView tv_broken_plate_or_screws_no;

    @BindView(R.id.tv_broken_plate_or_screws)
    CustomFontTextView tv_broken_plate_or_screws;

    @BindView(R.id.ll_broken_plate_or_screws)
    LinearLayout ll_broken_plate_or_screws;



    @BindView(R.id.cb_difficulty_swallowing_yes)
    ImageView cb_difficulty_swallowing_yes;

    @BindView(R.id.tv_difficulty_swallowing_yes)
    CustomFontTextView tv_difficulty_swallowing_yes;

    @BindView(R.id.cb_difficulty_swallowing_no)
    ImageView cb_difficulty_swallowing_no;

    @BindView(R.id.tv_difficulty_swallowing_no)
    CustomFontTextView tv_difficulty_swallowing_no;

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
        cb_new_numbness_yes.setSelected(false);
        cb_new_numbness_no.setSelected(false);

        cb_infection_yes.setSelected(false);
        cb_infection_no.setSelected(false);

        cb_spinal_fluid_leak_yes.setSelected(false);
        cb_spinal_fluid_leak_no.setSelected(false);

        cb_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(false);
        cb_spinal_fluid_leak_requiring_further_surgery_no.setSelected(false);

        cb_new_weakness_yes.setSelected(false);
        cb_new_weakness_no.setSelected(false);

        cb_new_balance_problems_yes.setSelected(false);
        cb_new_balance_problems_no.setSelected(false);

        cb_new_bladder_incontinence_yes.setSelected(false);
        cb_new_bladder_incontinence_no.setSelected(false);

        cb_new_bowel_incontinence_yes.setSelected(false);
        cb_new_bowel_incontinence_no.setSelected(false);

        cb_paralysis_yes.setSelected(false);
        cb_paralysis_no.setSelected(false);

        cb_bladder_infection_yes.setSelected(false);
        cb_bladder_infection_no.setSelected(false);

        cb_pneumonia_yes.setSelected(false);
        cb_pneumonia_no.setSelected(false);

        cb_heart_attack_yes.setSelected(false);
        cb_heart_attack_no.setSelected(false);

        cb_stroke_yes.setSelected(false);
        cb_stroke_no.setSelected(false);

        cb_blood_clot_in_leg_or_legs_yes.setSelected(false);
        cb_blood_clot_in_leg_or_legs_no.setSelected(false);

        cb_worse_pain_after_yes.setSelected(false);
        cb_worse_pain_after_no.setSelected(false);

        cb_needing_a_subsequent_fusion_yes.setSelected(false);
        cb_needing_a_subsequent_fusion_no.setSelected(false);

        cb_recurrent_or_new_disc_herniation_yes.setSelected(false);
        cb_recurrent_or_new_disc_herniation_no.setSelected(false);

        cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(false);
        cb_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(false);

        cb_screw_or_hardware_loosening_yes.setSelected(false);
        cb_screw_or_hardware_loosening_no.setSelected(false);

        cb_broken_screw_s_or_rod_s_yes.setSelected(false);
        cb_broken_screw_s_or_rod_s_no.setSelected(false);

        cb_hoarse_voice_yes.setSelected(false);
        cb_hoarse_voice_no.setSelected(false);

        cb_broken_plate_or_screws_yes.setSelected(false);
        cb_broken_plate_or_screws_no.setSelected(false);

        cb_difficulty_swallowing_yes.setSelected(false);
        cb_difficulty_swallowing_no.setSelected(false);


        fetchSurveys();

        cb_new_numbness_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_numbness_yes.setSelected(true);
                tv_new_numbness_yes.setSelected(true);
                cb_new_numbness_no.setSelected(false);
                tv_new_numbness_no.setSelected(false);
            }
        });

        cb_new_numbness_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_numbness_yes.setSelected(false);
                tv_new_numbness_yes.setSelected(false);
                cb_new_numbness_no.setSelected(true);
                tv_new_numbness_no.setSelected(true);
            }
        });

        cb_infection_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_infection_yes.setSelected(true);
                tv_infection_yes.setSelected(true);
                cb_infection_no.setSelected(false);
                tv_infection_no.setSelected(false);
            }
        });

        cb_infection_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_infection_yes.setSelected(false);
                tv_infection_yes.setSelected(false);
                cb_infection_no.setSelected(true);
                tv_infection_no.setSelected(true);
            }
        });


        cb_spinal_fluid_leak_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_spinal_fluid_leak_yes.setSelected(true);
                tv_spinal_fluid_leak_yes.setSelected(true);
                cb_spinal_fluid_leak_no.setSelected(false);
                tv_spinal_fluid_leak_no.setSelected(false);
            }
        });

        cb_spinal_fluid_leak_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_spinal_fluid_leak_yes.setSelected(false);
                tv_spinal_fluid_leak_yes.setSelected(false);
                cb_spinal_fluid_leak_no.setSelected(true);
                tv_spinal_fluid_leak_no.setSelected(true);
            }
        });


        cb_spinal_fluid_leak_requiring_further_surgery_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(true);
                tv_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(true);
                cb_spinal_fluid_leak_requiring_further_surgery_no.setSelected(false);
                tv_spinal_fluid_leak_requiring_further_surgery_no.setSelected(false);
            }
        });

        cb_spinal_fluid_leak_requiring_further_surgery_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(false);
                tv_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(false);
                cb_spinal_fluid_leak_requiring_further_surgery_no.setSelected(true);
                tv_spinal_fluid_leak_requiring_further_surgery_no.setSelected(true);
            }
        });


        cb_new_weakness_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_weakness_yes.setSelected(true);
                tv_new_weakness_yes.setSelected(true);
                cb_new_weakness_no.setSelected(false);
                tv_new_weakness_no.setSelected(false);
            }
        });

        cb_new_weakness_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_weakness_yes.setSelected(false);
                tv_new_weakness_yes.setSelected(false);
                cb_new_weakness_no.setSelected(true);
                tv_new_weakness_no.setSelected(true);
            }
        });


        cb_new_balance_problems_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_balance_problems_yes.setSelected(true);
                tv_new_balance_problems_yes.setSelected(true);
                cb_new_balance_problems_no.setSelected(false);
                tv_new_balance_problems_no.setSelected(false);
            }
        });

        cb_new_balance_problems_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_balance_problems_yes.setSelected(false);
                tv_new_balance_problems_yes.setSelected(false);
                cb_new_balance_problems_no.setSelected(true);
                tv_new_balance_problems_no.setSelected(true);
            }
        });


        cb_new_bladder_incontinence_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_bladder_incontinence_yes.setSelected(true);
                tv_new_bladder_incontinence_yes.setSelected(true);
                cb_new_bladder_incontinence_no.setSelected(false);
                tv_new_bladder_incontinence_no.setSelected(false);
            }
        });

        cb_new_bladder_incontinence_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_bladder_incontinence_yes.setSelected(false);
                tv_new_bladder_incontinence_yes.setSelected(false);
                cb_new_bladder_incontinence_no.setSelected(true);
                tv_new_bladder_incontinence_no.setSelected(true);
            }
        });


        cb_new_bowel_incontinence_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_bowel_incontinence_yes.setSelected(true);
                tv_new_bowel_incontinence_yes.setSelected(true);
                cb_new_bowel_incontinence_no.setSelected(false);
                tv_new_bowel_incontinence_no.setSelected(false);
            }
        });

        cb_new_bowel_incontinence_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_new_bowel_incontinence_yes.setSelected(false);
                tv_new_bowel_incontinence_yes.setSelected(false);
                cb_new_bowel_incontinence_no.setSelected(true);
                tv_new_bowel_incontinence_no.setSelected(true);
            }
        });


        cb_paralysis_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_paralysis_yes.setSelected(true);
                tv_paralysis_yes.setSelected(true);
                cb_paralysis_no.setSelected(false);
                tv_paralysis_no.setSelected(false);
            }
        });

        cb_paralysis_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_paralysis_yes.setSelected(false);
                tv_paralysis_yes.setSelected(false);
                cb_paralysis_no.setSelected(true);
                tv_paralysis_no.setSelected(true);
            }
        });


        cb_bladder_infection_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_bladder_infection_yes.setSelected(true);
                tv_bladder_infection_yes.setSelected(true);
                cb_bladder_infection_no.setSelected(false);
                tv_bladder_infection_no.setSelected(false);
            }
        });

        cb_bladder_infection_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_bladder_infection_yes.setSelected(false);
                tv_bladder_infection_yes.setSelected(false);
                cb_bladder_infection_no.setSelected(true);
                tv_bladder_infection_no.setSelected(true);
            }
        });


        cb_pneumonia_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_pneumonia_yes.setSelected(true);
                tv_pneumonia_yes.setSelected(true);
                cb_pneumonia_no.setSelected(false);
                tv_pneumonia_no.setSelected(false);
            }
        });

        cb_pneumonia_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_pneumonia_yes.setSelected(false);
                tv_pneumonia_yes.setSelected(false);
                cb_pneumonia_no.setSelected(true);
                tv_pneumonia_no.setSelected(true);
            }
        });


        cb_heart_attack_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_heart_attack_yes.setSelected(true);
                tv_heart_attack_yes.setSelected(true);
                cb_heart_attack_no.setSelected(false);
                tv_heart_attack_no.setSelected(false);
            }
        });

        cb_heart_attack_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_heart_attack_yes.setSelected(false);
                tv_heart_attack_yes.setSelected(false);
                cb_heart_attack_no.setSelected(true);
                tv_heart_attack_no.setSelected(true);
            }
        });


        cb_stroke_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_stroke_yes.setSelected(true);
                tv_stroke_yes.setSelected(true);
                cb_stroke_no.setSelected(false);
                tv_stroke_no.setSelected(false);
            }
        });

        cb_stroke_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_stroke_yes.setSelected(false);
                tv_stroke_yes.setSelected(false);
                cb_stroke_no.setSelected(true);
                tv_stroke_no.setSelected(true);
            }
        });


        cb_blood_clot_in_leg_or_legs_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_blood_clot_in_leg_or_legs_yes.setSelected(true);
                tv_blood_clot_in_leg_or_legs_yes.setSelected(true);
                cb_blood_clot_in_leg_or_legs_no.setSelected(false);
                tv_blood_clot_in_leg_or_legs_no.setSelected(false);
            }
        });

        cb_blood_clot_in_leg_or_legs_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_blood_clot_in_leg_or_legs_yes.setSelected(false);
                tv_blood_clot_in_leg_or_legs_yes.setSelected(false);
                cb_blood_clot_in_leg_or_legs_no.setSelected(true);
                tv_blood_clot_in_leg_or_legs_no.setSelected(true);
            }
        });


        cb_worse_pain_after_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_worse_pain_after_yes.setSelected(true);
                tv_worse_pain_after_yes.setSelected(true);
                cb_worse_pain_after_no.setSelected(false);
                tv_worse_pain_after_no.setSelected(false);
            }
        });

        cb_worse_pain_after_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_worse_pain_after_yes.setSelected(false);
                tv_worse_pain_after_yes.setSelected(false);
                cb_worse_pain_after_no.setSelected(true);
                tv_worse_pain_after_no.setSelected(true);
            }
        });


        cb_needing_a_subsequent_fusion_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_needing_a_subsequent_fusion_yes.setSelected(true);
                tv_needing_a_subsequent_fusion_yes.setSelected(true);
                cb_needing_a_subsequent_fusion_no.setSelected(false);
                tv_needing_a_subsequent_fusion_no.setSelected(false);
            }
        });

        cb_needing_a_subsequent_fusion_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_needing_a_subsequent_fusion_yes.setSelected(false);
                tv_needing_a_subsequent_fusion_yes.setSelected(false);
                cb_needing_a_subsequent_fusion_no.setSelected(true);
                tv_needing_a_subsequent_fusion_no.setSelected(true);
            }
        });


        cb_recurrent_or_new_disc_herniation_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_recurrent_or_new_disc_herniation_yes.setSelected(true);
                tv_recurrent_or_new_disc_herniation_yes.setSelected(true);
                cb_recurrent_or_new_disc_herniation_no.setSelected(false);
                tv_recurrent_or_new_disc_herniation_no.setSelected(false);
            }
        });

        cb_recurrent_or_new_disc_herniation_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_recurrent_or_new_disc_herniation_yes.setSelected(false);
                tv_recurrent_or_new_disc_herniation_yes.setSelected(false);
                cb_recurrent_or_new_disc_herniation_no.setSelected(true);
                tv_recurrent_or_new_disc_herniation_no.setSelected(true);
            }
        });


        cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(true);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(true);
                cb_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(false);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(false);
            }
        });

        cb_failure_to_fuse_on_x_ray_or_ct_scan_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(false);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(false);
                cb_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(true);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(true);
            }
        });


        cb_screw_or_hardware_loosening_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_screw_or_hardware_loosening_yes.setSelected(true);
                tv_screw_or_hardware_loosening_yes.setSelected(true);
                cb_screw_or_hardware_loosening_no.setSelected(false);
                tv_screw_or_hardware_loosening_no.setSelected(false);
            }
        });

        cb_screw_or_hardware_loosening_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_screw_or_hardware_loosening_yes.setSelected(false);
                tv_screw_or_hardware_loosening_yes.setSelected(false);
                cb_screw_or_hardware_loosening_no.setSelected(true);
                tv_screw_or_hardware_loosening_no.setSelected(true);
            }
        });


        cb_broken_screw_s_or_rod_s_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_broken_screw_s_or_rod_s_yes.setSelected(true);
                tv_broken_screw_s_or_rod_s_yes.setSelected(true);
                cb_broken_screw_s_or_rod_s_no.setSelected(false);
                tv_broken_screw_s_or_rod_s_no.setSelected(false);
            }
        });

        cb_broken_screw_s_or_rod_s_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_broken_screw_s_or_rod_s_yes.setSelected(false);
                tv_broken_screw_s_or_rod_s_yes.setSelected(false);
                cb_broken_screw_s_or_rod_s_no.setSelected(true);
                tv_broken_screw_s_or_rod_s_no.setSelected(true);
            }
        });


        cb_hoarse_voice_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_hoarse_voice_yes.setSelected(true);
                tv_hoarse_voice_yes.setSelected(true);
                cb_hoarse_voice_no.setSelected(false);
                tv_hoarse_voice_no.setSelected(false);
            }
        });

        cb_hoarse_voice_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_hoarse_voice_yes.setSelected(false);
                tv_hoarse_voice_yes.setSelected(false);
                cb_hoarse_voice_no.setSelected(true);
                tv_hoarse_voice_no.setSelected(true);
            }
        });


        cb_broken_plate_or_screws_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_broken_plate_or_screws_yes.setSelected(true);
                tv_broken_plate_or_screws_yes.setSelected(true);
                cb_broken_plate_or_screws_no.setSelected(false);
                tv_broken_plate_or_screws_no.setSelected(false);
            }
        });

        cb_broken_plate_or_screws_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_broken_plate_or_screws_yes.setSelected(false);
                tv_broken_plate_or_screws_yes.setSelected(false);
                cb_broken_plate_or_screws_no.setSelected(true);
                tv_broken_plate_or_screws_no.setSelected(true);
            }
        });


        cb_difficulty_swallowing_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_difficulty_swallowing_yes.setSelected(true);
                tv_difficulty_swallowing_yes.setSelected(true);
                cb_difficulty_swallowing_no.setSelected(false);
                tv_difficulty_swallowing_no.setSelected(false);
            }
        });

        cb_difficulty_swallowing_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_difficulty_swallowing_yes.setSelected(false);
                tv_difficulty_swallowing_yes.setSelected(false);
                cb_difficulty_swallowing_no.setSelected(true);
                tv_difficulty_swallowing_no.setSelected(true);
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

            if (hasNewNumbness) {
                cb_new_numbness_yes.setSelected(true);
                tv_new_numbness_yes.setSelected(true);
                cb_new_numbness_no.setSelected(false);
                tv_new_numbness_no.setSelected(false);
            }else{
                cb_new_numbness_yes.setSelected(false);
                tv_new_numbness_yes.setSelected(false);
                cb_new_numbness_no.setSelected(true);
                tv_new_numbness_no.setSelected(true);
            }
        }

        if (checklist.has("hasInfection")) {
            boolean hasInfection = checklist.getBoolean("hasInfection");

            if (hasInfection) {
                cb_infection_yes.setSelected(true);
                tv_infection_yes.setSelected(true);
                cb_infection_no.setSelected(false);
                tv_infection_no.setSelected(false);
            }else{
                cb_infection_yes.setSelected(false);
                tv_infection_yes.setSelected(false);
                cb_infection_no.setSelected(true);
                tv_infection_no.setSelected(true);
            }
        }

        if (checklist.has("hasSpinalFluidLeak")) {
            boolean hasSpinalFluidLeak = checklist.getBoolean("hasSpinalFluidLeak");

            if (hasSpinalFluidLeak) {
                cb_spinal_fluid_leak_yes.setSelected(true);
                tv_spinal_fluid_leak_yes.setSelected(true);
                cb_spinal_fluid_leak_no.setSelected(false);
                tv_spinal_fluid_leak_no.setSelected(false);
            }else{
                cb_spinal_fluid_leak_yes.setSelected(false);
                tv_spinal_fluid_leak_yes.setSelected(false);
                cb_spinal_fluid_leak_no.setSelected(true);
                tv_spinal_fluid_leak_no.setSelected(true);
            }
        }

        if (checklist.has("hasSpinalFluidLeakRequiringSurgery")) {
            boolean hasSpinalFluidLeakRequiringSurgery = checklist.getBoolean("hasSpinalFluidLeakRequiringSurgery");

            if (hasSpinalFluidLeakRequiringSurgery) {
                cb_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(true);
                tv_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(true);
                cb_spinal_fluid_leak_requiring_further_surgery_no.setSelected(false);
                tv_spinal_fluid_leak_requiring_further_surgery_no.setSelected(false);
            }else{
                cb_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(false);
                tv_spinal_fluid_leak_requiring_further_surgery_yes.setSelected(false);
                cb_spinal_fluid_leak_requiring_further_surgery_no.setSelected(true);
                tv_spinal_fluid_leak_requiring_further_surgery_no.setSelected(true);
            }
        }

        if (checklist.has("hasNewWeakness")) {
            boolean hasNewWeakness = checklist.getBoolean("hasNewWeakness");

            if (hasNewWeakness) {
                cb_new_weakness_yes.setSelected(true);
                tv_new_weakness_yes.setSelected(true);
                cb_new_weakness_no.setSelected(false);
                tv_new_weakness_no.setSelected(false);
            }else{
                cb_new_weakness_yes.setSelected(false);
                tv_new_weakness_yes.setSelected(false);
                cb_new_weakness_no.setSelected(true);
                tv_new_weakness_no.setSelected(true);
            }
        }

        if (checklist.has("hasNewBalanceProblems")) {
            boolean hasNewBalanceProblems = checklist.getBoolean("hasNewBalanceProblems");

            if (hasNewBalanceProblems) {
                cb_new_balance_problems_yes.setSelected(true);
                tv_new_balance_problems_yes.setSelected(true);
                cb_new_balance_problems_no.setSelected(false);
                tv_new_balance_problems_no.setSelected(false);
            }else{
                cb_new_balance_problems_yes.setSelected(false);
                tv_new_balance_problems_yes.setSelected(false);
                cb_new_balance_problems_no.setSelected(true);
                tv_new_balance_problems_no.setSelected(true);
            }
        }

        if (checklist.has("hasNewBladderIncontinence")) {
            boolean hasNewBladderIncontinence = checklist.getBoolean("hasNewBladderIncontinence");

            if (hasNewBladderIncontinence) {
                cb_new_bladder_incontinence_yes.setSelected(true);
                tv_new_bladder_incontinence_yes.setSelected(true);
                cb_new_bladder_incontinence_no.setSelected(false);
                tv_new_bladder_incontinence_no.setSelected(false);
            }else{
                cb_new_bladder_incontinence_yes.setSelected(false);
                tv_new_bladder_incontinence_yes.setSelected(false);
                cb_new_bladder_incontinence_no.setSelected(true);
                tv_new_bladder_incontinence_no.setSelected(true);
            }
        }

        if (checklist.has("hasNewBowelIncontinence")) {
            boolean hasNewBowelIncontinence = checklist.getBoolean("hasNewBowelIncontinence");

            if (hasNewBowelIncontinence) {
                cb_new_bowel_incontinence_yes.setSelected(true);
                tv_new_bowel_incontinence_yes.setSelected(true);
                cb_new_bowel_incontinence_no.setSelected(false);
                tv_new_bowel_incontinence_no.setSelected(false);
            }else{
                cb_new_bowel_incontinence_yes.setSelected(false);
                tv_new_bowel_incontinence_yes.setSelected(false);
                cb_new_bowel_incontinence_no.setSelected(true);
                tv_new_bowel_incontinence_no.setSelected(true);
            }
        }

        if (checklist.has("hasParalysis")) {
            boolean hasParalysis = checklist.getBoolean("hasParalysis");

            if (hasParalysis) {
                cb_paralysis_yes.setSelected(true);
                tv_paralysis_yes.setSelected(true);
                cb_paralysis_no.setSelected(false);
                tv_paralysis_no.setSelected(false);
            }else{
                cb_paralysis_yes.setSelected(false);
                tv_paralysis_yes.setSelected(false);
                cb_paralysis_no.setSelected(true);
                tv_paralysis_no.setSelected(true);
            }
        }

        if (checklist.has("hasBladderInfection")) {
            boolean hasBladderInfection = checklist.getBoolean("hasBladderInfection");

            if (hasBladderInfection) {
                cb_bladder_infection_yes.setSelected(true);
                tv_bladder_infection_yes.setSelected(true);
                cb_bladder_infection_no.setSelected(false);
                tv_bladder_infection_no.setSelected(false);
            }else{
                cb_bladder_infection_yes.setSelected(false);
                tv_bladder_infection_yes.setSelected(false);
                cb_bladder_infection_no.setSelected(true);
                tv_bladder_infection_no.setSelected(true);
            }
        }

        if (checklist.has("hasPneumonia")) {
            boolean hasPneumonia = checklist.getBoolean("hasPneumonia");

            if (hasPneumonia) {
                cb_pneumonia_yes.setSelected(true);
                tv_pneumonia_yes.setSelected(true);
                cb_pneumonia_no.setSelected(false);
                tv_pneumonia_no.setSelected(false);
            }else{
                cb_pneumonia_yes.setSelected(false);
                tv_pneumonia_yes.setSelected(false);
                cb_pneumonia_no.setSelected(true);
                tv_pneumonia_no.setSelected(true);
            }
        }

        if (checklist.has("hasHeartAttack")) {
            boolean hasHeartAttack = checklist.getBoolean("hasHeartAttack");

            if (hasHeartAttack) {
                cb_heart_attack_yes.setSelected(true);
                tv_heart_attack_yes.setSelected(true);
                cb_heart_attack_no.setSelected(false);
                tv_heart_attack_no.setSelected(false);
            }else{
                cb_heart_attack_yes.setSelected(false);
                tv_heart_attack_yes.setSelected(false);
                cb_heart_attack_no.setSelected(true);
                tv_heart_attack_no.setSelected(true);
            }
        }

        if (checklist.has("hasStroke")) {
            boolean hasStroke = checklist.getBoolean("hasStroke");

            if (hasStroke) {
                cb_stroke_yes.setSelected(true);
                tv_stroke_yes.setSelected(true);
                cb_stroke_no.setSelected(false);
                tv_stroke_no.setSelected(false);
            }else{
                cb_stroke_yes.setSelected(false);
                tv_stroke_yes.setSelected(false);
                cb_stroke_no.setSelected(true);
                tv_stroke_no.setSelected(true);
            }
        }

        if (checklist.has("hasBloodClot")) {
            boolean hasBloodClot = checklist.getBoolean("hasBloodClot");

            if (hasBloodClot) {
                cb_blood_clot_in_leg_or_legs_yes.setSelected(true);
                tv_blood_clot_in_leg_or_legs_yes.setSelected(true);
                cb_blood_clot_in_leg_or_legs_no.setSelected(false);
                tv_blood_clot_in_leg_or_legs_no.setSelected(false);
            }else{
                cb_blood_clot_in_leg_or_legs_yes.setSelected(false);
                tv_blood_clot_in_leg_or_legs_yes.setSelected(false);
                cb_blood_clot_in_leg_or_legs_no.setSelected(true);
                tv_blood_clot_in_leg_or_legs_no.setSelected(true);
            }
        }

        if (checklist.has("hasWorsePain")) {
            boolean hasWorsePain = checklist.getBoolean("hasWorsePain");

            if (hasWorsePain) {
                cb_worse_pain_after_yes.setSelected(true);
                tv_worse_pain_after_yes.setSelected(true);
                cb_worse_pain_after_no.setSelected(false);
                tv_worse_pain_after_no.setSelected(false);
            }else{
                cb_worse_pain_after_yes.setSelected(false);
                tv_worse_pain_after_yes.setSelected(false);
                cb_worse_pain_after_no.setSelected(true);
                tv_worse_pain_after_no.setSelected(true);
            }
        }

        if (checklist.has("needsSubsequentFusion")) {
            boolean needsSubsequentFusion = checklist.getBoolean("needsSubsequentFusion");

            if (needsSubsequentFusion) {
                cb_needing_a_subsequent_fusion_yes.setSelected(true);
                tv_needing_a_subsequent_fusion_yes.setSelected(true);
                cb_needing_a_subsequent_fusion_no.setSelected(false);
                tv_needing_a_subsequent_fusion_no.setSelected(false);
            }else{
                cb_needing_a_subsequent_fusion_yes.setSelected(false);
                tv_needing_a_subsequent_fusion_yes.setSelected(false);
                cb_needing_a_subsequent_fusion_no.setSelected(true);
                tv_needing_a_subsequent_fusion_no.setSelected(true);
            }
        }

        if (checklist.has("hasDiscHerniation")) {
            boolean hasDiscHerniation = checklist.getBoolean("hasDiscHerniation");

            if (hasDiscHerniation) {
                cb_recurrent_or_new_disc_herniation_yes.setSelected(true);
                tv_recurrent_or_new_disc_herniation_yes.setSelected(true);
                cb_recurrent_or_new_disc_herniation_no.setSelected(false);
                tv_recurrent_or_new_disc_herniation_no.setSelected(false);
            }else{
                cb_recurrent_or_new_disc_herniation_yes.setSelected(false);
                tv_recurrent_or_new_disc_herniation_yes.setSelected(false);
                cb_recurrent_or_new_disc_herniation_no.setSelected(true);
                tv_recurrent_or_new_disc_herniation_no.setSelected(true);
            }
        }

        if (checklist.has("hasFailureFuseScan")) {
            boolean hasFailureFuseScan = checklist.getBoolean("hasFailureFuseScan");

            if (hasFailureFuseScan) {
                cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(true);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(true);
                cb_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(false);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(false);
            }else{
                cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(false);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_yes.setSelected(false);
                cb_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(true);
                tv_failure_to_fuse_on_x_ray_or_ct_scan_no.setSelected(true);
            }
        }

        if (checklist.has("hasHardwareLoosening")) {
            boolean hasHardwareLoosening = checklist.getBoolean("hasHardwareLoosening");

            if (hasHardwareLoosening) {
                cb_screw_or_hardware_loosening_yes.setSelected(true);
                tv_screw_or_hardware_loosening_yes.setSelected(true);
                cb_screw_or_hardware_loosening_no.setSelected(false);
                tv_screw_or_hardware_loosening_no.setSelected(false);
            }else{
                cb_screw_or_hardware_loosening_yes.setSelected(false);
                tv_screw_or_hardware_loosening_yes.setSelected(false);
                cb_screw_or_hardware_loosening_no.setSelected(true);
                tv_screw_or_hardware_loosening_no.setSelected(true);
            }
        }

        if (checklist.has("hasBrokenScrewRod")) {
            boolean hasBrokenScrewRod = checklist.getBoolean("hasBrokenScrewRod");

            if (hasBrokenScrewRod) {
                cb_broken_screw_s_or_rod_s_yes.setSelected(true);
                tv_broken_screw_s_or_rod_s_yes.setSelected(true);
                cb_broken_screw_s_or_rod_s_no.setSelected(false);
                tv_broken_screw_s_or_rod_s_no.setSelected(false);
            }else{
                cb_broken_screw_s_or_rod_s_yes.setSelected(false);
                tv_broken_screw_s_or_rod_s_yes.setSelected(false);
                cb_broken_screw_s_or_rod_s_no.setSelected(true);
                tv_broken_screw_s_or_rod_s_no.setSelected(true);
            }
        }

        if (checklist.has("hasHoarseVoice")) {
            boolean hasHoarseVoice = checklist.getBoolean("hasHoarseVoice");

            if (hasHoarseVoice) {
                cb_hoarse_voice_yes.setSelected(true);
                tv_hoarse_voice_yes.setSelected(true);
                cb_hoarse_voice_no.setSelected(false);
                tv_hoarse_voice_no.setSelected(false);
            }else{
                cb_hoarse_voice_yes.setSelected(false);
                tv_hoarse_voice_yes.setSelected(false);
                cb_hoarse_voice_no.setSelected(true);
                tv_hoarse_voice_no.setSelected(true);
            }
        }

        if (checklist.has("hasBrokenPlateScrews")) {
            boolean hasBrokenPlateScrews = checklist.getBoolean("hasBrokenPlateScrews");

            if (hasBrokenPlateScrews) {
                cb_broken_plate_or_screws_yes.setSelected(true);
                tv_broken_plate_or_screws_yes.setSelected(true);
                cb_broken_plate_or_screws_no.setSelected(false);
                tv_broken_plate_or_screws_no.setSelected(false);
            }else{
                cb_broken_plate_or_screws_yes.setSelected(false);
                tv_broken_plate_or_screws_yes.setSelected(false);
                cb_broken_plate_or_screws_no.setSelected(true);
                tv_broken_plate_or_screws_no.setSelected(true);
            }
        }

        if (checklist.has("hasDifficultySwallowing")) {
            boolean hasDifficultySwallowing = checklist.getBoolean("hasDifficultySwallowing");

            if (hasDifficultySwallowing) {
                cb_difficulty_swallowing_yes.setSelected(true);
                tv_difficulty_swallowing_yes.setSelected(true);
                cb_difficulty_swallowing_no.setSelected(false);
                tv_difficulty_swallowing_no.setSelected(false);
            }else{
                cb_difficulty_swallowing_yes.setSelected(false);
                tv_difficulty_swallowing_yes.setSelected(false);
                cb_difficulty_swallowing_no.setSelected(true);
                tv_difficulty_swallowing_no.setSelected(true);
            }
        }


    }

    public void saveChecklist(){

        if (surgeryType.equals(SurgeryType.LumbarStenosis)) {
            checklist.put("hasNewNumbness", cb_new_numbness_yes.isSelected());
            checklist.put("hasInfection", cb_infection_yes.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak_yes.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery_yes.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness_yes.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems_yes.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence_yes.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence_yes.isSelected());
            checklist.put("hasParalysis", cb_paralysis_yes.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection_yes.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia_yes.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack_yes.isSelected());
            checklist.put("hasStroke", cb_stroke_yes.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs_yes.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after_yes.isSelected());
            checklist.put("needsSubsequentFusion", cb_needing_a_subsequent_fusion_yes.isSelected());

        } else if (surgeryType.equals(SurgeryType.LumbarDisectomy)) {

            checklist.put("hasNewNumbness", cb_new_numbness_yes.isSelected());
            checklist.put("hasInfection", cb_infection_yes.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak_yes.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery_yes.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness_yes.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems_yes.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence_yes.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence_yes.isSelected());
            checklist.put("hasParalysis", cb_paralysis_yes.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection_yes.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia_yes.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack_yes.isSelected());
            checklist.put("hasStroke", cb_stroke_yes.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs_yes.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after_yes.isSelected());
            checklist.put("needsSubsequentFusion", cb_needing_a_subsequent_fusion_yes.isSelected());
            checklist.put("hasDiscHerniation", cb_recurrent_or_new_disc_herniation_yes.isSelected());
        } else if (surgeryType.equals(SurgeryType.LumbarFusion)) {
            checklist.put("hasNewNumbness", cb_new_numbness_yes.isSelected());
            checklist.put("hasInfection", cb_infection_yes.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak_yes.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery_yes.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness_yes.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems_yes.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence_yes.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence_yes.isSelected());
            checklist.put("hasParalysis", cb_paralysis_yes.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection_yes.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia_yes.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack_yes.isSelected());
            checklist.put("hasStroke", cb_stroke_yes.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs_yes.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after_yes.isSelected());
            checklist.put("hasFailureFuseScan", cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.isSelected());
            checklist.put("hasHardwareLoosening", cb_screw_or_hardware_loosening_yes.isSelected());
            checklist.put("hasBrokenScrewRod", cb_broken_screw_s_or_rod_s_yes.isSelected());

        } else if (surgeryType.equals(SurgeryType.ACDF)) {
            checklist.put("hasNewNumbness", cb_new_numbness_yes.isSelected());
            checklist.put("hasInfection", cb_infection_yes.isSelected());
            checklist.put("hasSpinalFluidLeak", cb_spinal_fluid_leak_yes.isSelected());
            checklist.put("hasSpinalFluidLeakRequiringSurgery", cb_spinal_fluid_leak_requiring_further_surgery_yes.isSelected());
            checklist.put("hasNewWeakness", cb_new_weakness_yes.isSelected());
            checklist.put("hasNewBalanceProblems", cb_new_balance_problems_yes.isSelected());
            checklist.put("hasNewBladderIncontinence", cb_new_bladder_incontinence_yes.isSelected());
            checklist.put("hasNewBowelIncontinence", cb_new_bowel_incontinence_yes.isSelected());
            checklist.put("hasParalysis", cb_paralysis_yes.isSelected());
            checklist.put("hasBladderInfection", cb_bladder_infection_yes.isSelected());
            checklist.put("hasPneumonia", cb_pneumonia_yes.isSelected());
            checklist.put("hasHeartAttack", cb_heart_attack_yes.isSelected());
            checklist.put("hasStroke", cb_stroke_yes.isSelected());
            checklist.put("hasBloodClot", cb_blood_clot_in_leg_or_legs_yes.isSelected());
            checklist.put("hasWorsePain", cb_worse_pain_after_yes.isSelected());
            checklist.put("hasFailureFuseScan", cb_failure_to_fuse_on_x_ray_or_ct_scan_yes.isSelected());
            checklist.put("hasBrokenPlateScrews", cb_broken_plate_or_screws_yes.isSelected());
            checklist.put("hasHoarseVoice", cb_hoarse_voice_yes.isSelected());
            checklist.put("hasDifficultySwallowing", cb_difficulty_swallowing_yes.isSelected());

        }


        checklist.saveInBackground();

    }


}