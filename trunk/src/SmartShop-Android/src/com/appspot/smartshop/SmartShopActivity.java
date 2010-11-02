package com.appspot.smartshop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.adapter.MainAdapter;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.facebook.core.AsyncFacebookRunner;
import com.appspot.smartshop.facebook.core.Facebook;
import com.appspot.smartshop.facebook.core.Util;
import com.appspot.smartshop.facebook.utils.FacebookUtils;
import com.appspot.smartshop.facebook.utils.SessionEvents;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.ui.product.ViewProductActivity;
import com.appspot.smartshop.ui.user.notification.AddFriendNotificationActivity;
import com.appspot.smartshop.ui.user.notification.SmartShopNotificationService;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class SmartShopActivity extends ListActivity {
	public static final String TAG = "[SmartShopActivity]";

	public static final String PRODUCT = "product";
	public static final String PAGE = "page";
	// public static final String PARAM_NOFITICATION =
	// "{username:\"%s\",type_id:%d}";
	private int numOfNotifications = 0;
	private SimpleAsyncTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Init for constant
		Global.application = this;
		Global.drawableNoAvatar = getResources().getDrawable(
				R.drawable.no_avatar);

		ListView listView = getListView();
		// Config layout
		listView.setBackgroundResource(R.color.background);

		// Check whether user login or not
		// if (Global.userInfo == null) {
		// String session = Utils.loadSession();
		// Log.d(TAG, "Session: " + session);
		// if (StringUtils.isEmptyOrNull(session))
		// Global.isLogin = false;
		// else {
		// String url = String.format(URLConstant.GET_USER_INFO_SESSION,
		// session);
		// RestClient.getData(url, new JSONParser() {
		//
		// @Override
		// public void onSuccess(JSONObject json) throws JSONException {
		// Global.isLogin = true;
		// Global.userInfo = Global.gsonDateWithoutHour
		// .fromJson(json.get("userinfo").toString(),
		// UserInfo.class);
		// }
		//
		// @Override
		// public void onFailure(String message) {
		// Log.d(TAG, message);
		// }
		// });
		// }
		//
		// }
		listView.setAdapter(new MainAdapter(this));

		if (Global.mapParentCategories.size() != 0) {
			Log.d(TAG, "[DONT NEED TO GET CATEGORIES LIST]");
		} else {
			Log.d(TAG, "[GET CATEGORIES LIST]");
			task = new SimpleAsyncTask(getString(R.string.init_data), this,
					new DataLoader() {

						@Override
						public void updateUI() {
						}

						@Override
						public void loadData() {
							getCategoriesList();
						}
					});
			task.execute();
		}

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

	private void getCategoriesList() {
		// get parent categories
		RestClient.getData(URLConstant.GET_PARENT_CATEGORIES, new JSONParser() {

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				JSONArray arr = json.getJSONArray("categories");

				int len = arr.length();
				String name, key_cat;
				for (int i = 0; i < len; ++i) {
					name = arr.getJSONObject(i).getString("name");
					key_cat = arr.getJSONObject(i).getString("key_cat");

					Global.mapParentCategories.put(key_cat, name);
				}
			}

			@Override
			public void onFailure(String message) {
				task.hasData = false;
				task.message = message;
				task.cancel(true);

				Toast.makeText(SmartShopActivity.this,
						getString(R.string.cannot_connect_network),
						Toast.LENGTH_SHORT).show();
			}
		});

		// get child categories
		for (final String parentId : Global.mapParentCategories.keySet()) {
			String url = String.format(URLConstant.GET_CHILD_CATEGORIES,
					parentId);

			RestClient.getData(url, new JSONParser() {

				@Override
				public void onSuccess(JSONObject json) throws JSONException {
					JSONArray arr = json.getJSONArray("categories");

					int len = arr.length();
					String name, key_cat;
					String[] temp = new String[len];
					for (int k = 0; k < len; ++k) {
						name = arr.getJSONObject(k).getString("name");
						key_cat = arr.getJSONObject(k).getString("key_cat");

						temp[k] = name;

						Global.mapChildrenCategories.put(key_cat, name);
						Global.mapChildrenCategoriesName.put(name, key_cat);
					}

					Global.listCategories.add(temp);
				}

				@Override
				public void onFailure(String message) {
					task.hasData = false;
					task.message = message;
					task.cancel(true);
				}
			});
		}
	}

	private void loadNotifications() {
		String url = String.format(URLConstant.GET_NOTIFICATIONS,
				Global.userInfo.username, 1);
		RestClient.getData(url, new JSONParser() {
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				// load
				JSONArray arr = json.getJSONArray("notifications");
				Global.notifications.clear();
				Global.notifications = Global.gsonWithHour.fromJson(arr
						.toString(), SmartshopNotification.getType());
				Log.d(TAG, "found " + Global.notifications.size()
						+ " notification(s)");

				// display
				if (Global.notifications.size() != 0) {
					String content = "";
					int count = 0;
					for (SmartshopNotification notification : Global.notifications) {
						content = notification.content;
						showNotification(notification);
						count++;
					}

					numOfNotifications += Global.notifications.size();
				}
			}

			@Override
			public void onFailure(String message) {
				Toast.makeText(SmartShopActivity.this, message,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showNotification(SmartshopNotification sNotification) {
		if (sNotification == null || sNotification.jsonOutput == null) {
			// Toast.makeText(this,
			// getString(R.string.warn_cant_view_notification_detail),
			// Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = null;
		switch (sNotification.type) {
		case SmartshopNotification.ADD_FRIEND:
			intent = new Intent(this, AddFriendNotificationActivity.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
			break;

		case SmartshopNotification.TAG_PRODUCT:
		case SmartshopNotification.UNTAG_PRODUCT:
			intent = new Intent(this, ViewProductActivity.class);
			ProductInfo productInfo = Global.gsonWithHour.fromJson(
					sNotification.jsonOutput, ProductInfo.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
			intent.putExtra(Global.PRODUCT_INFO, productInfo);
			break;

		case SmartshopNotification.TAG_PAGE:
		case SmartshopNotification.UNTAG_PAGE:
			intent = new Intent(this, ViewPageActivity.class);
			Page page = Global.gsonDateWithoutHour.fromJson(
					sNotification.jsonOutput, Page.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
			// Log.d(TAG, "put notification id for ViewPage with id = " +
			// sNotification.id);
			intent.putExtra(Global.PAGE, page);
			break;

		case SmartshopNotification.ADD_COMMENT_PRODUCT:
			intent = new Intent(this, ViewCommentsActivity.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
			intent.putExtra(Global.TYPE_OF_COMMENTS, SmartShopActivity.PRODUCT);
			intent.putExtra(Global.ID_OF_COMMENTS, Long
					.parseLong(sNotification.jsonOutput));
			break;

		case SmartshopNotification.ADD_COMMENT_PAGE:
			intent = new Intent(this, ViewCommentsActivity.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
			intent.putExtra(Global.TYPE_OF_COMMENTS, SmartShopActivity.PAGE);
			intent.putExtra(Global.ID_OF_COMMENTS, Long
					.parseLong(sNotification.jsonOutput));
			break;

		default:
			intent = new Intent(this, SmartShopActivity.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
			break;
		}

		// The PendingIntent to launch our activity if the user selects this
		// notification. Note the use of FLAG_UPDATE_CURRENT so that if there
		// is already an active matching pending intent, we will update its
		// extras to be the ones passed in here.
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification(
				android.R.drawable.btn_star_big_on, null, System
						.currentTimeMillis());
		notification.setLatestEventInfo(this, sNotification.getTitle(),
				sNotification.content, contentIntent);
		Global.notificationManager.notify(sNotification.id, notification);
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
}
