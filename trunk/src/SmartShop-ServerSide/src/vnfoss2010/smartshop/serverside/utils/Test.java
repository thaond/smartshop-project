package vnfoss2010.smartshop.serverside.utils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String tmp = "Võ Minh Tâm";
		System.out.println(removeViSign(tmp));
	}
	
	private static String removeViSign(String str){
		String vietSign = "aáàảãạăắằẳẵặâấầẩẫậAÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬeéèẻẽẹêếềểễệEÉÈẺẼẸÊẾỀỂỄỆiíìỉĩịIÍÌỈĨỊoóòỏõọôốồổỗộơớờởỡợOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢuúùủũụưứừửữựUÚÙỦŨỤƯỨỪỬỮỰyýỳỷỹỵYÝỲỶỸỴdđDĐ";
		String vietNoSign = "aaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAeeeeeeeeeeeeEEEEEEEEEEEEiiiiiiIIIIIIooooooooooooooooooOOOOOOOOOOOOOOOOOOuuuuuuuuuuuuUUUUUUUUUUUUyyyyyyYYYYYYddDD";
		
		for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < 148; j++) {
                //str = str.replaceAll(String.valueOf(arrJavaString.charAt(j)), String.valueOf(vietSign.charAt(j)));
                str = str.replaceAll(String.valueOf(vietSign.charAt(j)), String.valueOf(vietNoSign.charAt(j)));

            }
        }
		
		return str;
	}

}
