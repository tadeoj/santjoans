package santjoans.client.transaction;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import santjoans.client.model.ICacheEntry;
import santjoans.client.model.IModelEntry;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.piezes.view.IView;
import santjoans.client.util.SantjoansConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;

public class Transaction implements ITransaction {

	static final public int PIEZES_BY_TRANSACTION = 2;
	
	private Boolean LOG_ENABLED = false;
	
	static protected SantjoansConstants constants = GWT.create(SantjoansConstants.class);

	static private long idCount = 0;
	
	private List<ICacheEntry> modelEntries;
	
	private IView view;
	private IControllerViewerContext context;
	private long id;
	
	/**
	 * Construye una transaccion para obtener las imagenes que corresponde a las piezas indicadas.
	 * <p>
	 * El centro de la transaccion es un Map que contiene una entrada para cada pieza de la cual se quiere obtener
	 * la imagen. Cada entrada esta indexada con una llave que se calcula con las coordenadas que la pieza ocupa en el tapiz.
	 * El valor almacenado en cada entrada contiene toda la informacion necesaria de la pieza asi como una variable que
	 * se podra utilizar para almacenar la imagen una vez se haya recibido.
	 * 
	 * @param view vista donde residen finalmente las imagenes y que proporcina los mecanismos para presentar la pieza
	 * @param context contexto que nos proporcina las caracteristicas de la vista que el usuario tiene del mosaico
	 * @param modelEntries piezas que se quieren incluir en la transaccion.
	 */
	public Transaction(IView view, IControllerViewerContext context, List<ICacheEntry> modelEntries) {
		this.view = view;
		this.context = context;
		this.modelEntries = modelEntries;
		this.id = idCount++;
	}
	
	/**
	 * Cuendo la transaccion ha terminado y las imagenes de las piezas se han obtenido, se debe llamar
	 * a este metodo para trasladar las imagenes obtenidas en la cache prinxipal y ademas para actualizarlas 
	 * en la vista del usuario.
	 */
	public void updateView() {
		StringBuffer buffer = null;
		for (ICacheEntry cacheEntry : modelEntries) {
			if (LOG_ENABLED) {
				if (buffer == null) { 
					buffer = new StringBuffer();
				} else {
					buffer.append(", ");
				} 
				buffer.append("imageName=" + cacheEntry.getImageName());
			}
			// Para cada imagen obtenida en la transaccion hay que refrescar todas las referencias afectadas por el contexto actual.
			Collection<IModelEntry> allModelEntries = cacheEntry.getReferences();
			Collection<IModelEntry> inContextModelEntries = view.getModel().queryByContext(allModelEntries, context);
			// Se actualizan las entradas del modelo afectadas
			for (IModelEntry modelEntry : inContextModelEntries) {
				if (LOG_ENABLED) {
					GWT.log(view.getModel().getName() + "-> Transaccion " + getId() + ", visualizando: " + modelEntry.toString());
				}
				view.drawPieze(
					context.getZoomMode().getPiezePixels(),
					context.getStartX(), 
					context.getStartY(),
					modelEntry);
			}
		}
		if (LOG_ENABLED) {
			GWT.log(view.getModel().getName() + "-> Finalizada transaccion " + getId() + ": " + buffer.toString());
		}
	}
	
	/**
	 * Una vez creada la transaccion podemos obtener los URLs correspondientes a las imagenes de sus
	 * correspondientes piezas mediante este metodo.
	 * 
	 * @return array con los urls de las piezas
	 */
	public String[] getImageUrls() {
		Vector<String> urls = new Vector<String>();
		for (ICacheEntry cacheEntry : modelEntries) {
			urls.add(cacheEntry.getImageUrl(context.getZoomMode().getPiezePixels()));
		}
		return urls.toArray(new String[] {});
	}
	
	/**
	 * Mediante este metodo se asigna a cada pieza de la transaccion su correspondiente imagen.
	 * <P>
	 * Este metodo es invocado por quien reciba la invocacion asincrona cuando la transaccion ha finalizado
	 *  
	 * @param imageElements array de imagenes que corresponden con el array de pìezas con las que se instancio la transaccion.
	 */
	public void updateFromCallbackResult(ImageElement[] imageElements) {
		int idx = 0;
		for (ICacheEntry cacheEntry : modelEntries) {
			cacheEntry.setImageElement(context.getZoomMode().getPiezePixels(), imageElements[idx++]);
		}
	}
	
	/**
	 * Obtiene el Identificador unico que ha sido asignado a cada transaccion en el momento que esta se creo.
	 * 
	 * @return identificadro de la transaccion.
	 */
	public long getId() {
		return id;
	}
	
	public int getPiezes() {
		return modelEntries.size();
	}
	
}
