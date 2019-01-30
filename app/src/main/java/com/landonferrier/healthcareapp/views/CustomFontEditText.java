package com.landonferrier.healthcareapp.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.landonferrier.healthcareapp.utils.CustomFontUtils;

import androidx.annotation.Nullable;


public class CustomFontEditText extends EditText {
    public CustomFontEditText(Context context) {
        super(context);
    }

    public CustomFontEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomFontEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        CustomFontUtils.applyCustomFont(this, context, attrs);
    }

    public CustomFontEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        CustomFontUtils.applyCustomFont(this, context, attrs);
    }
}
