package com.appspot.smartshop.adapter;

import com.appspot.smartshop.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DirectionAdapter extends ArrayAdapter<String> {
	
	private LayoutInflater inflater;
	private int resourceId;
	
	public DirectionAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			
			String item = getItem(position);
			
			TextView txtDirection = (TextView) convertView.findViewById(R.id.txtDirection);
			txtDirection.setText(Html.fromHtml(item));
		}
		
		return convertView;
	}

}
