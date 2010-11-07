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

public class YahooPlaceFinder {
	private final static String YAHOOURL = "http://where.yahooapis.com/geocode?appid=%s&gflags=R&q=%s";

	public static List<Placemark> geocode(String address, String country)
			throws UnsupportedEncodingException {
		String URL = String.format(YAHOOURL, Global.YAHOO_APP_ID, URLEncoder
				.encode(address.replaceAll(" ", "+"), "UTF-8"));

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
				NodeList eleLat = element.getElementsByTagName("latitude");
				NodeList eleLng = element.getElementsByTagName("longitude");

				if (eleLat != null && eleLng != null)
					p
							.setPoint(new Point(
									Double
											.parseDouble(getCharacterDataFromElement((Element) eleLat
													.item(0))),
									Double
											.parseDouble(getCharacterDataFromElement((Element) eleLng
													.item(0))), 0D));

				String line1 = getCharacterDataFromElement((Element) element
						.getElementsByTagName("line1").item(0));
				String line2 = getCharacterDataFromElement((Element) element
						.getElementsByTagName("line1").item(0));
				String line3 = getCharacterDataFromElement((Element) element
						.getElementsByTagName("line1").item(0));
				String line4 = getCharacterDataFromElement((Element) element
						.getElementsByTagName("line1").item(0));

				String fullAdr = (StringUtils.isEmptyOrNull(line1)) ? ""
						: line1 + " ";
				fullAdr += (StringUtils.isEmptyOrNull(line2)) ? "" : line2
						+ " ";
				fullAdr += (StringUtils.isEmptyOrNull(line3)) ? "" : line3
						+ " ";
				fullAdr += (StringUtils.isEmptyOrNull(line4)) ? "" : line4
						+ " ";

				p.setFullAddress(fullAdr);
				p.setCity(getCharacterDataFromElement((Element) element
						.getElementsByTagName("city").item(0)));
				p.setCountryName(getCharacterDataFromElement((Element) element
						.getElementsByTagName("country").item(0)));
				p.setCountryCode(getCharacterDataFromElement((Element) element
						.getElementsByTagName("countrycode").item(0)));
				p.setZipCode(getCharacterDataFromElement((Element) element
						.getElementsByTagName("postal").item(0)));
				p.setHouse(getCharacterDataFromElement((Element) element
						.getElementsByTagName("house").item(0)));
				p.setStreet(getCharacterDataFromElement((Element) element
						.getElementsByTagName("street").item(0)));

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

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out
				.println(YahooPlaceFinder.geocode(
						"268 Ly Thuong Kiet, Quan 10, Ho Chi Minh, Vietnam",
						"Vietnam"));
		// System.out.println(YahooGeocoder
		// .geocode("268 Ly Thuong Kiet, Quan 10, Ho Chi Minh, VietNam"));
	}

}