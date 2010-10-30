package com.appspot.smartshop.ui.product;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.facebook.Facebook;
import com.appspot.smartshop.facebook.LoginButton;
import com.appspot.smartshop.facebook.SessionEvents;
import com.appspot.smartshop.facebook.SessionStore;
import com.appspot.smartshop.facebook.Util;
import com.appspot.smartshop.facebook.SessionEvents.AuthListener;
import com.appspot.smartshop.facebook.SessionEvents.LogoutListener;
import com.appspot.smartshop.map.SearchProductsOnMapActivity;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;
import com.google.android.maps.MapActivity;

public class ProductsListActivity extends MapActivity {
	public static final String TAG = "[ProductsListActivity]";
	public static final int BEST_SELLER_PRODUCTS = 0;
	public static final int CHEAPEST_PRODUCTS = 1;
	public static final int NEWEST_PRODUCTS = 2;
	private static ProductsListActivity instance = null;

	private CheckBox chMostUpdate, chCheapest, chMostView;
	private ListView listView;
	private ProductAdapter productAdapter;
	
	private LinkedList<ProductInfo> products;
	private EditText txtSearch;
	//set up variable for facebook connection
	private LoginButton mLoginButton;
	//end set up variable for facebook connection
	
	public static ProductsListActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_list);
		
		BaseUIActivity.initHeader(this);
		
		if (Global.APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running");
		}
		
		instance = this;
		//set up variable for facebook connection
		mLoginButton = (LoginButton) findViewById(R.id.loginFb);
		if(!Global.isLogin){
			mLoginButton.setVisibility(View.GONE);
		}else{
			mLoginButton.setVisibility(View.VISIBLE);
		}

		Global.mFacebook = new Facebook();
		if(Global.mFacebook.isSessionValid()){
			mLoginButton.setVisibility(View.GONE);
		}
		SessionStore.restore(Global.mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.init(Global.mFacebook, Global.PERMISSIONS);
		if(Global.mFacebook.isSessionValid()){
			mLoginButton.setVisibility(View.GONE);
		}
		// search field
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = txtSearch.getText().toString(); 
				if (query == null || query.trim().equals("")) {
					loadProductsList();
				} else {
					searchProductsByQuery(URLEncoder.encode(query));
				}
			}
		});
		
		Button btnSearchOnMap = (Button) findViewById(R.id.btnSearchOnMap);
		btnSearchOnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductsListActivity.this, SearchProductsOnMapActivity.class);
				startActivity(intent);
			}
		});
		
		// checkboxes
		chMostUpdate = (CheckBox) findViewById(R.id.chMostUpdate);
		chCheapest = (CheckBox) findViewById(R.id.chCheapest);
		chMostView = (CheckBox) findViewById(R.id.chMostView);
		
		// list view
		listView = (ListView) findViewById(R.id.listViewProductAfterSearch);
		productAdapter = new ProductAdapter(this, R.layout.product_list_item,
				new LinkedList<ProductInfo>());
		loadProductsList();	
	}

	protected void searchProductsByQuery(String query) {
		constructUrl();
		url += "&q=" + query;
		
		loadData();
	}

	private SimpleAsyncTask task = null;
	private String url;
	protected void loadProductsList() {
		constructUrl();
		loadData();
	}

	private void constructUrl() {
		// determine what criteria is used to search products
		String criteria = "";
		if (chMostUpdate.isChecked()) {
			criteria += "1,";
		}
		if (chCheapest.isChecked()) {
			criteria += "2,";
		}
		if (chMostView.isChecked()) {
			criteria += "4,";
		}
		
		// construct url
		url = null;
		if (!criteria.equals("")) {
			criteria = criteria.substring(0, criteria.length() - 1);
			url = String.format(URLConstant.GET_PRODUCTS_BY_CRITERIA, criteria);
		} else {
			url = URLConstant.GET_PRODUCTS;
		}
	}
	
	public static final int MENU_SEARCH_BY_CATEGORIES = 0;
	public static final int MENU_COMPARE_TWO_PRODUCTS = 1;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH_BY_CATEGORIES, 0,
				getString(R.string.search_by_categories)).setIcon(R.drawable.category);
		menu.add(0, MENU_COMPARE_TWO_PRODUCTS, 0,
				getString(R.string.compare)).setIcon(R.drawable.compare);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SEARCH_BY_CATEGORIES:
			CategoriesDialog.showCategoriesDialog(this, new CategoriesDialogListener() {
				
				@Override
				public void onCategoriesDialogClose(Set<String> categories) {
					searchByCategories(categories);
				}
			});
			break;
		
		case MENU_COMPARE_TWO_PRODUCTS:
			Intent intent = new Intent(ProductsListActivity.this, SelectTwoProductActivity.class);
			intent.putExtra(Global.PRODUCTS, products);
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	public void searchByCategories(final Set<String> categories) {
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
	
	private void loadData() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				productAdapter = new ProductAdapter(
						ProductsListActivity.this, R.layout.product_list_item, products);
				listView.setAdapter(productAdapter);
			}
			
			@Override
			public void loadData() {
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						products = Global.gsonWithHour.fromJson(arr.toString(), ProductInfo.getType());
						Log.d(TAG, "load " + products.size() + " products");
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

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			Toast.makeText(ProductsListActivity.this,
					getString(R.string.loginFacebookSuccess),
					Toast.LENGTH_SHORT).show();
		}

		public void onAuthFail(String error) {
			Toast.makeText(ProductsListActivity.this,
					getString(R.string.loginFacebookFail), Toast.LENGTH_SHORT)
					.show();
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Toast.makeText(ProductsListActivity.this,
					getString(R.string.logoutFacebookLoading),
					Toast.LENGTH_SHORT).show();
		}

		public void onLogoutFinish() {
			Toast.makeText(ProductsListActivity.this,
					getString(R.string.logoutFacebookSuccess),
					Toast.LENGTH_SHORT).show();
		}
	}

}
