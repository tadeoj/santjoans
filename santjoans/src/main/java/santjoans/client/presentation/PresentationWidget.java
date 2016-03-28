package santjoans.client.presentation;

import santjoans.client.presentation.imageviewer.ImageViewerTool;
import santjoans.client.resources.ISantjoansResources;
import santjoans.client.transaction.sync.IPiezeLoaderListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PresentationWidget extends Composite implements ClickHandler, IPiezeLoaderListener {
	
	// Lo que ejecutara el boton de acceso
	private Runnable runnable;

	private static PresentationWidgetUiBinder uiBinder = GWT.create(PresentationWidgetUiBinder.class);

	interface PresentationWidgetUiBinder extends
			UiBinder<Widget, PresentationWidget> {
	}

	@UiField
	Image palacio;
	
	@UiField
	Image piezas;
	
	@UiField
	Image museo;
	
	@UiField
	Anchor escudos;
	
	@UiField
	Anchor escudotecho;
	
	@UiField
	Anchor indio;
	
	@UiField
	Button verMural;
	
	@UiField
	Label estadoCarga;
	
	public PresentationWidget(Runnable switchToViewer) {
		this.runnable = switchToViewer;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void install() {
		palacio.setUrl(ISantjoansResources.INSTANCE.palacioSmall().getURL());
		piezas.setUrl(ISantjoansResources.INSTANCE.piezasSmall().getURL());
		museo.setUrl(ISantjoansResources.INSTANCE.museoSmall().getURL());
		
		palacio.addClickHandler(new MouseClickHandlerImpl());
		piezas.addClickHandler(new MouseClickHandlerImpl());
		museo.addClickHandler(new MouseClickHandlerImpl());
		escudos.addClickHandler(new MouseClickHandlerImpl());
		escudotecho.addClickHandler(new MouseClickHandlerImpl());
		indio.addClickHandler(new MouseClickHandlerImpl());
		
		verMural.addClickHandler(this);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		runnable.run();
	}
	
	private class MouseClickHandlerImpl implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
	        if (event.getSource() == museo) {
	        	ImageViewerTool.displayLandscape(ISantjoansResources.INSTANCE.museoLong());
	        } else if (event.getSource() == palacio) {
	        	ImageViewerTool.displayLandscape(ISantjoansResources.INSTANCE.palacioLong());
	        } else if (event.getSource() == piezas) {
	        	ImageViewerTool.displayPortrait(ISantjoansResources.INSTANCE.piezasLong());
	        } else if (event.getSource() == escudotecho) {
	        	ImageViewerTool.displayPortrait(ISantjoansResources.INSTANCE.escudo0Long());
	        } else if (event.getSource() == indio) {
	        	ImageViewerTool.displayPortrait(ISantjoansResources.INSTANCE.indio());
	        } else if (event.getSource() == escudos) {
	        	ImageViewerTool.displayFourImages(new ImageResource[] {
	        			ISantjoansResources.INSTANCE.escudo1Long(),
	        			ISantjoansResources.INSTANCE.escudo2Long(),
	        			ISantjoansResources.INSTANCE.escudo3Long(),
	        			ISantjoansResources.INSTANCE.escudo4Long()
	        	});
	        }
		}
	}

	@Override
	public void piezesForLoad(int pending) {
		if (pending == 0) {
			estadoCarga.setVisible(false);
			verMural.setVisible(true);
		} else {
			estadoCarga.setText("Quedan " + pending + " piezas por cargar.");
		}
	}
	
}
