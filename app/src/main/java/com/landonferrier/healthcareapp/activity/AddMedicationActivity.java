package com.landonferrier.healthcareapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMedicationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.edt_medication_name)
    public CustomFontEditText edtMedicationName;

    @BindView(R.id.edt_amount)
    public CustomFontEditText edtAmount;

    @BindView(R.id.edt_frequency)
    public CustomFontTextView edtFrequency;

    @BindView(R.id.btn_save)
    public CustomFontTextView btnSave;


    @BindView(R.id.tvTitle)
    public CustomFontTextView tvTitle;

    ParseObject medication;
    String medicationId = "";

    KProgressHUD hud;
    CharSequence items[] = new CharSequence[] {"12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM",
            "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};
    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;

    private boolean mHasDoubleClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        if (getIntent().getBooleanExtra("isEdit", false)) {
            medicationId = getIntent().getStringExtra("medication");
        }

        if (!medicationId.equals("")) {
            btnSave.setVisibility(View.GONE);
            tvTitle.setText(R.string.edit_medication);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Medication");
            query.getInBackground(medicationId, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        medication = object;
                        updateView();
                        // object will be your game score
                    } else {
                        // something went wrong
                    }
                }
            });
        }
        edtFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long pressTime = System.currentTimeMillis();


                // If double click...
                if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
                    Toast.makeText(getApplicationContext(), "Double Click Event", Toast.LENGTH_SHORT).show();
                    mHasDoubleClicked = true;
                    edtFrequency.setText("");
                }
                else {     // If not double click....
                    mHasDoubleClicked = false;
                    Handler myHandler = new Handler() {
                        public void handleMessage(Message m) {
                            if (!mHasDoubleClicked) {
                                Toast.makeText(getApplicationContext(), "Single Click Event", Toast.LENGTH_SHORT).show();
                                showSelector();
                            }
                        }
                    };
                    Message m = new Message();
                    myHandler.sendMessageDelayed(m,DOUBLE_PRESS_INTERVAL);
                }
                // record the last time the menu button was pressed.
                lastPressTime = pressTime;

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                updateMedication();
                break;
            case R.id.btn_save:
                createMedication();
                break;
        }
    }

    public void updateView() {
        if (medication != null) {
            edtMedicationName.setText(medication.getString("name"));
            edtAmount.setText(String.valueOf(medication.getInt("amount")));
            JSONArray times = medication.getJSONArray("times");
            String timesString = "";
            try {
                for (int i = 0; i< times.length(); i++){
                    if (i == 0){
                        timesString = times.getString(i);
                    }else{
                        timesString = String.format("%s, %s", timesString, times.getString(i));
                    }
                }
                timesString = timesString + " +";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            edtFrequency.setText(timesString);
        }
    }

    public void createMedication() {
        if (edtMedicationName.getText().toString().equals("")) {
            Toast.makeText(this, "Medication name required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Medication amount name required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtFrequency.getText().toString().equals("")) {
            Toast.makeText(this, "Medication name required", Toast.LENGTH_SHORT).show();
            return;
        }
        medication = new ParseObject("Medication");
        medication.put("name", edtMedicationName.getText().toString());
        medication.put("amount", Integer.valueOf(edtAmount.getText().toString()));
        medication.put("creatorId", ParseUser.getCurrentUser().getObjectId());
        medication.put("date", new Date());
        String string = edtFrequency.getText().toString();
        string = string.replace(" ", "").replace("+", "");
        String[] strings = string.split(",");
        JSONArray array = new JSONArray();
        for (int i = 0; i < strings.length; i++) {
            try {
                array.put(i, strings[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        medication.put("times", array);
        hud.show();
        medication.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                if (e == null) {
                    EventBus.getDefault().post(new EventPush("updateMedications", "Medications"));
                    finish();
                }else{
                    Toast.makeText(AddMedicationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateMedication() {
        if (medicationId.equals("")) {
            finish();
        }else{
            if (edtMedicationName.getText().toString().equals("")) {
                Toast.makeText(this, "Medication name required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtAmount.getText().toString().equals("")) {
                Toast.makeText(this, "Medication amount name required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtFrequency.getText().toString().equals("")) {
                Toast.makeText(this, "Medication name required", Toast.LENGTH_SHORT).show();
                return;
            }
            medication.put("name", edtMedicationName.getText().toString());
            medication.put("amount", Integer.valueOf(edtAmount.getText().toString()));
            String string = edtFrequency.getText().toString();
            string = string.replace(" ", "").replace("+", "");
            String[] strings = string.split(",");
            JSONArray array = new JSONArray();
            for (int i = 0; i < strings.length; i++) {
                try {
                    array.put(i, strings[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            medication.put("times", array);
            hud.show();
            medication.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                    if (e == null) {
                        EventBus.getDefault().post(new EventPush("updateMedications", "Medications"));
                        finish();
                    }else{
                        Toast.makeText(AddMedicationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void showSelector(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {
                // ...
                String string = edtFrequency.getText().toString();
                string = string.replace(" ", "").replace("+", "");
                String[] strings = string.split(",");
                boolean isContain = false;
                for (String s: strings) {
                    if (s.equals(items[n])) {
                        isContain = true;
                    }
                }
                if (!isContain){
                    String timesString = string.replace(" +", "");
                    if (timesString.equals("")){
                        timesString = String.format("%s", items[n]);
                    }else{
                        timesString = String.format("%s, %s", timesString, items[n]);
                    }
                    timesString = timesString + " +";
                    edtFrequency.setText(timesString);
                }
                d.dismiss();
            }

        });
        adb.setNegativeButton("Cancel", null);
        adb.setTitle("Times");
        adb.show();

    }
}
