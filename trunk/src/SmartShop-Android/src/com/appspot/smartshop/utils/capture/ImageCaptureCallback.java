package com.appspot.smartshop.utils.capture;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import com.appspot.smartshop.utils.Global;

public class ImageCaptureCallback implements PictureCallback {
	private Activity activity;

	public ImageCaptureCallback(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
			// Callback here
			Intent intent = new Intent();
			intent.putExtra(Global.BYTE_ARRAY_INTENT_ID, data);
			activity.setResult(Activity.RESULT_OK, intent);
			activity.finish();
			
			// Write into SDCARD
			// Log.v(getClass().getSimpleName(), "onPictureTaken=" + data +
			// " length = " + data.length);
			// inputStream.write(data);
			// inputStream.flush();
			// inputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}