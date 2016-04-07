package santjoans.client;

import santjoans.client.piezes.navigator.NavigatorWidget;
import santjoans.client.presentation.PresentationWidget;
import santjoans.client.presentation.invalidbrowser.InvalidBrowserPanel;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Santjoans implements EntryPoint, IConfiguration {
	
	private PresentationWidget presentation;
	private NavigatorWidget navigator;
	private RootLayoutPanel root;
	
	private InvalidBrowserPanel invalidBrowserPanel = new InvalidBrowserPanel();
	
	public void onModuleLoad() {
		
		GWT.log("Se entra en el entry point", null);
		
		// Se obtiene el root panel
		root = RootLayoutPanel.get();
		
		// Se configura el root panel aplicandole un estilo
		root.getElement().setClassName("santjoansrootpanel");
		
		// Se crea el widget que corresponde a la presentacion.
		presentation = new PresentationWidget(new SwitchToViewer());
		presentation.install();
		History.newItem("presentation");
		
		GWT.log("Se ha pasado el primer breakPoint");
		
		// Se crea el widget que corresponde a la navegacion
		navigator = new NavigatorWidget(presentation);

		// Se prepara el gestor del History
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				if (historyToken.equals("presentation")) {
					root.clear();
					root.add(presentation);
				} else if (historyToken.equals("navigation")) {
					root.clear();
					root.add(navigator);
				}
			}
		});
		
		// Se asigna el root panel el widget de presentacion.
		root.add(presentation);

	}
	
	/* 
	 * Este interface es utilizado por el Widget de presentaci�n para 
	 * ocultar la presentacion y mostrar el mosaico.
	 */
	public class SwitchToViewer implements Runnable {

		@Override
		public void run() {
			GWT.log(Util.getUserAgent());
			if (Util.getUserAgent().startsWith("mozilla/5") || 
				Util.getUserAgent().startsWith("mozilla/6") ||
				Util.getUserAgent().startsWith("opera/") ) {
				root.clear();
				root.add(navigator);
				History.newItem("navigator");
			} else {
				final PopupPanel invalidBrowserPopup = new PopupPanel(true);
				invalidBrowserPopup.setWidget(invalidBrowserPanel);
				invalidBrowserPopup.setGlassEnabled(true);
				invalidBrowserPopup.setAnimationEnabled(true);
				invalidBrowserPopup.center();
			}
		}
	}
		
}
