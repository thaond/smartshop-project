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
	
	public static final int PAGES_OF_CATEGORIES = 0;
	public static final int PAGES_OF_USER = 1;
	public static final int ALL_PAGES = 2;
	
	private static PagesListActivity instance = null;
	
	private int pagesListMode = PAGE_MOST_VIEW;
	private int pagesListType = ALL_PAGES;
	
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
			Log.d(TAG, "view pages of user " + Global.username);
			pagesListType = getIntent().getExtras().getInt(Global.PAGES_LIST_TYPE);
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
		
		// radio buttons
		RadioButton rbPageMostView = (RadioButton) findViewById(R.id.rbPageMostView);
		rbPageMostView.setChecked(true);
		rbPageMostView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					pagesListMode = PAGE_MOST_VIEW;
					loadPagesList();
				}
			}
		});
		
		RadioButton rbPageMostUpdate = (RadioButton) findViewById(R.id.rbPageMostUpdate);
		rbPageMostUpdate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					pagesListMode = PAGE_MOST_UPDATE;
					loadPagesList();
				}
			}
		});
		
		// load pages to listview
		listPages = (ListView) findViewById(R.id.listPages);
		loadPagesList();
	}

	protected void searchPages() {
		// TODO (condorhero01): request pages list based on query string
		String query = txtSearch.getText().toString();
		Log.d(TAG, "query = " + query);
		
		url = String.format(URLConstant.GET_PAGES_BY_QUERY, query);
		loadPagesList();
	}

	private SimpleAsyncTask task;
	private String url = null;
	protected void loadPagesList() {
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
				adapter = new PageAdapter(PagesListActivity.this, 0, pages);
				listPages.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				switch (pagesListType) {
				case PAGES_OF_CATEGORIES:
					break;
					
				case PAGES_OF_USER:
					url = String.format(URLConstant.GET_PAGES_OF_USER, Global.username);
					break;
					
				case ALL_PAGES:
					// TODO all pages url
					break;
				}
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("pages");
//						pages = Global.gsonWithHour.fromJson(arr.toString(), Page.getType());
						pages = Utils.gson.fromJson(arr.toString(), Page.getType());
						Log.d(TAG, "load " + pages.size() + " pages");
					}
					
					@Override
					public void onFailure(String message) {
						task.cancel(true);
					}
				});
			}
		});
		
		task.execute();
	}
	
	public void searchByCategories(Set<String> categories) {
		// TODO append category id to request pages 
		
		pagesListType = PAGES_OF_CATEGORIES;
	}
}
