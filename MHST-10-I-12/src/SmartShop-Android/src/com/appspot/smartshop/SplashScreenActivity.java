package com.appspot.smartshop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {
	public static final String TAG = "[SpashScreenActivity]";
	public static final int HANDLER_MSG_WAIT = 1;
	public static final int HANDLER_CANT_CONNECT_TO_NETWORK = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        
        new Thread() {
        	public void run() {
        		// Load notification
        		if (Global.mapParentCategories.size() > 0 && Global.mapChildrenCategories.size() > 0
        				&& Global.mapChildrenCategoriesName.size() > 0) {
        			Log.d(TAG, "[DONT NEED TO GET CATEGORIES LIST]");
        			mHandler.sendEmptyMessageDelayed(HANDLER_MSG_WAIT, 4000);
        		} else {
        			Log.d(TAG, "[GET CATEGORIES LIST]");
        			Utils.loadCategories(SplashScreenActivity.this);
        			mHandler.sendEmptyMessage(HANDLER_MSG_WAIT);
        		}
        	};
        }.start();
    }
    
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_MSG_WAIT:
				Intent intent = new Intent(getApplicationContext(), SmartShopActivity.class);
				startActivity(intent);
				finish();
				break;
				
			case HANDLER_CANT_CONNECT_TO_NETWORK:
				Toast.makeText(getApplicationContext(), getString(R.string.cannot_connect_network),
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}
    	
    };
}
