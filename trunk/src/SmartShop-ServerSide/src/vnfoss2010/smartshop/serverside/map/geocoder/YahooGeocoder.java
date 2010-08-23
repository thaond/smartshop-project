package vnfoss2010.smartshop.serverside.map.geocoder;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.net.HttpRequest;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

/**
 * Using Place Finder Service from Yahoo Map, a newer version for Geocoder
 * URL for Geocoder: http://local.yahooapis.com/MapsService/V1/geocode?appid=YD-9G7bey8_JXxQP6rxl.fBFGgCdNjoDMACQA--&street=701+First+Ave&city=Sunnyvale&state=CA
 * URL from Place Finder: http://where.yahooapis.com/geocode?q=1600+Pennsylvania+Avenue,+Washington,+DC&appid=[yourappidhere]
 * They are a litte difference.
 * @author VoMinhTam
 *
 */
public class YahooGeocoder {
	private final static String YAHOOURL = "http://local.yahooapis.com/MapsService/V1/geocode";

	public static List<Placemark> geocode(String address, String country)
			throws UnsupportedEncodingException {
		Point geocoordinates = null;
		String URL = YAHOOURL + "?appid=" + Global.YAHOO_APP_ID + "&location="
				+ URLEncoder.encode(address.replaceAll(" ", "+"), "UTF-8");

		String output = HttpRequest.get(URL).content;
		System.out.println(output);

		List<Placemark> listPlacemarks = new ArrayList<Placemark>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(output));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("Result");

			// iterate the employees
			for (int i = 0; i < nodes.getLength(); i++) {
				Placemark p = new Placemark();
				Element element = (Element) nodes.item(i);

				// .element.getAttribute("Latitude");
				NodeList eleLat = element.getElementsByTagName("Latitude");
				NodeList eleLng = element.getElementsByTagName("Longitude");

				if (eleLat != null && eleLng != null)
					p
							.setPoint(new Point(
									Double
											.parseDouble(getCharacterDataFromElement((Element) eleLat
													.item(0))),
									Double
											.parseDouble(getCharacterDataFromElement((Element) eleLng
													.item(0))), 0D));

				p.setFullAddress(getCharacterDataFromElement((Element) element
						.getElementsByTagName("Address").item(0)));
				p.setCity(getCharacterDataFromElement((Element) element
						.getElementsByTagName("City").item(0)));
				p.setCountryName(getCharacterDataFromElement((Element) element
						.getElementsByTagName("State").item(0)));
				p.setCountryCode(getCharacterDataFromElement((Element) element
						.getElementsByTagName("Country").item(0)));
				p.setZipCode(getCharacterDataFromElement((Element) element
						.getElementsByTagName("Zip").item(0)));

				if (!StringUtils.isEmptyOrNull(country)) {
					if (p.getCountryName().equals(country)) {
						listPlacemarks.add(p);
					}
				} else {
					listPlacemarks.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPlacemarks;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	// private String createLocation(Placemark address) {
	// String s = "";
	//
	// s += address.getStreet() != null ? address.getStreet() + "+" : "";
	// s = address.getCity() != null ? address.getCity() + "+" : "";
	// s += address.getPostalCode() != null ? address.getPostalCode() + "+"
	// : "";
	// s += address.getCountry() != null ? address.getCountry() + "+" : "";
	// if (s.endsWith("+")) {
	// s = s.substring(0, s.length() - 1);
	// }
	// s = s.replace(" ", "+");
	// return s;
	// }

	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.out.println(YahooGeocoder
//				.geocode("268 Ly Thuong Kiet, Quan 10, Ho Chi Minh, VietNam"));
	}

}
