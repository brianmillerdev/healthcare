package com.landonferrier.healthcareapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.landonferrier.healthcareapp.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsOfConditionsActivity extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.pdfView)
    public PDFView pdfView;

    public static final String SAMPLE_FILE = "Terms of Service.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(0)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }
}
