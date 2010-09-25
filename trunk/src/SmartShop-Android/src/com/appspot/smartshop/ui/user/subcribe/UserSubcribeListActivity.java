package com.appspot.smartshop.ui.user.subcribe;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.UserSubcribeAdapter;
import com.appspot.smartshop.dom.UserSubcribeProduct;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

public class UserSubcribeListActivity extends Activity {
	public static final String TAG = "[UserSubcribeListActivity]";

	private List<UserSubcribeProduct> subcribes;
	private UserSubcribeAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subcribe_list);
		
		Button btnAddSubcribe = (Button) findViewById(R.id.btnAddSubcribe);
		btnAddSubcribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addNewSubcribe();
			}
		});
		
		listSubcribe = (ListView) findViewById(R.id.listSubcribe);
		loadSubcribeList();
	}

	private SimpleAsyncTask task;
	private ListView listSubcribe;
	private void loadSubcribeList() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new UserSubcribeAdapter(UserSubcribeListActivity.this, 0, subcribes);
				listSubcribe.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				// TODO remove Global.username=duc after test
				String url = String.format(URLConstant.GET_USER_SUBCRIBES, Global.username);
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("usersubscribeproducts");
						subcribes = Global.gsonWithHour.fromJson(arr.toString(), 
								UserSubcribeProduct.getType());
						Log.d(TAG, "load " + subcribes.size() + " subcribes");
					}
					
					@Override
					public void onFailure(String message) {
						task.hasData = false;
						task.message = message;
						task.cancel(true);
					}
				});
			}
		});
		task.execute();
	}

	protected void addNewSubcribe() {
		// TODO add new subcribe
		Log.d(TAG, "[ADD NEW SUBCRIBE]");
	}
}
