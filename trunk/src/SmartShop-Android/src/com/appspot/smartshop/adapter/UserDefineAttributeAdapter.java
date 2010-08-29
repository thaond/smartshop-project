package com.appspot.smartshop.adapter;

import java.util.ArrayList;
import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.util.Log;
import android.view.WindowManager;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserDefineAttributeAdapter extends
		ArrayAdapter<UserDefineAttribute> {
	public int resourceId;
	public ArrayList<UserDefineAttribute> att;

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
		att = new ArrayList<UserDefineAttribute>();
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
				att.add(new UserDefineAttribute(attName.getText().toString(),
						newValueAtt.getText().toString()));
				for (int i = 0; i < att.size(); i++) {
					Log.d("tag", att.get(i).getNewAttribute() + " "
							+ att.get(i).getValueOfNewAttribute());
				}
			}
		});
		return view;
	}
}
