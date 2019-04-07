package com.landonferrier.healthcareapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.Utils;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_signin)
    CustomFontTextView btnSignIn;
    @BindView(R.id.btn_signup)
    CustomFontTextView btnSignUp;
    @BindView(R.id.btn_reset_password)
    CustomFontTextView btnResetPassword;
    @BindView(R.id.edt_email)
    CustomFontEditText edtEmail;
    @BindView(R.id.edt_password)
    CustomFontEditText edtPassword;


    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    signin();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin:
                signin();
                break;
            case R.id.btn_signup:
                Intent intent = new Intent(SigninActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.btn_reset_password:
                resetPassword();
                break;
        }
    }

    public void  signin() {
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            Toast.makeText(this, "Please input email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isValidEmail(edtEmail.getText().toString())) {
            Toast.makeText(this, "Email address invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            Toast.makeText(this, "Please input email", Toast.LENGTH_SHORT).show();
            return;
        }

        hud.show();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (hud != null) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                }
                if (e == null) {
                    Intent intent = new Intent(SigninActivity.this, MainDrawerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    if (e.getCode() == 101 ) {
                        Toast.makeText(SigninActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SigninActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void  resetPassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SigninActivity.this);
        alertDialog.setTitle("Reset Password");
        alertDialog.setMessage("If an account exists with this email, you will receive a reset email");

        final EditText input = new EditText(SigninActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String forgotEmail = input.getText().toString();
                        if (forgotEmail.compareTo("") == 0) {
                            ParseUser.requestPasswordResetInBackground(forgotEmail, new RequestPasswordResetCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                    }else {
                                    }
                                }
                            });
//                            if (pass.equals(password)) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Password Matched", Toast.LENGTH_SHORT).show();
//                                Intent myIntent1 = new Intent(view.getContext(),
//                                        Show.class);
//                                startActivityForResult(myIntent1, 0);
//                            } else {
//                                Toast.makeText(getApplicationContext(),
//                                        "Wrong Password!", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}
