package vnfoss2010.smartshop.serverside.services.parser.vatgia;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.net.HttpRequest;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.Company;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.Group;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.Pair;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.ProductVatGia;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NProductInfoService extends BaseRestfulService {
	private final static Logger log = Logger.getLogger(ProductInfoService.class
			.getName());
	private static final String REGEX_PRODUCT_INFO = "<h1>(.+?)</h1>.+?<img src=\"(.+?)\"";
	private static final String REGEX_SHOP_INFORMATION = "div class=\"pro_estore_name\">(.+?)<.+?pro_estore_price\">(.+?)<div( class=\"pro_price_width\">(.+?)/div)?>(.+?)</div>.+?pro_estore_information\">.+?</b>(.+?)<br />(.+?)</div>(<div>.+?</div>)?";
	private static final String REGEX_PRODUCT_REFERENCE_PRICE = "class=\"pro_v2_price_top\">.+?pro_price\">(.+?)<.+?pro_price\">(.+?)<";
	private static final String REGEX_SUB_SHOP_PRICE = "pro_price\">(.+?)</span(.+?pro_price_usd\">[(](.+?)[)])?";
	private static final String REGEX_SUB_SHOP_VAT_STATUS = "pro_v2_vat\">(.+?)<.+?[(](.+?)[)]";
	private static final String REGEX_SUB_SHOP_STATUS = "/b>(.+?)- <b>.+?> (.+)";
	private static final String REGEX_SUB_SHOP_PHONE = "[0-9]+[-]?[0-9]+";
	private static final String REGEX_PRODUCT_ATT = "(<tr class=\"title\"><td colspan=\"[0-9]\">(.+?)</td></tr><tr>)? ?(<td class=\"name\">(.+?)</td><td class=\"value\">(.+?)</td>)+";

	public NProductInfoService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {

		JsonObject jsonReturn = new JsonObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}

		String url = getParameterWithThrow("url", params, json);

		log.log(Level.SEVERE, url);
		String result = HttpRequest.get(URLEncoder.encode(url, "UTF-8")
				.replaceAll("%2F", "/").replaceAll("%3A", ":")).content;
		try {
			ProductVatGia product = new ProductVatGia();
			if (!StringUtils.isEmptyOrNull(result)) {
				Pattern patProInfo = Pattern.compile(REGEX_PRODUCT_INFO);
				Pattern patProAtt = Pattern.compile(REGEX_PRODUCT_ATT);
				Pattern patProRefPrice = Pattern
						.compile(REGEX_PRODUCT_REFERENCE_PRICE);
				Pattern patShopAtt = Pattern.compile(REGEX_SHOP_INFORMATION);
				Pattern patSubShopPrice = Pattern.compile(REGEX_SUB_SHOP_PRICE);
				Pattern patSubShopVat = Pattern
						.compile(REGEX_SUB_SHOP_VAT_STATUS);
				Pattern patSubShopStatus = Pattern
						.compile(REGEX_SUB_SHOP_STATUS);
				Pattern patSubShopPhone = Pattern.compile(REGEX_SUB_SHOP_PHONE);

				Matcher matcherSub = null;
				Matcher matcher = patProInfo.matcher(result);
				if (matcher.find()) {
					product.name = matcher.group(1);
					product.thumbnail = matcher.group(2);
					// jsonReturn.addProperty("proName", matcher.group(1));
					// jsonReturn.addProperty("proImage", matcher.group(2));
				}
				matcher = patProAtt.matcher(result);
				Group groupProAtt = null;
				while (matcher.find()) {
					String groupName = matcher.group(2);
					if (groupName != null) {
						groupProAtt = new Group(groupName);
						product.listGroup.add(groupProAtt);
					}
					String attName = matcher.group(4);
					String attValue = matcher.group(5);
					attValue = attValue.replaceAll("&bull; ", "")
							.replaceAll("<br>- ", ";").replaceAll("<br>", ";");
					if (groupProAtt == null) {
						groupProAtt = new Group("Thông tin khác");
						product.listGroup.add(groupProAtt);
					}
					groupProAtt.listAtt.add(new Pair(attName, attValue));
				}

				matcher = patProRefPrice.matcher(result);
				if (matcher.find()) {
					jsonReturn.addProperty("proMinPrice", matcher.group(1));
					jsonReturn.addProperty("proRefPrice", matcher.group(2));
				}

				matcher = patShopAtt.matcher(result);
				JsonArray jsonArray = new JsonArray();
				while (matcher.find()) {
					Company company = new Company();
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("name", matcher.group(1));
					matcherSub = patSubShopPrice.matcher(matcher.group(2));
					if (matcherSub.find()) {
						company.vnd = matcherSub.group(1);
						company.usd = matcherSub.group(3);
						// jsonObject.addProperty("vnd", matcherSub.group(1));
						// String priceUSD = matcherSub.group(3);
						// if (priceUSD != null) {
						// jsonObject.addProperty("usd", priceUSD);
						// }
					}

					String vatStr = matcher.group(4);
					if (vatStr != null) {
						matcherSub = patSubShopVat.matcher(vatStr);
						if (matcherSub.find()) {
							company.comment = (matcherSub.group(1) == null ? ""
									: matcherSub.group(1))
									+ "-"
									+ (matcherSub.group(2) == null ? ""
											: matcherSub.group(2));
							// jsonObject.addProperty("vatStatus",
							// matcherSub.group(1));
							// jsonObject.addProperty("comparePrice",
							// matcherSub.group(2));
						}
					}
					matcherSub = patSubShopStatus.matcher(matcher.group(5));
					if (matcherSub.find()) {
						company.listProInfos.add(new Pair("Tình trạng",
								matcherSub.group(1)));
						company.listProInfos.add(new Pair("Bảo hành",
								matcherSub.group(2)));
						// jsonObject.addProperty("status",
						// matcherSub.group(1));
						// jsonObject.addProperty("warranty",
						// matcherSub.group(2));
					}

					company.listCoInfos.add(new Pair("Địa chỉ", matcher
							.group(6)));
					// jsonObject.addProperty("address", matcher.group(6));

					HashSet<String> set = new HashSet<String>();
					matcherSub = patSubShopPhone.matcher(matcher.group(7));
					while (matcherSub.find()) {
						set.add(matcherSub.group(0).trim().replace("-", ""));
					}
					String phoneStr = matcher.group(8);
					if (phoneStr != null) {
						matcherSub = patSubShopPhone.matcher(phoneStr);
						while (matcherSub.find()) {
							set.add(matcherSub.group(0).trim().replace("-", ""));
						}
					}
					String phones = "";
					for (String phone : set) {
						phones += phone + "-";
					}
					if (!phones.equals("")) {
						company.listCoInfos.add(new Pair("Phone", phones
								.substring(0, phones.length() - 1)));
					}
					// jsonObject.add("phones",
					// Global.gsonWithDate.toJsonTree(set));

					product.listCo.add(company);
					// jsonArray.add(jsonObject);
				}
				// jsonReturn.add("shops", jsonArray);
				jsonReturn.add("productvatgia",
						Global.gsonDateWithoutHour.toJsonTree(product));
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.addProperty("message", Global.messages
						.getString("get_info_from_url_successfully"));
			} else {
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message",
						Global.messages.getString("cant_get_from_url"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", "exception " + e.getMessage());
		}
		return jsonReturn.toString();
	}
}
