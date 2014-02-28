package santjoans.client.piezes.navigator.viewer;

import santjoans.client.util.ZoomModeEnum;

public interface IControllerViewerContext {

	public ZoomModeEnum getZoomMode();
	
	public int getStartX();
	public int getStartY();
	public int getEndX();
	public int getEndY();
	
	public boolean canMoveLeft();
	public boolean canMoveRight();
	public boolean canMoveUp();
	public boolean canMoveDown();
	
}
