package vnfoss2010.smartshop.serverside;

import java.util.LinkedHashMap;

public abstract class Global {
	public static String HOST_NAME = "http://localhost:8888"; 
	
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
}