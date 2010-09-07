package com.appspot.smartshop.ui.product.vatgia;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;

import com.appspot.smartshop.adapter.VatgiaProductDetailAdapter;
import com.appspot.smartshop.dom.Group;
import com.appspot.smartshop.dom.Pair;
import com.appspot.smartshop.dom.ProductVatGia;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

public class VatgiaProductDetailActivity extends Activity {
	protected LinkedList<Group> groups;
	
	private ExpandableListView listView;
	private VatgiaProductDetailAdapter adapter;
	private String[] parent;
	private Pair[][] children;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		listView = new ExpandableListView(this);
		addContentView(listView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		loadProductDetail2();
	}
	
	private void loadProductDetail2() {
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new VatgiaProductDetailAdapter(VatgiaProductDetailActivity.this, 
						parent, children);
				listView.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				groups = VatgiaCompaniesActivity.product.listGroup;
				
				int len = groups.size();
				parent = new String[len];
				children = new Pair[len][];
				int index = 0;
				for (Group g : groups) {
					if (g.name == null) {
						parent[index] = "";
					} else {
						parent[index] = g.name;
					}
					
					int attributeSize = g.listAtt.size();
					children[index] = new Pair[attributeSize];
					for (int i = 0; i < attributeSize; ++i) {
						children[index][i] = g.listAtt.get(i);
					}
					
					index++;
				}
			}
		});
		
	}
	
	private SimpleAsyncTask task;
	private void loadProductDetail() {
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter = new VatgiaProductDetailAdapter(VatgiaProductDetailActivity.this, 
						parent, children);
				listView.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				// TODO test
				String vatgiaUrl = "http://m.vatgia.com/438/699832/htc-hd2-htc-leo-100-t8585.html";
				String url = String.format(URLConstant.GET_DETAIL_OF_VATGIA_PRODUCT, vatgiaUrl);
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONObject vatgiaProduct = json.getJSONObject("productvatgia");
						ProductVatGia product = Global.gsonWithHour.fromJson(
								vatgiaProduct.toString(), ProductVatGia.class);
						
						groups = product.listGroup;
						
						int len = product.listGroup.size();
						parent = new String[len];
						children = new Pair[len][];
						int index = 0;
						for (Group g : product.listGroup) {
							if (g.name == null) {
								parent[index] = "";
							} else {
								parent[index] = g.name;
							}
							
							int attributeSize = g.listAtt.size();
							children[index] = new Pair[attributeSize];
							for (int i = 0; i < attributeSize; ++i) {
								children[index][i] = g.listAtt.get(i);
							}
							
							index++;
						}
					}
					
					@Override
					public void onFailure(String message) {
						task.hasData = false;
						task.message = message;
						task.cancel(true);
					}
				});
			}
		});
		task.execute();
	}
}
