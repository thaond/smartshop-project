package com.appspot.smartshop.ui.user.notification;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

/**
 * 
 * @author VoMinhTam
 */
public class AddFriendNotificationActivity extends BaseUIActivity {
	private static final String TAG = "[AddFriendNotification]";
	private TextView lblName;
	private TextView lblEmail;
	private ImageView imgAvatar;
	private TextView lblDetail;
	private Button btnOK;
	private Button btnCancel;
	private UserInfo userInfo;
	private SmartshopNotification notification;

	@Override
	protected void onCreatePre() {
		setContentView(R.layout.add_friend_confirm);
	}

	@Override
	protected void onCreatePost(Bundle savedInstanceState) {
		lblName = (TextView) findViewById(R.id.lblName);
		lblEmail = (TextView) findViewById(R.id.lblEmail);
		imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
		lblDetail = (TextView) findViewById(R.id.lblDetail);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = String.format(URLConstant.ADD_FRIENDS_TO_LIST,
						Global.userInfo.sessionId, userInfo.username);
				RestClient.getData(url, new JSONParser() {

					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						Log.d(TAG, json.toString());
						Toast.makeText(AddFriendNotificationActivity.this,
								json.getString("message"), Toast.LENGTH_SHORT)
								.show();
						Utils
								.returnHomeActivity(AddFriendNotificationActivity.this);
					}

					@Override
					public void onFailure(String message) {
						Log.d(TAG, message);
					}
				});
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.returnHomeActivity(AddFriendNotificationActivity.this);
			}
		});

		Intent intent = getIntent();
		notification = (SmartshopNotification) intent
				.getSerializableExtra(Global.NOTIFICATION);
		if (notification == null) {
			Toast.makeText(this, getString(R.string.invalid_intent),
					Toast.LENGTH_SHORT).show();
			Utils.returnHomeActivity(AddFriendNotificationActivity.this);
		} else {
			showData();
		}
	}

	private void showData() {
		userInfo = Global.gsonDateWithoutHour.fromJson(
				notification.jsonOutput, UserInfo.class);
		if (StringUtils.isEmptyOrNull(userInfo.avatarLink)) {
			imgAvatar.setBackgroundDrawable(Global.drawableNoAvatar);
		} else {
			try {
				Bitmap bitmapAvatar = Utils
						.getBitmapFromURL(userInfo.avatarLink);
				bitmapAvatar = Bitmap.createScaledBitmap(bitmapAvatar, 150,
						150, true);
				imgAvatar.setImageBitmap(bitmapAvatar);
			} catch (Exception e) {
				e.printStackTrace();
				imgAvatar.setBackgroundDrawable(Global.drawableNoAvatar);
			}
		}

		lblName.setText(getString(R.string.name) + " " + userInfo.first_name
				+ " " + userInfo.last_name);
		lblEmail.setText(getString(R.string.email) + " " + userInfo.email);
		lblDetail.setText(notification.content);
		
		// Cancel this notification
		Log.d(TAG, "remove notification" + notification.id);
		Global.notificationManager.cancel(notification.id);
		Global.notifications.remove(notification);
		Utils.markNotificationAsRead(notification.id);
	}

};