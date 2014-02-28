package santjoans.client.piezes.navigator.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NavigationHelp extends Composite {

	private static InvalidBrowserPanelUiBinder uiBinder = GWT
			.create(InvalidBrowserPanelUiBinder.class);

	interface InvalidBrowserPanelUiBinder extends
			UiBinder<Widget, NavigationHelp> {
	}

	public NavigationHelp() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
