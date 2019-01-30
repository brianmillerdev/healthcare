package com.landonferrier.healthcareapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CircleImageView;
import com.landonferrier.healthcareapp.views.CustomFontTextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.btn_logout)
    public CustomFontTextView btnLogout;

    @BindView(R.id.tv_name)
    public CustomFontTextView tvFullName;

    @BindView(R.id.btn_privacy_settings)
    public CustomFontTextView btnPrivacySettings;

    @BindView(R.id.btn_terms_service)
    public CustomFontTextView btnTermsOfService;

    @BindView(R.id.imv_profile_placeholder)
    public ImageView imvPlaceHolder;

    @BindView(R.id.imv_profile)
    public CircleImageView imvProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnPrivacySettings.setOnClickListener(this);
        btnTermsOfService.setOnClickListener(this);
        imvPlaceHolder.setOnClickListener(this);
        imvProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_logout:
                break;
            case R.id.btn_privacy_settings:
                break;
            case R.id.btn_terms_service:
                break;
            case R.id.imv_profile:
                break;
            case R.id.imv_profile_placeholder:
                break;
        }
    }
}
