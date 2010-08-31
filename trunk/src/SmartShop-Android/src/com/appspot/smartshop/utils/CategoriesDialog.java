package com.appspot.smartshop.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.style.ParagraphStyle;
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
	private static HashMap<String, String> mapCategories = new HashMap<String, String>();
	private static String[] groups;
	private static String[][] children;
	
	private static SimpleAsyncTask task;

	public static void showCategoriesDialog(final Context context, CategoriesDialogListener listener) {
		mapCategories.clear();
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
				CategoriesDialog.listener.onCategoriesDialogClose(setSelectedCategories);
				dialog.dismiss();
			}
		});
		
		// TODO get categories info
		task = new SimpleAsyncTask(context, new DataLoader() {
			
			@Override
			public void updateUI() {
				// list view
				expandableListView = (ExpandableListView) view.findViewById(R.id.listCategory);
				mAdapter = new CategoriesDialog.MyExpandableListAdapter(groups, children);
				expandableListView.setAdapter(mAdapter);
				
				dialogBuilder = new AlertDialog.Builder(context);
				dialogBuilder.setView(view);
				dialog = dialogBuilder.create(); 
				
				dialog.show();
				Log.d("Test", "after show");
			}
			
			@Override
			public void loadData() {
				getCategories();
			}
		});
		
		task.execute();
	}
	
	private static void getCategories() {
		// get parent id
		RestClient.getData(URLConstant.GET_PARENT_CATEGORIES, new JSONParser() {
			
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				JSONArray arr = json.getJSONArray("categories");
				
				int len = arr.length();
				groups = new String[len];
				String name, key_cat;
				for (int i = 0; i < len; ++i) {
					name = arr.getJSONObject(i).getString("name");
					key_cat = arr.getJSONObject(i).getString("key_cat");
					
					groups[i] = name;
					mapCategories.put(name, key_cat);
				}
			}
			
			@Override
			public void onFailure(String message) {
				task.cancel(true);
			}
		});
		
		// get child id
		index = 0;
		children = new String[groups.length][];
		String[] parentIds = new String[mapCategories.size()];
		for (int i = 0; i < parentIds.length; ++i) {
			parentIds[i] = mapCategories.get(groups[i]);
		}
		
		for (String parentId : parentIds) {
			String url = String.format(URLConstant.GET_CHILD_CATEGORIES, parentId);
			
			RestClient.getData(url, new JSONParser() {
				
				@Override
				public void onSuccess(JSONObject json) throws JSONException {
					JSONArray arr = json.getJSONArray("categories");
					
					int len = arr.length();
					children[index] = new String[len];
					String name, key_cat;
					for (int k = 0; k < len; ++k) {
						name = arr.getJSONObject(k).getString("name");
						key_cat = arr.getJSONObject(k).getString("key_cat");
						
						children[index][k] = name;
						mapCategories.put(name, key_cat);
					}
					
					index++;
				}
				
				@Override
				public void onFailure(String message) {
					task.cancel(true);
				}
			});
		}
	}
	private static int index = 0;
	
	public interface CategoriesDialogListener {
		void onCategoriesDialogClose(Set<String> categories);
	}
	
	static class MyExpandableListAdapter extends BaseExpandableListAdapter {
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.parent_category, null);
			TextView textView = (TextView) view
					.findViewById(R.id.txtParentCategory);
			textView.setText(groups[groupPosition]);
			return view;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
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
						setSelectedCategories.add(mapCategories.get(subCategory));
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
