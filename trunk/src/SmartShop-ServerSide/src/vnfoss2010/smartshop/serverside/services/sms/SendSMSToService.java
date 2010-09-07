package vnfoss2010.smartshop.serverside.services.sms;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class SendSMSToService extends BaseRestfulService {
	
	public SendSMSToService(String serviceName) {
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
		
		SendSMSService sendSMSService = new SendSMSService("");
		
		String userName = "0938365140";
		String password = "reborn";
		String toPhone = getParameterWithThrow("toPhone", params, json);
		String message = getParameterWithThrow("message", params, json);

		if(userName!=null && password!=null && toPhone!=null && message!=null)
		{
			if (sendSMSService.sendSms(userName, password, toPhone, message))
			{
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.addProperty("message", Global.messages.getString("lack_para"));
			}
			else
			{
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message", Global.messages.getString("send_sms_fail"));
			}
		}
		else
		{
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", Global.messages.getString("lack_para"));
		}
		
		return jsonReturn.toString();
	}
	
}
