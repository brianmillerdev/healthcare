package com.landonferrier.healthcareapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.landonferrier.healthcareapp.utils.Utils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public class BaseFragment extends Fragment implements View.OnClickListener{

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int viewId = addView();
        view = inflater.inflate(viewId, container, false);
        ButterKnife.bind(this, view);
        initView();
        setToolbar();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.dismissKeyboard(getContext(), view);
            }
        });
        return view;
    }

    protected int addView() {
        return 0;
    }

    protected void initView() {

    }

    @Override
    public void onClick(View v) {

    }

    public void setToolbar() {

    }
}
