package com.appspot.smartshop;

import com.appspot.smartshop.adapter.MainAdapter;
import com.appspot.smartshop.utils.Global;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class SmartShopActivity extends ListActivity {
	
	public static final String PRODUCT = "product";
	public static final String PAGE = "page";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Global.application = this;
		
		ListView listView = getListView();
		listView.setAdapter(new MainAdapter(this));
	}
}
