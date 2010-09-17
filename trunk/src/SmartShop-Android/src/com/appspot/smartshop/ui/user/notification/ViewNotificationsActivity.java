package com.appspot.smartshop.ui.user.notification;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.adapter.NotificationAdapter;
import com.appspot.smartshop.dom.MyNotification;
import com.appspot.smartshop.mock.MockNotification;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

import com.appspot.smartshop.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

public class ViewNotificationsActivity extends Activity {
	public static String charTicker = "Bạn có %d thông báo mới";
	public static final String TAG = "[ViewNotificationsActivity]";
	public static final String NOTIFICATIONS_PARAM = "{username:\"%s\",type_id:%d}";
	public static final String MARK_AS_READ = "{username:\"%s\"}";
	public NotificationAdapter adapter;
	public ListView listView;
	public LayoutInflater inflater;
	public List<MyNotification> notifications;
	public long id;
	public String username = Global.username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view notifications");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		adapter = new NotificationAdapter(this, 0, MockNotification
				.getInstance());
		listView = (ListView) findViewById(R.id.listNotification);
		// listView.setAdapter(adapter);
		loadNotifications();
	}

	private void markAsRead() {
		Log.d(TAG, "MARK AS READ CALLED");
		String param = String.format(MARK_AS_READ, username);
		RestClient.postData(URLConstant.MARK_AS_READ_ALL_NOTIFICATIONS, param,
				new JSONParser() {

					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						Log.d(TAG, json.toString());

					}

					@Override
					public void onFailure(String message) {
						Log.d(TAG, message);

					}
				});
	}
	public SimpleAsyncTask task;
	public void loadNotifications() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			@Override
			public void updateUI() {
				adapter = new NotificationAdapter(
						ViewNotificationsActivity.this, 0, notifications);
				listView.setAdapter(adapter);
			}
			@Override
			public void loadData() {
				String param = String.format(NOTIFICATIONS_PARAM, username, 1);
				Log.d(TAG, param);
				RestClient.postData(URLConstant.GET_NOTIFICATIONS, param,
						new JSONParser() {

							@Override
							public void onSuccess(JSONObject json)
									throws JSONException {
								JSONArray arr = json
										.getJSONArray("notifications");
								notifications = Global.gsonWithHour.fromJson(
										arr.toString(), MyNotification
												.getType());
								Log.d(TAG, "found " + notifications.size()
										+ " notification(s)");
								if (notifications.size() == 0) {
									task.hasData = false;
									task.message = getString(R.string.warn_no_notification);
								} else {
//									markAsRead();
									CharSequence title;
									CharSequence content;
									for(int i = 0 ; i< notifications.size();i++){
										title = notifications.get(i).date.toLocaleString();
										content = notifications.get(i).content;
										generateNotification(notifications.size(),i,title, content);
									}
								}
							}

							@Override
							public void onFailure(String message) {
								task.cancel(true);

							}
						});
			}
		});
		task.execute();

	}

	public void generateNotification(int numOfNewNotification,int notificatioinID,CharSequence charTitle, CharSequence charContent) {
		NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		long when = System.currentTimeMillis();
		charTicker = String.format(charTicker, numOfNewNotification);
		Notification notification = new Notification(
				android.R.drawable.btn_star_big_on, charTicker, when);
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this,
				ViewNotificationsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, charTitle, charContent,
				contentIntent);
		myNotificationManager.notify(notificatioinID, notification);
	}

}
