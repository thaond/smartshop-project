package com.appspot.smartshop.ui.page;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;

public class EditPageActivity extends Activity {
	
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
		
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			mode = CREATE_MODE;
			page = new Page();
		} else {
			mode = EDIT_MODE;
			// TODO get data from intent to fill in page object
		}
		
		txtName = (TextView) findViewById(R.id.txtName);
		txtContent = (TextView) findViewById(R.id.txtContent);
		
		Button btnOk = (Button) findViewById(R.id.btnOk);
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
	}

	protected void createPage() {
	}
}
