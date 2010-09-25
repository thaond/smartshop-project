package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserSubcribeProduct;
import com.appspot.smartshop.ui.user.subcribe.SubcribeActivity;
import com.appspot.smartshop.utils.Global;

public class UserSubcribeAdapter extends ArrayAdapter<UserSubcribeProduct> {
	public static final String TAG = "[UserSubcribeAdapter]";
	
	private LayoutInflater inflater;

	public UserSubcribeAdapter(Context context, int textViewResourceId,
			List<UserSubcribeProduct> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.subcribe_list_item, null);
			holder.chActive = (CheckBox) convertView.findViewById(R.id.chActive);
			holder.txtCategoriesList = (TextView) convertView.findViewById(R.id.txtCategoriesList);
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
			holder.txtQuery = (TextView) convertView.findViewById(R.id.txtQuery);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final UserSubcribeProduct item = getItem(position);
		holder.chActive.setChecked(item.isActive);
		if (item.date != null) {
			holder.txtDate.setText(Global.dfFull.format(item.date));
		} else {
			holder.txtDate.setVisibility(View.GONE);
		}
		holder.txtQuery.setText(item.q);
		
		String categoriesList = "";
		if (item.categoryList.size() > 0) {
			for (String cat : item.categoryList) {
				String catName = Global.mapParentCategories.get(cat);
				if (catName == null) {
					catName = Global.mapChildrenCategories.get(cat);
				}
				categoriesList += catName + " ,";
			}
			categoriesList = categoriesList.substring(0, categoriesList.length() - 1);
		} else {
			categoriesList = Global.application.getString(R.string.no_category);
		}
		holder.txtCategoriesList.setText(categoriesList);
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO listener for edit subcribe
				Log.d(TAG, "[EDIT SUBCRIBE]");
				
				Intent intent = new Intent(getContext(), SubcribeActivity.class);
				intent.putExtra(Global.SUBCRIBE_INFO, item);
				getContext().startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	public void addNewSubcribe(UserSubcribeProduct subcribe) {
		add(subcribe);
		notifyDataSetChanged();
	}
	
	static class ViewHolder {
		CheckBox chActive;
		TextView txtDate;
		TextView txtQuery;
		TextView txtCategoriesList;
	}
}
