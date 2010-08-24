package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SearchProductActivity extends Activity {
	private ListView listViewProduct;
	private ProductAdapter productAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_product_form);
		Button btnSearchProduct = (Button) findViewById(R.id.btnSearch);
		btnSearchProduct.setOnClickListener(new OnClickListener() {
			int i = 0;

			@Override
			public void onClick(View v) {
				// TODO:condohero01: xu li su kien cho btn Search
			}
		});
		listViewProduct = (ListView) findViewById(R.id.listProduct);
		productAdapter = new ProductAdapter(this, R.layout.product_list_item,
				new LinkedList<ProductInfo>()) {

		};
		listViewProduct.setAdapter(productAdapter);
		Button btnAdvancedSearchProduct = (Button) findViewById(R.id.btnAdvancedSearch);
		btnAdvancedSearchProduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO:(condohero01) xu ly su kien cho advance search

			}
		});

	}
}
