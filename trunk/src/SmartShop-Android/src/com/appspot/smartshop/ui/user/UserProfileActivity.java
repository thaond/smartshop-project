package com.appspot.smartshop.ui.user;

import com.appspot.smartshop.adapter.CurrentUserProfileAdapter;
import com.appspot.smartshop.adapter.ViewUserProfileAdapter;
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
		if (Global.isLogin) {
			list.setAdapter(new CurrentUserProfileAdapter(this));
		} else {
			list.setAdapter(new ViewUserProfileAdapter(this, username));
		}
	}
}
