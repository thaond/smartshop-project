package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;

public class SampleData {
	public static ArrayList<UserInfo> getSampleListUserInfos() {
		Calendar calendar = Calendar.getInstance();
		ArrayList<UserInfo> listUserInfos = new ArrayList<UserInfo>();
		listUserInfos.add(new UserInfo("hieu", "hieu", "Hieu", "Hua Phan Minh",
				"123123123", "hieu@gmail.com", new Date(87, 1, 1),
				"Tan Phu district", 10.213D, 106.123123D, ""));

		listUserInfos.add(new UserInfo("tam", "tam", "Tam", "Vo Minh",
				"123123123", "tam@gmail.com", new Date(89, 12, 22),
				"Binh Tan district", 10.213D, 106.123123D, ""));

		listUserInfos.add(new UserInfo("nghia", "nghia", "Nghia", "Le Trong",
				"123123123", "nghia@gmail.com", new Date(89, 1, 1),
				"District 6", 10.213D, 106.123123D, ""));

		listUserInfos.add(new UserInfo("duc", "duc", "Duc", "Cao Tien",
				"123123123", "duc@gmail.com", new Date(89, 12, 8),
				"District 10", 10.213D, 106.123123D, ""));

		listUserInfos.add(new UserInfo("loi", "loi", "Lợi", "Nguyễn Văn",
				"123123123", "loi@gmail.com", new Date(90, 1, 1),
				"District 10", 10.213D, 106.123123D, ""));

		return listUserInfos;
	}

	public static ArrayList<Category> getSampleCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		categories.add(new Category("mobile_pda", "Mobile, PDA", null));
		categories.add(new Category("mobile_pda_sub", "Mobile, PDA",
				"mobile_pda"));
		categories.add(new Category("mobile_accessories",
				"Phụ kiện, thiết bị sửa chữa", "mobile_pda"));
		categories.add(new Category("telecommunication", "Thiết bị viễn thông",
				"mobile_pda"));

		categories.add(new Category("computer", "Máy tính, linh kiện, dịch vụ",
				null));
		categories.add(new Category("desktop", "Máy tính để bàn", "computer"));
		categories.add(new Category("accessories_desktop",
				"Linh kiện máy tính để bàn", "computer"));
		categories.add(new Category("laptop", "Laptop", "computer"));
		categories.add(new Category("accessories_laptop", "Linh kiện Laptop",
				"computer"));
		categories
				.add(new Category("software", "Phần mềm máy tính", "computer"));
		categories.add(new Category("driver", "Driver", "computer"));
		categories.add(new Category("computer_other", "Khác", "computer"));

		categories.add(new Category("office", "Đồ dùng văn phòng", null));
		categories
				.add(new Category("edu_device", "Thiết bị giáo dục", "office"));
		categories.add(new Category("office_device", "Thiết bị văn phòng",
				"office"));
		categories.add(new Category("stationery", "Văn phòng phẩm", "office"));

		return categories;
	}

	public static ArrayList<Product> getSampleProducts() {
		ArrayList<Product> products = new ArrayList<Product>();

		Product product;
		product = new Product("Billiards - Bar - Coffee Inter", 7, false, 2,
				"12 month", "Việt Nam", "708, Lê Hồng Phong, P.12, Q.10, HCM",
				10.774344, 106.671703, "Billiards - Bar - Coffee Inter", 3,
				new Date(110, 1, 1), "hieu");
		product.getSetCategoryKeys().add("coffee");
		products.add(product);
		product = new Product("Đen & Trắng Coffee", 1000, false, 2, "12 month",
				"Việt Nam", "47, Tú Xương, P.7, Q.3", 10.779698, 106.68535,
				"Không gian yên tĩnh", 3, new Date(110, 8, 12), "tam");
		product.getSetCategoryKeys().add("coffee");
		products.add(product);

		product = new Product("Coffee World", 20, false, 2, "12 month",
				"China", "Võ Văn Tần, 6th Ward, Saigon", 10.776494, 106.689985,
				"Phục vụ chu đáo", 3, new Date(110, 7, 5), "duc");
		product.getSetCategoryKeys().add("coffee");
		products.add(product);

		product = new Product("New Pearl", 3, false, 2, "6 month", "China",
				"205 Pham Ngu Lao, Phạm Ngũ Lão, District 1", 10.770213,
				106.693118, "Giá cả phải chăng", 3, new Date(110, 4, 3),
				"nghia");
		product.getSetCategoryKeys().add("coffee");
		products.add(product);

		product = new Product("Fruit shake", 200, false, 2, "12 month",
				"China", "72 Nguyen Cu Trinh, Ho Chi Minh City", 10.765322,
				106.689942, "Không gian sang trọng, quý phái", 3, new Date(110,
						2, 3), "nghia");
		product.getSetCategoryKeys().add("coffee");
		products.add(product);

		/* Mobile sample */
		product = new Product("Nokia 6670", 1000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789, 106.618749,
				"Chất lượng vượt thời gian", 3, new Date(110, 5, 6), "tam");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);

		product = new Product("Samsung wave", 3000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789, 106.618749,
				"Chất lượng vượt thời gian", 3, new Date(110, 6, 5), "tam");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);

		product = new Product("Nokia 1110", 1000, false, 2, "12 month",
				"China", "Đường Hòa Bình", 10.770059, 106.632653,
				"Chất lượng vượt thời gian", 3, new Date(110, 2, 1), "duc");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);

		product = new Product("Android G1", 1000, false, 2, "12 month",
				"China", "Trường Chinh", 10.831394, 106.622547,
				"Chất lượng vượt thời gian", 3, new Date(110, 7, 9), "hieu");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);

		/* accessories sample */
		product = new Product("Sạc Nokia", 1000, false, 2, "12 month", "China",
				"Tân Hòa Đông", 10.764789, 106.618749,
				"Chất lượng vượt thời gian", 3, new Date(110, 1, 10), "tam");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);

		product = new Product("Sạc Samsung wave", 3000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789, 106.618749,
				"Chất lượng vượt thời gian", 3, new Date(110, 10, 24), "tam");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);

		product = new Product("Sạc Nokia 1110", 1000, false, 2, "12 month",
				"China", "Đường Hòa Bình", 10.770059, 106.632653,
				"Chất lượng vượt thời gian", 3, new Date(110, 3, 5), "duc");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);

		product = new Product("Sạc Android G1", 1000, false, 2, "12 month",
				"China", "Trường Chinh", 10.831394, 106.622547,
				"Chất lượng vượt thời gian", 3, new Date(110, 6, 9), "hieu");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);

		/* Laptop */
		product = new Product("sony vaio CW23 ", 1200, false, 2, "12 month",
				"China", "Đường Quang Trung", 10.841553, 106.644005,
				"Man hình rất xịn", 3, new Date(110, 4, 3), "tamvo");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);

		product = new Product("sony vaio  F115", 1000, false, 2, "12 month",
				"China", "Binh Tan", 10.777268, 106.6563, "Man hình rất xịn",
				3, new Date(110, 1, 7), "tamvo");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);

		product = new Product("HP Envy", 2000, false, 2, "12 month", "USA",
				"Tan Binh", 10.777268, 106.6563, "Màn hình 3D", 3, new Date(
						110, 2, 3), "hieu");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);

		product = new Product("HP Pavillon", 7000, false, 2, "12 month",
				"China", "Q3", 10.777268, 106.6563, "Man hình rất xịn", 3,
				new Date(110, 1, 2), "duc");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);

		return products;
	}

	public static ArrayList<Notification> getSampleNotifications() {
		ArrayList<Notification> a = new ArrayList<Notification>();

		// (String content, long date, String username, int type)
		// (String content, long date, boolean isNew, String username, int type)
		// {
		a.add(new Notification("Thông báo số 1", System.currentTimeMillis(),
				"tam"));
		a.add(new Notification("loi đã add bạn vào friend",  System.currentTimeMillis(), true,
				"tam", 1, "loi"));

		a.add(new Notification("Thông báo số 1", System.currentTimeMillis(), "nghia"));
		a.add(new Notification("loi đã add bạn vào friend",  System.currentTimeMillis(), true,
				"nghia", 1, "loi"));

		a.add(new Notification("Thông báo số 1", System.currentTimeMillis(), "duc"));
		a.add(new Notification("loi đã add bạn vào friend",  System.currentTimeMillis(), true,
				"duc", 1, "loi"));

		a.add(new Notification("Thông báo số 1", System.currentTimeMillis(), "hieu"));
		a.add(new Notification("loi đã add bạn vào friend",  System.currentTimeMillis(), true,
				"hieu", 1, "loi"));

		return a;
	}

	public static ArrayList<UserSubcribeProduct> getSampleUserSubcribeProducts() {
		ArrayList<UserSubcribeProduct> a = new ArrayList<UserSubcribeProduct>();

		a.add(new UserSubcribeProduct("tam", 10.776854, 106.690548, 10000D,
				true, new Date(), "Mô tả", "Coffee", 1));
		UserSubcribeProduct u = new UserSubcribeProduct("tam", 10.764789,
				106.618749, 10000D, true, new Date(), "Mô tả", "nokia", 1);
		u.getCategoryList().add("mobile_pda_sub");
		a.add(u);
		a.add(new UserSubcribeProduct("tam", 10.776854, 106.690548, 10000D,
				false, new Date(), "Mô tả", "nokia", 1));

		a.add(new UserSubcribeProduct("duc", 10.776854, 106.690548, 10000D,
				true, new Date(), "Mô tả", "Coffee", 1));
		u = new UserSubcribeProduct("duc", 10.764789, 106.618749, 10000D, true,
				new Date(), "Mô tả", "nokia", 1);
		u.getCategoryList().add("mobile_pda_sub");
		a.add(u);
		a.add(new UserSubcribeProduct("duc", 10.776854, 106.690548, 10000D,
				false, new Date(), "Mô tả", "nokia", 1));

		a.add(new UserSubcribeProduct("nghia", 10.776854, 106.690548, 10000D,
				true, new Date(), "Mô tả", "Coffee", 1));
		u = new UserSubcribeProduct("nghia", 10.764789, 106.618749, 10000D,
				true, new Date(), "Mô tả", "nokia", 1);
		u.getCategoryList().add("mobile_pda_sub");
		a.add(u);
		a.add(new UserSubcribeProduct("nghia", 10.776854, 106.690548, 10000D,
				false, new Date(), "Mô tả", "nokia", 1));

		a.add(new UserSubcribeProduct("loi", 10.776854, 106.690548, 10000D,
				true, new Date(), "Mô tả", "Coffee", 1));
		u = new UserSubcribeProduct("loi", 10.764789, 106.618749, 10000D, true,
				new Date(), "Mô tả", "nokia", 1);
		u.getCategoryList().add("mobile_pda_sub");
		a.add(u);
		a.add(new UserSubcribeProduct("loi", 10.776854, 106.690548, 10000D,
				false, new Date(), "Mô tả", "nokia", 1));

		return a;
	}

	// public static ArrayList<Page> getSamplePages(){
	// ArrayList<Page> listPages = new ArrayList<Page>();
	//		
	// Page page;
	// page = new Page("Giới thiệu sản phẩm Laptop", "Nội dung", null, 0, new
	// Date(), new Date(), "tam", "laptop");
	// listPages.add(page);
	//		
	// page = new Page("Giới thiệu sản phẩm các phụ kiện di động", "Nội dung",
	// null, 0, new Date(), new Date(), "tam", "mobile_accessories");
	// listPages.add(page);
	//		
	// page = new Page("Giới thiệu sản phẩm ĐTDĐ", "Nội dung", null, 0, new
	// Date(), new Date(), "duc", "mobile_pda_sub");
	// listPages.add(page);
	//		
	// page = new Page("Giới thiệu sản phẩm Laptop Dell", "Nội dung", null, 0,
	// new Date(), new Date(), "nghia", "laptop");
	// listPages.add(page);
	//		
	// return listPages;
	//		
	// }
}
