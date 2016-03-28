package santjoans.client.piezes.navigator.direction;

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

public class DirectionWidget extends Composite implements IConfiguration, IButtonPressed, IControllerViewerListener {

	private static ControlPanelWidgetUiBinder uiBinder = GWT.create(ControlPanelWidgetUiBinder.class);

	interface ControlPanelWidgetUiBinder extends
			UiBinder<Widget, DirectionWidget> {
	}

	@UiField(provided = true)
	ImageButton left;

	@UiField(provided = true)
	ImageButton right;

	@UiField(provided = true)
	ImageButton up;

	@UiField(provided = true)
	ImageButton down;

	@UiField(provided = true)
	ImageButton reset;

	final private IControllerViewerCommands piezeViewerCommands;
	
	private ZoomModeEnum currentZoom = ZoomModeEnum.MODE_100;

	public DirectionWidget(IControllerViewerCommands piezeViewerCommands) {
		left = new ImageButton(ISantjoansResources.INSTANCE.leftIcon());
		right = new ImageButton(ISantjoansResources.INSTANCE.rightIcon());
		up = new ImageButton(ISantjoansResources.INSTANCE.upIcon());
		down = new ImageButton(ISantjoansResources.INSTANCE.downIcon());
		reset = new ImageButton(ISantjoansResources.INSTANCE.resetIcon());
		
		// Comandos para gestionar el visor de piezas
		this.piezeViewerCommands = piezeViewerCommands;

		// Se construye el Widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Valor inicial de la botnoera hasta que reciba alguna notificacion
		initialValue();
		
		// Se instalan los listeners
		left.setButtonPressedListener(this);
		right.setButtonPressedListener(this);
		up.setButtonPressedListener(this);
		down.setButtonPressedListener(this);
		reset.setButtonPressedListener(this);
		
	}

	@Override
	public void buttonPressed(Object source) {
		if (source == left) {
			piezeViewerCommands.moveLeft();
		} else if (source == right) {
			piezeViewerCommands.moveRight();
		} else if (source == up) {
			piezeViewerCommands.moveUp();
		} else if (source == down) {
			piezeViewerCommands.moveDown();
		} else if (source == reset) {
			piezeViewerCommands.setZoom(ZoomModeEnum.MODE_100);
		}
	}

	@Override
	public void updatedControllerViewer(IControllerViewerContext context) {
		currentZoom = context.getZoomMode();
		if (currentZoom == ZoomModeEnum.MODE_100) {
			right.setEnabled(false);
			left.setEnabled(false);
			up.setEnabled(false);
			down.setEnabled(false);
			reset.setEnabled(false);
		} else {
			right.setEnabled(context.canMoveRight());
			left.setEnabled(context.canMoveLeft());
			up.setEnabled(context.canMoveUp());
			down.setEnabled(context.canMoveDown());
			reset.setEnabled(true);
		}
	}

	public void initialValue() {
		right.setEnabled(false);
		left.setEnabled(false);
		up.setEnabled(false);
		down.setEnabled(false);
		reset.setEnabled(false);
	}
	
}
