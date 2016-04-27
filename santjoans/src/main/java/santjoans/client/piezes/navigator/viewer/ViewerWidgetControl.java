package santjoans.client.piezes.navigator.viewer;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.Event;

import santjoans.client.canvas.CanvasNativeEventsHandler;
import santjoans.client.canvas.ICanvasEventEnabledListener;
import santjoans.client.model.IPieze;
import santjoans.client.piezes.navigator.viewer.piezepopup.PiezePopup;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

public class ViewerWidgetControl extends ViewerWidget implements IConfiguration, ICanvasEventEnabledListener {

	enum Status {
		OFF, ON
	};

	private String cursor = null;
	private Status status = Status.OFF;
	private Status touchStatus = Status.OFF;

	protected int initialPixelX;
	protected int initialPixelY;

	protected int initialTouchPixelX;
	protected int initialTouchPixelY;

	protected MovePiezeContext initialContext;
	protected MovePiezeContext currentContext;

	public ViewerWidgetControl() {
		super();
		Event.addNativePreviewHandler(new CanvasNativeEventsHandler(gwtCanvas, this));
	}

	@Override
	public void fireEvent(int x, int y, int eventType) {

		if (eventType == Event.ONDBLCLICK) {
			doubleClick(x, y);
		}
		switch (status) {
		case OFF:
			switch (eventType) {
			case Event.ONMOUSEMOVE:
				if (cursor == null) {
					cursor = gwtCanvas.getCanvasElement().getStyle().getCursor();
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.POINTER);
				}
				break;
			case Event.ONMOUSEOUT:
				// El cursor ha salida de la zona de vision
				if (cursor != null) {
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
					cursor = null;
				}
				break;
			case Event.ONMOUSEDOWN:
				// Mientras el cursor estaba en la zona de vision ha pulsado el
				// raton (ha enganchado a visa)
				status = Status.ON;
				gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.MOVE);
				currentContext = initialContext = new MovePiezeContext(controllerViewer.getContext().getZoomMode(),
						controllerViewer.getContext().getStartX(), controllerViewer.getContext().getStartY());
				initialPixelX = x;
				initialPixelY = y;
				break;
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
			case Event.ONMOUSEUP:
				// Esta moviendose con la vista enganchada (hay que utilizar el
				// contexto dinamico).
				gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.POINTER);
				status = Status.OFF;
				break;
			case Event.ONMOUSEOUT:
				// Se ha salido del control.
				if (cursor != null) {
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
					cursor = null;
				}
				status = Status.OFF;
				break;
			}
			break;
		}

	}

	@Override
	public void fireTouchEvent(int x, int y, int eventType) {

		switch (touchStatus) {
		case OFF:
			switch (eventType) {
			case Event.ONTOUCHSTART:
				// Mientras el cursor estaba en la zona de vision ha pulsado el
				// raton (ha enganchado a visa)
				touchStatus = Status.ON;
				currentContext = initialContext = new MovePiezeContext(controllerViewer.getContext().getZoomMode(), controllerViewer.getContext().getStartX(), controllerViewer.getContext().getStartY());
				initialTouchPixelX = x;
				initialTouchPixelY = y;
			}
			break;
		case ON:
			switch (eventType) {
			case Event.ONTOUCHMOVE:
				// Esta moviendose con la visa enganchada hay que utilizarel
				// contexto dinamico).
				if (updateTouchCurrentContext(x, y, false)) {
					moveStep(currentContext);
				}
				break;
			case Event.ONTOUCHEND:
				// Esta moviendose con la vista enganchada (hay que utilizar el
				// contexto dinamico).
				touchStatus = Status.OFF;
				break;
			case Event.ONTOUCHCANCEL:
				touchStatus = Status.OFF;
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

	protected boolean updateTouchCurrentContext(int x, int y, boolean force) {
		// Se calcula la diferencia en unidades de coordenada entre la posicion
		// actual y la posicion cuando se pulso el raton.
		int coordOffsiteX = calcCoordX((initialTouchPixelX - x) * 2);
		int coordOffsiteY = calcCoordY((initialTouchPixelY - y) * 2);
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
