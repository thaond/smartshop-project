package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.R;
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
import android.widget.Toast;

public class ViewRelatedProductsActivity extends ListActivity {
	public static final String TAG = "[ViewRelatedProductsActivity]";
	
	private LinkedList<ProductInfo> products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		list = getListView();
		adapter = new ProductAdapter(this, R.layout.product_list_item);
		
		long productId = getIntent().getLongExtra(Global.RELATED_PRODUCT_ID, 0);
		loadProducts(productId);
	}

	private SimpleAsyncTask task;

	private ProductAdapter adapter;

	private ListView list;
	private void loadProducts(final long productId) {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				if (products.size() == 0) {
					Toast.makeText(ViewRelatedProductsActivity.this, 
							getString(R.string.no_product_found), Toast.LENGTH_SHORT).show();
				} else {
					adapter = new ProductAdapter(
							ViewRelatedProductsActivity.this, R.layout.product_list_item, products);
					list.setAdapter(adapter);
				}
			}
			
			@Override
			public void loadData() {
				String url = String.format(URLConstant.GET_RELATED_PRODUCTS, productId);
				
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
