package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.Date;
import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class FindUserSubcribes extends BaseRestfulService {
	AccountServiceImpl dbAccount = AccountServiceImpl.getInstance();
	UserSubcribeProductImpl dbSub = UserSubcribeProductImpl.instance();

	public FindUserSubcribes(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JsonObject jsonReturn = new JsonObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception ex) {
		}
		String userName = getParameterWithThrow("username", params, json);
		String fromDateStr = getParameter("fromDate", params, json);
		String toDateStr = getParameter("toDate", params, json);

		try {
			if (dbAccount.getUserInfo(userName).isOK() == false) {
				throw new Exception("Khong tim thay user ");
			}
			Date fromDate = null, toDate = null;
			if (fromDateStr != null) {
				fromDate = Global.dfFull.parse(fromDateStr);
			}
			if (toDateStr != null) {
				toDate = Global.dfFull.parse(toDateStr);
			}
			if (fromDate != null && toDate != null && fromDate.after(toDate)) {
				throw new Exception("Dinh dang 2 ngay sai");
			}
			ServiceResult<List<UserSubcribeProduct>> serviceResult = dbSub.findSubcribe(
					userName, fromDate, toDate);
			if (serviceResult.isOK() == false){
				throw new Exception(serviceResult.getMessage());
			}
			jsonReturn.add("subcribes", Global.gsonWithDate.toJsonTree(serviceResult.getResult()));
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", "Tim thanh cong");
		} catch (Exception e) {
			e.printStackTrace();
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", e.getMessage());
		}
		return jsonReturn.toString();
	}

}
