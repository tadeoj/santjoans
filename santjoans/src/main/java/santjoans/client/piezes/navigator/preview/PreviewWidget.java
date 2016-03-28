package santjoans.client.piezes.navigator.preview;

import santjoans.client.canvas.GWTCanvasEventEnabled;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.piezes.navigator.viewer.IControllerViewerListener;
import santjoans.client.util.IConfiguration;

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
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

abstract public class PreviewWidget extends Composite implements IConfiguration, IControllerViewerListener {

	private static PreviewWidgetUiBinder uiBinder = GWT.create(PreviewWidgetUiBinder.class);

	interface PreviewWidgetUiBinder extends UiBinder<Widget, PreviewWidget> {
	}

	@UiField
	protected GWTCanvasEventEnabled gwtCanvas;
	
	@UiField
	protected CheckBox checkBox;
	
	protected PreviewWidgetContext previewWidgetContext;

	private ImageElement miniatura;
	
	public PreviewWidget() {
		
		// Se inicia la carga de la imagen.
		loadPreviewBackground();
		
		// Se construye el Widget
		initWidget(uiBinder.createAndBindUi(this));

		// Se le asigna un color de background
		gwtCanvas.setBackgroundColor(Color.GREY);
		
		// Se asigna el sistema de coordenadas
		gwtCanvas.setCoordSize(PREVIEW_X, PREVIEW_Y);
		
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
			gwtCanvas.drawImage(miniatura, 0, 0);
		}
	}
	
	private PreviewWidgetContext lastContext = null;
	
	protected void updatePreview(PreviewWidgetContext context) {
		lastContext = context;
		// La imagen de fondo
		updateBackground();

		// El cuadro rojo
		gwtCanvas.saveContext();
		gwtCanvas.setStrokeStyle(Color.RED);
		gwtCanvas.setLineWidth(1);
		gwtCanvas.strokeRect(context.getRectX(), context.getRectY(), context.getRectWidth(), context.getRectHeight());
		gwtCanvas.restoreContext();
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
