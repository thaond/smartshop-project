package com.appspot.smartshop;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.appspot.smartshop.adapter.MainAdapter;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.ui.user.notification.SmartShopNotificationService;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;

public class SmartShopActivity extends ListActivity {
	public static final String TAG = "[SmartShopActivity]";

	public static final String PRODUCT = "product";
	public static final String PAGE = "page";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO test compass manager
		Global.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// Init for constant
		Global.application = this;
		Global.drawableNoAvatar = getResources().getDrawable(
				R.drawable.no_avatar);

		ListView listView = getListView();
		listView.setBackgroundResource(R.color.background);

		listView.setAdapter(new MainAdapter(this));

		// Init notification manager
		if (Global.isLogin) {
			System.out.println("test = "
					+ SmartShopNotificationService.isRunning);
			if (!SmartShopNotificationService.isRunning) {
				if (Global.notificationManager == null) {
					Global.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				}
				Global.notificationManager.cancelAll(); // remove all old
				// notifications

				Log.d(TAG, "[START NOTIFICATION SERVICE]");
				Global.isWaitingForNotifications = true;
				startService(new Intent(this,
						SmartShopNotificationService.class));
			}
		}

		// Remove Notification
		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				SmartshopNotification notification = (SmartshopNotification) getIntent()
						.getExtras().get(Global.NOTIFICATION);
				if (notification != null) {
					Log.d(TAG, "remove notification " + notification.id);
					Global.notificationManager.cancel(notification.id);
					Global.notifications.remove(notification);
					Utils.markNotificationAsRead(notification.id);
				}
			}
		}

		// Init for Facebook
//		if (Global.facebookUtils == null) {
//			Global.facebookUtils = new FacebookUtils(this);
//		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Utils.clearAllNotifications();

		if (!Global.isLogin) {
			Log.d(TAG, "[STOP NOTIFICATION SERVICE]");
			Global.isWaitingForNotifications = false;
			stopService(new Intent(this, SmartShopNotificationService.class));
		}

		// TODO: store session
		// if (Utils.isLogined())
		// Utils.storeSessionState(Global.userInfo.sessionId);
	}
	
	private static final int MENU_EXIT = 0;
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_EXIT, 0,
				getString(R.string.exit)).setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_EXIT:
			exit();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void exit() {
		Global.isLogin = false;
		Global.userInfo = null;
		
		Utils.clearAllNotifications();
		Global.isWaitingForNotifications = false;
		Global.application.stopService(new Intent(this, SmartShopNotificationService.class));
		
		System.exit(0);
	}
}
