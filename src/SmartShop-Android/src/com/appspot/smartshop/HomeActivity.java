package com.appspot.smartshop;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.map.SearchProductsOnMapActivity;
import com.appspot.smartshop.mock.MockPage;
import com.appspot.smartshop.mock.MockUserInfo;
import com.appspot.smartshop.ui.page.PageActivity;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.ui.product.PostProductActivity;
import com.appspot.smartshop.ui.product.ViewProductActivity;
import com.appspot.smartshop.ui.product.vatgia.SearchVatgiaActivity;
import com.appspot.smartshop.ui.user.ViewUserInfoActivity;
import com.appspot.smartshop.ui.user.ViewUserProfileActivity;
import com.appspot.smartshop.ui.user.email.SendEmailActivity;
import com.appspot.smartshop.ui.user.email.SendEmailToAdminActivity;
import com.appspot.smartshop.ui.user.subcribe.CreateSubcribeActivity;
import com.appspot.smartshop.ui.user.subcribe.ProductsListOfSubcribeActivity;
import com.appspot.smartshop.ui.user.subcribe.UserSubcribeListActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;
import com.google.gson.JsonObject;

public class HomeActivity extends Activity {
	public static final String TAG = "[HomeActivity]";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		Global.application = this;

		Button btn1 = (Button) findViewById(R.id.Button01);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test1();
			}
		});

		Button btn2 = (Button) findViewById(R.id.Button02);
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test2();
			}
		});

		Button btn3 = (Button) findViewById(R.id.Button03);
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test3();
			}
		});
	}

	// TODO (condorhero01): place test function into test1, test2, or test3 to
	// test UI
	// should adjust the button's text in main.xml file as name of the test
	protected void test1() {
		testCreateSubcribe();
	}

	protected void test2() {
		testSendEmail();
	}

	protected void test3() {
	}
	
	void testCreateSubcribe() {
		Intent intent = new Intent(this, CreateSubcribeActivity.class);
		startActivity(intent);
	}
	
	void testLoadProductsOfSubcribe() {
		Intent intent = new Intent(this, ProductsListOfSubcribeActivity.class);
		intent.putExtra(Global.SUBCRIBE_ID, 365L);
		startActivity(intent);
	}
	
	void testViewProfile() {
		Intent intent = new Intent(this, ViewUserProfileActivity.class);
		startActivity(intent);
	}
	
	void testSendEmail() {
		Intent intent = new Intent(this, SendEmailActivity.class);
		startActivity(intent);
	}
	
	void testSendEmailToAdmin() {
		Intent intent = new Intent(this, SendEmailToAdminActivity.class);
		startActivity(intent);
	}
	
	void testSearchVatgia() {
		Intent intent = new Intent(this, SearchVatgiaActivity.class);
		startActivity(intent);
	}
	
	void testSearchProductsOnMap() {
		Intent intent = new Intent(this, SearchProductsOnMapActivity.class);
		startActivity(intent);
	}

	void testCategoriesDialog() {
		CategoriesDialog.showCategoriesDialog(this, new CategoriesDialogListener() {
			
			@Override
			public void onCategoriesDialogClose(Set<String> categories) {
				System.out.println(categories);
			}
		});
	}

	private void testEditPage() {
		Intent intent = new Intent(this, PageActivity.class);
		intent.putExtra(Global.PAGE, MockPage.getInstance());
		startActivity(intent);
	}

	private void testPagesList() {
		Intent intent = new Intent(this, PagesListActivity.class);
		startActivity(intent);
	}

	private void testCreatePage() {
		Intent intent = new Intent(this, PageActivity.class);
		startActivity(intent);
	}

	private void testViewPage() {
		Intent intent = new Intent(this, ViewPageActivity.class);
		intent.putExtra("page", MockPage.getInstance());

		startActivity(intent);
	}

	private void testEditUserInfo() {
		Intent intent = new Intent(this, ViewUserInfoActivity.class);
		intent.putExtra(Global.USER_INFO, MockUserInfo.getInstance());
		intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
		startActivity(intent);
	}

	private void testViewUserInfo() {
		Intent intent = new Intent(this, ViewUserInfoActivity.class);
		intent.putExtra(Global.USER_INFO, MockUserInfo.getInstance());
		startActivity(intent);
	}

	private void testDirectionList() {
		double lat1, lat2, lng1, lng2;
		lat1 = 10.754499;
		lng1 = 106.640582;
		lat2 = 10.83527;
		lng2 = 106.620069;
		Intent intent = new Intent(this, DirectionListActivity.class);
		intent.putExtra("lat1", lat1);
		intent.putExtra("lat2", lat2);
		intent.putExtra("lng1", lng1);
		intent.putExtra("lng2", lng2);
		startActivity(intent);
	}

	private void testRegisterForm() {
		Intent intent = new Intent(this, ViewUserInfoActivity.class);
		startActivity(intent);
	}

	private void testGmapIntent() {
		String url = "http://maps.google.com/maps?saddr=10.775495,106.661181&daddr=10.76072,106.661021&language=vi";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

	private void testViewProduct() {
		Intent intent = new Intent(this, ViewProductActivity.class);
		startActivity(intent);
	}

	private void testPostProduct() {
		Global.application = this;
		Intent intent = new Intent(this, PostProductActivity.class);
		startActivity(intent);
	}
}