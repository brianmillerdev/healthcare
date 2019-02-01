package com.landonferrier.healthcareapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.Parse;
import com.parse.ParseObject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SurgeryInfoDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.tv_title)
    public CustomFontTextView tvTitle;

    @BindView(R.id.tv_detail)
    public CustomFontTextView tvDetail;

    @BindView(R.id.web_view)
    public WebView webView;
    String detail;
    int type = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgery_info_detail);
        ButterKnife.bind(this);

        if (getIntent().getIntExtra("type", -1) == -1) {
            finish();
            return;
        }
        type = getIntent().getIntExtra("type", -1);
        detail = getIntent().getStringExtra("detail");


        btnBack.setOnClickListener(this);

        setupView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupView() {
        if (type == 0) {
            tvTitle.setText("DESCRIPTION");
            tvDetail.setText(detail);
            tvDetail.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }else if (type == 1) {
            tvTitle.setText("VIDEO");
            webView.setWebViewClient(new WebViewClient(){

            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setAllowContentAccess(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(detail);

            tvDetail.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }else if (type == 2) {
            tvTitle.setText("PRE-OP INFORMATION");
            tvDetail.setText(detail);
            tvDetail.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }else if (type == 3) {
            tvTitle.setText("POST-OP INFORMATION");
            tvDetail.setText(detail);
            tvDetail.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }else if (type == 4) {
            tvTitle.setText("FAQ");
            tvDetail.setText(detail);
            tvDetail.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }else if (type == 5) {
            tvTitle.setText("RED FLAGS");
            tvDetail.setText(detail);
            tvDetail.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
        tvDetail.setMovementMethod(new ScrollingMovementMethod());

    }
}
