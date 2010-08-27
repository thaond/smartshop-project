package com.appspot.smartshop.ui.page;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.PageAdapter;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.mock.MockPage;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.SimpleAsyncTask;

public class PagesListActivity extends Activity {
	public static final String TAG = "[PagesListActivity]";
	
	public static final int PAGE_MOST_VIEW = 0;
	public static final int PAGE_MOST_UPDATE = 1;
	
	public static final int PAGES_OF_CATEGORIES = 0;
	public static final int PAGES_OF_USER = 1;
	public static final int ALL_PAGES = 2;
	
	private int pagesListMode = PAGE_MOST_VIEW;
	private int pagesListType = ALL_PAGES;
	
	private Page[] arrPages = null;
	
	private ListView listPages;
	private PageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pages_list);
		
		// type of pages list
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Log.d(TAG, "view pages of user " + Global.username);
			pagesListType = getIntent().getExtras().getInt(Global.PAGES_LIST_TYPE);
		}
		
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

	protected void loadPagesList() {
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new PageAdapter(PagesListActivity.this, 0, arrPages);
				listPages.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				switch (pagesListType) {
				case PAGES_OF_CATEGORIES:
					// TODO (condorhero01): request pages list of categories
					break;
					
				case PAGES_OF_USER:
					// TODO (condorhero01): request pages list of Global.username
					
					break;
					
				case ALL_PAGES:
					// TODO (condorhero01): request all pages
					
					break;
				}
				arrPages = MockPage.getPages();
			}
		}).execute();
	}
}
