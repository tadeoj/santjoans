package santjoans.client.presentation.invalidbrowser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class InvalidBrowserPanel extends Composite {

	private static InvalidBrowserPanelUiBinder uiBinder = GWT
			.create(InvalidBrowserPanelUiBinder.class);

	interface InvalidBrowserPanelUiBinder extends
			UiBinder<Widget, InvalidBrowserPanel> {
	}

	public InvalidBrowserPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
