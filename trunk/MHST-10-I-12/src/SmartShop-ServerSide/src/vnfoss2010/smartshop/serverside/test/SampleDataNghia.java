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
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

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
		Page page;
		Random myRandom = new Random();

		page = new Page("Can mua ban phim game", "",
				"Can mua ban phim game xin", 1, new Date(110, 3, 2), new Date(
						110, 2, 3), "tam");
		page.getSetCategoryKeys().add("accessories_desktop");
		page.getSetCategoryKeys().add("computer");
		page.getSetCategoryKeys().add("desktop");
		pages.add(page);

		page = new Page("Can mua laptop", "", "Can mua lap top dell core i7",
				12, new Date(110, 6, 7), new Date(110, 11, 12), "nghia");
		page.getSetCategoryKeys().add("laptop");
		pages.add(page);

		page = new Page("Can mua loa bose", "",
				"Can mua loa bose xin gia duoi 5tr", 14, new Date(110, 5, 6),
				new Date(110, 4, 9), "tam");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua iphone", "", "Can mua iphone 3g chua unlock",
				Math.abs(myRandom.nextInt() % 1000), new Date(110, 10, 23),
				new Date(110, 6, 7), "duc");
		page.getSetCategoryKeys().add("mobile_pda");
		pages.add(page);

		page = new Page("Can mua headphone bose", "",
				"Can mua headphone bose gia tu 1.5tr den 2tr", Math
						.abs(myRandom.nextInt() % 1000), new Date(110, 1, 3),
				new Date(110, 26, 3), "hieu");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua chuot logitech", "",
				"Can mua chuot logitech xin sll", Math
						.abs(myRandom.nextInt() % 1000), new Date(110, 1, 23),
				new Date(110, 10, 3), "loi");
		page.getSetCategoryKeys().add("office_device");
		pages.add(page);

		page = new Page("Can mua man hinh LCD samsung", "",
				"Can mua man hinh LCD Samsung o Q6", Math.abs(myRandom
						.nextInt() % 1000), new Date(110, 1, 8), new Date(110,
						10, 2), "nghia");
		page.getSetCategoryKeys().add("office_device");
		page.getSetCategoryKeys().add("desktop");
		page.getSetCategoryKeys().add("accessories_desktop");
		pages.add(page);

		page = new Page("Can mua o cung di dong", "",
				"Can mua o cung di dong 500GB gia 2tr", Math.abs(myRandom
						.nextInt() % 1000), new Date(110, 3, 23), new Date(110,
						9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua o cung di dong", "",
				"Can mua o cung di dong 500GB gia 2tr", Math.abs(myRandom
						.nextInt() % 1000), new Date(110, 3, 23), new Date(110,
						9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua o tai phone Nokia", "",
				"Can mua tai phone Nokia gia 100k", Math
						.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("accessories_desktop");
		pages.add(page);

		page = new Page("Can mua bo tan nhiet", "",
				"Can mua bo tan nhiet gia 1tr", Math
						.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua RAM laptop", "", "Can mua RAM laptop gia 2tr",
				Math.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		page = new Page("Can mua Mouse", "", "Can mua Mouse gia 200k", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);

		// TODO
		page = new Page("Cần mua laptop để đi công tác xa và giải trí ?",
				UtilsFunction.getContent("./sample/page/page1.txt"), "", Math
						.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Có nên mua Galaxy Tab hàng FPT phân phối không ?",
				UtilsFunction.getContent("./sample/page/page2.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("HCM - Cần mua khúc dưới tai nghe 3,5mm có mic",
				UtilsFunction.getContent("./sample/page/page2.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("accessories_desktop");
		page.getSetCategoryKeys().add("accessories_laptop");
		pages.add(page);
		
		page = new Page("Chuyên thu mua phế liệu sắt ,thép,nhôm inox..v...v..",
				UtilsFunction.getContent("./sample/page/page4.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("stationery");
		page.getSetCategoryKeys().add("office_device");
		pages.add(page);
		
		page = new Page("HCM - cần MUA 1 thanh SDRAM 128/256MB và 1 thanh DDRAM2 2GB bus 533/667/800 .........",
				UtilsFunction.getContent("./sample/page/page5.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		page.getSetCategoryKeys().add("desktop");
		pages.add(page);
		
		page = new Page("cần mua vé ca nhạc thay lời muốn nói",
				UtilsFunction.getContent("./sample/page/page6.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("computer_other");
		pages.add(page);
		
		page = new Page("Cần mua Cùm phuộc và dĩa 300mm RACING BOY",
				UtilsFunction.getContent("./sample/page/page7.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("stationery");
		pages.add(page);
		
		page = new Page("Thu mua xác lò viba,nối cơm điện tử,bếp điện từ,đèn-quạt sạc và ly sinh tố các loại .... ",
				UtilsFunction.getContent("./sample/page/page8.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("driver");
		pages.add(page);
		
		page = new Page("Cần mua keyboard tiếng Nhật",
				UtilsFunction.getContent("./sample/page/page9.txt"), "", Math
				.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("software");
		page.getSetCategoryKeys().add("accessories_laptop");
		page.getSetCategoryKeys().add("accessories_desktop");
		pages.add(page);

		page = new Page("Thu mua xác lò vi ba hư cũ các loại",
				UtilsFunction.getContent("./sample/page/page10.txt"), "", Math
						.abs(myRandom.nextInt() % 1000), new Date(110, 3, 23),
				new Date(110, 9, 23), "loi");
		page.getSetCategoryKeys().add("office_device");
		pages.add(page);

		return pages;
	}

	public static ArrayList<Comment> getSampleComments() {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Comment comment;
		return comments;
	}
}
