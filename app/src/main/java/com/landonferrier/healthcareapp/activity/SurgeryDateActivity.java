package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SurgeryDateActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.datepicker)
    public SingleDateAndTimePicker dateAndTimePicker;

    @BindView(R.id.btn_next)
    public CustomFontTextView btnNext;
    @BindView(R.id.btn_skip)
    public CustomFontTextView btnSkip;

    KProgressHUD hud;

    String surgeryId = "";
    String surgeryName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_surgery_date);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        if (getIntent().getStringExtra("id") != null) {
            surgeryId = getIntent().getStringExtra("id");
            surgeryName = getIntent().getStringExtra("name");
        }
        if (surgeryId.equals("")) {
            finish();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_next:
                saveSurgery(true);
                break;
            case R.id.btn_skip:
                saveSurgery(false);
                break;
        }
    }

    public void saveSurgery(boolean isDate) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("currentSurgeryId", surgeryId);
        currentUser.put("surgeryName", surgeryName);
        if (currentUser.getJSONArray("surgeryIds") != null) {
            JSONArray array = currentUser.getJSONArray("surgeryIds");
            assert array != null;
            array.put(surgeryId);
            currentUser.put("surgeryIds", array);
        }else{
            JSONArray array = new JSONArray();
            array.put(surgeryId);
            currentUser.put("surgeryIds", array);
        }
        if (isDate) {
            if (currentUser.getJSONObject("surgeryDates") != null) {
                JSONObject object = currentUser.getJSONObject("surgeryDates");
                Date date = dateAndTimePicker.getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String dateString = simpleDateFormat.format(date);
                try {
                    object.put(surgeryId, dateString);
                    currentUser.put("surgeryDates", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                JSONObject object = new JSONObject();
                Date date = dateAndTimePicker.getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String dateString = simpleDateFormat.format(date);
                try {
                    object.put(surgeryId, dateString);
                    currentUser.put("surgeryDates", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        hud.show();

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser.getCurrentUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            hud.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }else{
                    hud.dismiss();
                }
            }
        });

    }
}
