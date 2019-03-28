package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.fragment.DashboardFragment;
import com.landonferrier.healthcareapp.fragment.JournalsFragment;
import com.landonferrier.healthcareapp.fragment.MedicationsFragment;
import com.landonferrier.healthcareapp.fragment.RemindersFragment;
import com.landonferrier.healthcareapp.fragment.SurgeryInfoFragment;
import com.landonferrier.healthcareapp.views.AutofitTextView;
import com.landonferrier.healthcareapp.views.CircleImageView;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.MaterialNavigationDrawer;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.MaterialSection;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import butterknife.BindView;


public class MainDrawerActivity extends MaterialNavigationDrawer implements View.OnClickListener {

    ImageView btnCloseMenu;
    RelativeLayout btnUserProfile;
    AutofitTextView tvUserName;
    CircleImageView ivUser;


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
        this.addSection(newSection("Home", R.drawable.ic_home,new DashboardFragment()));//.setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection(newSection("Surgery Info",R.drawable.ic_surgery_info,new SurgeryInfoFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
        this.addSection(newSection("Journal", R.drawable.ic_journal,new JournalsFragment()));//.setSectionColor(Color.parseColor("#9c27b0")));
        this.addSection(newSection("Reminders",R.drawable.ic_reminder,new RemindersFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
        this.addSection(newSection("Medications",R.drawable.ic_medications,new MedicationsFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));

        this.addDivisor1();
        this.addSection(newSection("Patient Survey",R.drawable.ic_patient_survey,new MedicationsFragment()));//.setSectionColor(Color.parseColor("#03a9f4")));
        this.addDivisor1();
        this.addSubheader("Checklist");
        this.addDivisor();
        MaterialSection surgicalSection = newSection("Surgical",R.drawable.ic_checl_button, listener);
        MaterialSection complicationsSection = newSection("Complications",R.drawable.ic_checl_button, listener);
        this.addSection(surgicalSection);//.setSectionColor(Color.parseColor("#03a9f4")));
        this.addSection(complicationsSection);

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
            section.setIconSelected(!section.getIconSelected());
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
}
