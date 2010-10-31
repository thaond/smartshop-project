package com.appspot.smartshop.ui.product;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CompareTwoProductsAdapter;
import com.appspot.smartshop.dom.Pair;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;

public class CompareTwoProductsActivity extends ExpandableListActivity {
	
	protected static String[] groups = {
		Global.application.getString(R.string.nameOfProduct),
		Global.application.getString(R.string.priceOfProduct),
		Global.application.getString(R.string.description),
		Global.application.getString(R.string.product_view),
		Global.application.getString(R.string.isVAT),
		Global.application.getString(R.string.quantityOfProduct),
		Global.application.getString(R.string.datePost),
		Global.application.getString(R.string.warrantyOfProduct),
		Global.application.getString(R.string.originOfProduct),
		Global.application.getString(R.string.addressOfProduct),
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get products info from intent
		Bundle bundle = getIntent().getExtras();
		ProductInfo productInfoOne = (ProductInfo) bundle.get(Global.PRODUCT_1);
		ProductInfo productInfoTwo = (ProductInfo) bundle.get(Global.PRODUCT_2);
		
		// get detail of 2 products
		Pair[] children = new Pair[groups.length];
		children[0] = new Pair(productInfoOne.name, productInfoTwo.name);
		children[1] = new Pair("" + productInfoOne.price, "" + productInfoTwo.price);
		children[2] = new Pair(productInfoOne.description, "" + productInfoTwo.description);
		children[3] = new Pair(productInfoOne.product_view + "", productInfoTwo.product_view + "");
		children[4] = new Pair(productInfoOne.is_vat ? getString(R.string.hasVAT) : getString(R.string.hasNoVAT), 
				productInfoTwo.is_vat ? getString(R.string.hasVAT) : getString(R.string.hasNoVAT));
		children[5] = new Pair("" + productInfoOne.quantity, "" + productInfoTwo.quantity);
		children[6] = new Pair(Global.df.format(productInfoOne.date_post), Global.df.format(productInfoTwo.date_post));
		children[7] = new Pair(productInfoOne.warranty, productInfoTwo.warranty);
		children[8] = new Pair(productInfoOne.origin, productInfoTwo.origin);
		children[9] = new Pair(productInfoOne.address, productInfoTwo.address);
		
		// list view
		ExpandableListView listView = getExpandableListView();
		listView.setAdapter(new CompareTwoProductsAdapter(this, groups, children));
	}
}
