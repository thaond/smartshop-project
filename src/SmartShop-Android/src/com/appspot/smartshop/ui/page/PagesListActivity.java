package com.appspot.smartshop.ui.page;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.PageAdapter;
import com.appspot.smartshop.dom.Page;

public class PagesListActivity extends Activity {
	
	public static final int PAGE_MOST_VIEW = 0;
	public static final int PAGE_MOST_UPDATE = 1;
	
	private int pagesListType = PAGE_MOST_VIEW;
	
	private Page[] arrPages = null;
	
	private ListView listPages;
	private PageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pages_list);
		
		// radio buttons
		RadioButton rbPageMostView = (RadioButton) findViewById(R.id.rbPageMostView);
		rbPageMostView.setChecked(true);
		rbPageMostView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					pagesListType = PAGE_MOST_VIEW;
					loadPagesList();
				}
			}
		});
		
		RadioButton rbPageMostUpdate = (RadioButton) findViewById(R.id.rbPageMostUpdate);
		rbPageMostUpdate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					pagesListType = PAGE_MOST_UPDATE;
					loadPagesList();
				}
			}
		});
		
		// listview
		listPages = (ListView) findViewById(R.id.listPages);
		adapter = new PageAdapter(this, 0);
		
		// TODO (condorhero01): load list of pages
//		new AsyncTask<String, Void, Void>() {
//
//			@Override
//			protected Void doInBackground(String... params) {
//				return null;
//			}
//			
//		}.execute();
	}

	protected void loadPagesList() {
		// TODO (condorhero01): load list of pages to arrPages
		
		listPages.setAdapter(adapter);
	}
}
