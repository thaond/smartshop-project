package vnfoss2010.smartshop.serverside.database;

import javax.jdo.PersistenceManager;

import vnfoss2010.smartshop.serverside.database.entity.Page;

public class PageServiceImpl {
	private static PageServiceImpl instance;

	private PageServiceImpl() {
	}

	public ServiceResult<Boolean> insertPage(Page page) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.makePersistent(page);
			if (page == null) {
				result.setMessage("khong the insert page");
			} else {
				result.setResult(true);
				result.setMessage("insert page thanh cong");
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page");
		}
		return result;
	}

	public static PageServiceImpl instance() {
		if (instance == null) {
			instance = new PageServiceImpl();
		}
		return instance;
	}
}
