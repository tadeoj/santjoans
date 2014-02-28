package santjoans.client.transaction.sync;

import java.util.ArrayList;
import java.util.List;

import santjoans.client.transaction.ITransaction;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

public class SyncPiezeLoader {
	
	private List<ITransaction> totalTransactions;
	private int piezesCount;
	private IPiezeLoaderListener listener;
	
	public SyncPiezeLoader() {
		this.totalTransactions = new ArrayList<ITransaction>();
		this.piezesCount = 0;
	}
	
	public void addTransactions(List<ITransaction> transactions) {
		for (ITransaction transaction: transactions) {
			totalTransactions.add(transaction);
			piezesCount += transaction.getPiezes();
		}
	}
	
	public void execute(IPiezeLoaderListener listener) {
		// Se gusrda una referencia al listener
		this.listener = listener;
		// Se informa del estado inicial de la carga
		this.listener.piezesForLoad(piezesCount);
		// Se lanzan todas las transacciones
		for (ITransaction transaction : totalTransactions) {
			ImageLoader.loadImages(transaction.getImageUrls(), new ImagesLoadedCallback(transaction));
		}
	}
	
	class ImagesLoadedCallback implements ImageLoader.CallBack {
		
		private ITransaction piezeTransaction;
		
		public ImagesLoadedCallback(ITransaction piezeTransaction) {
			this.piezeTransaction = piezeTransaction;
		}

		@Override
		public void onImagesLoaded(ImageElement[] imageElements) {
			
			// Se inyectan las imagenes recibidas en el contenedor de su propia transaccion.
			piezeTransaction.updateFromCallbackResult(imageElements);
			
			// Se traspasan las imaganes de la transaccion a la cache
			piezeTransaction.updateView();
			
			// Se descuentan las piezas procesadas
			piezesCount -= piezeTransaction.getPiezes();
			
			// Se utiliza el listener para indicar las piezas pendientes por cargar
			listener.piezesForLoad(piezesCount);
		}
		
	}
	

}
