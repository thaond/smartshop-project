package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.mock.MockProductInfo;
import com.appspot.smartshop.utils.Global;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class PostProductActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();

		Intent basicIntent = new Intent(this, PostProductActivityBasicAttribute.class);
//		basicIntent.putExtra(Global.PRODUCT_INFO,MockProductInfo.getInstance());
		basicIntent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, true);
		tabHost.addTab(tabHost.newTabSpec("Basic Features").setIndicator(
				"Basic Features").setContent(basicIntent));
		tabHost.addTab(tabHost.newTabSpec("User Define").setIndicator(
				"Advanced Fearture").setContent(
				new Intent(this, PostProductActivityUserDefine.class)));
	}
}
