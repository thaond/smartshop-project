package com.appspot.smartshop;

import sv.skunkworks.showtimes.lib.asynchronous.HttpService;
import sv.skunkworks.showtimes.lib.asynchronous.ServiceCallback;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import com.appspot.smartshop.mock.MockCategory;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.product.ProductsListActivity;
import com.appspot.smartshop.ui.product.SearchByCategoryActivity;
import com.appspot.smartshop.ui.user.LoginActivity;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.ui.user.UserProfileActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.JsonObject;

public class MainActivity extends TabActivity {
	public static final String TAG = "[MainActivity]";

	public static final String TAB_LIST_PRODUCTS = "list_products";
	public static final String TAB_LIST_PAGES = "list_pages";
	public static final String TAB_SEARCH_PRODUCTS = "search_products";

	public static final String PRODUCT = "product";
	public static final String PAGE = "page";

	private String type = PRODUCT;

	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartshop);

		Global.application = this;

		tabHost = getTabHost();
		Intent intent;

		// Products tab
		intent = new Intent(this, ProductsListActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_LIST_PRODUCTS).setIndicator(
				getString(R.string.tab_list_products)).setContent(intent));

		// Pages tab
		intent = new Intent(this, PagesListActivity.class);
		intent.putExtra(Global.PAGES_LIST_TYPE,
				PagesListActivity.ALL_PAGES);
		tabHost.addTab(tabHost.newTabSpec(TAB_LIST_PAGES).setIndicator(
				getString(R.string.tab_list_pages)).setContent(intent));

		// listeners
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(TAB_LIST_PRODUCTS)) {
					Log.d(TAG, "tab products");
					type = PRODUCT;
					loadProductsList();
				} else if (tabId.equals(TAB_LIST_PAGES)) {
					Log.d(TAG, "tab pages");
					type = PAGE;
					loadPagesList();
				}
			}
		});
		// set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
	}

	protected void loadPagesList() {
		// TODO (condorhero01): request pages list
	}

	protected void loadProductsList() {
	}

	public static final int MENU_LOGIN = 0;
	public static final int MENU_REGISTER = 1;
	public static final int MENU_USER_PROFILE = 2;
	public static final int MENU_SEARCH_BY_CATEGORIES = 3;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_LOGIN, 0, getString(R.string.lblLogin)).setIcon(
				R.drawable.login);
		menu.add(0, MENU_REGISTER, 0, getString(R.string.lblRegister)).setIcon(
				R.drawable.register);
		menu.add(0, MENU_USER_PROFILE, 0, getString(R.string.user_profile))
				.setIcon(R.drawable.user_profile);
		menu.add(0, MENU_SEARCH_BY_CATEGORIES, 0,
				getString(R.string.search_by_categories)).setIcon(
				R.drawable.category);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case MENU_LOGIN:
			intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			break;

		case MENU_REGISTER:
			intent = new Intent(MainActivity.this, UserActivity.class);
			startActivity(intent);
			break;

		case MENU_USER_PROFILE:
			if (!Global.isLogin) {
				Toast.makeText(this,
						getString(R.string.errMustLoginToViewProfile),
						Toast.LENGTH_SHORT).show();
			} else {
				intent = new Intent(MainActivity.this,
						UserProfileActivity.class);
				intent.putExtra(Global.USER_NAME, Global.username);
				startActivity(intent);
			}

			break;

		case MENU_SEARCH_BY_CATEGORIES:
			intent = new Intent(this, SearchByCategoryActivity.class);
			intent.putExtra(Global.TYPE, type);
			intent.putExtra(Global.CATEGORY_INFO, MockCategory.getInstance());
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
