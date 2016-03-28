package santjoans.client.presentation.imageviewer;

import santjoans.client.util.Util;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.reveregroup.gwt.imagepreloader.FitImage;
import com.reveregroup.gwt.imagepreloader.FitImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.FitImageLoadHandler;

public class ImageViewerTool {

	public static void setBusyCursor() {
		Element element = RootPanel.getBodyElement();
		DOM.setStyleAttribute(element, "cursor", "wait");
	}
	
	public static void setDefaultCursor() {
		Element element = RootPanel.getBodyElement();
		DOM.setStyleAttribute(element, "cursor", "default");
	}

	static private void loadOneImage(ImageResource imageResource, final IDisplayOneImage displayImage) {
		setBusyCursor();
		FitImage fitImage = new FitImage();
		fitImage.addFitImageLoadHandler(new FitImageLoadHandler() {
			@Override
			public void imageLoaded(FitImageLoadEvent event) {
		        if (event.isLoadFailed()) {
		        	// No hacemos nada
		        } else {
		        	// Se muestra la imagen.
		        	setDefaultCursor();
		        	displayImage.display(event.getFitImage());
		        }
			}
		});
		fitImage.setUrl(imageResource.getURL());
	}
	
	static private class DisplayLandscape implements IDisplayOneImage {
		@Override
		public void display(FitImage fitImage) {
			// Ajustamos el tamañño de la imagen
			fitImage.setMaxWidth(Util.getCurrentScreenType().getImageViewerToolMaxWidthLandscape());
			// Instanciamos el widget
			OneImageViewerWidget imageViewerWidget = new OneImageViewerWidget(fitImage);
	    	// Se inicializa el panel.
			PopupPanel popupPanel = new PopupPanel(true);
			popupPanel.setGlassEnabled(true);
			popupPanel.setAnimationEnabled(true);

			popupPanel.setWidget(imageViewerWidget);
			// Se visualiza centrado,
			popupPanel.center();
		}
	}
	
	static public void displayLandscape(final ImageResource imageResource) {
		loadOneImage(imageResource, new DisplayLandscape());
	}
	
	static private class DisplayPortrait implements IDisplayOneImage {
		@Override
		public void display(FitImage fitImage) {
			// Ajustamos el tamañño de la imagen
			fitImage.setMaxHeight(Util.getCurrentScreenType().getImageViewerToolMaxWidthPortrait());
			// Instanciamos el widget
			OneImageViewerWidget imageViewerWidget = new OneImageViewerWidget(fitImage);
	    	// Se inicializa el panel.
			PopupPanel popupPanel = new PopupPanel(true);
			popupPanel.setGlassEnabled(true);
			popupPanel.setAnimationEnabled(true);
			popupPanel.setWidget(imageViewerWidget);
			// Se visualiza centrado,
			popupPanel.center();
		}
	}
	
	static public void displayPortrait(final ImageResource imageResource) {
		loadOneImage(imageResource, new DisplayPortrait());
	}

	/////////////////////////////////////////////////////////
	

	static private void loadMultiImage(final ImageResource[] imageResources, final IDisplayThreeImage displayImages) {
		setBusyCursor();
		final Counter counter = new Counter();
		final FitImage[] fitImages = new FitImage[imageResources.length];
		for (int i = 0; i < imageResources.length; i++) {
			fitImages[i] = new FitImage(new FitImageLoadHandler() {
				@Override
				public void imageLoaded(FitImageLoadEvent event) {
			        if (event.isLoadFailed()) {
			        	// No hacemos nada
			        } else {
			        	// Se muestra la imagen.
			        	event.getFitImage().setMaxWidth(Util.getCurrentScreenType().getImageViewerToolMaxWidthMultiImage());
			        	counter.increment();
			        	if (counter.isFinished(imageResources.length)) {
				        	setDefaultCursor();
			        		displayImages.display(fitImages);
			        	}
			        }
				}
			});
			
		}
		for (int i = 0; i < imageResources.length; i++) {
			fitImages[i].setUrl(imageResources[i].getURL());
		}
	}
	
	
	static private class DisplayFourImages implements IDisplayThreeImage {
		@Override
		public void display(FitImage[] fitImages) {
			// Instanciamos el widget
			FourImageViewerWidget imageViewerWidget = new FourImageViewerWidget(fitImages);
	    	// Se inicializa el panel.
			PopupPanel popupPanel = new PopupPanel(true);
			popupPanel.setGlassEnabled(true);
			popupPanel.setAnimationEnabled(true);
			popupPanel.setWidget(imageViewerWidget);
			// Se visualiza centrado,
			popupPanel.center();
		}
	}
	
	static public void displayFourImages(ImageResource[] imageResources) {
		loadMultiImage(imageResources, new DisplayFourImages());
	}
	
	static private class Counter {
		
		int counter = 0;
		
		public void increment() {
			counter++;
		}
		
		public boolean isFinished(int maxValue) {
			return counter >= maxValue;
		}
	}
	
}
