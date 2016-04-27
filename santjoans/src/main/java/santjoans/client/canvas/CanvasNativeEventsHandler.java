package santjoans.client.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;

public class CanvasNativeEventsHandler implements Event.NativePreviewHandler {
	
	private Canvas canvas;
	private ICanvasEventEnabledListener canvasEventEnabledListener;
	
	public CanvasNativeEventsHandler(Canvas canvas, ICanvasEventEnabledListener canvasEventEnabledListener) {
		this.canvas = canvas;
		this.canvasEventEnabledListener = canvasEventEnabledListener;
	}
	
	@Override
	public void onPreviewNativeEvent(NativePreviewEvent previewEvent) {
		
		NativeEvent event = previewEvent.getNativeEvent();
		Element element = event.getEventTarget().cast();
		
		if (element == canvas.getCanvasElement()) {
			switch (previewEvent.getTypeInt()) {
			case Event.ONDBLCLICK:
			case Event.ONMOUSEOVER:
			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOUT:
				NativeEvent nativeEvent = previewEvent.getNativeEvent();
				int x = nativeEvent.getClientX() - canvas.getAbsoluteLeft() - 1;
				int y = nativeEvent.getClientY() - canvas.getAbsoluteTop() - 1;
				if (x < canvas.getCanvasElement().getClientWidth() && y < canvas.getCanvasElement().getClientHeight()) {
					canvasEventEnabledListener.fireEvent(x, y, previewEvent.getTypeInt());
				}
				break;
			case Event.ONTOUCHSTART:
			case Event.ONTOUCHMOVE:
				NativeEvent nativeTouchEvent = previewEvent.getNativeEvent();
				Touch touch = nativeTouchEvent.getTargetTouches().get(0);
				if (touch != null) {
					int tx = touch.getClientX() - canvas.getAbsoluteLeft() - 1;
					int ty = touch.getClientY() - canvas.getAbsoluteTop() - 1;
					if (tx < canvas.getCanvasElement().getClientWidth() && ty < canvas.getCanvasElement().getClientHeight()) {
						canvasEventEnabledListener.fireTouchEvent(tx, ty, previewEvent.getTypeInt());
					}
				}
				break;
			case Event.ONTOUCHEND:
			case Event.ONTOUCHCANCEL:
				canvasEventEnabledListener.fireTouchEvent(0, 0, previewEvent.getTypeInt());
			}
			previewEvent.cancel();
		} else {
			switch (previewEvent.getTypeInt()) {
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOVER:
				canvasEventEnabledListener.fireEvent(0, 0, Event.ONMOUSEOUT);
			}
		}
	}
}

