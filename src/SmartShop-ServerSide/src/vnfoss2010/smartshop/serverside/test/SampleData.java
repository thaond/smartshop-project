package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

public class SampleData {
	public static ArrayList<UserInfo> getSampleListUserInfos() {
		Calendar calendar = Calendar.getInstance();
		ArrayList<UserInfo> listUserInfos = new ArrayList<UserInfo>();
		listUserInfos.add(new UserInfo("hieu", "hieu", "Hieu", "Hua Phan Minh",
				"123123123", "hieu@gmail.com", new Date(1987, 1, 1),
				"Tan Phu district", 10.213D, 106.123123D, ""));
		
		listUserInfos.add(new UserInfo("tam", "tam", "Tam", "Vo Minh",
				"123123123", "tam@gmail.com", new Date(1989, 12, 22),
				"Binh Tan district", 10.213D, 106.123123D, ""));
		
		listUserInfos.add(new UserInfo("nghia", "nghia", "Nghia", "Le Trong",
				"123123123", "nghia@gmail.com", new Date(1989, 1, 1),
				"District 6", 10.213D, 106.123123D, ""));
		
		listUserInfos.add(new UserInfo("duc", "duc", "Duc", "Cao Tien",
				"123123123", "duc@gmail.com", new Date(1989, 12, 8),
				"District 10", 10.213D, 106.123123D, ""));
		
		listUserInfos.add(new UserInfo("loi", "loi", "Lợi", "Nguyễn Văn",
				"123123123", "loi@gmail.com", new Date(1990, 1, 1),
				"District 10", 10.213D, 106.123123D, ""));

		return listUserInfos;
	}
	public static ArrayList<Category> getSampleCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
//		categories.add(new Category("comp","May tinh",null));
//		categories.add(new Category("lap","Laptop","comp"));
//		categories.add(new Category("net","Thiet bi mang","comp"));
//		categories.add(new Category("soft","Software","comp"));
		
		return categories;
	}
}
