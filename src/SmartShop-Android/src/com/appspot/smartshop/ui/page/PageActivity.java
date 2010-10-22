package com.appspot.smartshop.ui.page;

import java.util.Date;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.mock.MockUserInfo;
import com.appspot.smartshop.ui.product.ViewProductBasicAttributeActivity;
import com.appspot.smartshop.ui.user.ViewUserInfoActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;

public class PageActivity extends Activity {
	public static final String TAG = "[PageActivity]";
	
	private static final int EDIT_MODE = 1;
	private static final int CREATE_MODE = 0;

	private Page page = new Page();
	private TextView txtName;
	private TextView txtContent;
	private int mode;
	private SimpleAsyncTask task;

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
				processPage();
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button btnCategories = (Button) findViewById(R.id.btnCategories);
		if (mode == EDIT_MODE) {
			btnCategories.setEnabled(false);
		}
		btnCategories.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choosePageCategories();
			}
		});
	}

	protected void choosePageCategories() {
		CategoriesDialog.showCategoriesDialog(this, new CategoriesDialogListener() {
			
			@Override
			public void onCategoriesDialogClose(Set<String> categories) {
				page.setCategoryKeys = categories;
			}
		});
	}
	
	private String url = "";
	protected void processPage() {
		switch (mode) {
		case CREATE_MODE:
			url = URLConstant.CREATE_PAGE; 
			break;
			
		case EDIT_MODE:
			url = URLConstant.EDIT_PAGE;
			break;
		}
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
			}
			
			@Override
			public void loadData() {
				// set attributes for page
				page.content = txtContent.getText().toString();
				page.name = txtName.getText().toString();
				page.date_post = new Date();
				page.username = Global.userInfo.username;
				page.page_view++;
				
				String param = Global.gsonWithHour.toJson(page);
				
				RestClient.postData(url, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
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
}
