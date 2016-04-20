package santjoans.client.piezes.navigator.viewer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;

import santjoans.client.model.IPieze;
import santjoans.client.piezes.view.IView;
import santjoans.client.transaction.TransactionFactory;
import santjoans.client.transaction.async.TaskChain;
import santjoans.client.transaction.sync.SyncPiezeLoader;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.ZoomModeEnum;

public class ControllerViewer implements IConfiguration {
	
	private Canvas canvas;
	private ZoomModeEnum zoomModeEnum;
	
	private List<IView> views;
	
	protected int startX = 0;
	protected int startY = 0;
	
	private ControllerViewerContextImpl context;
	
	private List<IControllerViewerListener> listeners;
	
	public ControllerViewer(Canvas canvas, List<IView> views) {
		this.canvas = canvas;
		this.views = views;
		this.context = new ControllerViewerContextImpl();
		this.listeners = new ArrayList<IControllerViewerListener>();
	}
	
	public void addListener(IControllerViewerListener piezeViewerListener) {
		listeners.add(piezeViewerListener);
	}
	
	public void firePiezeViewerListener() {
		for (IControllerViewerListener listener : listeners) {
			listener.updatedControllerViewer(context);
		}
	}
	
	public IControllerViewerContext getContext() {
		return context;
	}

	public SyncPiezeLoader firstLoad() {
		// Se ejecuta el ZOOM al 100%
		doSetZoom(ZoomModeEnum.MODE_100);
		// Se preparan las transacciones sincronas
		SyncPiezeLoader syncPiezeLoader = new SyncPiezeLoader();
		// Se incorporan las transacciones de las piecas main y center
		for (IView view : views) {
			syncPiezeLoader.addTransactions(TransactionFactory.getTransactionList(view, context));
		}
		return syncPiezeLoader;
	}
	
	public TaskChain setZoom(ZoomModeEnum zoomModeEnum) {
		// Se ejecuta el zoom
		doSetZoom(zoomModeEnum);
		// Se preparan las transacciones asincronas
		TaskChain taskChain = new TaskChain();
		for (IView view : views) {
			taskChain.addHeadLink(TransactionFactory.getTaskLink(view, context));
		}
		return taskChain;
	}
	
	public void doSetZoom(ZoomModeEnum zoomModeEnum) {
		ZoomModeEnum oldZoomModeEnum = this.zoomModeEnum == null ? ZoomModeEnum.MODE_100 : this.zoomModeEnum;
		// Se asigna nuevo modo de zoom.
		this.zoomModeEnum = zoomModeEnum;
		// Se modifica el sistema de coordenadas en funcion del modo de zoom.
		canvas.setCoordinateSpaceWidth(this.zoomModeEnum.getMillimetersWidth());
		canvas.setCoordinateSpaceHeight(this.zoomModeEnum.getMillimetersHeight());
		// Se ajustan las coordinadas de inicio al modo de zoom actual.
		int xDiff = oldZoomModeEnum.getUnitWidth() - zoomModeEnum.getUnitWidth();
		int yDiff = oldZoomModeEnum.getUnitHeight() - zoomModeEnum.getUnitHeight();
		// Si las nuevas coordenadas no son correctas las ajustamos.
		doSetPosition(adjustX(startX + xDiff / 2), adjustY(startY + yDiff / 2));
	}
	
	public TaskChain setPosition(int newStartX, int newStartY) {
		// Se cambia a la nueva posicion.
		doSetPosition(newStartX, newStartY);
		// Se preparan las transacciones asincronas
		TaskChain taskChain = new TaskChain();
		for (IView view : views) {
			taskChain.addHeadLink(TransactionFactory.getTaskLink(view, context));
		}
		return taskChain;
	}
	
	public void doSetPosition(int newStartX, int newStartY) {
		startX = adjustX(newStartX);
		startY = adjustY(newStartY);
		// Se actualiza el preview
		firePiezeViewerListener();
		// Las vistas tiene que actualizar su informacion desde su modelo
		for (IView view : views)
			view.updateFromModel(zoomModeEnum.getPiezePixels(), startX, startY, zoomModeEnum.getEndX(startX), zoomModeEnum.getEndY(startY));
	}
	
	private int adjustY(int newStartY) {
		if (newStartY < 0)
			newStartY = 0;
		int minusY = zoomModeEnum.getEndY(newStartY) - MODEL_MAIN_MAX_COORD_Y;
		return minusY > 0 ? newStartY - minusY : newStartY;
	}
	
	private int adjustX(int newStartX) {
		if (newStartX < 0) 
			newStartX = 0;
		int minusX = zoomModeEnum.getEndX(newStartX) - MODEL_MAIN_MAX_COORD_X;
		return minusX > 0 ? newStartX - minusX : newStartX;
	}
	
	public TaskChain moveLeft() {
		TaskChain taskChain = new TaskChain();
		// Calculamos la nueva posicion desplazandonos a la izquierda. 
		int newStartX = startX - zoomModeEnum.getSteepX();
		// Si la nueva posicion esta fuera de limites se trunca a la posicion valida.
		if (newStartX < 0)
			newStartX = 0;
		// Si despues de los ajustes la posicion es distinta a la original ejecutamos los cambios.
		if (startX != newStartX) {
			// Consolidamos la nueva posicion
			startX = newStartX;
			// Se actualiza el preview
			firePiezeViewerListener();
			// Se actualiza la vista con las piezas disponibles
			for (IView view : views)
				view.updateFromModel(zoomModeEnum.getPiezePixels(), startX, startY, zoomModeEnum.getEndX(startX), zoomModeEnum.getEndY(startY));
			// Se preparan las transacciones para obtener las piezas que faltan para completar la vista.
			for (IView view : views) {
				taskChain.addHeadLink(TransactionFactory.getTaskLink(view, context));
			}
			return taskChain;
		} else {
			// Se retorna un controlador de transacciones vacio.
			return taskChain;
		}
	}

	public boolean canMoveLeft() {
		if (startX == 0)
			return false;
		else
			return true;
	}
	
	public TaskChain moveRight() {
		TaskChain taskChain = new TaskChain();
		int newStartX = startX + zoomModeEnum.getSteepX();
		if ((newStartX + zoomModeEnum.getUnitWidth()) > MODEL_MAIN_MAX_COORD_X) {
			newStartX = MODEL_MAIN_MAX_COORD_X - zoomModeEnum.getUnitWidth();
		}
		if (startX != newStartX) {
			startX = newStartX;
			// Se actualiza el preview
			firePiezeViewerListener();
			// Se actualiza la vista con las piezas disponibles
			for (IView view : views)
				view.updateFromModel(zoomModeEnum.getPiezePixels(), startX, startY, zoomModeEnum.getEndX(startX), zoomModeEnum.getEndY(startY));
			// Las vistas preparan las transacciones para cargar las nuevas imagenes.
			for (IView view : views) {
				taskChain.addHeadLink(TransactionFactory.getTaskLink(view, context));
			}
			return taskChain;
		} else {
			return taskChain;
		}
	}

	public boolean canMoveRight() {
		if (startX + zoomModeEnum.getUnitWidth() >= MODEL_MAIN_MAX_COORD_X)
			return false;
		else
			return true;
	}
	
	public TaskChain moveUp() {
		TaskChain taskChain = new TaskChain();
		int newStartY = startY - zoomModeEnum.getSteepY();
		if (newStartY < 0)
			newStartY = 0;
		if (startY != newStartY) {
			startY = newStartY;
			// Se actualiza el preview
			firePiezeViewerListener();
			// Se actualiza la vista con las piezas disponibles
			for (IView view : views)
				view.updateFromModel(zoomModeEnum.getPiezePixels(), startX, startY, zoomModeEnum.getEndX(startX), zoomModeEnum.getEndY(startY));
			// Las vistas preparan las transacciones para cargar las nuevas imagenes.
			for (IView view : views) {
				taskChain.addHeadLink(TransactionFactory.getTaskLink(view, context));
			}
			return taskChain;
		} else {
			return taskChain;
		}
	}

	public boolean canMoveUp() {
		if (startY == 0)
			return false;
		else
			return true;
	}
	
	public TaskChain moveDown() {
		TaskChain taskChain = new TaskChain();
		int newStartY = startY + zoomModeEnum.getSteepY();
		if ((newStartY + zoomModeEnum.getUnitHeight()) > MODEL_MAIN_MAX_COORD_Y) {
			newStartY = MODEL_MAIN_MAX_COORD_Y - zoomModeEnum.getUnitHeight();
		}
		if (startY != newStartY) {
			startY = newStartY;
			// Se actualiza el preview
			firePiezeViewerListener();
			// Se actualiza la vista con las piezas disponibles
			for (IView view : views)
				view.updateFromModel(zoomModeEnum.getPiezePixels(), startX, startY, zoomModeEnum.getEndX(startX), zoomModeEnum.getEndY(startY));
			// Las vistas preparan las transacciones para cargar las nuevas imagenes.
			for (IView view : views) {
				taskChain.addHeadLink(TransactionFactory.getTaskLink(view, context));
			}
			return taskChain;
		} else {
			return taskChain;
		}
	}
	
	public boolean canMoveDown() {
		if (startY + zoomModeEnum.getUnitHeight() >= MODEL_MAIN_MAX_COORD_Y)
			return false;
		else
			return true;
	}
	
	public IPieze getPickedPieze(int xPixel, int yPixel) {
		for (IView view : views) {
			IPieze pieze = view.getPickedPiezeUrl(zoomModeEnum, startX, startY, xPixel, yPixel);
			if (pieze != null)
				return pieze;
		}
		return null;
	}

	
	private class ControllerViewerContextImpl implements IControllerViewerContext {

		@Override
		public int getStartX() {
			return startX;
		}

		@Override
		public int getStartY() {
			return startY;
		}

		@Override
		public int getEndX() {
			return getZoomMode().getEndX(getStartX());
		}

		@Override
		public int getEndY() {
			return getZoomMode().getEndY(getStartY());
		}
		
		@Override
		public ZoomModeEnum getZoomMode() {
			return zoomModeEnum;
		}

		@Override
		public boolean canMoveDown() {
			return ControllerViewer.this.canMoveDown();
		}

		@Override
		public boolean canMoveLeft() {
			return ControllerViewer.this.canMoveLeft();
		}

		@Override
		public boolean canMoveRight() {
			return ControllerViewer.this.canMoveRight();
		}

		@Override
		public boolean canMoveUp() {
			return ControllerViewer.this.canMoveUp();
		}

	}

	protected void log(String msg) {
		GWT.log("ControllerViewer: " + msg, null);
	}
	
}
