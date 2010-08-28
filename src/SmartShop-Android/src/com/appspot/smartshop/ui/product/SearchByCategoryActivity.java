package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.CategoryInfo;
import com.appspot.smartshop.utils.Global;

/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 */
public class SearchByCategoryActivity extends Activity {
	public static final String TAG = "[SearchByCategoryActivity]";
	
	ExpandableListAdapter mAdapter;
	private String type;
	private LinkedList<String> selectedCategories = new LinkedList<String>();
	ExpandableListView expandableListView;
	Button btnSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_by_category);

		// type (page or product)
		type = getIntent().getExtras().getString(Global.TYPE);

		// search button
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchCategories();
			}
		});
		
		// get categories info
		Bundle bundle = getIntent().getExtras();
		CategoryInfo categoryInfo = (CategoryInfo) bundle.get(Global.CATEGORY_INFO);
		String[] groups = categoryInfo.parentCategory;
		String[][] children = categoryInfo.childrenCategory;
		
		// list view
		expandableListView = (ExpandableListView) findViewById(R.id.listCategory);
		mAdapter = new MyExpandableListAdapter(groups, children);
		expandableListView.setAdapter(mAdapter);
	}

	protected void searchCategories() {
		// TODO request products or pages list based on selected cat_id
		Log.d(TAG, "type of request = " + type);
		Log.d(TAG, "sub cats = " + selectedCategories.toString());
		
		finish();
	}

	class MyExpandableListAdapter extends BaseExpandableListAdapter {
		public String[] groups;
		public String[][] children;

		public MyExpandableListAdapter(String[] groups, String[][] children) {
			this.groups = groups;
			this.children = children;
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

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.parent_category, null);
			TextView textView = (TextView) view
					.findViewById(R.id.txtParentCategory);
			textView.setText(groups[groupPosition]);
			return view;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_category, null);
			final TextView txtView = (TextView) convertView
					.findViewById(R.id.txtchildCategory);
			txtView.setText(children[groupPosition][childPosition]);
			final CheckBox chSubCategory = (CheckBox) convertView.findViewById(R.id.checkBox);
			chSubCategory.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						String subCategory = txtView.getText().toString();
						if (!selectedCategories.contains(subCategory)) {
							selectedCategories.add(subCategory);
						}
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
