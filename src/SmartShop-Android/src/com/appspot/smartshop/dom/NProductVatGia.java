package com.appspot.smartshop.dom;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class NProductVatGia {

	public int numOfResults;
	public int numOfPages;
	public int numOfStore;
	public String imageThumbnail, imageBlankThumbnail, urlListShop,
			productName, priceVND, categoryPageURL, categoryName;
	
	public NProductVatGia() {}
	
	public static Type getType() {
		return new TypeToken<List<NProductVatGia>>() {}.getType();
	}
}
