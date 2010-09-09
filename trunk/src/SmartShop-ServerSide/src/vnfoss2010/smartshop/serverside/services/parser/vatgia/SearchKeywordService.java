package vnfoss2010.smartshop.serverside.services.parser.vatgia;

import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.net.HttpRequest;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class SearchKeywordService extends BaseRestfulService {
	private final static Logger log = Logger
			.getLogger(SearchKeywordService.class.getName());
	private static final String URL_VAT_GIA = "http://m.vatgia.com";
	private static final String URL_VAT_GIA_THONG_SO = "thong_so_ky_thuat";
	private static final String URL_VAT_GIA_SEARCH = "http://m.vatgia.com/quicksearch.php?&";
	private static final String REGEX_NUM_RESULTS = "container_content.+?<b.+?>(.+?)<";
	private static final String REGEX_NUM_PAGE = "Trang cu.+?page=(.+?)\"";
	private static final String REGEX_EACH_RESULT = "pro_picture.+?src=\"(.+?)\".+?src='(.+?)'.+?href=\"(.+?)\">(.+?)<.+?price\">(.+?)<.+?estore\">.+?<b>(.+?)</b>.+?</div>.+?href=\"(.+?)\">(.+?)<";

	public SearchKeywordService(String serviceName) {
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
		try {
			String keyword = getParameterWithThrow("keyword", params, json);
			String pageNum = getParameter("page", params, json);

			String url = URL_VAT_GIA_SEARCH + "keyword=" + keyword
					+ (pageNum == null ? "" : "&page=" + pageNum);
			String pageContent = HttpRequest.get(URLEncoder
					.encode(url, "UTF-8").replaceAll("%2F", "/")
					.replaceAll("%3A", ":").replaceAll("%3F", "?")
					.replaceAll("%26", "&").replaceAll("%3D", "=")).content;
			// String pageContenta = HttpRequest.get(URLEncoder.encode(url,
			// "UTF-8")).content;
			// log.log(Level.SEVERE, pageContenta);
			log.log(Level.SEVERE, url);
			log.log(Level.SEVERE,
					URLEncoder.encode(url, "UTF-8").replaceAll("%2F", "/")
							.replaceAll("%3A", ":").replaceAll("%26", "&"));

			if (url.equals("") == false) {
				Pattern patNumResult = Pattern.compile(REGEX_NUM_RESULTS);
				Pattern patNumPage = Pattern.compile(REGEX_NUM_PAGE);
				Pattern patEachResult = Pattern.compile(REGEX_EACH_RESULT);

				// number of results
				Matcher matcher = patNumResult.matcher(pageContent);
				if (matcher.find()) {
					jsonReturn.addProperty("numOfResults", matcher.group(1));
				}

				// number of pages
				matcher = patNumPage.matcher(pageContent);
				if (matcher.find()) {
					jsonReturn.addProperty("numOfPages", matcher.group(1));
				}

				// result in page
				JsonArray jsonArray = new JsonArray();
				matcher = patEachResult.matcher(pageContent);
				while (matcher.find()) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("imageThumbnail", matcher.group(1));
					jsonObject.addProperty("imageBlankThumbnail",
							matcher.group(2));
					String urlListShop = matcher.group(3);
					int lastIndex = urlListShop.lastIndexOf("/");
					urlListShop = urlListShop.substring(0, lastIndex) + "/"
							+ URL_VAT_GIA_THONG_SO
							+ urlListShop.substring(lastIndex);
					jsonObject.addProperty("urlListShop", URL_VAT_GIA
							+ urlListShop);
					jsonObject.addProperty("productName", matcher.group(4));
					jsonObject.addProperty("priceVND", matcher.group(5));
					jsonObject.addProperty("numOfStore", matcher.group(6));
					jsonObject.addProperty("categoryPageURL", URL_VAT_GIA
							+ matcher.group(7));
					jsonObject.addProperty("categoryName", matcher.group(8));
					jsonArray.add(jsonObject);
				}
				jsonReturn.add("results", jsonArray);
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.addProperty("message", "Parse thành công");
			} else {
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message", "Loi");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", "Exception " + e.getMessage());
		}

		return jsonReturn.toString();
	}
}
