package vnfoss2010.smartshop.serverside.utils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "tam";
		
		String encode = UtilsFunction.encrypt(str);
		System.out.println(encode);
		
		String decode = UtilsFunction.decrypt(encode);
		System.out.println(decode);
	}
}
