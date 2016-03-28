package santjoans.client.piezes.navigator.viewer;

import santjoans.client.canvas.ICanvasEventEnabledListener;
import santjoans.client.model.IPieze;
import santjoans.client.piezes.navigator.viewer.piezepopup.PiezePopup;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

public class ViewerWidgetControl extends ViewerWidget implements IConfiguration, ICanvasEventEnabledListener {

	enum Status {
		OFF, ON
	};

	private String cursor = null;
	private Status status = Status.OFF;

	protected int initialPixelX;
	protected int initialPixelY;

	protected MovePiezeContext initialContext;
	protected MovePiezeContext currentContext;

	public ViewerWidgetControl() {
		super();
		this.gwtCanvas.setEventListener(this);
	}

	@Override
	public void firedEvent(int x, int y, int eventType) {

		if (eventType == Event.ONDBLCLICK) {
			doubleClick(x, y);
		}
		switch (status) {
		case OFF:
			switch (eventType) {
			case Event.ONMOUSEMOVE:
				if (cursor == null) {
					cursor = DOM.getStyleAttribute(gwtCanvas.getElement(), "cursor");
					DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", "pointer");
				}
				break;
			case Event.ONTOUCHMOVE:
				if (cursor == null) {
					cursor = DOM.getStyleAttribute(gwtCanvas.getElement(), "cursor");
					DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", "pointer");
				}
				break;
			case Event.ONMOUSEOUT:
				// El cursor ha salida de la zona de vision
				if (cursor != null) {
					DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", cursor);
					cursor = null;
				}
				break;
			case Event.ONMOUSEDOWN:
				// Mientras el cursor estaba en la zona de vision ha pulsado el
				// raton (ha enganchado a visa)
				status = Status.ON;
				DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", "move");
				currentContext = initialContext = new MovePiezeContext(controllerViewer.getContext().getZoomMode(),
						controllerViewer.getContext().getStartX(), controllerViewer.getContext().getStartY());
				initialPixelX = x;
				initialPixelY = y;
			case Event.ONTOUCHSTART:
				// Mientras el cursor estaba en la zona de vision ha pulsado el
				// raton (ha enganchado a visa)
				status = Status.ON;
				DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", "move");
				currentContext = initialContext = new MovePiezeContext(controllerViewer.getContext().getZoomMode(),
						controllerViewer.getContext().getStartX(), controllerViewer.getContext().getStartY());
				initialPixelX = x;
				initialPixelY = y;
			}
			break;
		case ON:
			switch (eventType) {
			case Event.ONMOUSEMOVE:
				// Esta moviendose con la visa enganchada hay que utilizarel
				// contexto dinamico).
				if (updateCurrentContext(x, y, false)) {
					moveStep(currentContext);
				}
				break;
			case Event.ONTOUCHMOVE:
				// Esta moviendose con la visa enganchada hay que utilizarel
				// contexto dinamico).
				if (updateCurrentContext(x, y, false)) {
					moveStep(currentContext);
				}
				break;
			case Event.ONMOUSEUP:
				// Esta moviendose con la vista enganchada (hay que utilizar el
				// contexto dinamico).
				DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", "pointer");
				status = Status.OFF;
				break;
			case Event.ONTOUCHSTART:
				// Esta moviendose con la vista enganchada (hay que utilizar el
				// contexto dinamico).
				DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", "pointer");
				status = Status.OFF;
				break;

			case Event.ONMOUSEOUT:
				// Se ha salido del control.
				if (cursor != null) {
					DOM.setStyleAttribute(gwtCanvas.getElement(), "cursor", cursor);
					cursor = null;
				}
				status = Status.OFF;
				break;
		}
		break;
	}

	}

	protected boolean updateCurrentContext(int x, int y, boolean force) {
		// Se calcula la diferencia en unidades de coordenada entre la posicion
		// actual y la posicion cuando se pulso el raton.
		int coordOffsiteX = calcCoordX((initialPixelX - x) * 2);
		int coordOffsiteY = calcCoordY((initialPixelY - y) * 2);
		// Se suma esta diferencia de coordenadas a las corrdenadas que habian
		// cuando se pulso el raton.
		int newStartX = adjustX(initialContext.getStartX() + coordOffsiteX);
		int newStartY = adjustY(initialContext.getStartY() + coordOffsiteY);
		// Si la posicion de las coordenadas calculadas es diferente a la ultima
		// que se calculo o si esta la operacion forzada
		// se retorna un nuevo contexto.
		if (newStartX != currentContext.getStartX() || newStartY != currentContext.getStartY() || force) {
			currentContext = new MovePiezeContext(controllerViewer.getContext().getZoomMode(), newStartX, newStartY);
			return true;
		} else {
			return false;
		}
	}

	private int pixelsToMillimetersX(int pixel) {
		return ((pixel * controllerViewer.getContext().getZoomMode().getMillimetersWidth())
				/ Util.getCurrentScreenType().getCanvasX());
	}

	private int pixelsToMillimetersY(int pixel) {
		return ((pixel * controllerViewer.getContext().getZoomMode().getMillimetersHeight())
				/ Util.getCurrentScreenType().getCanvasY());
	}

	protected int calcCoordX(int pixels) {
		return (int) (pixelsToMillimetersX(pixels) / PIEZE_MAIN_HALF_DIAGONAL);
	}

	protected int calcCoordY(int pixels) {
		return (int) (pixelsToMillimetersY(pixels) / PIEZE_MAIN_HALF_DIAGONAL);
	}

	protected int adjustY(int newStartY) {
		if (newStartY < 0)
			newStartY = 0;
		int minusY = controllerViewer.getContext().getZoomMode().getEndY(newStartY) - MODEL_MAIN_MAX_COORD_Y;
		return minusY > 0 ? newStartY - minusY : newStartY;
	}

	protected int adjustX(int newStartX) {
		if (newStartX < 0)
			newStartX = 0;
		int minusX = controllerViewer.getContext().getZoomMode().getEndX(newStartX) - MODEL_MAIN_MAX_COORD_X;
		return minusX > 0 ? newStartX - minusX : newStartX;
	}

	protected void moveStep(final MovePiezeContext context) {
		piezeViewerCommands.setPosition(context.getStartX(), context.getStartY());
	}

	protected void doubleClick(int x, int y) {
		IPieze pickedPieze = controllerViewer.getPickedPieze(x, y);
		if (pickedPieze != null) {
			PiezePopup.displayPieze(pickedPieze);
		}
	}

}
