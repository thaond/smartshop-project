package com.appspot.smartshop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.appspot.smartshop.adapter.MainAdapter;
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

	private SimpleAsyncTask task;
	private PersistentLocationManager persistentLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Global.application = this;

		ListView listView = getListView();
		listView.setAdapter(new MainAdapter(this));

		/**************************** Init data *********************************/
		Log.d(TAG, "[START NOTIFICATION SERVICE]");
		// TODO start service notification
		// startService(new Intent(this, NotifyingService.class));

		if (Global.mapParentCategories.size() != 0) {
			Log.d(TAG, "[DONT NEED TO GET CATEGORIES LIST]");
		} else {
			Log.d(TAG, "[GET CATEGORIES LIST]");
			task = new SimpleAsyncTask(getString(R.string.init_data), this,
					new DataLoader() {

						@Override
						public void updateUI() {
							// TODO test related with categories dialog
							// Intent intent = new
							// Intent(SmartShopActivity.this,
							// UserSubcribeListActivity.class);
							// startActivity(intent);
						}

						@Override
						public void loadData() {
							getCategoriesList();
						}
					});
			task.execute();
		}

		Thread xtifyThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e(TAG, "Thread run");
				persistentLocationManager = new PersistentLocationManager(
						SmartShopActivity.this);
				persistentLocationManager
						.setNotificationIcon(R.drawable.notification);
				persistentLocationManager
						.setNotificationDetailsIcon(R.drawable.icon);
				boolean trackLocation = persistentLocationManager
						.isTrackingLocation();
				boolean deliverNotifications = persistentLocationManager
						.isDeliveringNotifications();
				Log.e(TAG, persistentLocationManager.getUserKey());
				if (trackLocation || deliverNotifications) {
					persistentLocationManager.startService();
				}
			}
		});
		xtifyThread.start(); // to avoid Android's application-not-responding
								// dialog box, do non-essential work in another
								// thread
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getString(R.string.settings) );
		menu.findItem(0).setIcon(R.drawable.menu_preferences);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0: {
				persistentLocationManager.showSettingsActivity(this, true);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d(TAG, "[STOP NOTIFICATION SERVICE]");
		Global.isStop = true;
		// TODO stop service notification
		// stopService(new Intent(this, NotifyingService.class));
	}
}
