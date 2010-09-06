package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

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
		categories.add(new Category("mobile_pda","Mobile, PDA",null));
		categories.add(new Category("mobile_pda_sub","Mobile, PDA","mobile_pda"));
		categories.add(new Category("mobile_accessories","Phụ kiện, thiết bị sửa chữa","mobile_pda"));
		categories.add(new Category("telecommunication","Thiết bị viễn thông","mobile_pda"));
		
		categories.add(new Category("computer","Máy tính, linh kiện, dịch vụ",null));
		categories.add(new Category("desktop","Máy tính để bàn","computer"));
		categories.add(new Category("accessories_desktop","Linh kiện máy tính để bàn","computer"));
		categories.add(new Category("laptop","Laptop","computer"));
		categories.add(new Category("accessories_laptop","Linh kiện Laptop","computer"));
		categories.add(new Category("software","Phần mềm máy tính","computer"));
		categories.add(new Category("driver","Driver","computer"));
		categories.add(new Category("computer_other","Khác","computer"));
		
		categories.add(new Category("office","Đồ dùng văn phòng",null));
		categories.add(new Category("edu_device","Thiết bị giáo dục","office"));
		categories.add(new Category("office_device","Thiết bị văn phòng","office"));
		categories.add(new Category("stationery","Văn phòng phẩm","office"));
		
		return categories;
	}
	
	public static ArrayList<Product> getSampleProducts(){
		ArrayList<Product> products = new ArrayList<Product>();
		
		Product product;
		product = new Product("Laptop D830", 1000, false, 2, "12 month",
				"China", "Đường Lý Thường Kiệt", 10.761018,106.654902, "Mới 100%" , 3,new Date(110, 8, 12) , "tam");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		product = new Product("Bàn phím BenQ", 20, false, 2, "12 month",
				"China", "Đường Cách Mạng Tháng 8", 10.785636,106.667114, "Bàn phím gõ rất mềm mại" , 3,new Date(110,7,5), "duc");
		product.getSetCategoryKeys().add("accessories_desktop");
		products.add(product);
		
		product = new Product("Headphone Sonic", 3, false, 2, "6 month",
				"China", "Đường Cách Mạng Tháng 8", 10.785636,106.667114, "Tai nghe chất lượng tuyệt đỉnh" , 3,new Date(110,4,3), "nghia");
		product.getSetCategoryKeys().add("accessories_desktop");
		product.getSetCategoryKeys().add("accessories_laptop");
		products.add(product);
		
		product = new Product("Mouse Misumi", 7, false, 2, "12 month",
				"China", "Đường Lạc Long Quân", 10.783528,106.650828, "Chuột không sợ mèo" , 3,new Date(110,1,1), "hieu");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		product = new Product("Bàn Hòa Phát", 200, false, 2, "12 month",
				"China", "Đường Tân kì tân quý", 10.79803,106.615659, "Bàn làm việc chất lượng tuyệt vời" , 3,new Date(110,2,3), "nghia");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		/* Mobile sample */
		product = new Product("Nokia 6670", 1000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789,106.618749, "Chất lượng vượt thời gian" , 3,new Date(110,5,6), "tam");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);
		
		product = new Product("Samsung wave", 3000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789,106.618749, "Chất lượng vượt thời gian" , 3,new Date(110,6,5), "tam");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);
		
		product = new Product("Nokia 1110", 1000, false, 2, "12 month",
				"China", "Đường Hòa Bình", 10.770059,106.632653, "Chất lượng vượt thời gian" , 3,new Date(110,2,1), "duc");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);
		
		product = new Product("Android G1", 1000, false, 2, "12 month",
				"China", "Trường Chinh", 10.831394,106.622547, "Chất lượng vượt thời gian" , 3,new Date(110,7,9), "hieu");
		product.getSetCategoryKeys().add("mobile_pda_sub");
		products.add(product);
		
		/* accessories sample */
		product = new Product("Sạc Nokia", 1000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789,106.618749, "Chất lượng vượt thời gian" , 3,new Date(110,1,10), "tam");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);
		
		product = new Product("Sạc Samsung wave", 3000, false, 2, "12 month",
				"China", "Tân Hòa Đông", 10.764789,106.618749, "Chất lượng vượt thời gian" , 3,new Date(110,10,24), "tam");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);
		
		product = new Product("Sạc Nokia 1110", 1000, false, 2, "12 month",
				"China", "Đường Hòa Bình", 10.770059,106.632653, "Chất lượng vượt thời gian" , 3,new Date(110,3,5), "duc");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);
		
		product = new Product("Sạc Android G1", 1000, false, 2, "12 month",
				"China", "Trường Chinh", 10.831394,106.622547, "Chất lượng vượt thời gian" , 3,new Date(110,6,9), "hieu");
		product.getSetCategoryKeys().add("mobile_accessories");
		products.add(product);
		
		/* Laptop */
		product = new Product("sony vaio CW23 ", 1200, false, 2, "12 month",
				"China", "Đường Quang Trung", 10.841553,106.644005, "Man hình rất xịn" , 3,new Date(110,4,3), "tamvo");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		product = new Product("sony vaio  F115", 1000, false, 2, "12 month",
				"China", "Binh Tan", 10.777268,106.6563, "Man hình rất xịn" , 3,new Date(110,1,7), "tamvo");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		product = new Product("HP Envy", 2000, false, 2, "12 month",
				"USA", "Tan Binh", 10.777268,106.6563, "Màn hình 3D" , 3,new Date(110,2,3), "hieu");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		product = new Product("HP Pavillon", 7000, false, 2, "12 month",
				"China", "Q3", 10.777268,106.6563, "Man hình rất xịn" , 3,new Date(110,1,2), "duc");
		product.getSetCategoryKeys().add("laptop");
		products.add(product);
		
		return products;
	}
	
//	public static ArrayList<Page> getSamplePages(){
//		ArrayList<Page> listPages = new ArrayList<Page>();
//		
//		Page page;
//		page = new Page("Giới thiệu sản phẩm Laptop", "Nội dung", null, 0, new Date(), new Date(), "tam", "laptop");
//		listPages.add(page);
//		
//		page = new Page("Giới thiệu sản phẩm các phụ kiện di động", "Nội dung", null, 0, new Date(), new Date(), "tam", "mobile_accessories");
//		listPages.add(page);
//		
//		page = new Page("Giới thiệu sản phẩm ĐTDĐ", "Nội dung", null, 0, new Date(), new Date(), "duc", "mobile_pda_sub");
//		listPages.add(page);
//		
//		page = new Page("Giới thiệu sản phẩm Laptop Dell", "Nội dung", null, 0, new Date(), new Date(), "nghia", "laptop");
//		listPages.add(page);
//		
//		return listPages;
//		
//	}
}

