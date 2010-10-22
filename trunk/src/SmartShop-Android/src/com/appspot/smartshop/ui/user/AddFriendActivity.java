package com.appspot.smartshop.ui.user;

import java.util.LinkedList;

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
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.AddFriendAdapter;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.mock.MockAddFriend;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

public class AddFriendActivity extends Activity {
	public static final String TAG = "AddFriendActivity";
	public static final String PARAM_SEARCH_FRIENDS = "{username:\"%s\"}";
	public static final String PARAM_ADD_FRIENDS = "{username:\"%s\",friends:%s}";
	public static String friendsToAdd = "";
	public AddFriendAdapter adapter;
	public ListView friendList;
	public LinkedList<UserInfo> friends;
	public Button btnFriendSearch;
	public TextView txtFriendSearch;
	public Button btnAddFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: just for test, please remove after test
		Global.application = this;
		
		setContentView(R.layout.add_friend);
		friends = new LinkedList<UserInfo>();
		friendList = (ListView) findViewById(R.id.listFriend);
		txtFriendSearch = (TextView) findViewById(R.id.txtFriendSearch);
		btnFriendSearch = (Button) findViewById(R.id.btnFriendSearch);
		btnFriendSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadFriendList();
				
			}
		});
		btnAddFriend = (Button) findViewById(R.id.btnAddFriends);
		btnAddFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFriends();
				
			}
		});
		adapter = new AddFriendAdapter(this, 0, MockAddFriend.getInstance());
		friendList.setAdapter(adapter);
//		Log.d(TAG, "is executed");
//		loadFriendList();
	}
	public static SimpleAsyncTask taskAddFriend;
	boolean isSuccess = false;
	protected void addFriends() {
		final String param = String.format(PARAM_ADD_FRIENDS, Global.userInfo.username, friendsToAdd);
		Log.d(TAG,param);
		taskAddFriend = new SimpleAsyncTask(this, new DataLoader() {	
			@Override
			public void updateUI() {	
			}
			@Override
			public void loadData() {
				Log.d(TAG,"loadData");
				RestClient.postData(URLConstant.ADD_FRIENDS_TO_LIST, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						Log.d(TAG, json.toString());
						isSuccess = true;
						
					}
					
					@Override
					public void onFailure(String message) {
						Log.d(TAG,message);
						
					}
				});
			}
		});
		
		taskAddFriend.execute();
		if(isSuccess){
			Toast.makeText(AddFriendActivity.this, getString(R.string.addFriendSuccess), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(AddFriendActivity.this, getString(R.string.addFriendFail), Toast.LENGTH_SHORT).show();
		}
	}
	public static SimpleAsyncTask task;

	protected void loadFriendList() {
		String param = String.format(PARAM_SEARCH_FRIENDS, txtFriendSearch.getText().toString());
		Log.d(TAG, param);
		task = new SimpleAsyncTask(this, new DataLoader() {
			@Override
			public void updateUI() {
				adapter = new AddFriendAdapter(AddFriendActivity.this, R.layout.add_friend_item, friends);
				friendList.setAdapter(adapter);
				
			}
			String url = URLConstant.SEARCH_FRIEND_BY_QUERY + txtFriendSearch.getText().toString();
			@Override
			public void loadData() {
				RestClient.getData(url, new JSONParser() {
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("userinfos");
						friends = Global.gsonWithHour.fromJson(arr.toString(), UserInfo.getType());	
					}
					@Override
					public void onFailure(String message) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});	
		task.execute();
	}
		
}
