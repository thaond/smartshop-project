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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class CompanyAdapter extends ArrayAdapter<Company> {
	private LayoutInflater inflater;
	private static int ATTR_NAME_WIDTH = Utils.getScreenWidth() / 4;
	private static int ATTR_VALUE_WIDTH = Utils.getScreenWidth() * 3 / 4;
	
	public CompanyAdapter(Context context, int textViewResourceId,
			List<Company> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.company_item, null);

			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
			holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
			holder.txtExtraInfo = (TextView) convertView.findViewById(R.id.txtExtraInfo);
//			holder.txtPrice.setWidth(ATTR_NAME_WIDTH);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Company company = getItem(position);
		holder.txtName.setText(company.name);
		holder.txtPrice.setText(company.vnd + " VND " + 
				(company.usd != null ? ("(" + company.usd + " USD" + ")") : ""));
		// TODO comment of vatgia product
//		holder.txtComment.setText(Html.fromHtml(company.comment));
		holder.txtExtraInfo.setText(Html.fromHtml(company.getExtraInfo()));

		return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView txtPrice;
		TextView txtComment;
		TextView txtExtraInfo;
	}

}
