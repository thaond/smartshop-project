package com.appspot.smartshop.mock;

import com.appspot.smartshop.dom.CategoryInfo;

public class MockCategory {
	public static CategoryInfo getInstance() {
		String[] parentCategory = { "Áo quần", "Giày dép", "Mỹ phẩm",
				"Váy dự tiệc" };
		String[][] childrenCategory = { { "Nam", "Nữ" },
				{ "Mỹ", "Trung Quốc", "Việt Nam" }, { "Son môi", "Tào Lao" },
				{ "Đầm bầu", "Váy hiệu", "Hàng Fake" } };
		CategoryInfo category = new CategoryInfo(parentCategory,
				childrenCategory);
		return category;

	}

}
