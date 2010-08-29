package com.appspot.smartshop.ui.product;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.map.SearchProductsOnMapActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.google.android.maps.MapActivity;
import com.google.gson.reflect.TypeToken;

public class ProductsListActivity extends MapActivity {
	
	public static final int BEST_SELLER_PRODUCTS = 0;
	public static final int CHEAPEST_PRODUCTS = 1;
	public static final int NEWEST_PRODUCTS = 2;

	private CheckBox chMostUpdate, chCheapest, chMostView;
	private ListView listView;
	private ProductAdapter productAdapter;
	
	private int productsMode = NEWEST_PRODUCTS;
	
	private LinkedList<ProductInfo> products;
	private EditText txtSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_list);
		
		// search field
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = txtSearch.getText().toString();
				if (query.trim().equals("")) {
					loadProductsList();
				} else {
					searchProductsByQuery(query);
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

	protected void searchProductsByQuery(final String query) {
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				productAdapter = new ProductAdapter(
						ProductsListActivity.this, R.layout.product_list_item, products);
				listView.setAdapter(productAdapter);
			}
			
			@Override
			public void loadData() {
				String url = String.format(URLConstant.GET_PRODUCT_BY_QUERY, query);
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						Type listType = new TypeToken<List<ProductInfo>>() {}.getType();
						products = Global.gsonWithHour.fromJson(arr.toString(), listType);
						System.out.println(products);
					}
					
					@Override
					public void onFailure(String message) {
						
					}
				});
			}
		}).execute();
	}

	protected void loadProductsList() {
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				productAdapter = new ProductAdapter(
						ProductsListActivity.this, R.layout.product_list_item, products);
				listView.setAdapter(productAdapter);
			}
			
			@Override
			public void loadData() {
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
				String url = null;
				if (!criteria.equals("")) {
					criteria = criteria.substring(0, criteria.length() - 1);
					url = String.format(URLConstant.GET_PRODUCTS_BY_CRITERIA, criteria);
				} else {
					url = URLConstant.GET_PRODUCTS;
				}
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						Type listType = new TypeToken<List<ProductInfo>>() {}.getType();
						products = Global.gsonWithHour.fromJson(arr.toString(), listType);
						System.out.println(products);
					}
					
					@Override
					public void onFailure(String message) {
					}
				});
			}
		}).execute();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
