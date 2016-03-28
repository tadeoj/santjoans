package santjoans.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface ISantjoansResources extends ClientBundle {
	
	public static final ISantjoansResources INSTANCE =  GWT.create(ISantjoansResources.class);
	
	@Source("piezes.xml")
	TextResource piezes();

	@Source("escudo0_long.png")
	ImageResource escudo0Long();
		  
	@Source("escudo1_long.png")
	ImageResource escudo1Long();
		  
	@Source("escudo2_long.png")
	ImageResource escudo2Long();
		  
	@Source("escudo3_long.png")
	ImageResource escudo3Long();
	
	@Source("escudo4_long.png")
	ImageResource escudo4Long();
	
	@Source("ceramica_small.png")
	ImageResource ceramicaSmall();
		  
	@Source("ceramica_long.png")
	ImageResource ceramicaLong();
		  
	@Source("museo_small.png")
	ImageResource museoSmall();
		  
	@Source("museo_long.png")
	ImageResource museoLong();
		  
	@Source("palacio_small.png")
	ImageResource palacioSmall();
		  
	@Source("palacio_long.png")
	ImageResource palacioLong();

	@Source("indio.png")
	ImageResource indio();

	@Source("piezas_small.png")
	ImageResource piezasSmall();
		  
	@Source("piezas_long.png")
	ImageResource piezasLong();

	@Source("up-icon.png")
	ImageResource upIcon();
	
	@Source("down-icon.png")
	ImageResource downIcon();
	
	@Source("left-icon.png")
	ImageResource leftIcon();
	
	@Source("right-icon.png")
	ImageResource rightIcon();
	
	@Source("zoom-in-icon.png")
	ImageResource zoomInIcon();
	
	@Source("zoom-out-icon.png")
	ImageResource zoomOutIcon();
	
	@Source("home-icon.png")
	ImageResource homeIcon();
		  
	@Source("help-icon.png")
	ImageResource helpIcon();

	@Source("reset-icon.png")
	ImageResource resetIcon();
		  
}