package vnfoss2010.smartshop.serverside.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");

	/**
	 * dd/MM/yyyy HH:mm
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		try {
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
}
