package santjoans.client.model;

import santjoans.client.resources.ISantjoansResources;
import santjoans.client.util.SantjoansConstants;
import santjoans.client.util.SantjoansMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

abstract public class ModelLoader {
	
	private Boolean LOG_ENABLED = false;
	
	private static final String INDEX_URL = "piezes/piezes.xml";
	
	protected SantjoansConstants constants = GWT.create(SantjoansConstants.class);
	protected SantjoansMessages messages = GWT.create(SantjoansMessages.class);
	
	public void loadModels() {
		if (parseModelDefinition(ISantjoansResources.INSTANCE.piezes().getText())) {
			modelLoaded();
		}
	}
	
	public void loadModelsOld() {
		// 1: Se carga el fichero XML con la lista de imagenes que componen el modelo.
		GWT.log("Se instancia el RequestBuilder para obtener el fichero: " + INDEX_URL);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, INDEX_URL);
		try {
			GWT.log("Se ejecuta asincronamente la peticion mediante sendRequest()");
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error en la peticion.");
					String errorText = messages.piezesDirectoryUnavailable(GWT.getModuleBaseURL() + INDEX_URL);
					GWT.log(errorText, exception);
					Window.alert(errorText);
				}

				public void onResponseReceived(Request request, Response response) {
					GWT.log("Peticion ejecutada.");
					if (200 == response.getStatusCode()) {
						// 2: El fichero XML se ha cargado correctamente, se parsea la definicion del modelo.
						if (parseModelDefinition(response.getText())) {
							// 3: El modelo esta creado y tenemos los datos de cada imagen para poder cargarlas
							// cuando las necesitamos.
							
							// 4: Indicamos que el modelo esta disponible.
							modelLoaded();
						}
					} else {
						String errorText = messages.unexpectedRequestStatus(response.getStatusCode());
						GWT.log(errorText, null);
						Window.alert(errorText);
					}
				}
			});
			GWT.log("La llamada a sendRequest() ha finalizado.");
		} catch (RequestException e) {
			String errorText = constants.unknownRunningRequest();
			GWT.log(errorText, e);
			Window.alert(errorText);
		}
	}
	
	private boolean parseModelDefinition(String messageXml) {
		try {
			// Se convierte el contenido de fichero XML en un DOM
			Document document = XMLParser.parse(messageXml);
	
			////////////////////////////////////////////////////////
			// Se carga MAIN
			////////////////////////////////////////////////////////
			NodeList mainPiezeList = document.getElementsByTagName("mainpieze");
			if (LOG_ENABLED) {
				GWT.log("LOADER-MAIN: Parsing...", null);
			}
			for (int i = 0; i < mainPiezeList.getLength(); i++) {
				Element elementPieze = (Element) mainPiezeList.item(i);
				try {
					parsePieze(ModelDirectory.getMainModel(), elementPieze);
				} catch (PiezeParsingException e) {
					GWT.log(e.getMessage(), e);
					return false;
				}
			}
			
			////////////////////////////////////////////////////////
			// Se carga CENTRAL
			////////////////////////////////////////////////////////
			NodeList centerPiezeList = document.getElementsByTagName("centerpieze");
			if (LOG_ENABLED) {
				GWT.log("LOADER-CENTER: Parsing...", null);
			}
			for (int i = 0; i < centerPiezeList.getLength(); i++) {
				Element elementPieze = (Element) centerPiezeList.item(i);
				try {
					parsePieze(ModelDirectory.getCenterModel(), elementPieze);
				} catch (PiezeParsingException e) {
					GWT.log(e.getMessage(), e);
					return false;
				}
			}
			
			////////////////////////////////////////////////////////
			// Todas la definiciones de las piezas estan cargadas
			////////////////////////////////////////////////////////
			return true;
			
		} catch (DOMException e) {
			String errorText = constants.xmlSintaxException();
			GWT.log(errorText, e);
			Window.alert(errorText);
			return false;
		}
	}
	
	public void parsePieze(Model model, Element element) throws PiezeParsingException {
		
		String name = null;
		String x = null;
		String y = null;
		String miniatureRotation = null;
		String detailRotation = null;
		try {
			name = element.getAttribute("name");
			x = element.getAttribute("x");
			y = element.getAttribute("y");
			miniatureRotation = element.getAttribute("miniature_rotation");
			detailRotation = element.getAttribute("detail_rotation");
			model.addPieze(
					name,
					Integer.parseInt(x),
					Integer.parseInt(y),
					Integer.parseInt(miniatureRotation),
					Integer.parseInt(detailRotation)
		);
		} catch (NumberFormatException nfEx) {
			throw new PiezeParsingException(messages.invalidMainPieze(name, x, y, miniatureRotation, detailRotation));
		}
		
	}

	
	abstract protected void modelLoaded();
	
}
