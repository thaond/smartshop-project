package com.appspot.smartshop.ui.page;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.utils.Global;
import com.google.gson.Gson;

public class PageActivity extends Activity {
	public static final String TAG = "[PageActivity]";
	
	private static final int EDIT_MODE = 1;
	private static final int CREATE_MODE = 0;

	private Page page;
	private TextView txtName;
	private TextView txtContent;
	private int mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_page);
		
		// textviews
		txtName = (TextView) findViewById(R.id.txtName);
		txtContent = (TextView) findViewById(R.id.txtContent);
		
		// create page mode or edit page mode
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			mode = CREATE_MODE;
		} else {
			mode = EDIT_MODE;
			
			// TODO (condorhero01): get data from intent to fill in page object
			page = (Page) bundle.get(Global.PAGE);
			txtName.setText(page.name);
			txtContent.setText(page.content);
		}
		
		// buttons
		Button btnOk = (Button) findViewById(R.id.btnOk);
		if (mode == EDIT_MODE) {
			btnOk.setText(getString(R.string.lblUpdate));
		}
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (mode) {
				case CREATE_MODE:
					createPage();
					break;
					
				case EDIT_MODE:
					editPage();
					break;
					
				default:
					break;
				}
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected void editPage() {
		// set attributes for page
		page.content = txtContent.getText().toString();
		page.name = txtName.getText().toString();
		page.date_post = new Date();
		
		// TODO (condorhero01): request to server to update page
		Log.d(TAG, new Gson().toJson(page));
	}

	protected void createPage() {
		page = new Page();
		
		// set attributes for page
		page.content = txtContent.getText().toString();
		page.name = txtName.getText().toString();
		page.date_post = new Date();
		page.username = Global.username;
		
		// TODO (condorhero01): request to server to create new page
		Log.d(TAG, new Gson().toJson(page));
	}
}
