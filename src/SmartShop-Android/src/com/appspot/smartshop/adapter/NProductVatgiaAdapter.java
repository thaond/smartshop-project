package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.NProductVatGia;
import com.appspot.smartshop.ui.product.vatgia.VatgiaTabActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class NProductVatgiaAdapter extends ArrayAdapter<NProductVatGia>{

	public static final int IMAGE_WIDTH = 50;
	public static final int IMAGE_HEIGHT = 50;
	private LayoutInflater inflater;
	private Context context;
	
	public NProductVatgiaAdapter(Context context, int textViewResourceId,
			List<NProductVatGia> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nproductvatgia_list_item, null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtProductName);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
			holder.btnListShop = (Button) convertView.findViewById(R.id.btnListShop);
			holder.txtNumOfStore = (TextView) convertView.findViewById(R.id.txtNumOfStore);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// fill data to list item
		final NProductVatGia item = (NProductVatGia) getItem(position);
		
		Bitmap imageOfProduct = Utils.getBitmapFromURL(item.imageThumbnail);
		imageOfProduct = Bitmap.createScaledBitmap(imageOfProduct,
				IMAGE_WIDTH, IMAGE_HEIGHT, true);
		holder.image.setImageBitmap(imageOfProduct);
		
		holder.txtName.setText(item.productName);
		String numOfStore = String.format(Global.application.getString(R.string.num_of_stores), item.numOfStore);
		holder.txtNumOfStore.setText(numOfStore);
		holder.txtPrice.setText(item.priceVND);
		holder.btnListShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, VatgiaTabActivity.class);
				String url = String.format(URLConstant.GET_DETAIL_OF_VATGIA_PRODUCT, item.urlListShop);
				intent.putExtra(Global.VATGIA_URL_LIST_SHOP, url);
				context.startActivity(intent);
			}
		});

		return convertView;
	}
	
	static class ViewHolder {
		ImageView image;
		TextView txtName;
		TextView txtPrice;
		TextView txtNumOfStore;
		Button btnListShop;
	}
}
