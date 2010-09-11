package com.appspot.smartshop.ui.user;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.location.Address;
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
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserSubcribeProduct;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.google.gson.Gson;

public class CategoriesDialogForSubcribe {
	private static Builder dialogBuilder;
	private static AlertDialog dialog;
	private static LayoutInflater inflater;
	private static View view;
	private static Button btnSubscribe;
	private static ExpandableListView expandableListView;
	private static Context context;
	private static MyExpandableListAdapterForSubcribe mAdapter;
	private static Set<String> setSelectedCategories = new HashSet<String>();
	private static CategoriesDialogForSubcribeListener listener;
	private static HashMap<String, String> mapCategories = new HashMap<String, String>();
	private static String[] groups;
	private static String[][] children;
	private static CheckBox checkBoxEmail;
	private static CheckBox checkBoxMobile;
	private static String url = "";
	private static SimpleAsyncTask task;

	public static void showCategoriesDialog(final Context context, CategoriesDialogForSubcribeListener listener) {
		mapCategories.clear();
		setSelectedCategories.clear();
		
		CategoriesDialogForSubcribe.context = context;
		CategoriesDialogForSubcribe.listener = listener;
		
		if (inflater == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		view = inflater.inflate(R.layout.subscribe, null);
		
		// Subscribe button
		btnSubscribe = (Button) view.findViewById(R.id.btnSubscribe);
		btnSubscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CategoriesDialogForSubcribe.listener.onCategoriesDialogClose(setSelectedCategories);
				addNewSubcribe();
				dialog.dismiss();
			}	

			
			private UserSubcribeProduct userSubcribeProduct;
			private void addNewSubcribe() {
				if(setSelectedCategories.toString().equals("")){
					Log.d("CategoriesDialog", "empty");
					onAddNewSubcribeFailure();
					return;
				}
				if(Global.isLogin){
					url = URLConstant.EDIT_SUBCRIBE;
				}else{
					url = URLConstant.ADD_NEW_SUBCRIBE;
				}
				task = (SimpleAsyncTask) new SimpleAsyncTask(context, new DataLoader() {
					
					@Override
					public void updateUI() {
					}
					
					@Override
					public void loadData() {
						userSubcribeProduct = new UserSubcribeProduct();
						userSubcribeProduct.userName = "nghia";
						userSubcribeProduct.date = new Date(89, 1, 1);
						userSubcribeProduct.lat = 49.0;
						userSubcribeProduct.lng = 49.0;
						userSubcribeProduct.isActive = true;
						userSubcribeProduct.description = "tim tre lac";
						userSubcribeProduct.radius = 100.0;
						if(Global.isLogin){
							userSubcribeProduct.id = new Long(123);
						}
						List<String> list = new LinkedList<String>();
						list.add("lap");
						list.add("soft");
						userSubcribeProduct.categoryList = list;
						
						String param = Global.gsonDateWithoutHour.toJson(userSubcribeProduct);
						
						RestClient.postData(url, param, new JSONParser() {
							@Override
							public void onSuccess(JSONObject json) throws JSONException {
								System.out.println(json.toString());
							}
							@Override
							public void onFailure(String message) {
								System.out.println(message);
							}
						});	
					}
				}).execute();
				
				
			}
			private void onAddNewSubcribeFailure() {
		        Builder alertBuilder = new Builder(context);
		        alertBuilder.setMessage("Đăng ký nhận sản phẩm không thành công");	
		        alertBuilder.create().show();
				dialog.cancel();
			}
		});
		//Check Box Email and Mobile
		checkBoxEmail = (CheckBox) view.findViewById(R.id.checkBoxEmail);
		checkBoxMobile = (CheckBox) view.findViewById(R.id.checkBoxMobile);
		
		// TODO get categories info
		task = new SimpleAsyncTask(context, new DataLoader() {
			
			@Override
			public void updateUI() {
				// list view
				expandableListView = (ExpandableListView) view.findViewById(R.id.listCategorySubscribe);
				mAdapter = new CategoriesDialogForSubcribe.MyExpandableListAdapterForSubcribe(groups, children);
				expandableListView.setAdapter(mAdapter);
				
				dialogBuilder = new AlertDialog.Builder(context);
				dialogBuilder.setView(view);
				dialog = dialogBuilder.create(); 
				
				dialog.show();
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
				task.hasData = false;
				task.message = message;
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
	//
	public interface CategoriesDialogForSubcribeListener {
		void onCategoriesDialogClose(Set<String> categories);
	}
	
	static class MyExpandableListAdapterForSubcribe extends BaseExpandableListAdapter {
		public String[] groups;
		public String[][] children;

		public MyExpandableListAdapterForSubcribe(String[] groups, String[][] children) {
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
						Log.d("CategoriesDialogForSubscribe", setSelectedCategories.toString());
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
