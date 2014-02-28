package santjoans.client.piezes.navigator.viewer;

import santjoans.client.util.ZoomModeEnum;

public interface IControllerViewerCommands {
	public void moveLeft();
	public void moveRight();
	public void moveUp();
	public void moveDown();
	public void setZoom(ZoomModeEnum zoomModeEnum);
	public void setPosition(int startX, int startY);
}
