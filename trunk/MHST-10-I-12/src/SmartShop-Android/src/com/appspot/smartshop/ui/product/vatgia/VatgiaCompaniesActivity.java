package com.appspot.smartshop.ui.product.vatgia;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CommentAdapter;
import com.appspot.smartshop.adapter.CompanyAdapter;
import com.appspot.smartshop.dom.Comment;
import com.appspot.smartshop.dom.Company;
import com.appspot.smartshop.dom.ProductVatGia;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

public class VatgiaCompaniesActivity extends Activity{
	public static final String TAG = "[VatgiaCompaniesActivity]";
	
	protected static ProductVatGia product;
	protected static String url;
	
	private ListView listCompanies;
	private CompanyAdapter companyAdapter;
	
	private LinkedList<Company> companies;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_product_vatgia);
		
		listCompanies = (ListView) findViewById(R.id.listCompanies);
		loadData();
	}
	private SimpleAsyncTask task;
	private void loadData() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void loadData() {
				// TODO test
//				String vatgiaUrl = "http://m.vatgia.com/438/699832/htc-hd2-htc-leo-100-t8585.html";
//				String url = String.format(URLConstant.GET_DETAIL_OF_VATGIA_PRODUCT, vatgiaUrl);
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONObject vatgiaProduct = json.getJSONObject("productvatgia");
						product = Global.gsonWithHour.fromJson(
								vatgiaProduct.toString(), ProductVatGia.class);
						companies = product.listCo;
					}	
					
					@Override
					public void onFailure(String message) {
						task.hasData = false;
						task.message = message;
						task.cancel(true);
					}
				});
			}

			@Override
			public void updateUI() {
				companyAdapter = new CompanyAdapter(VatgiaCompaniesActivity.this, 0, companies);
				listCompanies.setAdapter(companyAdapter);
			}
		});
		task.execute();
	}
}
