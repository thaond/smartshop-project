package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
		
		page = new Page("Can mua ban phim game", "", "Can mua ban phim game xin", 100, new Date(), new Date(), "tam");
		page.getSetCategoryKeys().add("accessories_desktop");
		page.getSetCategoryKeys().add("computer");
		page.getSetCategoryKeys().add("desktop");
		pages.add(page);
		
		page = new Page("Can mua laptop", "", "Can mua lap top dell core i7", 100, new Date(), new Date(), "nghia");
		page.getSetCategoryKeys().add("laptop");
		pages.add(page);
		
		page = new Page("Can mua loa bose", "", "Can mua loa bose xin gia duoi 5tr", 100, new Date(), new Date(), "tam");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua iphone", "", "Can mua iphone 3g chua unlock", 100, new Date(), new Date(), "duc");
		page.getSetCategoryKeys().add("mobile_pda");
		pages.add(page);
		
		page = new Page("Can mua headphone bose", "", "Can mua headphone bose gia tu 1.5tr den 2tr", 100, new Date(), new Date(), "hieu");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Can mua chuot logitech", "", "Can mua chuot logitech xin sll", 100, new Date(), new Date(), "loi");
		page.getSetCategoryKeys().add("office_device");
		pages.add(page);
		
		page = new Page("Can mua man hinh LCD samsung", "", "Can mua man hinh LCD Samsung o Q6", 100, new Date(), new Date(), "nghia");
		page.getSetCategoryKeys().add("office_device");
		page.getSetCategoryKeys().add("desktop");
		page.getSetCategoryKeys().add("accessories_desktop");
		pages.add(page);
				
		page = new Page("Can mua o cung di dong", "", "Can mua o cung di dong 500GB gia 2tr", 100, new Date(), new Date(), "loi");
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
