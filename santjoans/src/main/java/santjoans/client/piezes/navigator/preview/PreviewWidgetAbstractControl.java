package santjoans.client.piezes.navigator.preview;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.Event;

import santjoans.client.canvas.CanvasNativeEventsHandler;
import santjoans.client.canvas.ICanvasEventEnabledListener;
import santjoans.client.piezes.navigator.viewer.IControllerViewerCommands;
import santjoans.client.util.ZoomModeEnum;

abstract public class PreviewWidgetAbstractControl extends PreviewWidget implements ICanvasEventEnabledListener {

	protected Boolean LOG_ENABLED = true;

	enum Status {
		OFF, PREPARED, ON
	};

	protected IControllerViewerCommands piezeViewerCommands;

	private String cursor = null;
	private Status status = Status.OFF;
	private Status touchStatus = Status.OFF;

	protected int initialPixelX;
	protected int initialPixelY;
	
	protected int initialTouchPixelX;
	protected int initialTouchPixelY;

	protected PreviewWidgetContext initialContext;
	protected PreviewWidgetContext currentContext;

	private boolean syncViewer;

	public PreviewWidgetAbstractControl(IControllerViewerCommands piezeViewerCommands, boolean syncViewer) {
		super();
		this.piezeViewerCommands = piezeViewerCommands;
		this.syncViewer = syncViewer;

		Event.addNativePreviewHandler(new CanvasNativeEventsHandler(gwtCanvas, this));
	}

	public boolean isSyncViewer() {
		return syncViewer;
	}

	public void setSyncViewer(boolean syncViewer) {
		this.syncViewer = syncViewer;
	}

	
	@Override
	public void fireEvent(int x, int y, int eventType) {
		
		if (previewWidgetContext == null) {
			// Todavia no esta preparado el viewer
			return;
		}
		
		switch (status) {
		case OFF:
			switch (eventType) {
			case Event.ONMOUSEMOVE:
				// Mientras no engancha la vista el movimiento el contexto de referencia es el del preview
				if (isInViewWindow(previewWidgetContext, x, y)) {
					// El cursor ha entrado en la zona de vision
					cursor = gwtCanvas.getCanvasElement().getStyle().getCursor();
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.POINTER);
					status = Status.PREPARED;
				}
				break;
			}
			break;
		case PREPARED:
			switch (eventType) {
			case Event.ONMOUSEMOVE:
				// Mientras no engancha la vista el movimiento el contexto de referencia es el del preview
				if (!isInViewWindow(previewWidgetContext, x, y)) {
					// El cursor ha salida de la zona de vision
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
					status = Status.OFF;
				}
				break;
			case Event.ONMOUSEOUT:
				// El cursor ha salida de la zona de vision
				gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
				status = Status.OFF;
				break;
			case Event.ONMOUSEDOWN:
				// Mientras no engancha la vista el movimiento el contexto de referencia es el del preview
				if (isInViewWindow(previewWidgetContext, x, y)) {
					// Mientras el cursor estaba en la zona de vision ha pulsado el raton (ha enganchado a visa)
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.MOVE);
					status = Status.ON;
					currentContext = initialContext = new PreviewWidgetContext(previewWidgetContext.getZoomMode(), previewWidgetContext.getStartX(), previewWidgetContext.getStartY());
					initialPixelX = x;
					initialPixelY = y;
				}
			}
			break;
		case ON:
			switch (eventType) {
			case Event.ONMOUSEMOVE:
				// Esta moviendose con la visa enganchada hay que utilizarel contexto dinamico).
				if (isInViewWindow(currentContext, x, y)) {
					// Se esta moviendo dentro de la zona de vision con el raton pulsado.
					if (updateCurrentContext(x, y, false)) {
						moveStep(currentContext);
					}
				} else {
					// Mientras estaba en la zona de vision y mantenia pulsado el raton, se ha salida de la
					// zona de vision lo cual debe ser porque se mueve muy rapido.
					// Intentamos anclar el cuadro.
					if (updateCurrentContext(x, y, false)) {
						moveStep(currentContext);
					}
				}
				break;
			case Event.ONMOUSEUP:
				// Esta moviendose con la vista enganchada (hay que utilizar el contexto dinamico).
				gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.POINTER);
				status = Status.PREPARED;
				updateCurrentContext(x, y, true);
				moveFinish(currentContext);
				break;
			case Event.ONMOUSEOUT:
				// Se ha salido del control.
				gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
				status = Status.OFF;
				//updateCurrentContext(x, y, true);
				moveFinish(currentContext);
				break;
			}
			break;
		}
		
	}
	
	@Override
	public void fireTouchEvent(int x, int y, int eventType) {

		if (previewWidgetContext == null) {
			// Todavia no esta preparado el viewer
			return;
		}
		
		switch (touchStatus) {
		case OFF:
			switch (eventType) {
			case Event.ONTOUCHSTART:
				// Mientras no engancha la vista el movimiento el contexto de referencia es el del preview
				if (isInViewWindow(previewWidgetContext, x, y)) {
					// Mientras el cursor estaba en la zona de vision ha pulsado el raton (ha enganchado a visa)
					touchStatus = Status.ON;
					currentContext = initialContext = new PreviewWidgetContext(previewWidgetContext.getZoomMode(), previewWidgetContext.getStartX(), previewWidgetContext.getStartY());
					initialTouchPixelX = x;
					initialTouchPixelY = y;
				}
			}
			break;
		case ON:
			switch (eventType) {
			case Event.ONTOUCHMOVE:
				// Esta moviendose con la visa enganchada hay que utilizarel contexto dinamico).
				if (isInViewWindow(currentContext, x, y)) {
					// Se esta moviendo dentro de la zona de vision con el raton pulsado.
					if (updateTouchCurrentContext(x, y, false)) {
						moveStep(currentContext);
					}
				} else {
					// Mientras estaba en la zona de vision y mantenia pulsado el raton, se ha salida de la
					// zona de vision lo cual debe ser porque se mueve muy rapido.
					// Intentamos anclar el cuadro.
					if (updateTouchCurrentContext(x, y, false)) {
						moveStep(currentContext);
					}
				}
				break;
			case Event.ONTOUCHEND:
			case Event.ONTOUCHCANCEL:
				// Se ha salido del control.
				touchStatus = Status.OFF;
				moveFinish(currentContext);
				break;
			}
			break;
		case PREPARED:
			break;
		default:
			break;
		}
		
	}

	private boolean isInViewWindow(PreviewWidgetContext context, int x, int y) {
		return x >= context.getViewStartX() && x <= context.getViewEndX() && y >= context.getViewStartY()
				&& y <= context.getViewEndY();
	}

	protected boolean updateCurrentContext(int x, int y, boolean force) {
		// Se calcula la diferencia en unidades de coordenada entre la posicion
		// actual y la posicion cuando se pulso el raton.
		int coordOffsiteX = calcCoordX(x - initialPixelX);
		int coordOffsiteY = calcCoordY(y - initialPixelY);
		// Se suma esta diferencia de coordenadas a las corrdenadas que habian
		// cuando se pulso el raton.
		int newStartX = adjustX(initialContext.getStartX() + coordOffsiteX);
		int newStartY = adjustY(initialContext.getStartY() + coordOffsiteY);
		// Si la posicion de las coordenadas calculadas es diferente a la ultima
		// que se calculo o si esta la operacion forzada
		// se retorna un nuevo contexto.
		if (newStartX != currentContext.getStartX() || newStartY != currentContext.getStartY() || force) {
			currentContext = new PreviewWidgetContext(previewWidgetContext.getZoomMode(), newStartX, newStartY);
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean updateTouchCurrentContext(int x, int y, boolean force) {
		// Se calcula la diferencia en unidades de coordenada entre la posicion
		// actual y la posicion cuando se pulso el raton.
		int coordOffsiteX = calcCoordX(x - initialTouchPixelX);
		int coordOffsiteY = calcCoordY(y - initialTouchPixelY);
		// Se suma esta diferencia de coordenadas a las corrdenadas que habian
		// cuando se pulso el raton.
		int newStartX = adjustX(initialContext.getStartX() + coordOffsiteX);
		int newStartY = adjustY(initialContext.getStartY() + coordOffsiteY);
		// Si la posicion de las coordenadas calculadas es diferente a la ultima
		// que se calculo o si esta la operacion forzada
		// se retorna un nuevo contexto.
		if (newStartX != currentContext.getStartX() || newStartY != currentContext.getStartY() || force) {
			currentContext = new PreviewWidgetContext(previewWidgetContext.getZoomMode(), newStartX, newStartY);
			return true;
		} else {
			return false;
		}
	}

	private int pixelsToMillimetersX(int pixel) {
		return ((pixel * ZoomModeEnum.MODE_100.getMillimetersWidth()) / PREVIEW_X);
	}

	private int pixelsToMillimetersY(int pixel) {
		return ((pixel * ZoomModeEnum.MODE_100.getMillimetersHeight()) / PREVIEW_Y);
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
		int minusY = previewWidgetContext.getZoomMode().getEndY(newStartY) - MODEL_MAIN_MAX_COORD_Y;
		return minusY > 0 ? newStartY - minusY : newStartY;
	}

	protected int adjustX(int newStartX) {
		if (newStartX < 0)
			newStartX = 0;
		int minusX = previewWidgetContext.getZoomMode().getEndX(newStartX) - MODEL_MAIN_MAX_COORD_X;
		return minusX > 0 ? newStartX - minusX : newStartX;
	}

	abstract protected void moveStep(PreviewWidgetContext context);

	abstract protected void moveFinish(PreviewWidgetContext context);

}
