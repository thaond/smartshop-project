package com.appspot.smartshop.ui.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import com.appspot.smartshop.utils.SimpleAsyncTask;

public class UserProductListActivity extends Activity {
	
	public static final int INTERESTED_PRODUCTS = 0;
	public static final int BUY_PRODUCTS = 1;
	public static final int SELL_PRODUCTS = 2;
	
	private static LinkedList<ProductInfo> products;
	
	private int productsListType = INTERESTED_PRODUCTS; 
	
	private ProductAdapter adapter;
	private ListView listProducts;

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
					productsListType = BUY_PRODUCTS;
					loadProductsList();
				}
			}
		});
		
		RadioButton rbSellProducts = (RadioButton) findViewById(R.id.rbSellProducts);
		rbSellProducts.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					productsListType = SELL_PRODUCTS;
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

	protected void loadProductsList() {
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new ProductAdapter(UserProductListActivity.this, 
						R.layout.product_list_item, products);
				listProducts.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				// TODO (condorhero01): request list of products (buy, sell and interested list)
				switch (productsListType) {
				case INTERESTED_PRODUCTS:
					
					break;
					
				case BUY_PRODUCTS:
					
					break;
					
				case SELL_PRODUCTS:
					
					break;
				}
				
				products = MockProduct.getProducts();
			}
		}).execute();
	}
}
