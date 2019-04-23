package com.landonferrier.healthcareapp.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.inputmethod.InputMethodManager;

import com.landonferrier.healthcareapp.activity.MainActivity;
import com.landonferrier.healthcareapp.activity.MainDrawerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;

public class Helper {

	private static Helper helperInstance;


	public static String surgeryTypeFor(String id) {

		if (id.equals(Constants.surgeryIdLumbarStenosis)) {

			return SurgeryType.LumbarStenosis;
		} else if (id.equals(Constants.surgeryIdLumbarFusion)) {

			return SurgeryType.LumbarFusion;
		} else if (id.equals(Constants.surgeryIdLumbarDisectomy)) {

			return SurgeryType.LumbarDisectomy;
		} else if (id.equals(Constants.surgeryIdLumbarAnteriorCervicalDiscectomyFusion)) {

			return SurgeryType.ACDF;
		}

		return "";
	}

	private Helper() {

	}

	public static Helper getHelperInstance() {

		if (null == helperInstance) {
			helperInstance = new Helper();
		}
		return helperInstance;

	}

	public static final String RECORDING_PATH = Environment
			.getExternalStorageDirectory() + "/Recordings";
	public static final String LOAD_RECORDINGS = "Load Records";


	public ArrayList<String> getAllFileInDirectory(File directory) {

		final File[] files = directory.listFiles();
		ArrayList<String> listOfRecordings = new ArrayList<String>();

		if (files != null) {
			for (File file : files) {
				if (file != null) {
					if (file.isDirectory()) { // it is a folder...
						getAllFileInDirectory(file);
					} else { // it is a file...

						listOfRecordings.add(file.getAbsolutePath());
					}
				}
			}
		}
		return listOfRecordings;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager =
				(InputMethodManager) activity.getSystemService(
						Activity.INPUT_METHOD_SERVICE);
		if (activity != null) {
			if (activity.getCurrentFocus() != null) {
				if(activity.getCurrentFocus().getWindowToken() != null) {
					inputMethodManager.hideSoftInputFromWindow(
							activity.getCurrentFocus().getWindowToken(), 0);
				}
			}
		}
	}


	public ArrayList<String> getAllRecordings() {
		return getAllFileInDirectory(new File(RECORDING_PATH));

	}

	public boolean createRecordingFolder() {

		if (!new File(RECORDING_PATH).exists()) {

			return new File(RECORDING_PATH).mkdir();
		} else {
			return true;
		}

	}


	public static void showLoginAlert(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle("Warning!").setMessage("You should sign in first.").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				activity.startActivity(new Intent(activity, MainDrawerActivity.class));
				activity.finish();
			}
		});
		builder.show();
	}

	public static String getFormattedDate(long smsTimeInMilis) {
		Calendar smsTime = Calendar.getInstance();
		smsTime.setTimeInMillis(smsTimeInMilis);

		Calendar now = Calendar.getInstance();

		final String timeFormatString = "h:mm aa";
		final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
		final long HOURS = 60 * 60 * 60;
		if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
			return String.valueOf(DateFormat.format(timeFormatString, smsTime));
		} else {
			return DateFormat.format("MM/dd", smsTime).toString();
		}
	}

}
