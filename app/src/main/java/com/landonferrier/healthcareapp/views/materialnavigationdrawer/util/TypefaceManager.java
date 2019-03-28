package com.landonferrier.healthcareapp.views.materialnavigationdrawer.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import androidx.collection.LruCache;

/**
 * Custom Typeface Manager for Roboto Fonts inside the drawer
 *
 * Created by neokree on 13/01/15.
 */
public class TypefaceManager {
    private static final String ROBOTO_REGULAR = "SFProText-Medium.ttf";
    private static final String ROBOTO_MEDIUM = "SFProText-Semibold.ttf";
    private final LruCache<String, Typeface> mCache;
    private final AssetManager mAssetManager;

    public TypefaceManager(AssetManager assetManager) {
        mAssetManager = assetManager;
        mCache = new LruCache<>(3);
    }

    public Typeface getRobotoRegular() {
        return getTypeface(ROBOTO_REGULAR);
    }

    public Typeface getRobotoMedium() {
        return getTypeface(ROBOTO_MEDIUM);
    }

    private Typeface getTypeface(final String filename) {
        Typeface typeface = mCache.get(filename);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(mAssetManager, filename);
            mCache.put(filename, typeface);
        }
        return typeface;
    }
}
