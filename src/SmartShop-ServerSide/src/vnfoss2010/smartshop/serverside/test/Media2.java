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
		for (int i = 0; i < 100; i++) {
			int randNum = (int) (Math.random() * 9 + 1);
			System.out.println(randNum);
		}
	}
}
