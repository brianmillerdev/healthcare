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

public class AddMedicationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.edt_medication_name)
    public CustomFontEditText edtMedicationName;

    @BindView(R.id.edt_amount)
    public CustomFontEditText edtAmount;

    @BindView(R.id.edt_frequency)
    public CustomFontEditText edtFrequency;

    @BindView(R.id.btn_save)
    public CustomFontTextView btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                finish();
                break;
        }
    }
}
