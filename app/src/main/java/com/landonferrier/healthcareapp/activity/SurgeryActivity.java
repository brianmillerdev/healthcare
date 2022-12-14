package com.landonferrier.healthcareapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jay.widget.StickyHeadersLinearLayoutManager;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.adapter.JournalsAdapter;
import com.landonferrier.healthcareapp.adapter.SurgeryAdapter;
import com.landonferrier.healthcareapp.adapter.SurgeryTypeAdapter;
import com.landonferrier.healthcareapp.models.TaskModel;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SurgeryActivity extends AppCompatActivity implements View.OnClickListener, SurgeryTypeAdapter.OnItemSelectedListener {

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.rc_surgery)
    public RecyclerView rcSurgery;

    @BindView(R.id.btn_select_surgery)
    public CustomFontTextView btnSelectSurgery;


    ArrayList<ParseObject> surgeries = new ArrayList<>();
    ArrayList<ParseObject> userSurgeries = new ArrayList<>();
    ParseObject selectedSurgery;

//    SurgeryAdapter mAdapter;
    SurgeryTypeAdapter mAdapter;
    String type = "current";
    private String TAG = SurgeryActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_surgery);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(this);
        btnSelectSurgery.setOnClickListener(this);
        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }

        rcSurgery.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rcSurgery.addItemDecoration(dividerItemDecoration);
        rcSurgery.setHasFixedSize(false);
        mAdapter = new SurgeryTypeAdapter(this, surgeries, this);
        rcSurgery.setAdapter(mAdapter);
        mAdapter.setType(type);
        btnSelectSurgery.setEnabled(false);
        fetchTypes();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_select_surgery:
                Intent intent = new Intent(SurgeryActivity.this, SurgeryDateActivity.class);
                intent.putExtra("id", selectedSurgery.getObjectId());
                intent.putExtra("name", selectedSurgery.getString("name"));
                startActivityForResult(intent, 200);
                break;
        }
    }

    public void fetchTypes() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SurgeryType");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    surgeries.clear();
                    surgeries.addAll(objects);
                    mAdapter.setmItems(surgeries);

                }else{
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void onSelect(ParseObject object, int position) {
        selectedSurgery = object;
        Intent currentSurgery = new Intent(SurgeryActivity.this, SelectSurgeryDetailActivity.class);
        currentSurgery.putExtra("type", object.getObjectId());
        currentSurgery.putExtra("name", object.getString("name"));
        startActivityForResult(currentSurgery, 991);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                EventBus.getDefault().post(new EventPush("updateCurrentSurgery", "Surgery"));
                finish();
            }
        } else if (requestCode == 991) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");
                Intent intent = new Intent(SurgeryActivity.this, SurgeryDateActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivityForResult(intent, 200);

            }
        }
    }
}
