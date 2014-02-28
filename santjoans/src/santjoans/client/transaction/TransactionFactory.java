package santjoans.client.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import santjoans.client.model.ICacheEntry;
import santjoans.client.model.IModelEntry;
import santjoans.client.model.Model;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.piezes.view.IView;
import santjoans.client.transaction.async.ITaskContext;
import santjoans.client.transaction.async.ITaskLink;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

public class TransactionFactory {

	private Boolean LOG_ENABLED = false;
	
	static public List<ITransaction> getTransactionList(IView view, IControllerViewerContext controllerViewerContext) {
		
		// Lista que almacenarfa todas las transacciones
		ArrayList<ITransaction> transactions = new ArrayList<ITransaction>();
		
		// Creamos el factory
		TransactionFactory factory = new TransactionFactory(view, controllerViewerContext);
		
		// Se incluyen en la lista todas las transacciones
		ITransaction transaction;
		while(true) {
			transaction = factory.buildNextTransaction();
			if (transaction == null) {
				return transactions;
			} else {
				transactions.add(transaction);
			}
		}
	}
	
	static public ITaskLink getTaskLink(IView view, IControllerViewerContext controllerViewerContext) {
		
		// Creamos el factory
		TransactionFactory factory = new TransactionFactory(view, controllerViewerContext);
		
		// Retornamos el primer eslabon de una cadena de tareas.
		return factory.buildFirstTaskLink();
	}
	
	private IView view;
	private Model model;
	private IControllerViewerContext context;
	private Iterator<IModelEntry> modelIterator;
	private Map<String,ICacheEntry> alreadyInTransaction;
	
	public TransactionFactory(IView view, IControllerViewerContext context) {
		this.view = view;
		this.model = view.getModel();
		this.context = context;
		this.modelIterator = model.queryByContext(model.getModelEntries(), context).iterator();
		this.alreadyInTransaction = new HashMap<String, ICacheEntry>();
	}

	public ITransaction buildNextTransaction() {
		// En este vector se almacenan las entradas que compondran la proxima transaccion
		List<ICacheEntry> cacheEntries = new ArrayList<ICacheEntry>(Transaction.PIEZES_BY_TRANSACTION);
	
		// Se construye la proxima transaccion
		while(modelIterator.hasNext() && cacheEntries.size() < Transaction.PIEZES_BY_TRANSACTION) {
			// Se obtiene la siguiente entrada del modelo candidata de formar parte de una transaccion.
			IModelEntry modelEntry = modelIterator.next();
			// De la entrada del modelo obtenemos ahora la correspondiente entrada de la cache
			ICacheEntry cacheEntry = modelEntry.getCacheEntry();
			// Se comprueba si para el tamaño de imagen en curso se dispone de la imagen
			if (cacheEntry.getImageElement(context.getZoomMode().getPiezePixels()) == null) {
				// 1) La imagen no esta disponible para esta cache de la imagen.
				// 2) Como una entrada de la cache puede ser utilizada por mas de una entrada del modelo
				//    hay que verificar que esta entrada de cache no forme parte de otra transaccion anterior.
				if (alreadyInTransaction.get(cacheEntry.getImageName()) == null) {
					// Se incorpora la entrada del modelo en la tra
					cacheEntries.add(cacheEntry);
					alreadyInTransaction.put(cacheEntry.getImageName(), cacheEntry);
				}
				
			}
		}
		if (cacheEntries.size() > 0) {
			return new Transaction(view, context, cacheEntries);
		} else {
			return null;
		}
		
	}
	
	public ITaskLink buildFirstTaskLink() {
		return new TaskLinkImpl();
	}
	
	private class TaskLinkImpl implements ITaskLink {

		@Override
		public void execute(final ITaskContext taskContext) {

			final ITransaction transaction = buildNextTransaction();

			if (transaction == null) {
				// Si no hay mas transacciones, la cadena de tareas ha terminado.
				taskContext.execute(null);
			} else {
				
				if (LOG_ENABLED) {
					GWT.log("Se inicia la transaccion " + transaction.getId());
				}
				
				// Se ejecuta la llamada AJAX que cargara las imagenes definidas en la transaccion
				ImageLoader.loadImages(transaction.getImageUrls(), new ImageLoader.CallBack() {
					@Override
					public void onImagesLoaded(ImageElement[] imageElements) {
						// Se inyectan las imagenes recibidas en el contenedor de su propia transaccion.
						transaction.updateFromCallbackResult(imageElements);
						
						// Se traspasan las imaganes de la transaccion a la cache
						transaction.updateView();
						
						// Una vez terminado el tratamiento asincroni de esta transaccion 
						// la cadena puede continuar su ejecucion.
						taskContext.execute(new TaskLinkImpl());
					}
				});
				
			}
			
		}
	}

}
