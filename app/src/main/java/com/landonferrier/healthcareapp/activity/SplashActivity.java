package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.landonferrier.healthcareapp.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    Intent intent = new Intent(SplashActivity.this, MainDrawerActivity.class);
                                    startActivity(intent);
//                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                        finish();
                    }

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
