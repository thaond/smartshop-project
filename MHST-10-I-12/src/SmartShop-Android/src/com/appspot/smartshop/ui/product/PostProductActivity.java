package com.appspot.smartshop.ui.product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Utils;

public class PostProductActivity extends TabActivity {
	public static final String PRODUCT_BASIC_INFO = "basic";
	public static final String PRODUCT_USER_DEFINED_INFO = "user_defined";
	public static final String PRODUCT_UPLOAD_IMAGE = "upload_images";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_BASIC_INFO).setIndicator(
				getString(R.string.product_basic_info)).setContent(
				new Intent(this, ProductBasicAttributeActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_UPLOAD_IMAGE).setIndicator(
				getString(R.string.upload_image)).setContent(
				new Intent(this, ProductUploadImagesActivity.class)));
//		tabHost.addTab(tabHost.newTabSpec(PRODUCT_USER_DEFINED_INFO)
//				.setIndicator(getString(R.string.product_user_defined_info))
//				.setContent(
//						new Intent(this,
//								ProductUserDefinedAttributeActivity.class)));
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, getString(R.string.return_to_home)).setIcon(R.drawable.home);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			Utils.returnHomeActivity(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
