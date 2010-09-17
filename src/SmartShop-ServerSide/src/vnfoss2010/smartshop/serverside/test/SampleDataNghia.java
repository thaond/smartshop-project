package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

public class SampleDataNghia {
	public static ArrayList<UserInfo> getSampleListUserInfos() {
		Calendar calendar = Calendar.getInstance();
		ArrayList<UserInfo> listUserInfos = new ArrayList<UserInfo>();
		return listUserInfos;
	}

	public static ArrayList<Category> getSampleCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		return categories;
	}

	public static ArrayList<Product> getSampleProducts() {
		ArrayList<Product> products = new ArrayList<Product>();

		return products;
	}

	public static ArrayList<Page> getSamplePages() {
		ArrayList<Page> pages = new ArrayList<Page>();
		Page page ;
		Random myRandom = new Random();
		
		page = new Page("Can mua ban phim game", "", "Can mua ban phim game xin", 1, new Date(110,3,2), new Date(110,2,3), "tam");
		page.getSetCategoryKeys().add("accessories_desktop");
		page.getSetCategoryKeys().add("computer");
		page.getSetCategoryKeys().add("desktop");
		pages.add(page);
		
		page = new Page("Can mua laptop", "", "Can mua lap top dell core i7",12, new Date(110,6,7), new Date(110,11,12), "nghia");
		page.getSetCategoryKeys().add("laptop");
		pages.add(page);
		
		page = new Page("Can mua loa bose", "", "Can mua loa bose xin gia duoi 5tr", 14, new Date(110,5,6), new Date(110,4,9), "tam");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua iphone", "", "Can mua iphone 3g chua unlock",Math.abs(myRandom.nextInt()%1000), new Date(110,10,23), new Date(110,6,7), "duc");
		page.getSetCategoryKeys().add("mobile_pda");
		pages.add(page);
		
		page = new Page("Can mua headphone bose", "", "Can mua headphone bose gia tu 1.5tr den 2tr", Math.abs(myRandom.nextInt()%1000), new Date(110,1,3), new Date(110,26,3), "hieu");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Can mua chuot logitech", "", "Can mua chuot logitech xin sll", Math.abs(myRandom.nextInt()%1000), new Date(110,1,23), new Date(110,10,3), "loi");
		page.getSetCategoryKeys().add("office_device");
		pages.add(page);
		
		page = new Page("Can mua man hinh LCD samsung", "", "Can mua man hinh LCD Samsung o Q6", Math.abs(myRandom.nextInt()%1000), new Date(110,1,8), new Date(110,10,2), "nghia");
		page.getSetCategoryKeys().add("office_device");
		page.getSetCategoryKeys().add("desktop");
		page.getSetCategoryKeys().add("accessories_desktop");
		pages.add(page);
				
		page = new Page("Can mua o cung di dong", "", "Can mua o cung di dong 500GB gia 2tr", Math.abs(myRandom.nextInt()%1000), new Date(110,3,23), new Date(110,9,23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Can mua o cung di dong", "", "Can mua o cung di dong 500GB gia 2tr", Math.abs(myRandom.nextInt()%1000), new Date(110,3,23), new Date(110,9,23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Can mua o tai phone Nokia", "", "Can mua tai phone Nokia gia 100k", Math.abs(myRandom.nextInt()%1000), new Date(110,3,23), new Date(110,9,23), "loi");
		page.getSetCategoryKeys().add("accessories_desktop");
		pages.add(page);
		
		page = new Page("Can mua bo tan nhiet", "", "Can mua bo tan nhiet gia 1tr", Math.abs(myRandom.nextInt()%1000), new Date(110,3,23), new Date(110,9,23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Can mua RAM laptop", "", "Can mua RAM laptop gia 2tr", Math.abs(myRandom.nextInt()%1000), new Date(110,3,23), new Date(110,9,23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Can mua Mouse", "", "Can mua Mouse gia 200k", Math.abs(myRandom.nextInt()%1000), new Date(110,3,23), new Date(110,9,23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		return pages;
	}

	public static ArrayList<Comment> getSampleComments() {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Comment comment ;
		return comments;
	}
}
