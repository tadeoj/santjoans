package santjoans.client.piezes.view;

import santjoans.client.model.IPieze;
import santjoans.client.model.Model;
import santjoans.client.util.PiezePixelsEnum;
import santjoans.client.util.ZoomModeEnum;

public interface IView {
	public void updateFromModel(PiezePixelsEnum piezePixelsEnum, int startX, int startY, int stopX, int stopY);
	public void drawPieze(PiezePixelsEnum piezePixelsEnum, int startX, int startY, IPieze pieze);
	public IPieze getPickedPiezeUrl(ZoomModeEnum zoomModeEnum, int startX, int startY, int xPixel, int yPixel);
	public Model getModel();
}