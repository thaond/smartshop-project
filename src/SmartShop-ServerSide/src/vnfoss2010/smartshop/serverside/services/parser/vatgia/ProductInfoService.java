package vnfoss2010.smartshop.serverside.services.parser.vatgia;

import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.net.HttpRequest;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.AttributeVatGia;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.Company;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.Group;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.Pair;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.dom.ProductVatGia;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class ProductInfoService extends BaseRestfulService{
	private final static Logger log = Logger.getLogger(ProductInfoService.class
			.getName());
	
	//Sammple URL: http://m.vatgia.com/6087/541022/cảm-ứng-o2-xda-diamond-pro.html
	private static final String REGEX_NAME_PRODUCT = "<h1>(.+?)</h1>.+?<img src=\"(.+?)\"";
	private static final String REGEX_NAME_INFO_CO = "<div class=\"pro_estore_name\">(.+?)</div><div class=\"pro_estore_price\"><span class=\"pro_price\">(.+?)</span>&nbsp;<span class=\"pro_price_usd\">(.+?)</span><div class=\"pro_price_width\">(.+?)</div></div><div><(.+?)<div class=\"pro_estore_information\">(.+?)</div>";
	private static final String REGEX_EACH_PRO_INFO = "b>(.+?)</b>(.+?)<";
	private static final String REGEX_EACH_CO_INFO = "<b>(.+?)</b>(.+?)<br />";
	private static final String REGEX_ATT_PRODUCT = "(<tr class=\"title\"><td colspan=\"[0-9]\">(.+?)</td></tr><tr>)? ?(<td class=\"name\">(.+?)</td><td class=\"value\">(.+?)</td>)+";

	public ProductInfoService(String serviceName) {
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
		String result = HttpRequest.get(URLEncoder.encode(url,"UTF-8").replaceAll("%2F", "/").replaceAll("%3A", ":")).content;

		if (!StringUtils.isEmptyOrNull(result)){
			// Init pattern
			Pattern patAttProduct = Pattern.compile(REGEX_ATT_PRODUCT);
			Pattern patNameThumbPro = Pattern.compile(REGEX_NAME_PRODUCT);
			Pattern patCo = Pattern.compile(REGEX_NAME_INFO_CO);
			Pattern patEachCoInfo = Pattern.compile(REGEX_EACH_CO_INFO);
			Pattern patEachProInfo = Pattern.compile(REGEX_EACH_PRO_INFO);
			
			ProductVatGia product = new ProductVatGia(); 
			// Get name and thumnail of product
			Matcher matcher = patNameThumbPro.matcher(result);
			if (matcher.find()) {
				product.name = matcher.group(1);
				product.thumbnail = matcher.group(2);
			}

			// Get list of Company
			matcher = patCo.matcher(result);
			while (matcher.find()) {
				Company company = new Company(matcher.group(1), matcher.group(2),
						matcher.group(3), matcher.group(4));
				product.listCo.add(company);
				String proInfos = matcher.group(5);
				String coInfos = matcher.group(6);

				Matcher matcher2 = patEachProInfo.matcher(proInfos);
				while (matcher2.find()) {
					company.listProInfos.add(new Pair(matcher2.group(1), matcher2.group(2)));
				}
				
				matcher2 = patEachCoInfo.matcher(coInfos);
				while (matcher2.find()) {
					company.listCoInfos.add(new Pair(matcher2.group(1), matcher2.group(2)));
				}
			}
			
			//Get product specification
			url = URLEncoder.encode(url.replaceAll("([0-9]+/[0-9]+/)", "$1thong_so_ky_thuat/"),"UTF-8").replaceAll("%2F", "/").replaceAll("%3A", ":");
			result = HttpRequest.get(url).content;
			// Get list group and attribute
			matcher = patAttProduct.matcher(result);
			while (matcher.find()) {
				String groupName = matcher.group(2);
				String name = matcher.group(4);
				String value = matcher.group(5);
				if (StringUtils.isEmptyOrNull(groupName)) {
					Group group;
					if (product.listGroup.isEmpty()) {
						group = new Group();
						product.listGroup.add(group);
					} else {
						group = product.listGroup.get(product.listGroup.size() - 1);
						if (StringUtils.isEmptyOrNull(group.name)) {
							group = new Group();
							product.listGroup.add(group);
						}
					}
					group.list.add(new AttributeVatGia(name, value));
				} else {
					Group group = new Group(groupName);
					group.list.add(new AttributeVatGia(name, value));
					product.listGroup.add(group);
				}
			}
			jsonReturn.add("productvatgia", Global.gsonDateWithoutHour.toJsonTree(product));
			
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", Global.messages.getString("get_info_from_url_successfully"));
		}else{
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", Global.messages.getString("cant_get_from_url"));
		}
		return jsonReturn.toString();
	}
	
	public static void main(String[] args) throws RestfulException, Exception {
		ProductInfoService p = new ProductInfoService("asd");
		
		System.out.println(p.process(null, null));
	}

}
