package com.appspot.smartshop.ui.page;

import java.util.Date;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.FriendListDialog;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;

public class PageActivity extends BaseUIActivity {
	public static final String TAG = "[PageActivity]";

	private static final int EDIT_MODE = 1;
	private static final int CREATE_MODE = 0;

	private Page page = new Page();
	private TextView txtName;
	private TextView txtContent;
	private int mode;
	private SimpleAsyncTask task;

	@Override
	protected void onCreatePre() {
		setContentView(R.layout.edit_page);
	}

	@Override
	protected void onCreatePost(Bundle savedInstanceState) {
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
			btnCategories.setVisibility(View.GONE);
		}
		btnCategories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				choosePageCategories();
			}
		});

		// Button tag friend
		Button btnTagFriend = (Button) findViewById(R.id.tag);
		if (mode == EDIT_MODE) {
			btnTagFriend.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					tagFriendToProduct();
				}
			});
		} else {
			btnTagFriend.setVisibility(View.GONE);
		}

		// button untag friend from product
		Button btnUntagFriend = (Button) findViewById(R.id.un_tag);
		if (mode == EDIT_MODE) {
			btnUntagFriend.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					untagFriendToProduct();
				}
			});
		} else {
			btnUntagFriend.setVisibility(View.GONE);
		}
	}

	protected void tagFriendToProduct() {
		FriendListDialog.tagType = FriendListDialog.PAGE;
		FriendListDialog.showTagFriendDialog(this, page.id);
	}

	protected void untagFriendToProduct() {
		FriendListDialog.tagType = FriendListDialog.PAGE;
		FriendListDialog.showUntagFriendDialog(this, page.id);
	}

	protected void choosePageCategories() {
		CategoriesDialog.showCategoriesDialog(this,
				new CategoriesDialogListener() {

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
			url = String.format(URLConstant.CREATE_PAGE, Global.getSession());
			break;

		case EDIT_MODE:
			Log.e(TAG, "edit mode");
			url = String.format(URLConstant.EDIT_PAGE, Global.getSession());
			break;
		}
		
		Log.e(TAG, "Old json: " + Global.gsonWithHour.toJson(page));

		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
			}

			@Override
			public void loadData() {
				// set attributes for page
				page.content = txtContent.getText().toString();
				page.name = txtName.getText().toString();
//				page.date_post = new Date();
				page.username = Global.userInfo.username;
				page.page_view++;

				String param = Global.gsonWithHour.toJson(page);
				Log.e(TAG, "New json: " + Global.gsonWithHour.toJson(page));

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
