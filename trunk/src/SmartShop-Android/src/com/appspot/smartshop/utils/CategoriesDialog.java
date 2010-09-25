package com.appspot.smartshop.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;

public class CategoriesDialog {
	public static final String TAG = "[CategoriesDialog]";

	private static Builder dialogBuilder;
	private static AlertDialog dialog;
	private static LayoutInflater inflater;
	private static View view;
	private static Button btnSearch;
	private static ExpandableListView expandableListView;
	private static Context context;
	private static MyExpandableListAdapter mAdapter;
	private static Set<String> setSelectedCategories = new HashSet<String>();
	private static CategoriesDialogListener listener;
	private static String[] groups;
	private static String[][] children;

	public static void showCategoriesDialog(final Context context, CategoriesDialogListener listener) {
		setSelectedCategories.clear();
		
		CategoriesDialog.context = context;
		CategoriesDialog.listener = listener;
		
		if (inflater == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		view = inflater.inflate(R.layout.search_by_category, null);
		
		// search button
		btnSearch = (Button) view.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (CategoriesDialog.listener != null) {
					CategoriesDialog.listener.onCategoriesDialogClose(setSelectedCategories);
				}
				dialog.dismiss();
			}
		});
		
		// get categories info
		getCategories();
		
		expandableListView = (ExpandableListView) view.findViewById(R.id.listCategory);
		mAdapter = new CategoriesDialog.MyExpandableListAdapter(groups, children);
		expandableListView.setAdapter(mAdapter);
		
		dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create(); 
		
		dialog.show();
	}
	
	private static void getCategories() {
		// get parent categories
		int index = 0;
		int len = Global.mapParentCategories.size();
		groups = new String[len];
		for (String cat : Global.mapParentCategories.values()) {
			groups[index++] = cat;
		}
		System.out.println(Arrays.toString(groups));
		
		// get children categories
		children = new String[len][];
		index = 0;
		for (String[] child : Global.listCategories) {
			children[index++] = child;
			System.out.println(Arrays.toString(children[index - 1]));
		}
	}
	
	public interface CategoriesDialogListener {
		void onCategoriesDialogClose(Set<String> categories);
	}
	
	static class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private String[] groups;
		private String[][] children;
		private LayoutInflater inflater;

		public MyExpandableListAdapter(String[] groups, String[][] children) {
			this.groups = groups;
			this.children = children;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.parent_category, null);
			
			TextView textView = (TextView) convertView.findViewById(R.id.txtParentCategory);
			textView.setText(groups[groupPosition]);
			
			return convertView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.child_category, null);
			
			final TextView txtView = (TextView) convertView.findViewById(R.id.txtchildCategory);
			txtView.setText(children[groupPosition][childPosition]);
			final CheckBox chSubCategory = (CheckBox) convertView.findViewById(R.id.checkBox);
			chSubCategory.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						String subCategory = txtView.getText().toString();
						setSelectedCategories.add(Global.mapChildrenCategoriesName.get(subCategory));
					}
				}
			});
			
			return convertView;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}
	}
}
