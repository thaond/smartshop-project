package com.appspot.smartshop.adapter;

import java.util.List;
import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserDefineAttribute;
import com.appspot.smartshop.ui.product.PostProductActivityUserDefine;
import com.appspot.smartshop.utils.Global;

import android.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class UserDefineAttributeAdapter extends
		ArrayAdapter<UserDefineAttribute> {
	public int resourceId;

	public UserDefineAttributeAdapter(Context context, int textViewResourceId,
			List<UserDefineAttribute> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		final UserDefineAttribute product = this.getItem(position);
		// label width
		final TextView attName = (TextView) view
				.findViewById(R.id.txtNewAttribute);
		attName.setText(product.newAttribute);

		final TextView newValueAtt = (TextView) view
				.findViewById(R.id.txtValueOfNewAttribute);
		newValueAtt.setText(product.valueOfNewAttribute);
		Button btnXong = (Button) view.findViewById(R.id.KetThuc);
		btnXong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// attributeSet contains all new attribute that user define
				PostProductActivityUserDefine.attributeSet
						.add(new UserDefineAttribute(attName.getText()
								.toString(), newValueAtt.getText().toString()));
				//test the result
				for (int i = 0; i < PostProductActivityUserDefine.attributeSet
						.size(); i++) {
					Log.d("tag", PostProductActivityUserDefine.attributeSet
							.get(i).getNewAttribute()
							+ " "
							+ PostProductActivityUserDefine.attributeSet.get(i)
									.getValueOfNewAttribute());
				}
				//set Label finished for btnXong
				newValueAtt.setText("Dữ liệu đã được đưa tới máy chủ");
				newValueAtt.setEnabled(false);
				attName.setEnabled(false);
				

			}
		});
		return view;
	}
}
