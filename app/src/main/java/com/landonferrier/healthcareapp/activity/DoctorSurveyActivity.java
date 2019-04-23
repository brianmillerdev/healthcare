package com.landonferrier.healthcareapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DoctorSurveyActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.edt_overall_doctor_visit)
    CustomFontEditText edtOverallDoctorVisit;

    @BindView(R.id.scheduling_ease_rating_bar)
    MaterialRatingBar rbSchedulingEase;

    @BindView(R.id.overall_doctor_visit_rating_bar)
    MaterialRatingBar rbOverallDoctorVisit;

    @BindView(R.id.professionalism_rating_bar)
    MaterialRatingBar rbProfessionalism;

    @BindView(R.id.knowledge_rating_bar)
    MaterialRatingBar rbKnowledge;

    @BindView(R.id.accessibility_rating_bar)
    MaterialRatingBar rbAccessibility;

    @BindView(R.id.btn_submit)
    CustomFontTextView btnSubmit;

    @BindView(R.id.overlay)
    RelativeLayout overlayView;

    @BindView(R.id.iv_completed)
    ImageView ivCompleted;

    String surveyId;
    ParseObject survey;
    KProgressHUD hud;
    Handler mHandler = new Handler();

    private String TAG = DoctorSurveyActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_survey);
        ButterKnife.bind(this);
        surveyId = getIntent().getStringExtra("survey");
        if (surveyId == null) {
            finish();
        }


        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        btnBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        getSurvey();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                submitSurvey();
                break;
        }
    }

    public void getSurvey() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SurveyDoctor");
        query.getInBackground(surveyId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    survey = object;
                    // object will be your game score
                } else {
                    // something went wrong
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
    }

    public void submitSurvey() {
        int professRating = (int) rbProfessionalism.getRating();
        int accessibilityRating = (int) rbAccessibility.getRating();
        int knowledgeRating = (int) rbKnowledge.getRating();
        int schedulingRating = (int) rbSchedulingEase.getRating();
        int doctorsCareRating = (int) rbOverallDoctorVisit.getRating();
        String doctorName = edtOverallDoctorVisit.getText().toString();

        if (professRating == 0 || accessibilityRating == 0 || knowledgeRating == 0 || doctorName.equals("")) {

            Toast.makeText(this, "Please make sure to fill out all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        hud.show();

        survey.put("professRating", professRating);
        survey.put("accessibilityRating", accessibilityRating);
        survey.put("knowledgeRating", knowledgeRating);
        survey.put("schedulingRating", schedulingRating);
        survey.put("doctorCareRating", doctorsCareRating);
        survey.put("doctorName", doctorName);
        survey.put("completed", true);

        survey.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    completed();
                }else {
                    if (hud != null) {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                    }
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
    }

    public void completed() {
        mHandler.postDelayed(mUpdateMicStatusTimer, 2000);

        overlayView.setVisibility(View.VISIBLE);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha_anim);
        ivCompleted.startAnimation(myFadeInAnimation);
    }

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            if (hud != null) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
            }
            finish();
        }
    };

}
