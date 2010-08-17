package vnfoss2010.smartshop.serverside.database;

import java.util.ResourceBundle;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;

public class AttributeServiceImpl {
	private static AttributeServiceImpl instance;
	private ResourceBundle messages;

	public AttributeServiceImpl() {
		messages = ResourceBundle
				.getBundle("vnfoss2010/smartshop/serverside.localization/MessagesBundle");
	}

	public Attribute findAttribute(String attName) {
		Attribute result = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			result = (Attribute) pm.getObjectById(Attribute.class, attName);
		} catch (NucleusObjectNotFoundException e) {
		} catch (JDOObjectNotFoundException e) {
		}
		return result;
	}

	public ServiceResult<Boolean> insertAttribute(Attribute attribute) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (attribute == null) {
//			result.setMessage(messages.getString("cannot_handle_with_null"));
			result.setMessage("ATT null");
		}

		try {
			attribute = pm.makePersistent(attribute);
			if (attribute == null) {
//			result.setMessage(messages.getString("insert_product_fail"));
				result.setMessage("ATT insert fail");
			} else {
				result.setResult(true);
//				result.setMessage(messages
//						.getString("insert_product_successfully"));
				result.setMessage("ATT insert successfully");
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("ATT insert fail");
		}

		return result;
	}

	public static AttributeServiceImpl getInstance() {
		if (instance == null) {
			instance = new AttributeServiceImpl();
		}
		return instance;
	}
}
