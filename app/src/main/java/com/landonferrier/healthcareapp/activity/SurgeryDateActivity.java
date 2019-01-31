package com.landonferrier.healthcareapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SurgeryDateActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.datepicker)
    public SingleDateAndTimePicker dateAndTimePicker;

    @BindView(R.id.btn_next)
    public CustomFontTextView btnNext;
    @BindView(R.id.btn_skip)
    public CustomFontTextView btnSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_surgery_date);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                break;
            case R.id.btn_skip:
                break;
        }
    }
}
