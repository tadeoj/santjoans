package santjoans.client.presentation.imageviewer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class OneImageViewerWidget extends Composite {

	private static ImageViewerWidgetUiBinder uiBinder = GWT
			.create(ImageViewerWidgetUiBinder.class);

	interface ImageViewerWidgetUiBinder extends
			UiBinder<Widget, OneImageViewerWidget> {
	}

	@UiField
	Image image;
	
	private Image imageParam;

	public OneImageViewerWidget(Image imageParam) {
		this.imageParam = imageParam;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiFactory Image instantiateImage() {
		return imageParam; 
	}


}
