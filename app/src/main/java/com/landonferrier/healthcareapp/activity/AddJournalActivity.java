package com.landonferrier.healthcareapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.adapter.PhotosAdapter;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddJournalActivity extends AppCompatActivity implements View.OnClickListener, PhotosAdapter.OnItemSelectedListener {
    private static final int PERMISSIONS_REQUEST_STORAGE = 212;

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.edt_journal_title)
    public CustomFontEditText edtJournalTitle;

    @BindView(R.id.edt_entry)
    public CustomFontEditText edtJournalEntry;

    @BindView(R.id.tv_right_arm_pain)
    public CustomFontTextView tvRightArmPain;

    @BindView(R.id.tv_left_arm_pain)
    public CustomFontTextView tvLeftArmPain;

    @BindView(R.id.tv_right_leg_pain)
    public CustomFontTextView tvRightLegPain;

    @BindView(R.id.tv_left_leg_pain)
    public CustomFontTextView tvLeftLegPain;

    @BindView(R.id.tv_back_pain)
    public CustomFontTextView tvBackPain;

    @BindView(R.id.tv_neck_pain)
    public CustomFontTextView tvNeckPain;

    @BindView(R.id.tv_photos)
    public CustomFontTextView tvPhotos;

    @BindView(R.id.rc_photos)
    public RecyclerView rcPhotos;

    @BindView(R.id.btn_save)
    public CustomFontTextView btnSave;


    @BindView(R.id.tvTitle)
    public CustomFontTextView tvTitle;

    ParseObject journal;
    String journalId = "";
    CharSequence items[] = new CharSequence[] {"10  MAXIMUM", "9", "8", "7  SEVERE", "6", "5", "4  MODERATE", "3", "2", "1", "0  NO PAIN"};
    KProgressHUD hud;

    ArrayList<Uri> photos = new ArrayList<>();
    ArrayList<ParseObject> photosArray = new ArrayList<>();
    PhotosAdapter mAdapter;
    int selectedPainIndex = 0;

    ArrayList<CustomFontTextView> painViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvRightArmPain.setOnClickListener(listener);
        tvLeftArmPain.setOnClickListener(listener);
        tvRightLegPain.setOnClickListener(listener);
        tvLeftLegPain.setOnClickListener(listener);
        tvBackPain.setOnClickListener(listener);
        tvNeckPain.setOnClickListener(listener);
        painViews.add(tvRightArmPain);
        painViews.add(tvLeftArmPain);
        painViews.add(tvRightLegPain);
        painViews.add(tvLeftLegPain);
        painViews.add(tvBackPain);
        painViews.add(tvNeckPain);
        rcPhotos.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new PhotosAdapter(this, photos, this);
        rcPhotos.setAdapter(mAdapter);
        tvPhotos.setText(String.valueOf(photos.size()));
        if (getIntent().getBooleanExtra("isEdit", false)) {
            journalId = getIntent().getStringExtra("journal");
        }
        if (!journalId.equals("")) {
            btnSave.setVisibility(View.GONE);
            tvTitle.setText(R.string.journla_entry);
            tvRightArmPain.setClickable(false);
            tvLeftArmPain.setClickable(false);
            tvRightLegPain.setClickable(false);
            tvLeftLegPain.setClickable(false);
            tvBackPain.setClickable(false);
            tvNeckPain.setClickable(false);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Journal");
            query.getInBackground(journalId, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        journal = object;
                        updateView();
                        // object will be your game score
                    } else {
                        // something went wrong
                    }
                }
            });
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getClass().equals(CustomFontTextView.class)) {
                CustomFontTextView cfv = (CustomFontTextView) v;
                showSelector(cfv);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                updateJournal();
                break;
            case R.id.btn_save:
                createJournal();
                break;
        }
    }

    public void updateView() {
        if (journal != null) {
            edtJournalTitle.setText(journal.getString("name"));
            edtJournalEntry.setText(journal.getString("text"));
            tvRightArmPain.setText(journal.getString("rightArmPain"));
            tvLeftArmPain.setText(journal.getString("leftArmPain"));
            tvRightLegPain.setText(journal.getString("rightLegPain"));
            tvLeftLegPain.setText(journal.getString("leftLegPain"));
            tvBackPain.setText(journal.getString("backPain"));
            tvNeckPain.setText(journal.getString("neckPain"));

            fetchImages();
        }
    }

    public void fetchImages() {

        ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery("JournalImage");
        photoQuery.whereEqualTo("journalId", journalId);
        photoQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    photosArray.clear();
                    photos.clear();
                    for (ParseObject object: objects) {
                        ParseFile file = object.getParseFile("imageFile");
                        photosArray.add(object);
                        photos.add(Uri.parse(file.getUrl()));
                    }
                    mAdapter.setmItems(photos);
                    tvPhotos.setText(String.valueOf(photos.size()));
                }
            }
        });

    }

    public void createJournal() {
        if (edtJournalTitle.getText().toString().equals("")) {
            Toast.makeText(this, "Journal title required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtJournalEntry.getText().toString().equals("")) {
            Toast.makeText(this, "Journal entry required", Toast.LENGTH_SHORT).show();
            return;
        }
        journal = new ParseObject("Journal");
        journal.put("name", edtJournalTitle.getText().toString());
        journal.put("text", edtJournalEntry.getText().toString());
        journal.put("rightArmPain", tvRightArmPain.getText().toString());
        journal.put("leftArmPain", tvLeftArmPain.getText().toString());
        journal.put("rightLegPain", tvRightLegPain.getText().toString());
        journal.put("leftLegPain", tvLeftLegPain.getText().toString());
        journal.put("backPain", tvBackPain.getText().toString());
        journal.put("neckPain", tvNeckPain.getText().toString());

        journal.put("creatorId", ParseUser.getCurrentUser().getObjectId());
        journal.put("date", new Date());
        if ( ParseUser.getCurrentUser().get("currentSurgeryId") != null){
            String surgeryId = ParseUser.getCurrentUser().getString("currentSurgeryId");
            assert surgeryId != null;
            journal.put("surgeryId", surgeryId);
        }
        hud.show();
        journal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                if (e == null) {
                    if (photosArray.size() > 0) {
                        updateJournalPhotos(0, journal.getObjectId());
                    }
                }else{
                    Toast.makeText(AddJournalActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateJournalPhotos(int index, String id) {
        if (index < photosArray.size()) {
            ParseObject object = photosArray.get(index);
            object.put("journalId", id);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    updateJournalPhotos(index + 1, id);
                }
            });
        }else{
            EventBus.getDefault().post(new EventPush("updateJournals", "Journals"));
            finish();
        }
    }

    public void updateJournal() {
        if (journalId.equals("")) {
            ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery("JournalImage");
            photoQuery.whereEqualTo("journalId", "");
            photoQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        ParseObject.deleteAllInBackground(objects, new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    finish();
                                }
                            }
                        });
                    }else{
                        finish();
                    }
                }
            });
        }else{
            if (edtJournalTitle.getText().toString().equals("")) {
                Toast.makeText(this, "Journal title required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtJournalEntry.getText().toString().equals("")) {
                Toast.makeText(this, "Journal entry required", Toast.LENGTH_SHORT).show();
                return;
            }
            journal.put("name", edtJournalTitle.getText().toString());
            journal.put("text", edtJournalEntry.getText().toString());
            journal.put("rightArmPain", tvRightArmPain.getText().toString());
            journal.put("leftArmPain", tvLeftArmPain.getText().toString());
            journal.put("rightLegPain", tvRightLegPain.getText().toString());
            journal.put("leftLegPain", tvLeftLegPain.getText().toString());
            journal.put("backPain", tvBackPain.getText().toString());
            journal.put("neckPain", tvNeckPain.getText().toString());
            hud.show();
            journal.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                    if (e == null) {
                        ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery("JournalImage");
                        photoQuery.whereEqualTo("journalId", "");
                        photoQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    ParseObject.deleteAllInBackground(objects, new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {

                                                EventBus.getDefault().post(new EventPush("updateJournals", "Journals"));
                                                finish();
                                            }
                                        }
                                    });
                                }else{
                                    EventBus.getDefault().post(new EventPush("updateJournals", "Journals"));
                                    finish();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(AddJournalActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public byte[] selectImageBytes(File imageFile) {
        int size = (int) imageFile.length();
        byte[] selecteBytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imageFile));
            buf.read(selecteBytes, 0, selecteBytes.length);
            buf.close();
            return selecteBytes;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new byte[0];
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new byte[0];
        }

    }

    public void showSelector(CustomFontTextView cfv){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setSingleChoiceItems(items, selectedPainIndex, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AddJournalActivity.this);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPainIndex = n;
                        cfv.setText(items[n]);
                        dialog.dismiss();
                        d.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.setTitle("Warning");
                alert.setMessage("Are you sure this is your pain level?\nOnce you have added this journal entry, your pain level cannot be changed.");
                alert.show();

            }

        });
        adb.setNegativeButton("Cancel", null);
        adb.show();

    }

    @Override
    public void onSelect(int position) {
        Uri object =  photos.get(position);
        Intent intent = new Intent(AddJournalActivity.this, ImageViewActivity.class);
        intent.putExtra("image", object.toString());
        startActivity(intent);
    }

    @Override
    public void onDelete(int position) {
        ParseObject object =  photosArray.get(position);
        hud.show();
        object.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
                if (e == null) {
                    fetchImages();
                }else{

                }
            }
        });
    }

    @Override
    public void onAddPhoto() {
        checkCameraPermission();
    }

    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddJournalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddJournalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
        } else {
            chooseImage();
        }

    }

    private void chooseImage() {
        EasyImage.openChooserWithGallery(this, "Choose your profile photo" , 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, AddJournalActivity.this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File file = null;
                    try {
                        file = saveImage(imageFile);
                        uploadImage(file);
                        selectImageBytes(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddJournalActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    uploadImage(imageFile);
                    selectImageBytes(imageFile);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage();
                } else {
                    Toast.makeText(this, "Permission denined", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private File saveImage(File cameraImage) throws IOException {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap original = BitmapFactory.decodeFile(cameraImage.getAbsolutePath(),bmOptions);
        Bitmap finalBitmap = rotateImageIfRequired(original, Uri.fromFile(cameraImage));
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String fname = "image_profile.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void uploadImage(File file) {
        if (hud != null) {
            if (!hud.isShowing()) {
                hud.show();
            }
        }
        byte[] bytes = selectImageBytes(file);
        ParseFile imageFile = new ParseFile("image.jpg", bytes);
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseObject object = new ParseObject("JournalImage");
                    object.put("imageFile", imageFile);
                    object.put("creatorId", ParseUser.getCurrentUser().getObjectId());
                    object.put("journalId", journalId);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (hud != null) {
                                if (hud.isShowing()) {
                                    hud.dismiss();
                                }
                            }
                            if (e == null) {
                                fetchImages();
                            }else{
                                Toast.makeText(AddJournalActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    if (hud != null) {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                    }
                    Toast.makeText(AddJournalActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
