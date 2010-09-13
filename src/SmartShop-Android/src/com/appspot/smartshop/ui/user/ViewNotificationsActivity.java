package com.appspot.smartshop.ui.user;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.adapter.NotificationAdapter;
import com.appspot.smartshop.dom.Notification;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

import com.appspot.smartshop.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

public class ViewNotificationsActivity extends Activity{
	public static final String TAG = "[ViewNotificationsActivity]";
	public static final String NOTIFICATIONS_PARAM = "{username:\"%s\",type_id:%d}";
	public NotificationAdapter adapter;
	public ListView listView;
	public LayoutInflater inflater;
	public List<Notification> notifications;
	public long id;
	public String username = "loi";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view notifications");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		adapter = new NotificationAdapter(this, 0, new LinkedList<Notification>());
		listView.setAdapter(adapter);
		loadNotifications();
	}
	public SimpleAsyncTask task;
	private void loadNotifications() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new NotificationAdapter(ViewNotificationsActivity.this, 0, notifications);
				listView.setAdapter(adapter);
				
			}
			
			@Override
			public void loadData() {
				String param = String.format(NOTIFICATIONS_PARAM,username,1);
				RestClient.postData(URLConstant.GET_NOTIFICATIONS, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("notifications");
						notifications = Global.gsonWithHour.fromJson(arr.toString()	, Notification.getType());
						Log.d(TAG, "found " + notifications.size() + " notification(s)");
						if(notifications.size()==0){
							task.hasData = false;
							task.message = getString(R.string.warn_no_notification);
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

}
