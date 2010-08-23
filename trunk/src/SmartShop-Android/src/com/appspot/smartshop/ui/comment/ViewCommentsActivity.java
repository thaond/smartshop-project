package com.appspot.smartshop.ui.comment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CommentAdapter;
import com.appspot.smartshop.dom.Comment;
import com.appspot.smartshop.mock.MockComments;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.SimpleAsyncTask;

public class ViewCommentsActivity extends Activity {
	public static final String TAG = "[ViewCommentsActivity]";

	private CommentAdapter adapter;
	private ListView listComments;
	
	private long id;
	
	private List<Comment> comments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_comments);
		
		// TODO (condorhero01): get page_id or product_id from intent
		id = getIntent().getExtras().getLong(Global.ID_OF_COMMENTS);
		Log.d(TAG, "id of comments = " + id);
		
		// add new comment
		Button btnAddComment = (Button) findViewById(R.id.btnAddComment);
		btnAddComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addNewComment();
			}
		});
		
		// list comments
		listComments = (ListView) findViewById(R.id.listComments);
		
		// load comments
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void loadData() {
				// TODO (condorhero01): load list comments based on id of page or product
				comments = MockComments.getInstance();
				adapter = new CommentAdapter(ViewCommentsActivity.this, 0, comments);
			}

			@Override
			public void updateUI() {
				listComments.setAdapter(adapter);
			}
		}).execute();
	}

	protected void addNewComment() {
		Log.d(TAG, "add new comment");
		// TODO (condorhero01): add new comments to list
		Comment comment = new Comment();
		comment.content = "new comment";
		
		adapter.addNewComment(comment);
	}
}
