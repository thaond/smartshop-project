/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appspot.smartshop.utils;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.dom.SmartshopNotification;

public class NotifyingService extends Service {
	public static final String TAG = "[NotifyingService]";
	public static final String PARAM_NOFITICATION = "{username:\"%s\",type_id:%d}";
	
	private int numOfNotifications = 0;
    
    // variable which controls the notification thread 
    private ConditionVariable mCondition;
 
    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread notifyingThread = new Thread(null, mTask, "NotifyingService");
        mCondition = new ConditionVariable(false);
        notifyingThread.start();
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
    	for (int i = 0; i < numOfNotifications; ++i) {
    		notificationManager.cancel(i);
    	}
    	
        // Stop the thread from generating further notifications
        mCondition.open();
    }

    private Runnable mTask = new Runnable() {
        public void run() {
        	while (!Global.isStop) {
        		// request new notificaiton from server
        		Log.d(TAG, "[LOAD NOTIFICATIONS]");
        		loadNotifications();
        		
        		// pause an interval
        		mCondition.block(Global.UPDATE_INTERVAL);
        	}
            
            // Done with our work...  stop the service!
//            NotifyingService.this.stopSelf();
        }
    };
    
    private List<SmartshopNotification> notifications;
    private SimpleAsyncTask task;
    private void loadNotifications() {
		String param = String.format(PARAM_NOFITICATION, Global.username, 1);
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
					task.hasData = false;
					task.message = getString(R.string.warn_no_notification);
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
				task.hasData = false;
				task.message = message;
				task.cancel(true);
			}
		});
	}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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

    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    private NotificationManager notificationManager;
}
