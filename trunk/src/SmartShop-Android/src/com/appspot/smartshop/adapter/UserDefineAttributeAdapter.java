package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserDefineAttribute;
import android.view.WindowManager;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class UserDefineAttributeAdapter extends
		ArrayAdapter<UserDefineAttribute> {
	public int resourceId;

	public UserDefineAttributeAdapter(Context context, int textViewResourceId,
			List<UserDefineAttribute> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		UserDefineAttribute product = this.getItem(position);
		// label width
		TextView attName = (TextView) view
				.findViewById(R.id.txtNewAttribute);
		attName.setWidth(100);
		attName.setText(product.newAttribute);

		TextView productPrice = (TextView) view
				.findViewById(R.id.txtValueOfNewAttribute);
		productPrice.setText(product.valueOfNewAttribute);
		
		return view;
	}
}
