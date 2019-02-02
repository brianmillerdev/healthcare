package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.Helper;
import com.landonferrier.healthcareapp.utils.Utils;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

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

    @BindView(R.id.edt_first_name)
    CustomFontEditText edtFirstName;
    @BindView(R.id.edt_last_name)
    CustomFontEditText edtLastName;
    @BindView(R.id.edt_email)
    CustomFontEditText edtEmail;
    @BindView(R.id.edt_password)
    CustomFontEditText edtPassword;

    public KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        btnRegister.setOnClickListener(this);
        btnSignin.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    createAccount();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                createAccount();
                break;
            case R.id.btn_signin:
                startActivity(new Intent(RegisterActivity.this, SigninActivity.class));
                break;
            case R.id.tv_terms:
                startActivity(new Intent(RegisterActivity.this, TermsPrivacyActivity.class));
                break;
        }
    }

    public void createAccount() {

        if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
            Toast.makeText(this, "Please input first name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtLastName.getText().toString())) {
            Toast.makeText(this, "Please input last name", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (edtPassword.getText().toString().length() < 8) {
            Toast.makeText(this, "Password must be equal or greater than 8", Toast.LENGTH_SHORT).show();
            return;
        }

        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        ParseUser newUser = new ParseUser();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUsername(email);
        newUser.put("firstName", firstName);
        newUser.put("lastName", lastName);
        newUser.put("fullName", String.format("%s %s", firstName, lastName));
        newUser.put("surgeryIds", new JSONArray());
        newUser.put("completedIds", new JSONArray());
        hud.show();
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (hud != null) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                }
                if (e == null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
