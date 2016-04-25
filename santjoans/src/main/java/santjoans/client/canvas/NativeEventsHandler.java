package santjoans.client.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;

public class NativeEventsHandler implements Event.NativePreviewHandler {
	
	private Canvas canvas;
	private ICanvasEventEnabledListener canvasEventEnabledListener;
	
	public NativeEventsHandler(Canvas canvas, ICanvasEventEnabledListener canvasEventEnabledListener) {
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
			case Event.ONTOUCHSTART:
			case Event.ONMOUSEOVER:
			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOUT:
				NativeEvent nativeEvent = previewEvent.getNativeEvent();
				int x = nativeEvent.getClientX() - canvas.getAbsoluteLeft() - 1;
				int y = nativeEvent.getClientY() - canvas.getAbsoluteTop() - 1;
				if (x < event.getClientX() && y < event.getClientY()) {
					canvasEventEnabledListener.firedEvent(x, y, previewEvent.getTypeInt());
				}
				break;
			}
			previewEvent.cancel();
		} else {
			switch (previewEvent.getTypeInt()) {
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOVER:
				canvasEventEnabledListener.firedEvent(0, 0, Event.ONMOUSEOUT);
			}
		}
	}
}

