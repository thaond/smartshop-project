package com.appspot.smartshop.ui.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.ui.user.UserProfileActivity;
import com.appspot.smartshop.ui.user.ViewProfileActivity;
import com.appspot.smartshop.utils.Global;

public class ViewPageActivity extends Activity {
	public static final String TAG = "[ViewPageActivity]";
	
	private Page page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_page);
		
		// get page info from intent
		Bundle bundle = getIntent().getExtras();
		page = (Page) bundle.get(Global.PAGE);
		
		// display page info on form
		TextView txtUsername = (TextView) findViewById(R.id.txtUsername);
		txtUsername.setText(page.username);
		
		TextView txtName = (TextView) findViewById(R.id.txtName);
		txtName.setText(page.name);
		
		TextView txtPostDate = (TextView) findViewById(R.id.txtPostDate);
		txtPostDate.setText(Global.dfFull.format(page.date_post));
		
		EditText txtContent = (EditText) findViewById(R.id.txtContent);
		txtContent.setText(page.content);
		
		TextView txtPageView = (TextView) findViewById(R.id.txtPageView);
		txtPageView.setText(getString(R.string.page_view) + " " + page.page_view);
		
		// Buttons
		Button btnViewComment = (Button) findViewById(R.id.btnViewComment);
		btnViewComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewComment();
			}
		});
		
		Button btnViewUserInfo = (Button) findViewById(R.id.viewUserInfo);
		btnViewUserInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewUserProfile();
			}
		});
	}
	
	protected void viewUserProfile() {
		Intent intent = new Intent(this, ViewProfileActivity.class);
		intent.putExtra(Global.USER_NAME, page.username);
		if (Global.isLogin && Global.username.equals(page.username)) {
			intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
		}
		
		startActivity(intent);
	}

	protected void viewComment() {
		Intent intent = new Intent(this, ViewCommentsActivity.class);
		intent.putExtra(Global.ID_OF_COMMENTS, page.id);
		intent.putExtra(Global.TYPE_OF_COMMENTS, SmartShopActivity.PAGE);
		
		startActivity(intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
}
