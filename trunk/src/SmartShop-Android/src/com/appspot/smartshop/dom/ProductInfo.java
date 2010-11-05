package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.reflect.TypeToken;

public class ProductInfo implements Serializable {
	public Long id;
	public int product_view;
	public String name = null;
	public double price;
	public String description = null;
	public boolean is_vat;
	public int quantity;
	public Date date_post = null;
	public String warranty = null;
	public String origin = null;
	public String address = null;
	public double lat;
	public double lng;
	public int sum_star;
	public int count_vote;
	public String username = null;
	public List<Media> setMedias;
	public Set<Long> setPagesId;
	public Set<String> setCategoryKeys;
	public Set<Attribute> attributeSets;

	public ProductInfo(String name, double price, String description) {
		this.name = name;
		this.price = price;
		this.description = description;
	}

	public ProductInfo() {
	}

	@Override
	public String toString() {
		// return "ProductInfo [address=" + address + ", datePost=" + date_post
		// + ", description=" + description + ", id=" + id + ", isVAT="
		// + isVAT + ", lat=" + lat + ", lng=" + lng + ", name=" + name
		// + ", origin=" + origin + ", price=" + price + ", product_view="
		// + product_view + ", quantity=" + quantity + ", setAttributes="
		// + attributeSets + ", setCategoryKeys=" + setCategoryKeys
		// + ", setPagesId=" + setPagesId + ", username=" + username
		// + ", warranty=" + warranty + "]";
		return name;
	}

	public static Type getType() {
		return new TypeToken<List<ProductInfo>>() {
		}.getType();
	}

//	public Drawable getRandomThumbImage() {
//		if (setMedias == null || setMedias.isEmpty()) {
//			return Global.application.getResources().getDrawable(
//					R.drawable.product_unknown);
//		} else {
//			Drawable productDrawable = setMedias.get(
//					(int) (Math.random() * setMedias.size())).getDrawable();
//			if (productDrawable == null)
//				return Global.application.getResources().getDrawable(
//						R.drawable.product_unknown);
//			return productDrawable;
//		}
//	}

	public String getRandomThumbImageURL() {
		if (setMedias == null || setMedias.isEmpty())
			return URLConstant.URL_NO_PRODUCT_IMG;
		else {
			return setMedias.get((int) (Math.random() * setMedias.size())).link;
		}
	}

	private static final int MAX_LENGTH_OF_DESCRIPTION = 130;
	public String getShortDescription() {
		if (StringUtils.isEmptyOrNull(description)) {
			return "";
		}
		
		String plainText = description.replaceAll("<.*?>", "").replace("&nbsp;", "");
		if (plainText.length() > MAX_LENGTH_OF_DESCRIPTION) {
			return plainText.substring(0, MAX_LENGTH_OF_DESCRIPTION) + " ...";
		} 
		
		return plainText;
	}
}
