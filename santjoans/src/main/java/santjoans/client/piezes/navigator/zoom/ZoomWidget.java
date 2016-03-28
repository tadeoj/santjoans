package santjoans.client.piezes.navigator.zoom;

import santjoans.client.imagebutton.IButtonPressed;
import santjoans.client.imagebutton.ImageButton;
import santjoans.client.piezes.navigator.viewer.IControllerViewerCommands;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.piezes.navigator.viewer.IControllerViewerListener;
import santjoans.client.resources.ISantjoansResources;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.ZoomModeEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ZoomWidget extends Composite implements IConfiguration, IButtonPressed, IControllerViewerListener {

	private static ControlPanelWidgetUiBinder uiBinder = GWT.create(ControlPanelWidgetUiBinder.class);

	interface ControlPanelWidgetUiBinder extends
			UiBinder<Widget, ZoomWidget> {
	}

	@UiField(provided = true)
	ImageButton zoomIn;
	
	@UiField(provided = true)
	ImageButton zoomOut;
	
	final private IControllerViewerCommands piezeViewerCommands;
	
	private ZoomModeEnum currentZoom = ZoomModeEnum.MODE_100;

	public ZoomWidget(IControllerViewerCommands piezeViewerCommands) {
		zoomIn = new ImageButton(ISantjoansResources.INSTANCE.zoomInIcon());
		zoomOut = new ImageButton(ISantjoansResources.INSTANCE.zoomOutIcon());
		
		// Comandos para gestionar el visor de piezas
		this.piezeViewerCommands = piezeViewerCommands;

		// Se construye el Widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Valor inicial de la botnoera hasta que reciba alguna notificacion
		initialValue();
		
		// Se instalan los listeners
		zoomIn.setButtonPressedListener(this);
		zoomOut.setButtonPressedListener(this);
		
	}

	@Override
	public void buttonPressed(Object source) {
		if (source == zoomIn) {
			currentZoom = currentZoom.getZoomIn();
			piezeViewerCommands.setZoom(currentZoom);
		} else if (source == zoomOut) {
			currentZoom = currentZoom.getZoomOut();
			piezeViewerCommands.setZoom(currentZoom);
		}
	}

	@Override
	public void updatedControllerViewer(IControllerViewerContext context) {
		currentZoom = context.getZoomMode();
		zoomIn.setEnabled(currentZoom.getZoomIn() != currentZoom);
		zoomOut.setEnabled(currentZoom.getZoomOut() != currentZoom);
	}

	public void initialValue() {
		zoomIn.setEnabled(false);
		zoomOut.setEnabled(false);
	}
	
}
