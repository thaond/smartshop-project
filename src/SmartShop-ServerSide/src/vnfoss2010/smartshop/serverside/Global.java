/**
 * Copyright 2010 BkitMobile
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vnfoss2010.smartshop.serverside;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.authentication.SessionObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public abstract class Global {
	public static String HOST_NAME = "http://localhost:8888";
	public static final String NORMAL_DATE = "dd/MM/yyyy";
	public static final String NORMAL_DATE_WITH_HOUR = "dd/MM/yyyy hh:mm:ss";
	public static DateFormat df = new SimpleDateFormat(NORMAL_DATE);
	public static DateFormat dfFull = new SimpleDateFormat(
			NORMAL_DATE_WITH_HOUR);
	public static ResourceBundle messages = ResourceBundle
			.getBundle("vnfoss2010/smartshop/serverside.localization/MessagesBundle");;
	public static final String YAHOO_APP_ID = "VFv3MmLV34EjL9IdhgJeiiS9024qwIYt6HwMOWwoIG69CZ0kJDyi2lHprgwSoAtnvg--";
	public static final String GOOGLE_APP_ID = "VFv3MmLV34EjL9IdhgJeiiS9024qwIYt6HwMOWwoIG69CZ0kJDyi2lHprgwSoAtnvg--";
	public static final String XTIFY_API_KEY = "e32689d3-597b-43b4-afac-4387af15647e";
	public static final int MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH = 5;
	public static final int MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX = 200;
	public static final long SESSION_EXPRIED = 30*60*1000;//30 mins
	
	public static final Gson gsonDateWithoutHour = new GsonBuilder()
			.setDateFormat(NORMAL_DATE).excludeFieldsWithExcludeAnnotation()
			.create();
	public static final Gson gsonWithDate = new GsonBuilder().setDateFormat(
			NORMAL_DATE_WITH_HOUR).excludeFieldsWithExcludeAnnotation()
			.create();
	public static final JsonParser jsonParser = new JsonParser();

	private static Logger log = Logger.getLogger(Global.class.getName());
	public static Map<String, SessionObject> mapSession = new HashMap<String, SessionObject>();
	public static List<String> listAPIKeys = new ArrayList<String>();
	
	public static long lastScanExpired = 0;

	public static final String[][] LANGUAGE = new String[][] {
			{ "vi", "Ti\u1EBFng Vi\u1EC7t" }, { "en", "English" } };

	public static final LinkedHashMap<String, String> COUNTRIES_NAME;
	static {
		COUNTRIES_NAME = new LinkedHashMap<String, String>();
		COUNTRIES_NAME.put("AF", "Afghanistan");
		COUNTRIES_NAME.put("AR", "Argentina");
		COUNTRIES_NAME.put("BE", "Belgium");
		COUNTRIES_NAME.put("CA", "Canada");
		COUNTRIES_NAME.put("TD", "Chad");
		COUNTRIES_NAME.put("CL", "Chile");
		COUNTRIES_NAME.put("CN", "China");
		COUNTRIES_NAME.put("CO", "Colombia");
		COUNTRIES_NAME.put("CD", "Congo, The Democratic Republic Of The");
		COUNTRIES_NAME.put("CR", "Costa Rica");
		COUNTRIES_NAME.put("DK", "Denmark");
		COUNTRIES_NAME.put("EE", "Estonia");
		COUNTRIES_NAME.put("FI", "Finland");
		COUNTRIES_NAME.put("FR", "France");
		COUNTRIES_NAME.put("DE", "Germany");
		COUNTRIES_NAME.put("GR", "Greece");
		COUNTRIES_NAME.put("HU", "Hungary");
		COUNTRIES_NAME.put("IS", "Iceland");
		COUNTRIES_NAME.put("IN", "India");
		COUNTRIES_NAME.put("ID", "Indonesia");
		COUNTRIES_NAME.put("IQ", "Iraq");
		COUNTRIES_NAME.put("IT", "Italy");
		COUNTRIES_NAME.put("JP", "Japan");
		COUNTRIES_NAME.put("KR", "Korea, Republic Of");
		COUNTRIES_NAME.put("LA", "Lao People's Democratic Republic");
		COUNTRIES_NAME.put("MG", "Madagascar");
		COUNTRIES_NAME.put("MY", "Malaysia");
		COUNTRIES_NAME.put("MX", "Mexico");
		COUNTRIES_NAME.put("NO", "Norway");
		COUNTRIES_NAME.put("PK", "Pakistan");
		COUNTRIES_NAME.put("PE", "Peru");
		COUNTRIES_NAME.put("PH", "Philippines");
		COUNTRIES_NAME.put("PL", "Poland");
		COUNTRIES_NAME.put("PR", "Puerto Rico");
		COUNTRIES_NAME.put("RO", "Romania");
		COUNTRIES_NAME.put("RU", "Russian Federation");
		COUNTRIES_NAME.put("SG", "Singapore");
		COUNTRIES_NAME.put("ES", "Spain");
		COUNTRIES_NAME.put("SE", "Sweden");
		COUNTRIES_NAME.put("CH", "Switzerland");
		COUNTRIES_NAME.put("TW", "Taiwan, Province Of China");
		COUNTRIES_NAME.put("TH", "Thailand");
		COUNTRIES_NAME.put("TR", "Turkey");
		COUNTRIES_NAME.put("UA", "Ukraine");
		COUNTRIES_NAME.put("GB", "United Kingdom");
		COUNTRIES_NAME.put("US", "United States");
		COUNTRIES_NAME.put("UY", "Uruguay");
		COUNTRIES_NAME.put("VE", "Venezuela");
		COUNTRIES_NAME.put("VN", "Vi\u1EC7t Nam");
		COUNTRIES_NAME.put("YE", "Yemen");
	}

	public static final LinkedHashMap<String, String> TIME_ZONES = new LinkedHashMap<String, String>();
	static {
		TIME_ZONES.put("-11", "(GMT-11 01:00)Apia, Pago Pago");
		TIME_ZONES.put("-10", "(GMT-10 02:00)Honolulu, Papeete");
		TIME_ZONES.put("-9", "(GMT-9 03:00)Anchorage, Juneau");
		TIME_ZONES
				.put("-8",
						"(GMT-8 04:00)Los Angeles, San Francisco, Las Vegas, Vancouver");
		TIME_ZONES.put("-7", "(GMT-7 05:00)Calgary, Denver");
		TIME_ZONES.put("-6", "(GMT-6 06:00)Chicago, Mexico City");
		TIME_ZONES
				.put(
						"-5",
						"(GMT-5 07:00)Toronto, New York City, Havana, Bogot\u0102\u0192\u00C2\u00A1, Lima");
		TIME_ZONES
				.put("-4",
						"(GMT-4 08:00)Asunci\u0102\u0192\u00C2\u00B3n, Halifax, Santiago");
		TIME_ZONES
				.put(
						"-3",
						"(GMT-3 09:00)Buenos Aires, Montevideo, Rio de Janeiro, S\u0102\u0192\u00C2\u00A3o Paulo");
		TIME_ZONES
				.put(
						"-2",
						"(GMT-2 10:00)Fernando de Noronha, South Georgia and the South Sandwich Islands");
		TIME_ZONES.put("-1", "(GMT-1 11:00)Azores, Cape Verde");
		TIME_ZONES
				.put(
						"0",
						"(GMT+0 12:00)Dakar, Casablanca, London, Lisbon, Reykjav\u0102\u0192\u00C2\u00ADk, Tenerife");
		TIME_ZONES
				.put("1",
						"(GMT+1 13:00)Algiers, Berlin, Kinshasa, Lagos, Paris, Rome, Tunis");
		TIME_ZONES
				.put("2",
						"(GMT+2 14:00)Istanbul,Athens ,Cairo, Cape Town, Helsinki, Jerusalem");
		TIME_ZONES
				.put("3",
						"(GMT+3 15:00)Addis Ababa, Baghdad, Moscow, Nairobi, Saint Petersburg");
		TIME_ZONES.put("4",
				"(GMT+4 16:00)Baku, Dubai, Mauritius, Samara, Tbilisi ");
		TIME_ZONES.put("5",
				"(GMT+5 17:00)Karachi, Maldives, Tashkent, Yekaterinburg");
		TIME_ZONES.put("6", "(GMT+6 18:00)Almaty, Dhaka, Omsk 	");
		TIME_ZONES
				.put("7", "(GMT+7 19:00)Bangkok, Jakarta, Hanoi, Krasnoyarsk");
		TIME_ZONES
				.put("8",
						"(GMT+8 20:00)Beijing, Hong Kong, Irkutsk, Kuala Lumpur, Manila, Perth");
		TIME_ZONES.put("9", "(GMT+9 21:00)Pyongyang, Seoul, Tokyo, Yakutsk");
		TIME_ZONES.put("10", "(GMT+10 22:00)Melbourne, Sydney, Vladivostok");
		TIME_ZONES.put("11",
				"(GMT+11 23:00)Magadan, Noum\u0102\u0192\u00C2\u00A9a");
		TIME_ZONES
				.put("12",
						"(GMT+12 00:00 (the following day))Auckland, Petropavlovsk-Kamchatsky, Suva");
		TIME_ZONES
				.put("13",
						"(GMT+13 01:00 (the following day))Nuku\u0102\u00C2\u00BBalofa");
	}

	private static final String UPPER_ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";

	public static String getUpperAlphaNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * UPPER_ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}

	public static String getAlphaNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}

	public static void log(Logger log, Object message) {
		if (log == null)
			log = Global.log;
		log.log(Level.SEVERE, message.toString());
	}

}
