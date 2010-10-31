package vnfoss2010.smartshop.webbased.client.utils;

import com.google.gwt.user.client.Window;

/**
 * @author VoMinhTam
 */
public abstract class WebbasedUtils {
	public static int getScreenWidth(){
		return Window.getClientWidth();
	}
	
	public static int getScreenHeight(){
		return Window.getClientHeight();
	}
	
	public static boolean isEmptyOrNull(String txt) {
		return (txt == null || txt.trim().equals("")) ? true : false;
	}
}
