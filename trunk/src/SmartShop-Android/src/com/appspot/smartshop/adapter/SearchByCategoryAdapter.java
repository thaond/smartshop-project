package com.appspot.smartshop.adapter;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ChildItemInSearchByCategory;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class SearchByCategoryAdapter extends SimpleExpandableListAdapter {
	public int childResource;
	public Context _context;
	public SearchByCategoryAdapter(Context context,
			List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		childResource = childLayout;
		_context = context;
		Log.d("TEST", "constructor called");
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub\
		Log.d("TEST", "getView called");
		LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(childResource, null);
		ChildItemInSearchByCategory child = (ChildItemInSearchByCategory)getChild(groupPosition, childPosition);
		TextView text = (TextView)view.findViewById(R.id.txtchildCategory);
		text.setText("aaaaaaaaaaaaaaaaaaaaaaaaaa");
		CheckBox checkbox = (CheckBox)view.findViewById(R.id.checkBox);
		checkbox.setChecked(child.checkbox.isChecked());
		if(checkbox.isChecked()){
			text.setText("CheckBox is Clicked");
		}

		
		return view;
	}
}
