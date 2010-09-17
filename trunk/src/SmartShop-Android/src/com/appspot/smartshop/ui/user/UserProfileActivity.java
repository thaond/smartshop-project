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
               
                Bundle bundle = getIntent().getExtras();
                username = bundle.getString(Global.USER_NAME);
                Boolean canEditProfile = bundle.getBoolean(Global.CAN_EDIT_USER_PROFILE);
                Log.d(TAG, "profile of " + username);
                Log.d(TAG, "can edit profile = " + canEditProfile);
               
                ListView list = getListView();
                if (canEditProfile != null && canEditProfile == true) {
                        list.setAdapter(new CurrentUserProfileAdapter(this));
                } else {
                        list.setAdapter(new ViewUserProfileAdapter(this, username));
                }
        }
}

