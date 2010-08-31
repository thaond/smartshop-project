package com.appspot.smartshop.ui.user;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.mock.MockProduct;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.reflect.TypeToken;

public class UserProductListActivity extends Activity {
	
	public static final int INTERESTED_PRODUCTS = 0;
	public static final int BUYED_PRODUCTS = 1;
	public static final int SELLED_PRODUCTS = 2;
	
	private static LinkedList<ProductInfo> products;
	
	private int productsListType = INTERESTED_PRODUCTS; 
	
	private ProductAdapter adapter;
	private ListView listProducts;
	
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_product_list);
		
		// radio buttons
		RadioButton rbInterestedProducts = (RadioButton) findViewById(R.id.rbInterestedProducts);
		rbInterestedProducts.setChecked(true);
		rbInterestedProducts.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					productsListType = INTERESTED_PRODUCTS;
					loadProductsList();
				}
			}
		});
		
		RadioButton rbBuyProducts = (RadioButton) findViewById(R.id.rbBuyProducts);
		rbBuyProducts.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					productsListType = BUYED_PRODUCTS;
					loadProductsList();
				}
			}
		});
		
		RadioButton rbSellProducts = (RadioButton) findViewById(R.id.rbSellProducts);
		rbSellProducts.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					productsListType = SELLED_PRODUCTS;
					loadProductsList();
				}
			}
		});
		
		// products listview
		listProducts = (ListView) findViewById(R.id.listUserProducts);
		if (products != null) {
			adapter = new ProductAdapter(this, R.layout.product_list_item, products);
			listProducts.setAdapter(adapter);
		} else {
			loadProductsList();
		}
	}

	private SimpleAsyncTask task;
	protected void loadProductsList() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new ProductAdapter(UserProductListActivity.this, 
						R.layout.product_list_item, products);
				listProducts.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				switch (productsListType) {
				case INTERESTED_PRODUCTS:
					url = String.format(URLConstant.GET_INTERESTED_PRODUCTS_OF_USER, Global.username);
					break;
					
				case BUYED_PRODUCTS:
					url = String.format(URLConstant.GET_BUYED_PRODUCTS_OF_USER, Global.username);
					break;
					
				case SELLED_PRODUCTS:
					url = String.format(URLConstant.GET_SELLED_PRODUCTS_OF_USER, Global.username);
					break;
				}
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						if (arr == null || arr.length() == 0) {
							task.hasData = false;
							task.message = json.getString(URLConstant.MESSAGE);
							return;
						}
						products = Global.gsonWithHour.fromJson(arr.toString(), ProductInfo.getType());
					}
					
					@Override
					public void onFailure(String message) {
					}
				});
			}
		});
		
		task.execute();
	}
}
