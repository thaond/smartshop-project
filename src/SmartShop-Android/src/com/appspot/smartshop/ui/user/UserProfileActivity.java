package com.appspot.smartshop.ui.user;

import com.appspot.smartshop.adapter.UserProfileAdapter;
import com.appspot.smartshop.utils.Global;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class UserProfileActivity extends ListActivity {
	
	public static final String TAG = "[UserProfileActivity]";
	
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		username = getIntent().getExtras().getString(Global.USER_NAME);
		Log.d(TAG, "profile of " + username);
		
		ListView list = getListView();
		list.setAdapter(new UserProfileAdapter(this));
	}
}
