package santjoans.client.transaction;

import com.google.gwt.dom.client.ImageElement;

public interface ITransaction {

	public void updateView();
	public String[] getImageUrls();
	public void updateFromCallbackResult(ImageElement[] imageElements);
	public long getId();
	public int getPiezes();
	
}
