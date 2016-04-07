package santjoans.client.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Composite;

public class GWTCanvasEventEnabled extends Composite {

	private int coordX;
	private int coordY;

	private Canvas canvas;
	private HandlerRegistration handlerRegistration;
	private ICanvasEventEnabledListener nativeListener;

	public GWTCanvasEventEnabled(int coordX, int coordY) {
		
		this.canvas = Canvas.createIfSupported();
		canvas.getCanvasElement().setWidth(coordX);
		canvas.getCanvasElement().setHeight(coordY);
		
		canvas.setCoordinateSpaceWidth(coordX);
		canvas.setCoordinateSpaceHeight(coordY);

		initWidget(this.canvas);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	public void setEventListener(ICanvasEventEnabledListener listener) {
		this.nativeListener = listener;
		if (nativeListener == null) {
			if (handlerRegistration != null) {
				handlerRegistration.removeHandler();
				handlerRegistration = null;
			}
		} else {
			if (handlerRegistration != null) {
				throw new IllegalStateException("Ya hay un CanvasPreviewHandler para el canvas.");
			}
			handlerRegistration = Event.addNativePreviewHandler(new CanvasPreviewHandler());
		}
	}

	class CanvasPreviewHandler implements Event.NativePreviewHandler {
		@Override
		public void onPreviewNativeEvent(NativePreviewEvent previewEvent) {

			NativeEvent event = previewEvent.getNativeEvent();
			Element element = event.getEventTarget().cast();

			if (element == getCanvas().getElement()) {
				switch (previewEvent.getTypeInt()) {
				case Event.ONDBLCLICK:
				case Event.ONMOUSEOVER:
				case Event.ONMOUSEDOWN:
				case Event.ONMOUSEUP:
				case Event.ONMOUSEMOVE:
				case Event.ONMOUSEOUT:
					NativeEvent nativeEvent = previewEvent.getNativeEvent();
					int x = nativeEvent.getClientX() - getAbsoluteLeft() - 1;
					int y = nativeEvent.getClientY() - getAbsoluteTop() - 1;
					if (x < coordX && y < coordY) {
						nativeListener.firedEvent(x, y, previewEvent.getTypeInt());
					}
					break;
				}
				previewEvent.cancel();
			} else {
				switch (previewEvent.getTypeInt()) {
				case Event.ONMOUSEMOVE:
				case Event.ONMOUSEOVER:
					nativeListener.firedEvent(0, 0, Event.ONMOUSEOUT);
				}
			}
		}
	}

}
