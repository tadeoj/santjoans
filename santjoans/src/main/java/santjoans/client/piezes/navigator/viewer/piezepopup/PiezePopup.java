package santjoans.client.piezes.navigator.viewer.piezepopup;

import santjoans.client.model.IPieze;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

public class PiezePopup extends PopupPanel implements IConfiguration {

	private static PiezePopupUiBinder uiBinder = GWT.create(PiezePopupUiBinder.class);

	interface PiezePopupUiBinder extends UiBinder<Widget, PiezePopup> {
	}
	
	private static void setBusyCursor() {
		Element element = RootPanel.getBodyElement();
		DOM.setStyleAttribute(element, "cursor", "wait");
	}
	
	private static void setDefaultCursor() {
		Element element = RootPanel.getBodyElement();
		DOM.setStyleAttribute(element, "cursor", "default");
	}

	static public void displayPieze(final IPieze pieze) {
		
		// Se indica con el cursor que se esta trabajando.
		setBusyCursor();

		// Se carga la imagen
		ImageLoader.loadImages(new String[] { pieze.getImageDetailedUrl() }, new ImageLoader.CallBack() {
			@Override
			public void onImagesLoaded(ImageElement[] imageElements) {
	        	// Se muestra la imagen.
	        	setDefaultCursor();
	        	// Obtenemos la imagen
	        	ImageElement piezeImageElement = imageElements[0];
	        	// Se prepara el PopupPanel donde mostrar la imagen
	        	PiezePopup piezePopup = new PiezePopup(pieze, piezeImageElement);
	        	piezePopup.setGlassEnabled(true);
	        	piezePopup.setAnimationEnabled(true);
				// Se visualiza centrado,
	        	piezePopup.center();
			}
		});

	}


	@UiField
	GWTCanvas gwtCanvas;
	
	public PiezePopup(IPieze pieze, ImageElement piezeImageElment) {
		// El EopupPanel desaparecera al picar fuera de el. 
		super(true);
		
		// Se construye el Widget con el UIBinder
		Widget widget = uiBinder.createAndBindUi(this);
		
		// Se le asigna el widget al PopupPanel
		setWidget(widget);
		
		// Se pinta la imagen el en Canvas
		gwtCanvas.saveContext();
		
		// Trasladamos el origen
		gwtCanvas.translate(Util.getCurrentScreenType().getPiezeViewerHeight() / 2, Util.getCurrentScreenType().getPiezeViewerWidth() / 2);
		
		// Definimos la rotacion
		gwtCanvas.rotate(pieze.getDetailRadians());
		
		// Dibujamos la pieza
		gwtCanvas.drawImage( piezeImageElment, -(Util.getCurrentScreenType().getPiezeViewerSide() / 2), -(Util.getCurrentScreenType().getPiezeViewerSide()/ 2), Util.getCurrentScreenType().getPiezeViewerSide(), Util.getCurrentScreenType().getPiezeViewerSide());
		
		// Restauramos el contexto de dibujo
		gwtCanvas.restoreContext();

	}

	@UiFactory GWTCanvas instantiateGWTCanvas() {
		return new GWTCanvas(Util.getCurrentScreenType().getPiezeViewerHeight(), Util.getCurrentScreenType().getPiezeViewerWidth());
	}
	
}
