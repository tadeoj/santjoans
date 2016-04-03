package santjoans.client.piezes.navigator.preview;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import santjoans.client.canvas.GWTCanvasEventEnabled;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.piezes.navigator.viewer.IControllerViewerListener;
import santjoans.client.util.IConfiguration;

abstract public class PreviewWidget extends Composite implements IConfiguration, IControllerViewerListener {

	private static PreviewWidgetUiBinder uiBinder = GWT.create(PreviewWidgetUiBinder.class);

	interface PreviewWidgetUiBinder extends UiBinder<Widget, PreviewWidget> {
	}

	@UiField(provided = true)
	Canvas gwtCanvas;
	
	@UiField
	protected CheckBox checkBox;
	
	protected GWTCanvasEventEnabled gwtCanvasEvent;
	
	protected PreviewWidgetContext previewWidgetContext;

	private ImageElement miniatura;
	
	public PreviewWidget() {
		
		// Se inicia la carga de la imagen.
		loadPreviewBackground();
		
		gwtCanvasEvent = new GWTCanvasEventEnabled();
		gwtCanvas = Canvas.wrap(gwtCanvasEvent);
		
		// Se construye el Widget
		initWidget(uiBinder.createAndBindUi(this));

//		// Se le asigna un color de background
//		gwtCanvas.setBackgroundColor(Color.GREY);
		
		// Se asigna el sistema de coordenadas
		gwtCanvas.setCoordinateSpaceWidth(PREVIEW_X);
		gwtCanvas.setCoordinateSpaceHeight(PREVIEW_Y);
		
	}
	
	@UiFactory GWTCanvasEventEnabled instantiateGWTCanvas() {
		return new GWTCanvasEventEnabled(PREVIEW_X, PREVIEW_Y);
	}
	
	private void loadPreviewBackground() {
		String miniaturaUrl = GWT.getModuleBaseURL() + "miniatura.png";
		ImageLoader.loadImages(new String[] { miniaturaUrl }, new ImageLoader.CallBack() {
			@Override
			public void onImagesLoaded(ImageElement[] imageElements) {
				if (imageElements.length == 1) {
					miniatura = imageElements[0];
					updatePreview(lastContext);
				}
			}
		});
	}

	protected void updateBackground() {
		// Si la miniatura esta disponible (si ya se ha cargado) se pinta.
		if (miniatura != null) {
			gwtCanvas.getContext2d().drawImage(miniatura, 0, 0);
		}
	}
	
	private PreviewWidgetContext lastContext = null;
	
	protected void updatePreview(PreviewWidgetContext context) {
		lastContext = context;
		// La imagen de fondo
		updateBackground();

		// El cuadro rojo
		gwtCanvas.getContext2d().save();
		gwtCanvas.getContext2d().setStrokeStyle(CssColor.make("#DC143C"));
		gwtCanvas.getContext2d().setLineWidth(1);
		gwtCanvas.getContext2d().strokeRect(context.getRectX(), context.getRectY(), context.getRectWidth(), context.getRectHeight());
		gwtCanvas.getContext2d().restore();
	}
	
	@Override
	public void updatedControllerViewer(IControllerViewerContext viewerContext) {
		previewWidgetContext = new PreviewWidgetContext(viewerContext);
		updatePreview(previewWidgetContext);
	}
	
	@UiHandler("checkBox")
	public void onClick(ClickEvent event) {
		autoSync(((CheckBox) event.getSource()).getValue());
	}
	
	abstract protected void autoSync(boolean sync);
	
}
