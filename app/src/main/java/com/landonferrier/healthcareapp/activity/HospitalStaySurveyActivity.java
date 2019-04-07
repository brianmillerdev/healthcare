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

public class HospitalStaySurveyActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.edt_overall_doctor_visit)
    CustomFontEditText edtOverallDoctorVisit;

    @BindView(R.id.edt_nurse)
    CustomFontEditText edtNurse;

    @BindView(R.id.edt_tech)
    CustomFontEditText edtTech;

    @BindView(R.id.pre_registration_process_rating_bar)
    MaterialRatingBar rbPreRegistrationProcess;

    @BindView(R.id.overall_doctor_visit_rating_bar)
    MaterialRatingBar rbOverallDoctorVisit;

    @BindView(R.id.nurse_rating_bar)
    MaterialRatingBar rbNurse;

    @BindView(R.id.tech_rating_bar)
    MaterialRatingBar rbTech;

    @BindView(R.id.stuff_responsiveness_rating_bar)
    MaterialRatingBar rbStuffResponsiveness;

    @BindView(R.id.kindness_rating_bar)
    MaterialRatingBar rbKindness;

    @BindView(R.id.professionalism_rating_bar)
    MaterialRatingBar rbProfessionalism;

    @BindView(R.id.knowledge_rating_bar)
    MaterialRatingBar rbKnowledge;

    @BindView(R.id.attitude_rating_bar)
    MaterialRatingBar rbAttitude;

    @BindView(R.id.btn_submit)
    CustomFontTextView btnSubmit;

    @BindView(R.id.overlay)
    RelativeLayout overlayView;

    @BindView(R.id.iv_completed)
    ImageView ivCompleted;


    String surveyId;
    ParseObject survey;

    KProgressHUD hud;
    private String TAG = HospitalStaySurveyActivity.class.getName();
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_stay_survey);
        ButterKnife.bind(this);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SurveyHospital");
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
        int registrationRating = (int) rbPreRegistrationProcess.getRating();
        int stayRating = (int) rbOverallDoctorVisit.getRating();
        int knowledgeRating = (int) rbKnowledge.getRating();
        int nurseRating = (int) rbNurse.getRating();
        int techRating = (int) rbTech.getRating();
        int responsivenessRating = (int) rbStuffResponsiveness.getRating();
        int kindnessRating = (int) rbKindness.getRating();
        int profesRating = (int) rbProfessionalism.getRating();
        int attitudeRating = (int) rbAttitude.getRating();
        String hospitalName = edtOverallDoctorVisit.getText().toString();
        String nurseName = edtNurse.getText().toString();
        String techName = edtTech.getText().toString();

        if (registrationRating == 0 || stayRating == 0 || knowledgeRating == 0 || nurseRating == 0 ||
                techRating == 0 || responsivenessRating == 0 || kindnessRating == 0 || profesRating == 0 || attitudeRating == 0
                || hospitalName.equals("") || nurseName.equals("") || techName.equals("")) {

            Toast.makeText(this, "Please make sure to fill out all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        hud.show();

        survey.put("professionalRating", profesRating);
        survey.put("registrationRating", registrationRating);
        survey.put("stayRating", stayRating);
        survey.put("nurseRating", nurseRating);
        survey.put("techRating", techRating);
        survey.put("responsivenessRating", responsivenessRating);
        survey.put("kindnessRating", kindnessRating);
        survey.put("knowledgeRating", knowledgeRating);
        survey.put("attitudeRating", attitudeRating);
        survey.put("hospitalName", hospitalName);
        survey.put("nurseName", nurseName);
        survey.put("techName", techName);
        survey.put("completed", true);

        survey.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    completed();
                }else {
                    Log.e(TAG, e.getLocalizedMessage());
                    if (hud != null) {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                    }
                }
            }
        });

    }
    public void completed() {
        mHandler.postDelayed(mUpdateMicStatusTimer, 2000);

        overlayView.setVisibility(View.VISIBLE);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(null, R.anim.alpha_anim);
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
