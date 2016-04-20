package santjoans.client.piezes.navigator.viewer.piezepopup;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

import santjoans.client.model.IPieze;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

public class PiezePopup extends PopupPanel implements IConfiguration {

	private static PiezePopupUiBinder uiBinder = GWT.create(PiezePopupUiBinder.class);

	interface PiezePopupUiBinder extends UiBinder<Widget, PiezePopup> {
	}
	
	private static void setBusyCursor() {
		Element element = RootPanel.getBodyElement();
		element.getStyle().setCursor(Cursor.WAIT);
	}
	
	private static void setDefaultCursor() {
		Element element = RootPanel.getBodyElement();
		element.getStyle().setCursor(Cursor.DEFAULT);
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
	
	public PiezePopup(IPieze pieze, ImageElement piezeImageElment) {
		// El PopupPanel desaparecera al picar fuera de el. 
		super(true);
				
		// Se construye el Widget con el UIBinder
		Widget widget = uiBinder.createAndBindUi(this);
		
		// Se le asigna el widget al PopupPanel
		setWidget(widget);
		
		// Se pinta la imagen el en Canvas
		gwtCanvas.getCanvasElement().getContext2d().save();
		
		// Trasladamos el origen
		gwtCanvas.getCanvasElement().getContext2d().translate(Util.getCurrentScreenType().getPiezeViewerHeight() / 2, Util.getCurrentScreenType().getPiezeViewerWidth() / 2);
		
		// Definimos la rotacion
		gwtCanvas.getCanvasElement().getContext2d().rotate(pieze.getDetailRadians());
		
		// Dibujamos la pieza
		gwtCanvas.getCanvasElement().getContext2d().drawImage( piezeImageElment, -(Util.getCurrentScreenType().getPiezeViewerSide() / 2), -(Util.getCurrentScreenType().getPiezeViewerSide()/ 2), Util.getCurrentScreenType().getPiezeViewerSide(), Util.getCurrentScreenType().getPiezeViewerSide());
		
		// Restauramos el contexto de dibujo
		gwtCanvas.getCanvasElement().getContext2d().restore();

	}

	@UiFactory 
	public Canvas instantiateGWTCanvas() {
		gwtCanvas = Canvas.createIfSupported();
		gwtCanvas.setHeight(Util.getCurrentScreenType().getPiezeViewerHeight() + "px");
		gwtCanvas.setWidth(Util.getCurrentScreenType().getPiezeViewerWidth() + "px");
		gwtCanvas.setCoordinateSpaceHeight(Util.getCurrentScreenType().getPiezeViewerHeight());
		gwtCanvas.setCoordinateSpaceWidth(Util.getCurrentScreenType().getPiezeViewerWidth());
		return gwtCanvas;
	}
	
}
