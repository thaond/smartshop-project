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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SpashScreenActivity extends Activity {
	public static final String TAG = "[SpashScreenActivity]";
	public static final int HANDLER_MSG_WAIT = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_screen);
        
        new Thread() {
        	public void run() {
        		// Load notification
                if (Global.mapParentCategories.size() != 0) {
        			Log.d(TAG, "[DONT NEED TO GET CATEGORIES LIST]");
        			mHandler.sendEmptyMessageDelayed(HANDLER_MSG_WAIT, 2000);
        		} else {
        			Log.d(TAG, "[GET CATEGORIES LIST]");
        			getCategoriesList();
        			mHandler.sendEmptyMessage(HANDLER_MSG_WAIT);
        		}
        	};
        }.start();
    }
    
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent(getApplicationContext(), SmartShopActivity.class);
			startActivity(intent);
			finish();
		}
    	
    };
    
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
				Toast.makeText(getApplicationContext(), getString(R.string.cannot_connect_network),
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
					Toast.makeText(getApplicationContext(), getString(R.string.cannot_connect_network),
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
