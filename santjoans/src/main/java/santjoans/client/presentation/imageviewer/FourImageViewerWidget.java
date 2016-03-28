package santjoans.client.presentation.imageviewer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.reveregroup.gwt.imagepreloader.FitImage;

public class FourImageViewerWidget extends Composite {

	private static ThreeImageViewerUiBinder uiBinder = GWT
			.create(ThreeImageViewerUiBinder.class);

	interface ThreeImageViewerUiBinder extends
			UiBinder<Widget, FourImageViewerWidget> {
	}

	@UiField(provided = true)
	Image image1;
	
	@UiField(provided = true)
	Image image2;
	
	@UiField(provided = true)
	Image image3;
	
	@UiField(provided = true)
	Image image4;


	public FourImageViewerWidget(FitImage[] fitImages) {
		image1 = fitImages[0];
		image2 = fitImages[1];
		image3 = fitImages[2];
		image4 = fitImages[3];
		initWidget(uiBinder.createAndBindUi(this));
	}

}
