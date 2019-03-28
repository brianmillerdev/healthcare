package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.adapter.SurgeryAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeRemoveSurgeryActivity extends AppCompatActivity implements View.OnClickListener, SurgeryAdapter.OnItemSelectedListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.rc_surgery)
    public RecyclerView rcSurgery;

    @BindView(R.id.tv_title)
    public CustomFontTextView tvTitle;

    ArrayList<ParseObject> surgeries = new ArrayList<>();
    ArrayList<ParseObject> userSurgeries = new ArrayList<>();
    ParseObject selectedSurgery;

    SurgeryAdapter mAdapter;
    String type = "change";

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_remove_surgery);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }
        if (type.equals("change")) {
            tvTitle.setText("Change Surgery");
        }else if (type.equals("remove")) {
            tvTitle.setText("Remove Surgery");
        }
        rcSurgery.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rcSurgery.setHasFixedSize(false);
        rcSurgery.addItemDecoration(dividerItemDecoration);
        mAdapter = new SurgeryAdapter(this, surgeries, this);
        rcSurgery.setAdapter(mAdapter);
        mAdapter.setType(type);

        fetchSurgeries();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    public void fetchSurgeries() {
        ParseQuery<ParseObject> surgeryQuery = ParseQuery.getQuery("Surgery");
        surgeryQuery.orderByAscending("sortingIndex");
        JSONArray surgeryIds = ParseUser.getCurrentUser().getJSONArray("surgeryIds");
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < surgeryIds.length(); i++) {
            try {
                ids.add(surgeryIds.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        hud.show();
        surgeryQuery.whereContainedIn("objectId", ids);
        surgeryQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (hud.isShowing())
                hud.dismiss();
                if (e == null) {
                    surgeries.clear();
                    surgeries.addAll(objects);
                    for (ParseObject object: surgeries) {
                        if (ParseUser.getCurrentUser().get("currentSurgeryId") != null) {
                            if (object.getObjectId().equals(ParseUser.getCurrentUser().getString("currentSurgeryId"))) {
                                selectedSurgery = object;
                            }
                        }
                    }
                    if (selectedSurgery != null) {
                        mAdapter.setSelectedSurgery(selectedSurgery);
                    }
                    mAdapter.setmItems(surgeries);

                }
            }
        });

    }

    @Override
    public void onEdit(ParseObject object, int position) {

    }

    @Override
    public void onDelete(ParseObject object, int position) {
        ParseUser currnetUser = ParseUser.getCurrentUser();
        String deleteId = object.getObjectId();
        JSONArray array = currnetUser.getJSONArray("surgeryIds");
        String currentSurgeryId = "";
        if (currnetUser.get("currentSurgeryId") != null) {
            currentSurgeryId = currnetUser.getString("currentSurgeryId");
        }
        int selectedIndex = -1;
        for (int i = 0; i < array.length(); i++) {
            try {
                if (array.getString(i).equals(deleteId) ) {
                    selectedIndex = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (selectedIndex > -1) {
            array.remove(selectedIndex);
        }
        currnetUser.put("surgeryIds", array);
        if (deleteId.equals(currentSurgeryId)){
            if (array.length() > 0) {
                try {
                    currnetUser.put("currentSurgeryId", array.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                currnetUser.remove("currentSurgeryId");
            }
        }

        if (currnetUser.getJSONObject("surgeryDates") != null) {
            JSONObject surgeryDates = currnetUser.getJSONObject("surgeryDates");
            if (surgeryDates.has(deleteId)) {
                surgeryDates.remove(deleteId);
            }
            currnetUser.put("surgeryDates", surgeryDates);
        }
        hud.show();
        currnetUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    List<ParseObject> deleteObjects = new ArrayList<>();
                    ParseQuery<ParseObject> reminderuery = ParseQuery.getQuery("Reminder");
                    reminderuery.whereEqualTo("creatorId", currnetUser.getObjectId());
                    reminderuery.whereEqualTo("surgeryId", deleteId);
                    reminderuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                deleteObjects.addAll(objects);
                            }
                            ParseQuery<ParseObject> journalQuery = ParseQuery.getQuery("Journal");
                            journalQuery.whereEqualTo("creatorId", currnetUser.getObjectId());
                            journalQuery.whereEqualTo("surgeryId", deleteId);
                            journalQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        deleteObjects.addAll(objects);
                                    }
                                    ParseObject.deleteAllInBackground(deleteObjects, new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (hud.isShowing()) {
                                                hud.dismiss();
                                            }
                                            if (e == null) {
                                                EventBus.getDefault().post(new EventPush("updateCurrentSurgery", "Surgery"));
                                                finish();
                                            }else{
                                                Toast.makeText(ChangeRemoveSurgeryActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    });
                }else{
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onSelect(ParseObject object, int position) {
        hud.show();
        selectedSurgery = object;
        mAdapter.setSelectedSurgery(object);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("currentSurgeryId", object.getObjectId());
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                if (e == null) {
                    ParseUser.getCurrentUser().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            EventBus.getDefault().post(new EventPush("updateCurrentSurgery", "Surgery"));
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
