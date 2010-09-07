package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.AttributeAdapter.ViewHolder;
import com.appspot.smartshop.dom.Attribute;
import com.appspot.smartshop.dom.Company;
import com.appspot.smartshop.ui.product.ViewProductUserDefinedAttributeActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class CompanyAdapter extends ArrayAdapter<Company> {
	public CompanyAdapter(Context context, int textViewResourceId,
			List<Company> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}
	

	private LayoutInflater inflater;



	private static int ATTR_NAME_WIDTH = Utils.getScreenWidth() / 4;
	private static int ATTR_VALUE_WIDTH = Utils.getScreenWidth() * 3 / 4;


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.company_item, null);

			holder = new ViewHolder();
			holder.txtNameOfCompany = (TextView) convertView
					.findViewById(R.id.nameOfCompany);
			holder.txtPrice = (EditText) convertView
					.findViewById(R.id.txtPrice);

			holder.valueOfPrice = (EditText) convertView
					.findViewById(R.id.price);

			holder.txtPrice.setWidth(ATTR_NAME_WIDTH);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Company company = getItem(position);
		holder.txtNameOfCompany.setText(company.name);
		holder.valueOfPrice.setText(company.vnd + " VND " + "(" + company.usd
				+ "USD" + ")");

		holder.valueOfPrice.setFilters(Global.uneditableInputFilters);

		return convertView;
	}

	static class ViewHolder {
		public static final String txtName = null;
		TextView txtNameOfCompany;
		TextView txtPrice;
		EditText valueOfPrice;
	}

}
