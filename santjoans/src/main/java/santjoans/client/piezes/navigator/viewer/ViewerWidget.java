package santjoans.client.piezes.navigator.viewer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import santjoans.client.canvas.GWTCanvasEventEnabled;
import santjoans.client.piezes.view.CenterView;
import santjoans.client.piezes.view.IView;
import santjoans.client.piezes.view.MainView;
import santjoans.client.transaction.async.TaskManager;
import santjoans.client.transaction.sync.SyncPiezeLoader;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.Util;
import santjoans.client.util.ZoomModeEnum;

abstract public class ViewerWidget extends Composite implements IConfiguration {

	private static PiezeViewerWidgetUiBinder uiBinder = GWT.create(PiezeViewerWidgetUiBinder.class);

	interface PiezeViewerWidgetUiBinder extends UiBinder<Widget, ViewerWidget> {
	}

	@UiField
	GWTCanvasEventEnabled gwtCanvas;

	protected ControllerViewer controllerViewer;
	protected IControllerViewerCommands piezeViewerCommands;

	public ViewerWidget() {

		initWidget(uiBinder.createAndBindUi(this));

		// Se le asigna un color de background
		// gwtCanvas.setBackgroundColor(Color.GREY);
		
		// Se crean una lista con las vistas
		// El orden es importante ya que interesa que las piezas centrales
		// se pinten despues que las otras para tapar los cuadrados de las
		// piezas
		// an cartabon que estan en la periferia del conjunto central.
		List<IView> views = new ArrayList<IView>();
		views.add(new MainView(gwtCanvas));
		views.add(new CenterView(gwtCanvas));

		// Se crea el controlador de las vistas
		controllerViewer = new ControllerViewer(gwtCanvas, views);

		// Se preparan el onjeto con las invocaciones asincronas para el viewer
		piezeViewerCommands = new ViewerCommandsImpl();

	}

	@UiFactory
	GWTCanvasEventEnabled instantiateGWTCanvas() {
		return new GWTCanvasEventEnabled(Util.getCurrentScreenType().getCanvasX(),
				Util.getCurrentScreenType().getCanvasY());
	}

	public IControllerViewerCommands getPiezeViewerCommands() {
		return piezeViewerCommands;
	}

	public void addPiezeViewerListener(IControllerViewerListener piezeViewerListener) {
		controllerViewer.addListener(piezeViewerListener);
	}

	public SyncPiezeLoader firstLoad() {
		return controllerViewer.firstLoad();
	}

	private class ViewerCommandsImpl implements IControllerViewerCommands {

		@Override
		public void moveDown() {
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					TaskManager.execute(controllerViewer.moveDown());
				}
			});
		}

		@Override
		public void moveLeft() {
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					TaskManager.execute(controllerViewer.moveLeft());
				}
			});
		}

		@Override
		public void moveRight() {
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					TaskManager.execute(controllerViewer.moveRight());
				}
			});
		}

		@Override
		public void moveUp() {
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					TaskManager.execute(controllerViewer.moveUp());
				}
			});
		}

		@Override
		public void setZoom(final ZoomModeEnum zoomModeEnum) {
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					TaskManager.execute(controllerViewer.setZoom(zoomModeEnum));
				}
			});
		}

		@Override
		public void setPosition(final int startX, final int startY) {
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					TaskManager.execute(controllerViewer.setPosition(startX, startY));
				}
			});
		}

	}

}
