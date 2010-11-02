package com.appspot.smartshop.ui.user.subcribe;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.UserSubcribeProduct;
import com.appspot.smartshop.facebook.utils.FacebookUtils;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class ProductsListOfSubcribeActivity extends ListActivity {
	public static final String TAG = "[ProductsListOfSubcribeActivity]";
	
	public static UserSubcribeProduct subcribe;
	private ListView listProducts;
	private long subcribeId;
	private List<ProductInfo> products;
	private ProductAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get subcribe id
		subcribeId = getIntent().getExtras().getLong(Global.SUBCRIBE_ID);
		
		// load products
		listProducts = getListView();
		loadProducts();
	}

	private SimpleAsyncTask task;
	private boolean foundProduct = true;
	private void loadProducts() {
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new ProductAdapter(ProductsListOfSubcribeActivity.this, 
						R.layout.product_list_item, products,new FacebookUtils(ProductsListOfSubcribeActivity.this));
				adapter.isNormalProductList = false;
				listProducts.setAdapter(adapter);
				if (!foundProduct) {
					Toast.makeText(ProductsListOfSubcribeActivity.this, 
							getString(R.string.no_product_found), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void loadData() {
				String url = String.format(URLConstant.GET_PRODUCTS_OF_SUBCRIBE, subcribeId);
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						products = Global.gsonWithHour.fromJson(arr.toString(), ProductInfo.getType());
						Log.d(TAG, "load " + products.size() + " products");
						if (products.size() == 0) {
							foundProduct = false;
						}
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
	
	private static final int MENU_VIEW_SUBCRIBE_DETAIL = 0;
	private static final int MENU_RETURN_TO_HOME = 1;
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_VIEW_SUBCRIBE_DETAIL, 0, getString(R.string.subcribe_detail));
		menu.add(0, MENU_RETURN_TO_HOME, 0, getString(R.string.return_to_home)).setIcon(R.drawable.home);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_RETURN_TO_HOME:
			Utils.returnHomeActivity(this);
			break;
			
		case MENU_VIEW_SUBCRIBE_DETAIL:
			Log.d(TAG, "[EDIT SUBCRIBE]");
			
			Intent intent = new Intent(this, SubcribeActivity.class);
			intent.putExtra(Global.SUBCRIBE_INFO, subcribe);
			startActivity(intent);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
