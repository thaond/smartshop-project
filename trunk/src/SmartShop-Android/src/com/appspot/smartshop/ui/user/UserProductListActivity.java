package com.appspot.smartshop.ui.user;

import java.net.URLEncoder;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.facebook.Facebook;
import com.appspot.smartshop.facebook.LoginButton;
import com.appspot.smartshop.facebook.SessionEvents;
import com.appspot.smartshop.facebook.SessionStore;
import com.appspot.smartshop.facebook.SessionEvents.AuthListener;
import com.appspot.smartshop.facebook.SessionEvents.LogoutListener;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class UserProductListActivity extends BaseUIActivity {

	public static final int INTERESTED_PRODUCTS = 0;
	public static final int BUYED_PRODUCTS = 1;
	public static final int SELLED_PRODUCTS = 2;
	private static LinkedList<ProductInfo> products;
	private int productsListType = INTERESTED_PRODUCTS;
	private String username;
	private ProductAdapter adapter;
	private ListView listProducts;
	private String url;
	// set up variable for facebook connection
	private LoginButton mLoginButton;

	@Override
	protected void onCreatePre() {
		setContentView(R.layout.user_product_list);
	}

	@Override
	protected void onCreatePost(Bundle savedInstanceState) {
		// set up variable for facebook connection
		mLoginButton = (LoginButton) findViewById(R.id.loginFace);
		Global.mFacebook = new Facebook();
		SessionStore.restore(Global.mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.init(Global.mFacebook, Global.PERMISSIONS);
		// get username
		username = getIntent().getExtras().getString(Global.PRODUCTS_OF_USER);

		// radio buttons
		RadioButton rbInterestedProducts = (RadioButton) findViewById(R.id.rbInterestedProducts);
		rbInterestedProducts.setChecked(true);
		rbInterestedProducts
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							productsListType = INTERESTED_PRODUCTS;
							constructUrl();
							loadProductsList();
						}
					}
				});

		RadioButton rbBuyProducts = (RadioButton) findViewById(R.id.rbBuyProducts);
		rbBuyProducts.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					productsListType = BUYED_PRODUCTS;
					constructUrl();
					loadProductsList();
				}
			}
		});

		RadioButton rbSellProducts = (RadioButton) findViewById(R.id.rbSellProducts);
		rbSellProducts
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							productsListType = SELLED_PRODUCTS;
							constructUrl();
							loadProductsList();
						}
					}
				});

		// search fields
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchProductsByQuery();
			}
		});

		// products listview
		listProducts = (ListView) findViewById(R.id.listUserProducts);
		constructUrl();
		loadProductsList();
	}

	protected void searchProductsByQuery() {
		String query = URLEncoder.encode(txtSearch.getText().toString());

		constructUrl();
		url += "&q=" + query;
		loadProductsList();
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

	private void constructUrl() {
		// construct url
		switch (productsListType) {
		case INTERESTED_PRODUCTS:
			url = String.format(URLConstant.GET_INTERESTED_PRODUCTS_OF_USER,
					username);
			break;

		case BUYED_PRODUCTS:
			url = String.format(URLConstant.GET_SELLED_PRODUCTS_OF_USER,
					username);
			break;

		case SELLED_PRODUCTS:
			url = String.format(URLConstant.GET_BUYED_PRODUCTS_OF_USER,
					username);
			break;
		}
	}

	private SimpleAsyncTask task;
	private EditText txtSearch;

	protected void loadProductsList() {
		// load data
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
				if (products != null) {
					adapter = new ProductAdapter(UserProductListActivity.this,
							R.layout.product_list_item, products);
					adapter.isNormalProductList = false;
					listProducts.setAdapter(adapter);
				}
			}

			@Override
			public void loadData() {
				RestClient.getData(url, new JSONParser() {

					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						products = Global.gsonWithHour.fromJson(arr.toString(),
								ProductInfo.getType());
					}

					@Override
					public void onFailure(String message) {
						// task.cancel(true);
						// Toast.makeText(UserProductListActivity.this, message,
						// Toast.LENGTH_SHORT);
					}
				});
			}
		});

		task.execute();
	}

	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			Toast.makeText(UserProductListActivity.this,
					getString(R.string.loginFacebookSuccess),
					Toast.LENGTH_SHORT).show();
		}

		public void onAuthFail(String error) {
			Toast.makeText(UserProductListActivity.this,
					getString(R.string.loginFacebookFail), Toast.LENGTH_SHORT)
					.show();
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Toast.makeText(UserProductListActivity.this,
					getString(R.string.logoutFacebookLoading),
					Toast.LENGTH_SHORT).show();
		}

		public void onLogoutFinish() {
			Toast.makeText(UserProductListActivity.this,
					getString(R.string.logoutFacebookSuccess),
					Toast.LENGTH_SHORT).show();
		}
	}
}
