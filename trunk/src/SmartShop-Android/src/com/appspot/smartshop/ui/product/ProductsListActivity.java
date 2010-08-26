package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.mock.MockProduct;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.google.android.maps.MapActivity;

public class ProductsListActivity extends MapActivity implements
		RadioGroup.OnCheckedChangeListener, View.OnClickListener {
	
	public static final int BEST_SELLER_PRODUCTS = 0;
	public static final int CHEAPEST_PRODUCTS = 1;
	public static final int NEWEST_PRODUCTS = 2;

	private RadioGroup mRadioGroup;
	private RadioButton btnRadioLatest, btnBestSeller, btnPrice;
	private ListView listView;
	private ProductAdapter productAdapter;
	private int productsMode = NEWEST_PRODUCTS;
	private LinkedList<ProductInfo> products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_product_after_search);
		
		// search fields
		final EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = txtSearch.getText().toString();
				// TODO (vanloi999): request products list based on query string
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
		
		// radio group
		mRadioGroup = (RadioGroup) findViewById(R.id.menu);
		mRadioGroup.setOnCheckedChangeListener(this);
		
		// radio buttons
		btnRadioLatest = (RadioButton) findViewById(R.id.radioLatest);
		btnBestSeller = (RadioButton) findViewById(R.id.radioBestSeller);
		btnPrice = (RadioButton) findViewById(R.id.radioPrice);
		
		// list view
		listView = (ListView) findViewById(R.id.listViewProductAfterSearch);
		productAdapter = new ProductAdapter(this, R.layout.product_list_item,
				new LinkedList<ProductInfo>());
//		loadProductsList();
		
	}

//	private void loadProductsList() {
//		new SimpleAsyncTask(this, new DataLoader() {
			
			public void updateUI() {
				productAdapter = new ProductAdapter(ProductsListActivity.this, R.layout.product_list_item,
						products);
				listView.setAdapter(productAdapter);
			}
			
			public void loadData() {
				products = MockProduct.getProducts();	// TODO (vanloi999): remove mock
				switch (productsMode) {
				case BEST_SELLER_PRODUCTS:
					// TODO (vanloi999): request best seller products list
					
					products.add(new ProductInfo("Best Seller", 1000, " Tào Lao"));
					
					break;
					
				case CHEAPEST_PRODUCTS:
					// TODO (vanloi999): request  products listsort by price

					products.add(new ProductInfo("CHEAPEST_PRODUCTS", 1000, " Tào Lao"));
					break;
					
				case NEWEST_PRODUCTS:
					// TODO (vanloi999): request products list sort by date

					products.add(new ProductInfo("NEWEST_PRODUCTS", 1000, " Tào Lao"));
					
					break;

				default:
					break;
				}
			}
//		});
//	}

	@Override
	public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
		if (mRadioGroup.getCheckedRadioButtonId() == btnBestSeller.getId()) {
			productsMode = BEST_SELLER_PRODUCTS;
		} else if (mRadioGroup.getCheckedRadioButtonId() == btnPrice.getId()) {
			productsMode = CHEAPEST_PRODUCTS;
		} else {
			productsMode = NEWEST_PRODUCTS;
		}
		loadData();
		updateUI();
		
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
