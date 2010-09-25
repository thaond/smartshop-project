package com.appspot.smartshop.ui.user.subcribe;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ProductsListOfSubcribeActivity extends ListActivity {
	public static final String TAG = "[ProductsListOfSubcribeActivity]";

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
	private void loadProducts() {
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new ProductAdapter(ProductsListOfSubcribeActivity.this, 0, products);
				listProducts.setAdapter(adapter);
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
}
