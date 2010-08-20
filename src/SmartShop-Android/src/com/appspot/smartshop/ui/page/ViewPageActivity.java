package com.appspot.smartshop.ui.page;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.utils.Global;

public class ViewPageActivity extends Activity {
	
	private Page page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_page);
		
		// TODO get page object from intent
		Bundle bundle = getIntent().getExtras();
		page = (Page) bundle.get("page");
		
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
	}
}
