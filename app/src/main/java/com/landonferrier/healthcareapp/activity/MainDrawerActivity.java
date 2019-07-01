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

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.landonferrier.healthcareapp.utils.Constants;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.AutofitTextView;
import com.landonferrier.healthcareapp.views.CircleImageView;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.MaterialNavigationDrawer;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.Element;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.MaterialAccount;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.MaterialSection;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.MaterialSubheader;
import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class MainDrawerActivity extends MaterialNavigationDrawer implements View.OnClickListener {

    ImageView btnCloseMenu;
    RelativeLayout btnUserProfile;
    AutofitTextView tvUserName;
    CircleImageView ivUser;

    public MaterialSection surgeryInfoSection;

    MaterialSection homeSection;
    MaterialSection reminderSection;
    MaterialSection journalSection;
    MaterialSection medicationSection;
    MaterialSection surveySection;
    MaterialSection preSugical;
    MaterialSection postSurgical;
    MaterialSection complicationsSection;
    public static int previousState = 0;


    List<MaterialSection> sectionList1 = new LinkedList<>();
    List<MaterialSubheader> subheaderList1 = new LinkedList<>();
    List<Element> elementsList1 = new LinkedList<>();

    List<MaterialSection> sectionList2 = new LinkedList<>();
    List<MaterialSubheader> subheaderList2 = new LinkedList<>();
    List<Element> elementsList2 = new LinkedList<>();

    List<MaterialSection> sectionList3 = new LinkedList<>();
    List<MaterialSubheader> subheaderList3 = new LinkedList<>();
    List<Element> elementsList3 = new LinkedList<>();

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
        homeSection = newSection("Home", R.drawable.ic_home,new DashboardSurgeryFragment());
        reminderSection = newSection("Reminders/Appointments",R.drawable.ic_reminder,new RemindersFragment());
        journalSection = newSection("Journal", R.drawable.ic_journal,new JournalsFragment());
        medicationSection = newSection("Medications",R.drawable.ic_medications,new MedicationsFragment());
        surgeryInfoSection = newSection("Surgery Info",R.drawable.ic_surgery_info, new SurgeryInfoFragment());
        surveySection = newSection("Patient Survey",R.drawable.ic_patient_survey,new SurveysFragment());
        preSugical = newSection("Pre-op Surgical",R.drawable.ic_checked, new PreOpSurgicalChecklistFragment());
        postSurgical = newSection("Post-op Surgical",R.drawable.ic_checked, new PostOpSurgicalChecklistFragment());
        complicationsSection = newSection("Complications",R.drawable.ic_checked, new ComplicationsChecklistFragment());


        this.addDivisor();
//        if (Objects.requireNonNull(ParseUser.getCurrentUser().getJSONArray("surgeryIds")).length() > 0) {
            this.addSection(homeSection);//.setSectionColor(Color.parseColor("#9c27b0")));
            this.addSection(reminderSection);
            this.addSection(journalSection);
            this.addSection(medicationSection);
            this.addSection(surgeryInfoSection);

            this.addSection(surveySection);
            this.addDivisor1();
            this.addSubheader("Checklist");
            this.addDivisor();

            this.addSection(preSugical);//.setSectionColor(Color.parseColor("#03a9f4")));
            this.addSection(postSurgical);
            this.addSection(complicationsSection);

            previousState = 1;

        sectionList1 = sectionList;
        subheaderList1 = subheaderList;
        elementsList1 = elementsList;

        sectionList2.add(homeSection);
        sectionList2.add(reminderSection);
        sectionList2.add(medicationSection);
        sectionList2.add(surveySection);

        elementsList2.add(elementsList1.get(0));
        elementsList2.add(elementsList1.get(1));
        elementsList2.add(elementsList1.get(2));
        elementsList2.add(elementsList1.get(4));
        elementsList2.add(elementsList1.get(6));

        sectionList3.add(sectionList1.get(0));
        sectionList3.add(sectionList1.get(1));
        sectionList3.add(sectionList1.get(2));
        sectionList3.add(sectionList1.get(3));
        sectionList3.add(sectionList1.get(4));
        sectionList3.add(sectionList1.get(5));

        elementsList3.add(elementsList1.get(0));
        elementsList3.add(elementsList1.get(1));
        elementsList3.add(elementsList1.get(2));
        elementsList3.add(elementsList1.get(3));
        elementsList3.add(elementsList1.get(4));
        elementsList3.add(elementsList1.get(5));
        elementsList3.add(elementsList1.get(6));



        checkCurrentSurvey();
//        }else{
//            this.addSection(homeSection);//.setSectionColor(Color.parseColor("#9c27b0")));
//            this.addSection(reminderSection);
//            this.addSection(medicationSection);
//            this.addSection(surveySection);
//            previousState = 0;
//        }

        this.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                checkCurrentSurvey();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    public void checkCurrentSurvey() {
        if (Objects.requireNonNull(ParseUser.getCurrentUser().getJSONArray("surgeryIds")).length() > 0) {
            String currentSurgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            if (currentSurgeryId != null) {
                ParseObject currentSurgery = ParseObject.createWithoutData( "Surgery", currentSurgeryId);
                currentSurgery.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String typeId = object.getString("typeId");
                            if (typeId != null) {
                                if (typeId.equals(Constants.injection)) {
                                    updateDrawer(Constants.injection);
                                } else {
                                    updateDrawer(Constants.neurosurgery);
                                }
                            } else {

                            }

                        }
                    }
                });
            }
        } else {
            updateDrawer("");
        }
    }

    public void updateDrawer(String type) {
        // create sections
        if (type.equals("")) {
            if (previousState > 0) {
                this.elementsList = elementsList2;
                this.sectionList = sectionList2;
                this.subheaderList = subheaderList2;
                previousState = 0;
                this.refreshSections();
            }
        } else if (type.equals(Constants.neurosurgery)) {
            if (previousState != 1) {
                this.elementsList = elementsList1;
                this.sectionList = sectionList1;
                this.subheaderList = subheaderList1;
                previousState = 1;
                this.refreshSections();
            }
        } else if (type.equals(Constants.injection)) {
            if (previousState != 2) {
                this.elementsList = elementsList3;
                this.sectionList = sectionList3;
                this.subheaderList = subheaderList3;
                previousState = 2;
                this.refreshSections();
            }
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
