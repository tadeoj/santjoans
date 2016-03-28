package santjoans.client.model;

import santjoans.client.util.PiezePixelsEnum;

import com.google.gwt.dom.client.ImageElement;

public interface IPieze {
	public String getName();
	public int getX();
	public int getY();
	public int getMiniatureRotation();
	public int getDetailRotation();
	public double getMiniatureRadians();
	public double getDetailRadians();
	public ImageElement getImageElement(PiezePixelsEnum piezePixelsEnum);
	public String getImageDetailedUrl();
}
