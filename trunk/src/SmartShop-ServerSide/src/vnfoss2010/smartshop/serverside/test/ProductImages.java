package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author VoMinhTam
 */
public class ProductImages {
	static List<Media2> images = new ArrayList<Media2>();
	
	public static void main(String[] args) {
		images.add(new Media2("Iphone1", "http://localhost/testupload/images/img1.jpg"));
		images.add(new Media2("Iphone2", "http://localhost/testupload/images/img2.jpg"));
		images.add(new Media2("Iphone3", "http://localhost/testupload/images/img3.jpg"));
		images.add(new Media2("Iphone4", "http://localhost/testupload/images/img4.jpg"));
		
		JsonObject json = new JsonObject();
		json.addProperty("errCode", 0);
		Gson gson = new Gson(); 
		json.add("message", gson.toJsonTree(images));
		
		System.out.println(json.toString());
	}
}
