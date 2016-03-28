package santjoans.client.piezes.navigator.viewer;

import santjoans.client.util.IConfiguration;
import santjoans.client.util.ZoomModeEnum;

public class MovePiezeContext implements IConfiguration {
	
	private ZoomModeEnum zoomMode;
	
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	public MovePiezeContext(IControllerViewerContext viewerContext) {
		this(viewerContext.getZoomMode(), viewerContext.getStartX(), viewerContext.getStartY());
	}
	
	public MovePiezeContext(ZoomModeEnum zoomMode, int startX, int startY) {
		this.zoomMode = zoomMode;
		
		this.startX = startX;
		this.startY = startY;
		this.endX = zoomMode.getEndX(this.startX);
		this.endY = zoomMode.getEndY(this.startY);
		
	}
	
	public ZoomModeEnum getZoomMode() {
		return zoomMode;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[");
		buffer.append("startX:" + getStartX());
		buffer.append(",startY:" + getStartY());
		buffer.append(",endX:" + getEndX());
		buffer.append(",endY:" + getEndY());
		buffer.append("]");

		return buffer.toString();
	}
	
}
