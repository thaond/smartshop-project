package com.appspot.smartshop.ui.page;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CommentAdapter;
import com.appspot.smartshop.dom.Comment;

public class ViewCommentsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_comments);
		
		Button btnAddComment = (Button) findViewById(R.id.btnAddComment);
		btnAddComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addNewComment();
			}
		});
		
		// TODO (condorhero01): sample comments
		LinkedList<Comment> comments = new LinkedList<Comment>();
		for (int i = 0; i < 30; ++i) {
			Comment c = new Comment();
			c.content = "this is comment number " + (i + 1);
			comments.add(c);
		}
		
		ListView listComments = (ListView) findViewById(R.id.listComments);
		listComments.setAdapter(new CommentAdapter(this, 0, comments));
	}

	protected void addNewComment() {
	}
}
