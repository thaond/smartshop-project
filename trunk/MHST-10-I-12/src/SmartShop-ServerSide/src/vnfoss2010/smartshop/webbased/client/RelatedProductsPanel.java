package vnfoss2010.smartshop.webbased.client;

import java.util.List;

import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author VoMinhTam
 */
public class RelatedProductsPanel extends VerticalPanel {
	private static RelatedProductsPanel instance = null;
	// private Grid grid;
	private VerticalPanel pnl;
	private Image imgThumb;

	public static RelatedProductsPanel getInstance() {
		if (instance == null)
			instance = new RelatedProductsPanel();

		return instance;
	}

	private RelatedProductsPanel() {
		initUI();
		setStyleName("pnl-related-product");

		//initMockTest();
	}

	private void initUI() {
		pnl = new VerticalPanel();
		// grid = new Grid(5, 1);
		add(pnl);
	}

	public void showData(List<WProduct> listProducts) {
		pnl.clear();
		pnl.add(new HTML("<b>Các sản phẩm liên quan:</b> "));
		for (WProduct product : listProducts) {
			pnl.add(eachProduct(product));
		}
	}
	
	public void clearData(){
		pnl.clear();
	}

	public Panel eachProduct(final WProduct product) {
		HorizontalPanel pnlProduct = new HorizontalPanel();

		imgThumb = new Image(product.getRandomThumbImage());
		imgThumb.addStyleName("img-product");
		imgThumb.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem("product;" + product.id);
			}
		});
		imgThumb.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				imgThumb.addStyleName("cursor-pointer");
			}
		});
		imgThumb.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				imgThumb.addStyleName("cursor-default");
			}
		});

		HTML htmlTitle = new HTML("<b>" + product.name + "</b>");
		HTML htmlDes = new HTML(product.getShortDescription());

		VerticalPanel pnlNameDes = new VerticalPanel();
		pnlNameDes.add(htmlTitle);
		pnlNameDes.add(htmlDes);

		pnlProduct.add(imgThumb);
		pnlProduct.add(pnlNameDes);

		return pnlProduct;
	}

	private void initMockTest() {
		WebbasedServiceAsync serviceAsync = WebbasedService.Util.getInstance();
		serviceAsync.getProduct(58L, new AsyncCallback<WProduct>() {

			@Override
			public void onSuccess(WProduct result) {
				showData(result.listRelatedProduct);
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

}
