package vnfoss2010.smartshop.webbased.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vnfoss2010.smartshop.webbased.client.utils.IShowSlideShow;
import vnfoss2010.smartshop.webbased.client.utils.ImageProduct;
import vnfoss2010.smartshop.webbased.client.utils.WebbasedUtils;
import vnfoss2010.smartshop.webbased.share.WComment;
import vnfoss2010.smartshop.webbased.share.WProduct;

import com.codelathe.gwt.client.SlideShow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author VoMinhTam
 */
public class ViewProductPanel extends VerticalPanel implements IShowSlideShow {
	private static ViewProductPanel instance = null;
	private Label lblTitle;
	private Label lblDate;
	private HTML htmlContent;
	private Label lblAddress;
	private Image imgMap;

	private List<ImageProduct> listImages = new ArrayList<ImageProduct>();
	private SlideShow slideShow = new SlideShow();

	private WProduct product;
	private HTML htmlComment;
	private HorizontalPanel pnlImageList;

	private static final String GOOGLE_MAPS_Q = "http://maps.google.com/?q=_1+_2";

	public static ViewProductPanel getInstance() {
		if (instance == null)
			instance = new ViewProductPanel();

		return instance;
	}

	private ViewProductPanel() {
		initUI();
	}

	private void initUI() {
		lblTitle = new Label("Title");
		lblTitle.setStyleName("content-title");

		lblDate = new Label(new Date().toString());
		lblDate.setStyleName("content-date");

		lblAddress = new Label("address");
		imgMap = new Image("./webbased/map_icon.png");
		imgMap.setStyleName("img-map");
		imgMap.setTitle("Hiển thị trên Google Maps");
		imgMap.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showAddress();
			}
		});

		pnlImageList = new HorizontalPanel();

		HorizontalPanel pnlAddress = new HorizontalPanel();
		pnlAddress.add(lblAddress);
		pnlAddress.add(imgMap);

		htmlContent = new HTML();
		htmlContent.setStyleName("content");
		htmlComment = new HTML();

		this.add(lblTitle);
		this.add(lblDate);
		this.add(pnlImageList);
		this.add(htmlContent);
		this.add(new HTML("<b>Address:</b> "));
		this.add(pnlAddress);
		this.add(htmlComment);

	}

	public void showData(WProduct product) {
		if (product == null) {
			lblTitle.setVisible(false);
			lblDate.setVisible(false);
			pnlImageList.setVisible(false);
			htmlContent.setHTML("Không có sản phẩm tương ứng");
			htmlComment.setVisible(false);
		} else {
			this.product = product;
			lblTitle.setVisible(true);
			lblDate.setVisible(true);
			System.out.println("Media:  " + product.setMedias);
			if (product.setMedias.isEmpty()){
				pnlImageList.setVisible(false);
			}else {
				showImageList();
				pnlImageList.setVisible(true);
			}

			if (product.lat == 0 || product.lng == 0)
				imgMap.setVisible(false);
			else
				imgMap.setVisible(true);

			htmlComment.setVisible(true);
			lblTitle.setText(product.name);
			if (product.date_post == null)

				lblDate.setText(product.date_post.toString());
			htmlContent.setText(product.description);
			lblAddress.setText(product.address);
			showComments();
		}
	}

	private void showAddress() {
		if (product.lat != 0 && product.lng != 0) {
			Window.open(GOOGLE_MAPS_Q.replaceAll("_1", product.lat + "")
					.replaceAll("_2", product.lng + ""), "_blank", "");
		}
	}

	@Override
	public void showSlideShow() {
		String[] arrStringURLs = new String[listImages.size()];
		String[] arrTitles = new String[listImages.size()];
		for (int i = 0; i < listImages.size(); i++) {
			arrStringURLs[i] = listImages.get(i).getUrl();
			if (WebbasedUtils.isEmptyOrNull(listImages.get(i).getMedia().name))
				arrTitles[i] = "No title";
			else
				arrTitles[i] = listImages.get(i).getMedia().name;
		}

		slideShow.createNewSlideShow(0, "Group", arrStringURLs, arrTitles);
	}

	private void showComments() {
		int numOfComment;
		if (this.product.listComments == null
				|| this.product.listComments.isEmpty())
			numOfComment = 0;
		else
			numOfComment = this.product.listComments.size();

		String comment = "<b>Comments (" + numOfComment + ")</b><br><br>";
		for (int i = 0; i < numOfComment; i++) {
			WComment ccomment = this.product.listComments.get(i);
			comment += "<br><b>" + ccomment.username + "</b> - "
					+ ccomment.date_post + "<br>" + "<p>" + ccomment.content
					+ "</p>";
		}

		htmlComment.setHTML(comment);
		// htmlComment.setOverflow(Overflow.VISIBLE);
	}

	private void showImageList() {
		pnlImageList.clear();
		if (product == null)
			return;
		for (int i = 0; i < product.setMedias.size(); i++) {
			ImageProduct imgProduct = new ImageProduct(
					product.setMedias.get(i), this);
			listImages.add(imgProduct);
			System.out.println("add: " + imgProduct);
			pnlImageList.add(imgProduct);
		}
	}

}
