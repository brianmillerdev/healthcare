package com.landonferrier.healthcareapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.EventPush;
import com.landonferrier.healthcareapp.views.CircleImageView;
import com.landonferrier.healthcareapp.views.CustomFontEditText;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_STORAGE = 212;

    @BindView(R.id.btn_back)
    public ImageView btnBack;

    @BindView(R.id.btn_logout)
    public CustomFontTextView btnLogout;

    @BindView(R.id.edt_name)
    public CustomFontEditText tvFullName;

    @BindView(R.id.btn_privacy_settings)
    public CustomFontTextView btnPrivacySettings;

    @BindView(R.id.btn_terms_service)
    public CustomFontTextView btnTermsOfService;

    @BindView(R.id.imv_profile_placeholder)
    public ImageView imvPlaceHolder;

    @BindView(R.id.imv_profile)
    public CircleImageView imvProfile;

    @BindView(R.id.tv_add_new_surgery)
    public CustomFontTextView tvAddNewSurgery;

    @BindView(R.id.tv_change_current_surgery)
    public CustomFontTextView tvChangeCurrentSurgery;

    @BindView(R.id.view_current_surgery)
    public LinearLayout viewCurrentSurgery;

    @BindView(R.id.tv_currnet_surgery_date)
    public CustomFontTextView tvCurrentSugeryDate;

    @BindView(R.id.tv_current_surgery_name)
    public CustomFontTextView tvCurrentSurgeryName;

    @BindView(R.id.tv_remove_surgery)
    public CustomFontTextView tvRemoveSurgery;
    @BindView(R.id.tv_change_surgery_date)
    public CustomFontTextView tvChangeSurgeryDate;
    byte[] selecteBytes;
    boolean isImageSelected = false;

    KProgressHUD hud;
    String surgeryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnPrivacySettings.setOnClickListener(this);
        btnTermsOfService.setOnClickListener(this);
        imvPlaceHolder.setOnClickListener(this);
        imvProfile.setOnClickListener(this);
        tvAddNewSurgery.setOnClickListener(this);
        tvChangeCurrentSurgery.setOnClickListener(this);
        tvRemoveSurgery.setOnClickListener(this);
        tvCurrentSurgeryName.setOnClickListener(this);
        tvChangeSurgeryDate.setOnClickListener(this);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventPush event) {
        if (event.getMessage().equals("updateCurrentSurgery")) {
            initView();
        }else if (event.getMessage().equals("updateSurgery")) {
            initView();
        }
    }


    public void initView() {
        ParseUser currentUser = ParseUser.getCurrentUser();
//        JSONArray surgeruies = currentUser.getJSONArray("surgeryIds");
//        assert surgeruies != null;
//        if (surgeruies.length() > 0) {
        if (currentUser.has("currentSurgeryId")) {
            viewCurrentSurgery.setVisibility(View.VISIBLE);
            surgeryId = currentUser.getString("currentSurgeryId");
            ParseObject surgery = ParseObject.createWithoutData("Surgery", surgeryId);
            if (hud != null) {
                if (!hud.isShowing()) {
                    hud.show();
                }
            }
            surgery.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject o, ParseException e) {
                    if (hud != null) {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                    }
                    if (e == null) {
                        String name = o.getString("name");
                        tvCurrentSurgeryName.setText(name);
                        if (ParseUser.getCurrentUser().get("surgeryDates") != null) {
                            JSONObject object = ParseUser.getCurrentUser().getJSONObject("surgeryDates");
                            if (object.has(o.getObjectId())) {
                                try {
                                    String dateStr = object.getString(o.getObjectId());
                                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                                    Date date = format.parse(dateStr);
                                    SimpleDateFormat format1 = new SimpleDateFormat("MMMM d, yyyy");
                                    String dateString = format1.format(date);
                                    tvCurrentSugeryDate.setText(String.format("Scheduled for %s.", dateString));
                                    tvCurrentSugeryDate.setVisibility(View.VISIBLE);
                                    tvChangeSurgeryDate.setVisibility(View.VISIBLE);

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (java.text.ParseException e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                tvCurrentSugeryDate.setVisibility(View.GONE);
                            }
                        } else {
                            tvCurrentSugeryDate.setVisibility(View.GONE);
                        }

                    } else {
                        Log.e("error", e.getLocalizedMessage());
                    }
                }
            });
        }else{
            viewCurrentSurgery.setVisibility(View.GONE);
        }
        tvFullName.setText(currentUser.getString("fullName"));
        if (currentUser.get("imageFile") != null) {
            imvProfile.setVisibility(View.VISIBLE);
            imvPlaceHolder.setVisibility(View.INVISIBLE);
            ParseFile file = (ParseFile) currentUser.get("imageFile");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imvProfile.setImageBitmap(bmp);
                    }else{
                        Log.e("error", "photo downloading error");
                    }
                }
            });
        }else{
            imvProfile.setVisibility(View.INVISIBLE);
            imvPlaceHolder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                updateProfile();
                break;
            case R.id.btn_logout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(ProfileActivity.this, SigninActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.btn_privacy_settings:
                startActivity(new Intent(ProfileActivity.this, PrivacySettingsActivity.class));
                break;
            case R.id.btn_terms_service:
                startActivity(new Intent(ProfileActivity.this, TermsOfConditionsActivity.class));
                break;
            case R.id.imv_profile:
                checkCameraPermission();
                break;
            case R.id.imv_profile_placeholder:
                checkCameraPermission();
                break;

            case R.id.tv_current_surgery_name:
                Intent currentSurgery = new Intent(ProfileActivity.this, SurgeryActivity.class);
                currentSurgery.putExtra("type", "new");
                startActivity(currentSurgery);
                break;
            case R.id.tv_add_new_surgery:
                Intent addNewSurgery = new Intent(ProfileActivity.this, SurgeryActivity.class);
                addNewSurgery.putExtra("type", "new");
                startActivity(addNewSurgery);
                break;
            case R.id.tv_change_current_surgery:
                Intent changeSurgery = new Intent(ProfileActivity.this, ChangeRemoveSurgeryActivity.class);
                changeSurgery.putExtra("type", "change");
                startActivityForResult(changeSurgery, 300);
                break;
            case R.id.tv_change_surgery_date:
                Intent changeDate = new Intent(ProfileActivity.this, SurgeryDateActivity.class);
                changeDate.putExtra("id", surgeryId);
                changeDate.putExtra("name", tvCurrentSurgeryName.getText().toString());
                startActivityForResult(changeDate, 300);
                break;
            case R.id.tv_remove_surgery:
                Intent deleteSurgery = new Intent(ProfileActivity.this, ChangeRemoveSurgeryActivity.class);
                deleteSurgery.putExtra("type", "remove");
                startActivity(deleteSurgery);
                break;
        }
    }

    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
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
        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {

                finish();
            }
        }else{
            EasyImage.handleActivityResult(requestCode, resultCode, data, ProfileActivity.this, new DefaultCallback() {
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
                            Picasso.get()
                                    .load(file)
                                    .fit()
                                    .centerCrop()
                                    .into(imvProfile);
                            selectImageBytes(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Picasso.get()
                                .load(imageFile)
                                .fit()
                                .centerCrop()
                                .into(imvProfile);
                        selectImageBytes(imageFile);
                    }
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                }
            });
        }

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

    public void selectImageBytes(File imageFile) {
        int size = (int) imageFile.length();
        selecteBytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imageFile));
            buf.read(selecteBytes, 0, selecteBytes.length);
            buf.close();
            isImageSelected = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void updateProfile() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }

        if (!currentUser.getString("fullName").equals(tvFullName.getText().toString())){
            currentUser.put("fullName", tvFullName.getText().toString());
        }
        if (isImageSelected) {
            ParseFile imageFile = new ParseFile("image.jpg", selecteBytes);
            imageFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        currentUser.put("imageFile", imageFile);
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    finish();
                                }else{
                                    Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        finish();
                    }else{
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
