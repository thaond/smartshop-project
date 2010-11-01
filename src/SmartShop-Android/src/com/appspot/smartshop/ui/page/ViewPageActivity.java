package com.appspot.smartshop.ui.page;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.ui.user.ViewUserInfoActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class ViewPageActivity extends BaseUIActivity {
	public static final String TAG = "[ViewPageActivity]";

	private Page page;

	@Override
	protected void onCreatePre() {
		setContentView(R.layout.view_page);
	}

	@Override
	protected void onCreatePost(Bundle savedInstanceState) {
		// get page info from intent
		Bundle bundle = getIntent().getExtras();
		
		page = (Page) bundle.get(Global.PAGE);
		Boolean isNormalPage = (Boolean) bundle.getBoolean(Global.IS_NORMAL_PAGE);
		isNormalPage = (isNormalPage != null) ? isNormalPage.booleanValue() : false;
		SmartshopNotification notification = (SmartshopNotification) bundle.get(Global.NOTIFICATION);
		if (notification != null) {
			Log.d(TAG, "remove notification " + notification.id);
			Global.notificationManager.cancel(notification.id);
			Global.notifications.remove(notification);
		} else {
			Log.e(TAG, "[NO NOTIFICATION FOR THIS PAGE]");
		}

		// display page info on form
		TextView txtUsername = (TextView) findViewById(R.id.txtUsername);
		txtUsername.setText(page.username);

		TextView txtName = (TextView) findViewById(R.id.txtName);
		txtName.setText(page.name);

		TextView txtPostDate = (TextView) findViewById(R.id.txtPostDate);
		txtPostDate.setText(Global.dfFull.format(page.date_post));

		EditText txtContent = (EditText) findViewById(R.id.txtContent);
		Utils.setEditableEditText(txtContent, false);
		txtContent.setText(page.content);

		TextView txtPageView = (TextView) findViewById(R.id.txtPageView);
		txtPageView.setText(getString(R.string.page_view) + " "
				+ page.page_view);

		// Buttons
		Button btnViewComment = (Button) findViewById(R.id.btnViewComment);
		btnViewComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewComment();
			}
		});

		Button btnViewUserInfo = (Button) findViewById(R.id.viewUserInfo);
		btnViewUserInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewUserProfile();
			}
		});

		// TODO: Remove Notification
//		System.out.println("test");
//		SmartshopNotification sNotification = (SmartshopNotification) getIntent()
//				.getSerializableExtra(Global.NOTIFICATION);
//		if (sNotification != null) {
//			System.out.println("remove noti");
//			Global.notifications.remove(sNotification);
//			Global.notificationManager.cancel(sNotification.id);
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, getString(R.string.return_to_home)).setIcon(R.drawable.home);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			Utils.returnHomeActivity(this);
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void viewUserProfile() {
		final Intent intent = new Intent(this, ViewUserInfoActivity.class);

		// get userinfo of page creator
		String url = String.format(URLConstant.GET_USER_INFO, page.username);
		RestClient.getData(url, new JSONParser() {

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				UserInfo userInfo = Global.gsonDateWithoutHour.fromJson(json
						.getString("userinfo"), UserInfo.class);
				intent.putExtra(Global.USER_INFO, userInfo);
				if (Global.isLogin
						&& Global.userInfo.username.equals(page.username)) {
					intent.putExtra(Global.CAN_EDIT_USER_INFO, true);
				}

				startActivity(intent);
			}

			@Override
			public void onFailure(String message) {
				Toast.makeText(ViewPageActivity.this, message,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void viewComment() {
		Intent intent = new Intent(this, ViewCommentsActivity.class);
		intent.putExtra(Global.ID_OF_COMMENTS, page.id);
		intent.putExtra(Global.TYPE_OF_COMMENTS, SmartShopActivity.PAGE);

		startActivity(intent);
	}
}
