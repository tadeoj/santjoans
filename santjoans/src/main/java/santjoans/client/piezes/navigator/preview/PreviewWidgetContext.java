package santjoans.client.piezes.navigator.preview;

import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.ZoomModeEnum;

public class PreviewWidgetContext implements IConfiguration {
	
	private ZoomModeEnum zoomMode;
	
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	private double rectX;
	private double rectY;
	private double rectWidth;
	private double rectHeight;
	
	private int viewStartX;
	private int viewEndX;
	private int viewStartY;
	private int viewEndY;
	
	public PreviewWidgetContext(IControllerViewerContext viewerContext) {
		this(viewerContext.getZoomMode(), viewerContext.getStartX(), viewerContext.getStartY());
	}
	
	public PreviewWidgetContext(ZoomModeEnum zoomMode, int startX, int startY) {
		this.zoomMode = zoomMode;
		
		this.startX = startX;
		this.startY = startY;
		this.endX = zoomMode.getEndX(this.startX);
		this.endY = zoomMode.getEndY(this.startY);
		
		this.rectX = millimetersToPixelsX(startX * PIEZE_MAIN_HALF_DIAGONAL);
		this.rectY = millimetersToPixelsY(startY * PIEZE_MAIN_HALF_DIAGONAL);
		this.rectWidth = millimetersToPixelsX((endX - startX) * PIEZE_MAIN_HALF_DIAGONAL);
		this.rectHeight = millimetersToPixelsY((endY - startY) * PIEZE_MAIN_HALF_DIAGONAL);
		
		this.viewStartX = (int) this.rectX;
		this.viewEndX = (int) (this.rectX + this.rectWidth);
		this.viewStartY = (int) this.rectY;
		this.viewEndY = (int) (this.rectY + this.rectHeight);
	}
	
	private double millimetersToPixelsX(double millimeters) {
		return ((millimeters * PREVIEW_X) / ZoomModeEnum.MODE_100.getMillimetersWidth());
	}
	
	private double millimetersToPixelsY(double millimeters) {
		return ((millimeters * PREVIEW_Y) / ZoomModeEnum.MODE_100.getMillimetersHeight());
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

	public double getRectX() {
		return rectX;
	}

	public double getRectY() {
		return rectY;
	}

	public double getRectWidth() {
		return rectWidth;
	}

	public double getRectHeight() {
		return rectHeight;
	}

	public int getViewStartX() {
		return viewStartX;
	}

	public int getViewStartY() {
		return viewStartY;
	}

	public int getViewEndX() {
		return viewEndX;
	}

	public int getViewEndY() {
		return viewEndY;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[");
		buffer.append("startX:" + getStartX());
		buffer.append(",startY:" + getStartY());
		buffer.append(",endX:" + getEndX());
		buffer.append(",endY:" + getEndY());
		buffer.append("]");

		buffer.append("[");
		buffer.append("rectX:" + (int) getRectX());
		buffer.append(",rectY:" + (int) getRectY());
		buffer.append(",rectWidth:" + (int) getRectWidth());
		buffer.append(",rectHeight:" + (int) getRectHeight());
		buffer.append("]");
		
		buffer.append("[");
		buffer.append("viewStartX:" + getViewStartX());
		buffer.append(",viewStartY:" + getViewStartY());
		buffer.append(",viewEndX:" + getViewEndX());
		buffer.append(",viewEndY:" + getViewEndY());
		buffer.append("]");
		
		return buffer.toString();
	}
	
}
