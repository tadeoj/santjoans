package santjoans.client.piezes.navigator.viewer.piezepopup;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import santjoans.client.canvas.GWTCanvasEventEnabled;
import santjoans.client.model.IPieze;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

public class PiezePopup extends PopupPanel implements IConfiguration {

	private static PiezePopupUiBinder uiBinder = GWT.create(PiezePopupUiBinder.class);

	interface PiezePopupUiBinder extends UiBinder<Widget, PiezePopup> {
	}
	
	private static void setBusyCursor() {
		Element element = RootPanel.getBodyElement();
		element.getStyle().setProperty("cursor", "wait");
	}
	
	private static void setDefaultCursor() {
		Element element = RootPanel.getBodyElement();
		element.getStyle().setProperty("cursor", "default");
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
	Canvas gwtCanvas;
	
	private GWTCanvasEventEnabled gwtCanvasEvent;
	
	public PiezePopup(IPieze pieze, ImageElement piezeImageElment) {
		// El PopupPanel desaparecera al picar fuera de el. 
		super(true);
		
		gwtCanvasEvent = new GWTCanvasEventEnabled();
		gwtCanvas = Canvas.wrap(gwtCanvasEvent);
		
		// Se construye el Widget con el UIBinder
		Widget widget = uiBinder.createAndBindUi(this);
		
		// Se le asigna el widget al PopupPanel
		setWidget(widget);
		
		// Se pinta la imagen el en Canvas
		gwtCanvas.getContext2d().save();
		
		// Trasladamos el origen
		gwtCanvas.getContext2d().translate(Util.getCurrentScreenType().getPiezeViewerHeight() / 2, Util.getCurrentScreenType().getPiezeViewerWidth() / 2);
		
		// Definimos la rotacion
		gwtCanvas.getContext2d().rotate(pieze.getDetailRadians());
		
		// Dibujamos la pieza
		gwtCanvas.getContext2d().drawImage( piezeImageElment, -(Util.getCurrentScreenType().getPiezeViewerSide() / 2), -(Util.getCurrentScreenType().getPiezeViewerSide()/ 2), Util.getCurrentScreenType().getPiezeViewerSide(), Util.getCurrentScreenType().getPiezeViewerSide());
		
		// Restauramos el contexto de dibujo
		gwtCanvas.getContext2d().restore();

	}

	@UiFactory Canvas instantiateGWTCanvas() {
		gwtCanvasEvent = new GWTCanvasEventEnabled(Util.getCurrentScreenType().getPiezeViewerHeight(), Util.getCurrentScreenType().getPiezeViewerWidth());
		return Canvas.wrap(gwtCanvasEvent);
	}
	
}
