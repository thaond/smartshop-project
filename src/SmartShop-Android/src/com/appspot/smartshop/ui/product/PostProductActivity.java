package com.appspot.smartshop.ui.product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class PostProductActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();

		// Intent basicIntent = new Intent(this,
		// PostProductActivityBasicAttribute.class);
		// basicIntent.putExtra(Global.PRODUCT_INFO,MockProductInfo.getInstance());
		// basicIntent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, true);
		tabHost.addTab(tabHost.newTabSpec("Basic").setIndicator(
				"Thông tin cơ bản").setContent(
				new Intent(this, PostProductActivityBasicAttribute.class)));
		tabHost.addTab(tabHost.newTabSpec("Advance").setIndicator(
				"Thông tin chi tiết").setContent(
				new Intent(this, PostProductActivityUserDefine.class)));
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
	}
}
