package com.appspot.smartshop;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.adapter.MainAdapter;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.xtify.android.sdk.PersistentLocationManager;

public class SmartShopActivity extends ListActivity {
	public static final String TAG = "[SmartShopActivity]";

	public static final String PRODUCT = "product";
	public static final String PAGE = "page";
	
	public static final String PARAM_NOFITICATION = "{username:\"%s\",type_id:%d}";
	private List<SmartshopNotification> notifications;
	private int numOfNotifications = 0;
	private NotificationManager notificationManager;

	private SimpleAsyncTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Global.application = this;

		ListView listView = getListView();
		listView.setAdapter(new MainAdapter(this));
		
		if (Global.isLogin) {
			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Log.d(TAG, "[LOAD NOTIFICATIONS]");
			loadNotifications();
		}

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

		Global.persistentLocationManager = new PersistentLocationManager(
				SmartShopActivity.this);
		Global.persistentLocationManager
				.setNotificationIcon(R.drawable.notification);
		Global.persistentLocationManager
				.setNotificationDetailsIcon(R.drawable.icon);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getString(R.string.settings));
		menu.findItem(0).setIcon(R.drawable.menu_preferences);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0: {
			Global.persistentLocationManager.showSettingsActivity(this, true);
			return true;
		}
		}
		return false;
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
		String param = String.format(PARAM_NOFITICATION, Global.userInfo.username, 1);
		Log.d(TAG, param);
		
		RestClient.postData(URLConstant.GET_NOTIFICATIONS, param, new JSONParser() {
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				// load
				JSONArray arr = json.getJSONArray("notifications");
				notifications = Global.gsonWithHour.fromJson(arr.toString(), 
						SmartshopNotification.getType());
				Log.d(TAG, "found " + notifications.size() + " notification(s)");
				
				// display
				if (notifications.size() == 0) {
					Toast.makeText(SmartShopActivity.this, getString(R.string.warn_no_notification), 
							Toast.LENGTH_SHORT).show();
				} else {
					String title;
					String content;
					
					int count = 0;
					for (SmartshopNotification notification : notifications) {
						title = notification.date.toLocaleString();
						content = notification.content;
						showNotification(count + numOfNotifications, title, content);
						count++;
					}
					
					numOfNotifications += notifications.size();
				}
			}

			@Override
			public void onFailure(String message) {
				Toast.makeText(SmartShopActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    private void showNotification(int id, String title, String content) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SmartShopActivity.class), 0);

        Notification notification = new Notification(android.R.drawable.btn_star_big_on, 
        		null, System.currentTimeMillis());
        notification.setLatestEventInfo(this, title, content, contentIntent);
        // TODO custom view for notification

        notificationManager.notify(id, notification);
    }
}
