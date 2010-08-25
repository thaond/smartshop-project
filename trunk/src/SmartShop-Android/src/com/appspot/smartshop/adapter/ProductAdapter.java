package com.appspot.smartshop.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.ui.product.ViewSingleProduct;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;

public class ProductAdapter extends ArrayAdapter<ProductInfo> {
	
	private Context context;
	private LayoutInflater inflater;
	private int resourceId;
	
	public ProductAdapter(Context context, int textViewResourceId,
			List<ProductInfo> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		inflater = LayoutInflater.from(context);
		resourceId = textViewResourceId;
	}

	public ProductAdapter(Context context, int textViewResourceId,
			ProductInfo[] objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public ProductAdapter(Context context, int textViewResourceId) {
		this(context, textViewResourceId, new ProductInfo[] {});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.product_list_item, null);
			
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.btnMap = (Button) convertView.findViewById(R.id.btnMap);
			holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtProductName);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ProductInfo product = (ProductInfo) getItem(position);
		holder.txtName.setText(product.name);
		holder.txtPrice.setText(""+product.price);
		holder.txtDescription.setText(product.description);
		holder.btnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO (condorhero01): can product has no lat, lng?
				MapDialog.createLocationDialog(
						context, new GeoPoint((int)product.lat, (int)product.lng), null).show();
			}
		});
		//Load image of product from internet
		String url = "http://hangxachtayusa.net/img/p/89-129-medium.jpg";
		try{
			Drawable drawable = getDrawableFromUrl(url);
			holder.image.setImageDrawable(drawable);
		}catch(Exception ex){
			Log.d("ProductAdapter","load image failed");
		}
		
		
		// go to product detail
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ViewSingleProduct.class);
				intent.putExtra(Global.PRODUCT_INFO, product);
				
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	private Drawable getDrawableFromUrl(String url) throws MalformedURLException, IOException {
		InputStream is = (InputStream) new URL(url).getContent();
		Drawable drawable = Drawable.createFromStream(is, "src name");
		return drawable;
	}
	static class ViewHolder {
		ImageView image;
		TextView txtName;
		TextView txtPrice;
		TextView txtDescription;
		Button btnMap;
	}
}