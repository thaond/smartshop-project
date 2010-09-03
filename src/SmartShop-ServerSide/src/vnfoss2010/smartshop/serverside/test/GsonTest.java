package vnfoss2010.smartshop.serverside.test;

import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Product;

import com.google.gson.Gson;

public class GsonTest {
	
	public static void main(String[] args) {
		Product product = new Product("name", 100, true, 0, "0 months", "China", "BT", 10D, 10D, "Mo ta", 0, "tam");
		product.getAttributeSets().add(new Attribute("cat", "name", "value", "tam"));
		System.out.println("Product1: " + product);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(product);
		System.out.println("Before: " + jsonString);
		
		Product product2 = gson.fromJson(jsonString, Product.class);
		System.out.println("Product2: " + product2);
		
	}
	
}

