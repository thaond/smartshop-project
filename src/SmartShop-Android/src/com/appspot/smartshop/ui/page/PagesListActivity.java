package com.appspot.smartshop.ui.page;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.PageAdapter;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.mock.MockPage;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class PagesListActivity extends Activity {
	public static final String TAG = "[PagesListActivity]";
	
	public static final int PAGE_MOST_VIEW = 0;
	public static final int PAGE_MOST_UPDATE = 1;
	
	public static final int PAGES_OF_USER = 1;
	public static final int NORMAL_PAGES = 0;
	
	private int type = NORMAL_PAGES;
	
	private String username;
	
	private static PagesListActivity instance = null;
	
	private List<Page> pages = null;
	
	private ListView listPages;
	private PageAdapter adapter;

	private EditText txtSearch;
	
	public static PagesListActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pages_list);
		
		instance = this;
		
		// type of pages list
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			type = bundle.getInt(Global.PAGES_TYPE);
			if (type == PAGES_OF_USER) {
				username = bundle.getString(Global.PAGES_OF_USER);
			} else {
				username = Global.username;
			}
		}
		
		// search field
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchPages();
			}
		});
		
		// checkboxes
		chPageMostUpdate = (CheckBox) findViewById(R.id.chPageMostUpdate);
		chPageMostView = (CheckBox) findViewById(R.id.chPageMostView);
		
		// load pages to listview
		listPages = (ListView) findViewById(R.id.listPages);
		loadPagesList();
	}

	protected void searchPages() {
		String query = txtSearch.getText().toString();
		
		if (query == null || query.trim().equals("")) {
			loadPagesList();
		} else {
			Log.d(TAG, "query = " + query);
			constructUrl();
			url += "&query=" + query;
			loadData();
		}
	}

	private SimpleAsyncTask task;
	private String url = null;

	private CheckBox chPageMostUpdate;

	private CheckBox chPageMostView;
	protected void loadPagesList() {
		Log.d(TAG, "load pages list");
		
		constructUrl();
		loadData();
	}
	
	public void searchByCategories(Set<String> categories) {
		if (categories.size() == 0) {
			return;
		}
		
		constructUrl();
		url += "&cat_keys=";
		for (String cat : categories) {
			url += cat + ",";
		}
		
		loadData();
	}
	
	private void constructUrl() {
		// criteria
		String criteria = "";
		if (chPageMostUpdate.isChecked()) {
			criteria += "1,";
		}
		if (chPageMostView.isChecked()) {
			criteria += "5,";
		}
		
		// construct url
		url = null;
		if (!criteria.equals("")) {
			criteria = criteria.substring(0, criteria.length() - 1);
			url = String.format(URLConstant.GET_PAGES_BY_CRITERIA, criteria);
		} else {
			url = URLConstant.GET_PAGES;
		}
		
		if (type == PAGES_OF_USER) {
			url += "&user" + username;
//			url = String.format(URLConstant.GET_PAGES_OF_USER, username);
		}
	}
	
	private void loadData() {
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
				adapter = new PageAdapter(PagesListActivity.this, 0, pages);
				listPages.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("pages");
						pages = Global.gsonWithHour.fromJson(arr.toString(), Page.getType());
						Log.d(TAG, "load " + pages.size() + " pages");
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