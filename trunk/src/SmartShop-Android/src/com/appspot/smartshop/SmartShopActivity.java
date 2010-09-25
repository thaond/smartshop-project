package com.appspot.smartshop;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.appspot.smartshop.adapter.MainAdapter;
import com.appspot.smartshop.ui.user.subcribe.SubcribeActivity;
import com.appspot.smartshop.ui.user.subcribe.UserSubcribeListActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.NotifyingService;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;

public class SmartShopActivity extends ListActivity {
	public static final String TAG = "[SmartShopActivity]";
	
	public static final String PRODUCT = "product";
	public static final String PAGE = "page";
	
	private SimpleAsyncTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Global.application = this;
		
		ListView listView = getListView();
		listView.setAdapter(new MainAdapter(this));
		
		/**************************** Init data *********************************/
		Log.d(TAG, "[START NOTIFICATION SERVICE]");
		// TODO start service notification
//		startService(new Intent(this, NotifyingService.class));
		
		Log.d(TAG, "[GET CATEGORIES LIST]");
		task = new SimpleAsyncTask(getString(R.string.init_data), this, new DataLoader() {
			
			@Override
			public void updateUI() {
				// TODO test related with categories dialog
				Intent intent = new Intent(SmartShopActivity.this, UserSubcribeListActivity.class);
				startActivity(intent);
			}
			
			@Override
			public void loadData() {
				getCategoriesList();
			}
		});
		task.execute();
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
			}
		});
		
		// get child categories
		for (final String parentId : Global.mapParentCategories.keySet()) {
			String url = String.format(URLConstant.GET_CHILD_CATEGORIES, parentId);
			
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.d(TAG, "[STOP NOTIFICATION SERVICE]");
		Global.isStop = true;
		// TODO stop service notification
//		stopService(new Intent(this, NotifyingService.class));
	}
}
