package com.appspot.smartshop.mock;

import java.sql.Date;

import com.appspot.smartshop.dom.ProductInfo;

public class MockProductInfo {
	public static ProductInfo getInstance(){
		ProductInfo productInfo = new ProductInfo();
		productInfo.name = "Thịt chó";
		productInfo.price = 50000;
		productInfo.is_vat = true;
		productInfo.quantity = 2;
		productInfo.date_post = new Date(2010, 12, 12);
		productInfo.warranty = "Việt Nam";
		productInfo.origin = "USA";
		productInfo.address = "268 Lý Thường Kiệt P7 Q10 HCM";
		
		return productInfo;
		
	}

}
