package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Comment;

public class CommentAdapter extends ArrayAdapter<Comment> {
	
	private int viewResourceId;
	private LayoutInflater inflater;

	public CommentAdapter(Context context, int textViewResourceId,
			List<Comment> objects) {
		super(context, textViewResourceId, objects);
		viewResourceId = R.layout.comment_list_item;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(viewResourceId, null);
			
			Comment comment = (Comment) getItem(position);
			
			Button imgAvatar = (Button) convertView.findViewById(R.id.btnAvatar);
			// TODO (condorhero01): load avatar of user and display
			
			TextView txtComment = (TextView) convertView.findViewById(R.id.txtComment);
			txtComment.setText(comment.content);
		}
		
		return convertView;
	}

}
