package vnfoss2010.smartshop.serverside.database;

import java.util.logging.Level;

import javax.jdo.PersistenceManager;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;

public class PageServiceImpl {
	private static PageServiceImpl instance;

	private PageServiceImpl() {
	}

	public ServiceResult<Page> findPage(Long id) {
		ServiceResult<Page> result = new ServiceResult<Page>();
		Page page = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, id);
			if (page == null) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("khong tim thay page"));
			} else {
				result.setMessage("Tim thay page");
				result.setOK(true);
				result.setResult(page);
			}
		} catch (Exception e) {
			result.setMessage("khong tim thay page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Long> insertPage(Page page) {
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.makePersistent(page);
			if (page == null) {
				result.setMessage("khong the insert page");
			} else {
				result.setResult(page.getId());
				result.setMessage("insert page thanh cong");
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> tagProductToPage(Long pageID, Long productID) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Page page = null;
		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.getObjectById(Page.class, pageID);
			product = pm.getObjectById(Product.class, productID);
			if (page == null || product == null) {
				result.setMessage("Khong the tim thay page hay product");
			} else {
				page.getSetProduct().add(productID);
				product.getSetPagesID().add(pageID);
				result.setOK(true);
				result.setMessage("Tag product to page thanh cong");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> untagProductToPage(Long pageID, Long productID) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Page page = null;
		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.getObjectById(Page.class, pageID);
			product = pm.getObjectById(Product.class, productID);
			if (page == null || product == null) {
				result.setMessage("Khong the tim thay page hay product");
			} else {
				page.getSetProduct().remove(productID);
				product.getSetPagesID().remove(pageID);
				result.setOK(true);
				result.setMessage("Tag product to page thanh cong");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> updatePage(Page editPage) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Page page = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, editPage.getId());
			if (page == null) {
				result.setOK(false);
				result.setMessage("Khong tim thay page");
			} else {
				page.setContent(editPage.getContent());
				page.setDate_post(editPage.getDate_post());
				page.setLast_modified(editPage.getLast_modified());
				page.setLink_thumbnail(editPage.getLink_thumbnail());
				page.setName(editPage.getName());
				page.setPage_view(editPage.getPage_view());
				page.setSetCategoryKeys(editPage.getSetCategoryKeys());
				page.setSetProduct(editPage.getSetProduct());
				result.setOK(true);
				result.setMessage("Update thanh cong");
			}
		} catch (Exception e) {
			result.setMessage("exception " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
