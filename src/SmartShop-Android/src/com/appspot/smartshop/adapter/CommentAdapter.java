package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

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
			
			ImageView imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
			// TODO load avatar of user and display
			
			EditText txtComment = (EditText) convertView.findViewById(R.id.txtComment);
			txtComment.setText(comment.content);
		}
		
		return convertView;
	}

}
