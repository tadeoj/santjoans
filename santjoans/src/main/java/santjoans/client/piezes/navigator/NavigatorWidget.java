package santjoans.client.piezes.navigator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import santjoans.client.imagebutton.IButtonPressed;
import santjoans.client.imagebutton.ImageButton;
import santjoans.client.model.ModelLoader;
import santjoans.client.piezes.navigator.direction.DirectionWidget;
import santjoans.client.piezes.navigator.help.NavigationHelp;
import santjoans.client.piezes.navigator.preview.PreviewWidget;
import santjoans.client.piezes.navigator.preview.PreviewWidgetControlImpl;
import santjoans.client.piezes.navigator.viewer.ViewerWidget;
import santjoans.client.piezes.navigator.viewer.ViewerWidgetControl;
import santjoans.client.piezes.navigator.zoom.ZoomWidget;
import santjoans.client.resources.ISantjoansResources;
import santjoans.client.transaction.sync.IPiezeLoaderListener;
import santjoans.client.transaction.sync.SyncPiezeLoader;

public class NavigatorWidget extends Composite implements IButtonPressed {

	private static SantjoansWindgetUiBinder uiBinder = GWT.create(SantjoansWindgetUiBinder.class);

	interface SantjoansWindgetUiBinder extends
			UiBinder<Widget, NavigatorWidget> {
	}
	
	@UiField(provided=true)
	ViewerWidget viewer;
	
	@UiField(provided=true)
	DirectionWidget directionWidget;
	
	@UiField(provided=true)
	PreviewWidget previewWidget;

	@UiField(provided=true)
	ZoomWidget zoomWidget;

	@UiField(provided=true)
	ImageButton helpImageButton;
	
	private NavigationHelp navigationHelp = new NavigationHelp();

	public NavigatorWidget(final IPiezeLoaderListener listener) {
		
		// Se el widget que se encarga de visualizar las piezas
		viewer = new ViewerWidgetControl();
		
		// Controles de direccion.
		directionWidget = new DirectionWidget(viewer.getPiezeViewerCommands());
		
		// Se crea el widget encargado de mostrar el preview
		previewWidget = new PreviewWidgetControlImpl(viewer.getPiezeViewerCommands(), false);
		
		// Controles de zoom.
		zoomWidget = new ZoomWidget(viewer.getPiezeViewerCommands());
		
		// Help
		helpImageButton = new ImageButton(ISantjoansResources.INSTANCE.helpIcon());
		helpImageButton.setButtonPressedListener(this);
		
		// Todos escuchan los cambiosn el Viewer
		viewer.addPiezeViewerListener(previewWidget);
		viewer.addPiezeViewerListener(directionWidget);
		viewer.addPiezeViewerListener(zoomWidget);
		
		// Se crea el widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Se carga el modelo y una vez terminada esta operacion se cargara al ZOOM 100%
		ModelLoader modelLoader = new ModelLoader() {
			@Override
			protected void modelLoaded() {
				SyncPiezeLoader syncPiezeLoader = viewer.firstLoad();
				syncPiezeLoader.execute(listener);
			}
		};
		modelLoader.loadModels();
		
	}

	@Override
	public void buttonPressed(Object source) {
		final PopupPanel navigationHelpPopup = new PopupPanel(true);
		navigationHelpPopup.setWidget(navigationHelp);
		navigationHelpPopup.setGlassEnabled(true);
		navigationHelpPopup.setAnimationEnabled(true);
		navigationHelpPopup.center();
	}
	
}
