package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontTextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_register)
    CustomFontTextView btnRegister;

    @BindView(R.id.btn_signin)
    CustomFontTextView btnSignin;
    @BindView(R.id.tv_terms)
    CustomFontTextView tvTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        btnRegister.setOnClickListener(this);
        btnSignin.setOnClickListener(this);
        tvTerms.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                break;
            case R.id.btn_signin:
                startActivity(new Intent(RegisterActivity.this, SigninActivity.class));
                break;
            case R.id.tv_terms:
                break;
        }
    }
}
