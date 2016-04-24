package santjoans.client.piezes.navigator.viewer;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

import santjoans.client.model.IPieze;
import santjoans.client.piezes.navigator.viewer.piezepopup.PiezePopup;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;

public class ViewerWidgetControl extends ViewerWidget implements IConfiguration {

	enum Status {
		OFF, ON
	};

	private int coordX;
	private int coordY;

	private String cursor = null;
	private Status status = Status.OFF;

	protected int initialPixelX;
	protected int initialPixelY;

	protected MovePiezeContext initialContext;
	protected MovePiezeContext currentContext;

	public ViewerWidgetControl() {
		super();

		coordX = gwtCanvas.getCoordinateSpaceWidth();
		coordY = gwtCanvas.getCoordinateSpaceHeight();
		
		// Se añaden los listeners para los eventos especificos dentro del
		// canvas.
		gwtCanvas.addDoubleClickHandler(new ViewerWidgetDblClickHandler());
		gwtCanvas.addTouchStartHandler(new ViewerWidgetTouchStartHandler());
		gwtCanvas.addMouseOutHandler(new ViewerWidgetMouseOutHandler());
		gwtCanvas.addMouseMoveHandler(new ViewerWidgetMouseMoveHandler());
		gwtCanvas.addMouseDownHandler(new ViewerWidgetMouseDownHandler());
		gwtCanvas.addMouseUpHandler(new ViewerWidgetMouseUpHandler());
	}

	class ViewerWidgetDblClickHandler implements DoubleClickHandler {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			int x = event.getClientX() - getAbsoluteLeft() - 1;
			int y = event.getClientY() - getAbsoluteTop() - 1;
			if (x < coordX && y < coordY) {
				doubleClick(x, y);
			}
		}

	}
	
	class ViewerWidgetTouchStartHandler implements TouchStartHandler {

		@Override
		public void onTouchStart(TouchStartEvent event) {
		
			int x = event.getTouches().get(0).getRelativeX(gwtCanvas.getCanvasElement()) - getAbsoluteLeft() - 1;
			int y = event.getTouches().get(0).getRelativeY(gwtCanvas.getCanvasElement()) - getAbsoluteTop() - 1;
			if (x < coordX && y < coordY) {
				doubleClick(x, y);
			}
		}
		
	}

	class ViewerWidgetMouseOutHandler implements MouseOutHandler {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			int x = event.getClientX() - getAbsoluteLeft() - 1;
			int y = event.getClientY() - getAbsoluteTop() - 1;
			if (x < coordX && y < coordY) {
				switch (status) {
				case OFF:
					if (cursor != null) {
						gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
						cursor = null;
					}
					status = Status.OFF;
					break;
				case ON:
					if (cursor != null) {
						gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.valueOf(cursor));
						cursor = null;
					}
					status = Status.OFF;
					break;
				}
			}
		}

	}

	class ViewerWidgetMouseMoveHandler implements MouseMoveHandler {

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			int x = event.getClientX() - getAbsoluteLeft() - 1;
			int y = event.getClientY() - getAbsoluteTop() - 1;
			if (x < coordX && y < coordY) {
				switch (status) {
				case OFF:
					if (cursor == null) {
						cursor = gwtCanvas.getCanvasElement().getStyle().getCursor();
						gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.POINTER);
					}
					break;
				case ON:
					if (updateCurrentContext(x, y, false)) {
						moveStep(currentContext);
					}
					break;
				}
			}
		}

	}

	class ViewerWidgetMouseDownHandler implements MouseDownHandler {

		@Override
		public void onMouseDown(MouseDownEvent event) {
			int x = event.getClientX() - getAbsoluteLeft() - 1;
			int y = event.getClientY() - getAbsoluteTop() - 1;
			if (x < coordX && y < coordY) {
				if (status.equals(Status.OFF)) {
					status = Status.ON;
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.MOVE);
					currentContext = initialContext = new MovePiezeContext(controllerViewer.getContext().getZoomMode(),
							controllerViewer.getContext().getStartX(), controllerViewer.getContext().getStartY());
					initialPixelX = x;
					initialPixelY = y;
				}
			}
		}

	}

	class ViewerWidgetMouseUpHandler implements MouseUpHandler {

		@Override
		public void onMouseUp(MouseUpEvent event) {
			int x = event.getClientX() - getAbsoluteLeft() - 1;
			int y = event.getClientY() - getAbsoluteTop() - 1;
			if (x < coordX && y < coordY) {
				if (status.equals(Status.ON)) {
					gwtCanvas.getCanvasElement().getStyle().setCursor(Cursor.POINTER);
					status = Status.OFF;
				}
			}
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
