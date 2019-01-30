package com.landonferrier.healthcareapp.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.landonferrier.healthcareapp.R;

public class CustomFontUtils {

    public static void applyCustomFont(TextView customFontTextView, Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomFont);

        String fontStyle = attributeArray.getString(R.styleable.CustomFont_font_style);

        assert fontStyle != null;
        Typeface customFont = selectTypeface(context, fontStyle);
        customFontTextView.setTypeface(customFont);

        attributeArray.recycle();
    }

    private static Typeface selectTypeface(Context context, String fontStyle) {
        if (fontStyle.equals("bold")){
            return FontCache.getTypeface("Lato-Bold.ttf", context);
        }

        if (fontStyle.equals("regular")){
            return FontCache.getTypeface("Lato-Regular.ttf", context);
        }

        return FontCache.getTypeface("Lato-Regular.ttf", context);
    }
}