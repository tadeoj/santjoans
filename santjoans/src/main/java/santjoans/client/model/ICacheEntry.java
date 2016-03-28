package santjoans.client.model;

import java.util.List;

import santjoans.client.util.PiezePixelsEnum;

import com.google.gwt.dom.client.ImageElement;

public interface ICacheEntry {
	public String getImageName();
	public ImageElement getImageElement(PiezePixelsEnum piezePixelsEnum);
	public void setImageElement(PiezePixelsEnum piezePixelsEnum, ImageElement imageElement);
	public String getImageUrl(PiezePixelsEnum piezePixelsEnum);
	public void addReference(IModelEntry modelEntry);
	public List<IModelEntry> getReferences();
}
