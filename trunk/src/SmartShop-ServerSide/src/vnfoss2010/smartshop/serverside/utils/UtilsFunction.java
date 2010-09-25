package vnfoss2010.smartshop.serverside.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import vnfoss2010.smartshop.serverside.map.direction.GeoPoint;

/**
 * 
 * @author VoMinhTam
 */
public class UtilsFunction {
	public static final int R = 6371; // km

	private static double deg2rad(double deg) {
		return (Math.PI / 180) * deg;
	}

	private static double rad2deg(double radians) {
		return (180 / Math.PI) * radians;
	}

	/**
	 * Refer <a href="http://www.freevbcode.com/ShowCode.asp?ID=5532">Calculate
	 * Distance Between 2 Points Given Longitude/Latitude (ASP)</a>
	 * 
	 * @return kilometer
	 */
	public static double distance(double lat1, double lng1, double lat2,
			double lng2) {
		double theta = lng1 - lng2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		double result = dist * 60 * 1.1515;
		return result * 1.609344;
	}

	/**
	 * Calculate the initial bearing (angle between 2 points with equator). This
	 * formula is for the initial bearing (sometimes referred to as forward
	 * azimuth) which if followed in a straight line along a great-circle arc
	 * will take you from the start point to the end point.<br >
	 * Refer: <a
	 * href="http://www.movable-type.co.uk/scripts/latlong.html#bearing"
	 * >Calculate bearing</a>
	 * 
	 * @return
	 */
	public static double bearing(double lat1, double lon1, double lat2,
			double lon2) {
		// double dLat = deg2rad(lat2-lat1);
		double dLon = deg2rad(lon2 - lon1);
		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);
		return rad2deg(Math.atan2(y, x));
	}

	/**
	 * Calculate the midpoint between 2 points<br>
	 * Refer <a
	 * href="http://www.movable-type.co.uk/scripts/latlong.html#midpoint"
	 * >Calculate Midpoint</a>
	 * 
	 * @return
	 */
	public GeoPoint midpoint(double lat1, double lon1, double lat2, double lon2) {
		double dLon = deg2rad(lon2 - lon1);
		double Bx = Math.cos(lat2) * Math.cos(dLon);
		double By = Math.cos(lat2) * Math.sin(dLon);
		double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math
				.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
		double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

		return new GeoPoint(lat3, lon3);
	}

	public GeoPoint destFromDistanceBearingStartPoint(double lat1, double lon1,
			double bearing, double d) {
		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R)
				+ Math.cos(lat1) * Math.sin(d / R) * Math.cos(bearing));
		double lon2 = lon1
				+ Math.atan2(Math.sin(bearing) * Math.sin(d / R)
						* Math.cos(lat1), Math.cos(d / R) - Math.sin(lat1)
						* Math.sin(lat2));
		return new GeoPoint(lat2, lon2);
	}

	public static boolean isInsideCircle(double latCenter1, double lonCenter1,
			double r, double lat2, double lon2) {
		double d = distance(latCenter1, lonCenter1, lat2, lon2);

		return (d > r) ? false : true;
	}

	/**
	 * Overcome the problem of Set's serilization of datastore GAE
	 * 
	 * @param originSet
	 *            Set instance which is obtained from datastore
	 * @return a copy of <code>orginSet</code>, but can be serizabled
	 * @author tamvo
	 */
	public static <T> Set<T> cloneSet(Set<T> originSet) {
		Set<T> result = null;
		if (originSet != null) {
			result = new HashSet<T>();
			for (T t : originSet) {
				result.add(t);
			}
		}
		return result;
	}

	public static <T> List<T> cloneList(List<T> originList) {
		List<T> result = null;
		if (originList != null) {
			result = new ArrayList<T>();
			for (T t : originList) {
				result.add(t);
			}
		}
		return result;
	}

	public static String removeViSign(String str) {
		String vietSign = "aáàảãạăắằẳẵặâấầẩẫậAÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬeéèẻẽẹêếềểễệEÉÈẺẼẸÊẾỀỂỄỆiíìỉĩịIÍÌỈĨỊoóòỏõọôốồổỗộơớờởỡợOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢuúùủũụưứừửữựUÚÙỦŨỤƯỨỪỬỮỰyýỳỷỹỵYÝỲỶỸỴdđDĐ";
		String vietNoSign = "aaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAeeeeeeeeeeeeEEEEEEEEEEEEiiiiiiIIIIIIooooooooooooooooooOOOOOOOOOOOOOOOOOOuuuuuuuuuuuuUUUUUUUUUUUUyyyyyyYYYYYYddDD";

		for (int i = 0; i < str.length(); i++) {
			for (int j = 0; j < 148; j++) {
				// str = str.replaceAll(String.valueOf(arrJavaString.charAt(j)),
				// String.valueOf(vietSign.charAt(j)));
				str = str.replaceAll(String.valueOf(vietSign.charAt(j)), String
						.valueOf(vietNoSign.charAt(j)));

			}
		}

		return str;
	}

	/**
	 * Returns a new list containing all elements that are contained in both
	 * given lists.
	 * 
	 * @param list1
	 *            the first list
	 * @param list2
	 *            the second list
	 * @return the intersection of those two lists
	 * @throws NullPointerException
	 *             if either list is null
	 */
	public static List intersection(final Set list1, final List list2) {
		final ArrayList result = new ArrayList();
		final Iterator iterator = list2.iterator();

		while (iterator.hasNext()) {
			final Object o = iterator.next();

			if (list1.contains(o)) {
				result.add(o);
			}
		}

		return result;
	}

	/**
	 * Filter collection <br>
	 * Reference: <a href="http://stackoverflow.com/questions/122105/java-what-is-the-best-way-to-filter-a-collection"
	 * >Stack Overflow</a>
	 */
	public static <T> List<T> filter(Collection<T> target,
			Predicate<T> predicate) {
		List<T> result = new ArrayList<T>();
		for (T element : target) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(distance(50, 50, 51, 50));
	}
}
