package com.appspot.smartshop.ui.user.notification;

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
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.ui.product.ViewProductActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class SmartShopNotificationService extends Service {
	public static final String TAG = "[NotifyingService]";
	public static final String PARAM_NOFITICATION = "{username:\"%s\",type_id:%d}";
    
    // variable which controls the notification thread 
    private ConditionVariable mCondition;
 
    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread notifyingThread = new Thread(null, mTask, "SmartShopNotificationService");
        mCondition = new ConditionVariable(false);
        notifyingThread.start();
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
    	notificationManager.cancelAll();
    	
        // Stop the thread from generating further notifications
        mCondition.open();
    }

    private Runnable mTask = new Runnable() {
        public void run() {
        	while (Global.isWaitingForNotifications) {
        		// request new notificaiton from server
        		Log.d(TAG, "[LOAD NOTIFICATIONS]");
        		loadNotifications();
        		
        		// pause an interval
        		mCondition.block(Global.UPDATE_INTERVAL);
        	}
            
            // Done with our work...  stop the service!
            SmartShopNotificationService.this.stopSelf();
        }
    };
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    private NotificationManager notificationManager;
    private String loadNotificationsFailureMessage = null;
    private void loadNotifications() {
    	String username = Global.userInfo.username;
    	if (username == null || username.trim().equals("")) {
    		return;
    	}
    	
		String url = String.format(URLConstant.GET_NOTIFICATIONS, username, 1);
		RestClient.getData(url, new JSONParser() {
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				// load
				JSONArray arr = json.getJSONArray("notifications");
				Global.notifications = Global.gsonWithHour.fromJson(arr.toString(), 
						SmartshopNotification.getType());
				Log.d(TAG, "found " + Global.notifications.size() + " notification(s)");

				// display
				if (Global.notifications.size() != 0) {
					String content = null;
					for (SmartshopNotification notification : Global.notifications) {
						content = notification.content;
						showNotification(notification);
					}
				}
			}

			@Override
			public void onFailure(String message) {
				loadNotificationsFailureMessage = message;
			}
		});
		
		// TODO: can't make Toast inside another thread, use Handler here
		if (loadNotificationsFailureMessage != null) {
			Toast.makeText(Global.application, loadNotificationsFailureMessage, 
					Toast.LENGTH_SHORT).show();
			Log.d(TAG, "[STOP SMARTSHOP NOTIFICATION SERVICE BECAUSE CAN'T LOAD CONNECT TO NETWORK]");
			Global.isWaitingForNotifications = false;
			SmartShopNotificationService.this.stopSelf();
			
			loadNotificationsFailureMessage = null;
		}
	}

	private void showNotification(SmartshopNotification sNotification) {
		if (sNotification == null || sNotification.jsonOutput == null) {
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
			Page page = Global.gsonDateWithoutHour.fromJson(sNotification.jsonOutput, Page.class);
			intent.putExtra(Global.NOTIFICATION, sNotification);
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
        // notification.  Note the use of FLAG_UPDATE_CURRENT so that if there
        // is already an active matching pending intent, we will update its
        // extras to be the ones passed in here.
		PendingIntent contentIntent  = PendingIntent.getActivity(this, 0, intent, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification(
				android.R.drawable.btn_star_big_on, null, System.currentTimeMillis());
		notification.setLatestEventInfo(this, sNotification.getTitle(),
				sNotification.content, contentIntent);
		Global.notificationManager.notify(sNotification.id, notification);
	}

}
