package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.fragment.ComplicationsChecklistFragment;
import com.landonferrier.healthcareapp.fragment.DashboardFragment;
import com.landonferrier.healthcareapp.fragment.DashboardSurgeryFragment;
import com.landonferrier.healthcareapp.fragment.JournalsFragment;
import com.landonferrier.healthcareapp.fragment.MedicationsFragment;
import com.landonferrier.healthcareapp.fragment.PostOpSurgicalChecklistFragment;
import com.landonferrier.healthcareapp.fragment.PreOpSurgicalChecklistFragment;
import com.landonferrier.healthcareapp.fragment.RemindersFragment;
import com.landonferrier.healthcareapp.fragment.SurgeryInfoFragment;
import com.landonferrier.healthcareapp.fragment.SurveysFragment;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.AutofitTextView;
import com.landonferrier.healthcareapp.views.CircleImageView;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.MaterialNavigationDrawer;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.MaterialSection;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;


public class MainDrawerActivity extends MaterialNavigationDrawer implements View.OnClickListener {

    ImageView btnCloseMenu;
    RelativeLayout btnUserProfile;
    AutofitTextView tvUserName;
    CircleImageView ivUser;

    public MaterialSection surgeryInfoSection;
    @Override
    public void init(Bundle savedInstanceState) {


        View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer,null);
        btnCloseMenu = (ImageView) view.findViewById(R.id.btn_close_menu);
        btnUserProfile = (RelativeLayout) view.findViewById(R.id.avatar_view);
        tvUserName = (AutofitTextView) view.findViewById(R.id.tv_user);
        ivUser = (CircleImageView) view.findViewById(R.id.iv_avatar);
        btnCloseMenu.setOnClickListener(this);
        btnUserProfile.setOnClickListener(this);
        setDrawerHeaderCustom(view);

        this.addDivisor();
        // create sections
        if (Objects.requireNonNull(ParseUser.getCurrentUser().getJSONArray("surgeryIds")).length() > 0) {
            this.addSection(newSection("Home", R.drawable.ic_home,new DashboardSurgeryFragment()));//.setSectionColor(Color.parseColor("#9c27b0")));
            this.addSection(newSection("Reminders",R.drawable.ic_reminder,new RemindersFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
            this.addSection(newSection("Journal", R.drawable.ic_journal,new JournalsFragment()));//.setSectionColor(Color.parseColor("#9c27b0")));
            this.addSection(newSection("Medications",R.drawable.ic_medications,new MedicationsFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
             surgeryInfoSection = newSection("Surgery Info",R.drawable.ic_surgery_info, new SurgeryInfoFragment());
            this.addSection(surgeryInfoSection);//.setSectionColor(Color.parseColor("#03a9f4")));

//            this.addDivisor1();
            this.addSection(newSection("Patient Survey",R.drawable.ic_patient_survey,new SurveysFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
            this.addDivisor1();
            this.addSubheader("Checklist");
            this.addDivisor();
            MaterialSection preSugical = newSection("Pre-op Surgical",R.drawable.ic_checked, new PreOpSurgicalChecklistFragment());
            MaterialSection postSurgical = newSection("Post-op Surgical",R.drawable.ic_checked, new PostOpSurgicalChecklistFragment());
            MaterialSection complicationsSection = newSection("Complications",R.drawable.ic_checked, new ComplicationsChecklistFragment());
            this.addSection(preSugical);//.setSectionColor(Color.parseColor("#03a9f4")));
            this.addSection(postSurgical);
            this.addSection(complicationsSection);
        }else{
            this.addSection(newSection("Home", R.drawable.ic_home,new DashboardFragment()));//.setSectionColor(Color.parseColor("#9c27b0")));
            this.addSection(newSection("Reminders",R.drawable.ic_reminder,new RemindersFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
            this.addSection(newSection("Medications",R.drawable.ic_medications,new MedicationsFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
//
//            this.addDivisor1();
            this.addSection(newSection("Patient Survey",R.drawable.ic_patient_survey,new SurveysFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
        }

        ParseUser currentUser = ParseUser.getCurrentUser();
        tvUserName.setText(currentUser.getString("fullName"));
        if (currentUser.get("imageFile") != null) {
            ParseFile file = (ParseFile) currentUser.get("imageFile");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivUser.setImageBitmap(bmp);
                    }else{
                        Log.e("error", "photo downloading error");
                        ivUser.setImageResource(R.drawable.icon_user);
                    }
                }
            });
        }else{
            ivUser.setImageResource(R.drawable.icon_user);
        }

    }

    MaterialSectionListener listener = new MaterialSectionListener() {
        @Override
        public void onClick(MaterialSection section) {
            closeDrawer();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close_menu:
                closeDrawer();
                break;
            case R.id.avatar_view:
                closeDrawer();
                startActivity(new Intent(MainDrawerActivity.this, ProfileActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventPush event) {
        if (event.getMessage().equals("SurgeryInfo")) {
            changeToolbarColor(surgeryInfoSection);
            setFragment( surgeryInfoSection.getTargetFragment(), surgeryInfoSection.getTitle(), currentSection.getTargetFragment());
            afterFragmentSetted( surgeryInfoSection.getTargetFragment(),surgeryInfoSection.getTitle());
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            syncSectionsState(surgeryInfoSection);
        }
    }

}
