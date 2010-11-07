package vnfoss2010.smartshop.webbased.client.utils;

import vnfoss2010.smartshop.webbased.share.WMedia;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;

/**
 * @author VoMinhTam
 */
public class ImageProduct extends Image {
	private IShowSlideShow showSlideShow;
	private WMedia media;

	public ImageProduct(WMedia wm, IShowSlideShow i) {
		super(wm.link);
		this.media = wm;
		this.showSlideShow = i;
		init();
	}

	private void init() {
		setStyleName("img-list-item");
		addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				addStyleName("cursor-hover");
			}
		});

		addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				addStyleName("cursor-default");
			}
		});

		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (showSlideShow != null)
					showSlideShow.showSlideShow();
			}
		});
	}
	
	public WMedia getMedia(){
		return media;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ImageProduct [media=" + media + "]";
	}
}
