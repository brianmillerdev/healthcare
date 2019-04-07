package com.landonferrier.healthcareapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.edt_reminder_name)
    public CustomFontEditText edtReminderName;

    @BindView(R.id.datepicker)
    public SingleDateAndTimePicker dateAndTimePicker;

    @BindView(R.id.btn_save)
    public CustomFontTextView btnSave;

    @BindView(R.id.tvTitle)
    public CustomFontTextView tvTitle;

    @BindView(R.id.btn_hospital)
    public LinearLayout btnHospital;

    @BindView(R.id.iv_hospital)
    public ImageView ivHospital;

    @BindView(R.id.tv_hospital)
    public CustomFontTextView tvHospital;

    @BindView(R.id.btn_doctor)
    public LinearLayout btnDoctor;

    @BindView(R.id.iv_doctor)
    public ImageView ivDoctor;

    @BindView(R.id.tv_doctor)
    public CustomFontTextView tvDoctor;

    ParseObject reminder;
    String reminderId = "";

    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnHospital.setOnClickListener(this);
        btnDoctor.setOnClickListener(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        if (getIntent().getBooleanExtra("isEdit", false)) {
            reminderId = getIntent().getStringExtra("reminder");
        }

        if (!reminderId.equals("")) {
            tvTitle.setText(R.string.reminder_details);
            btnSave.setVisibility(View.GONE);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Reminder");
            query.getInBackground(reminderId, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        reminder = object;
                        updateView();
                        // object will be your game score
                    } else {
                        // something went wrong
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                updateReminder();
                break;
            case R.id.btn_save:
                createReminder();
                break;
            case R.id.btn_hospital:
                ivHospital.setSelected(!ivHospital.isSelected());
                tvHospital.setSelected(!tvHospital.isSelected());
                break;
            case R.id.btn_doctor:
                ivDoctor.setSelected(!ivDoctor.isSelected());
                tvDoctor.setSelected(!tvDoctor.isSelected());
                break;
        }
    }

    public void updateView() {
        if (reminder != null) {
            edtReminderName.setText(reminder.getString("name"));
            Date date = reminder.getDate("time");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dateAndTimePicker.selectDate(cal);
            ivDoctor.setSelected(reminder.getBoolean("isDoctorAppt"));
            tvDoctor.setSelected(reminder.getBoolean("isDoctorAppt"));
            tvHospital.setSelected(reminder.getBoolean("isHospitalStay"));
            ivHospital.setSelected(reminder.getBoolean("isHospitalStay"));
        }
    }

    public void createReminder() {
        if (edtReminderName.getText().toString().equals("")) {
            Toast.makeText(this, "Reminder name required", Toast.LENGTH_SHORT).show();
            return;
        }
        reminder = new ParseObject("Reminder");
        reminder.put("name", edtReminderName.getText().toString());
        reminder.put("time", dateAndTimePicker.getDate());
        reminder.put("creatorId", ParseUser.getCurrentUser().getObjectId());
        reminder.put("active", true);
        reminder.put("complete", false);
        reminder.put("isDoctorAppt", ivDoctor.isSelected());
        reminder.put("isHospitalStay", ivHospital.isSelected());

        if ( ParseUser.getCurrentUser().get("currentSurgeryId") != null){
            String surgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            assert surgeryId != null;
            reminder.put("surgeryId", surgeryId);
        }
        hud.show();
        reminder.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                if (e == null) {
                    EventBus.getDefault().post(new EventPush("updateReminders", "Reminders"));
                    finish();
                }else{
                    Toast.makeText(AddReminderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateReminder() {
        if (reminderId.equals("")) {
            finish();
        }else{
            if (edtReminderName.getText().toString().equals("")) {
                Toast.makeText(this, "Reminder name required", Toast.LENGTH_SHORT).show();
                return;
            }
            hud.show();
            reminder.put("name", edtReminderName.getText().toString());
            reminder.put("time", dateAndTimePicker.getDate());
            reminder.put("isDoctorAppt", ivDoctor.isSelected());
            reminder.put("isHospitalStay", ivHospital.isSelected());
            reminder.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                    if (e == null) {
                        EventBus.getDefault().post(new EventPush("updateReminders", "Reminders"));
                        finish();
                    }else{
                        Toast.makeText(AddReminderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
