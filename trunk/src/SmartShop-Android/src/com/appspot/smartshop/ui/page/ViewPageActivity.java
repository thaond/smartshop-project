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
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
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
		
		Button btnViewComment = (Button) findViewById(R.id.btnViewComment);
		btnViewComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewComment();
			}
		});
	}

	protected void viewComment() {
		Intent intent = new Intent(this, ViewCommentsActivity.class);
		intent.putExtra(Global.ID_OF_COMMENTS, page.id);
		
		startActivity(intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
}
