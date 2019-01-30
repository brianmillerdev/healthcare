package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.landonferrier.healthcareapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
//                    if (SharedPref.getPrefForLoginStatus(SplashActivity.this)) {
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    } else {
//                        ProjectUtils.genericIntent(SplashActivity.this, LoginActivity.class, null, true);
//                    }

                }
            }
        };
        timer.start();

    }
}
