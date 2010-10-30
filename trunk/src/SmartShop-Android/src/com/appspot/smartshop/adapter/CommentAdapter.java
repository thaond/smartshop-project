package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Comment;

public class CommentAdapter extends ArrayAdapter<Comment> {
	private LayoutInflater inflater;
	
	public CommentAdapter(Context context, int textViewResourceId,
			List<Comment> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.comment_list_item, null);
			
			holder = new ViewHolder();
			holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
			holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Comment comment = (Comment) getItem(position);
		holder.txtComment.setText(comment.content);
		holder.txtUsername.setText(comment.username + "\t");
		
		return convertView;
	}
	
	public void addNewComment(Comment comment) {
		add(comment);
		notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView txtUsername;
		TextView txtComment;
	}
}
