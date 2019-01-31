package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CircleImageView;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @BindView(R.id.view_current_surgery)
    public LinearLayout viewCurrentSurgery;

    @BindView(R.id.tv_surgery_name)
    public CustomFontTextView tvSurgeryName;

    @BindView(R.id.tv_surgery_date)
    public CustomFontTextView tvSurgeryDate;


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
        tvSurgeryName.setOnClickListener(this);
        initView();
    }

    public void initView() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        JSONArray surgeruies = currentUser.getJSONArray("surgeryIds");
        assert surgeruies != null;
        if (surgeruies.length() > 0) {
            viewCurrentSurgery.setVisibility(View.VISIBLE);
            tvSurgeryName.setText(currentUser.getString("surgeryName"));
            Date date = currentUser.getDate("surgeryDate");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
            String dateString = simpleDateFormat.format(date);
            tvSurgeryDate.setText(String.format("Scheduled for %s.", dateString));
        }else{
            viewCurrentSurgery.setVisibility(View.GONE);
        }
        tvFullName.setText(currentUser.getString("fullName"));
        if (currentUser.get("imageFile") != null) {
            imvProfile.setVisibility(View.VISIBLE);
            imvPlaceHolder.setVisibility(View.INVISIBLE);
            ParseFile file = (ParseFile) currentUser.get("imageFile");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imvProfile.setImageBitmap(bmp);
                    }else{
                        Log.e("error", "photo downloading error");
                    }
                }
            });
        }else{
            imvProfile.setVisibility(View.INVISIBLE);
            imvPlaceHolder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_logout:
                ParseUser.logOut();

                break;
            case R.id.btn_privacy_settings:
                startActivity(new Intent(ProfileActivity.this, PrivacySettingsActivity.class));
                break;
            case R.id.btn_terms_service:
                startActivity(new Intent(ProfileActivity.this, TermsOfConditionsActivity.class));
                break;
            case R.id.imv_profile:
                break;
            case R.id.imv_profile_placeholder:
                break;

            case R.id.tv_surgery_name:
                break;
        }
    }
}
