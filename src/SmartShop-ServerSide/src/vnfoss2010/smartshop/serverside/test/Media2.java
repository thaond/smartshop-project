package vnfoss2010.smartshop.serverside.test;

/**
 * @author VoMinhTam
 */
public class Media2 {
	public String name;
	public String link;

	public Media2(String name, String link) {
		this.name = name;
		this.link = link;
	}

	public static void main(String[] args) {
		String t = "http://127.0.0.1:8888/image_host/product/img1.jpg";
		
		System.out.println(t);
	}
}
