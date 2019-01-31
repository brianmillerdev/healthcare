package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SurgeryActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.rc_surgery)
    public RecyclerView rcSurgery;

    @BindView(R.id.btn_select_surgery)
    public CustomFontTextView btnSelectSurgery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_surgery);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        btnSelectSurgery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_select_surgery:
                startActivity(new Intent(SurgeryActivity.this, SurgeryDateActivity.class));
                break;
        }
    }
}
