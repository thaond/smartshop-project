package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ListProductAfterSearch extends Activity implements
		RadioGroup.OnCheckedChangeListener, View.OnClickListener {

	private RadioGroup mRadioGroup;
	public RadioButton btnRadioLatest, btnBestSeller, btnPrice;
	public ListView listView;
	public ProductAdapter productAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_product_after_search);
		productAdapter = new ProductAdapter(this, R.layout.product_list_item,
				new LinkedList<ProductInfo>());
		mRadioGroup = (RadioGroup) findViewById(R.id.menu);
		btnRadioLatest = (RadioButton) findViewById(R.id.radioLatest);
		btnBestSeller = (RadioButton) findViewById(R.id.radioBestSeller);
		btnPrice = (RadioButton) findViewById(R.id.radioPrice);
		listView = (ListView) findViewById(R.id.listViewProductAfterSearch);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
		if (mRadioGroup.getCheckedRadioButtonId() == btnBestSeller.getId()) {
			productAdapter.add(new ProductInfo("name", 300, "Best Seller"));
		} else if (mRadioGroup.getCheckedRadioButtonId() == btnPrice.getId()) {
			productAdapter.add(new ProductInfo("name", 400, "Price"));
		} else {
			productAdapter.add(new ProductInfo("name", 500,
					"Latest Product"));
		}
		System.out.println("onFocusChange called");
		listView.setAdapter(productAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
