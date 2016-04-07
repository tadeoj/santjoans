package santjoans.client.piezes.navigator.preview;

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
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

import santjoans.client.canvas.GWTCanvasEventEnabled;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.piezes.navigator.viewer.IControllerViewerListener;
import santjoans.client.util.IConfiguration;

abstract public class PreviewWidget extends Composite implements IConfiguration, IControllerViewerListener {

	private static PreviewWidgetUiBinder uiBinder = GWT.create(PreviewWidgetUiBinder.class);

	interface PreviewWidgetUiBinder extends UiBinder<Widget, PreviewWidget> {
	}

	@UiField
	GWTCanvasEventEnabled gwtCanvas;
	
	@UiField
	protected CheckBox checkBox;
	
	protected PreviewWidgetContext previewWidgetContext;

	private ImageElement miniatura;
	
	public PreviewWidget() {
		
		// Se inicia la carga de la imagen.
		loadPreviewBackground();
		
		// Se construye el Widget
		initWidget(uiBinder.createAndBindUi(this));

//		// Se le asigna un color de background
//		gwtCanvas.setBackgroundColor(Color.GREY);
		
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
			gwtCanvas.getCanvas().getContext2d().drawImage(miniatura, 0, 0);
		}
	}
	
	private PreviewWidgetContext lastContext = null;
	
	protected void updatePreview(PreviewWidgetContext context) {
		lastContext = context;
		// La imagen de fondo
		updateBackground();

		// El cuadro rojo
		gwtCanvas.getCanvas().getContext2d().save();
		gwtCanvas.getCanvas().getContext2d().setStrokeStyle(CssColor.make("#DC143C"));
		gwtCanvas.getCanvas().getContext2d().setLineWidth(1);
		gwtCanvas.getCanvas().getContext2d().strokeRect(context.getRectX(), context.getRectY(), context.getRectWidth(), context.getRectHeight());
		gwtCanvas.getCanvas().getContext2d().restore();
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
