package santjoans.client.piezes.view;

import santjoans.client.model.IPieze;
import santjoans.client.model.Model;
import santjoans.client.model.ModelDirectory;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.PiezePixelsEnum;
import santjoans.client.util.Util;
import santjoans.client.util.ZoomModeEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class CenterView implements IConfiguration, IView {
	
	private Boolean LOG_ENABLED = false;
	
	private GWTCanvas canvas;
	private Model model;
	
	public CenterView(GWTCanvas canvas) {
		this.canvas = canvas;
		this.model = ModelDirectory.getCenterModel();
	}

	@Override
	public void updateFromModel(PiezePixelsEnum piezePixelsEnum, int startX, int startY, int stopX, int stopY) {
		// Obtenemos todos los identificadores de las piezas del modelo
		// que se encuentran en la ventana de vision 
		// (pueden haber coordenadas vacias en el modelo)
		if (LOG_ENABLED) {
			log("startX=" + startX + ",startY=" + startY + ",stopX=" + stopX + ",stopY=" + stopY);
		}
		boolean[][] drawFlags = new boolean[MODEL_CENTER_MAX_COORD_Y + 1][MODEL_CENTER_MAX_COORD_X + 1];
		for (int y = 0; y <= MODEL_CENTER_MAX_COORD_Y; y++) {
			for (int x = 0; x <= MODEL_CENTER_MAX_COORD_X; x++) {
				drawFlags[y][x] = false;
			}
		}
		for (int y = startY; y <= stopY; y++) {
			for (int x = startX; x <= stopX; x++) {
				if (Util.isIntoCenterCoord(x, y)) {
					int centerX = Util.mainXtoCenterX(x);
					int centerY = Util.mainYtoCenterY(y);
					if (drawFlags[centerY][centerX]) {
						// Ya se ha pintado antes
						continue;
					}
					// Si es la primera vez que intenta pintarse ya se peude marcar.
					drawFlags[centerY][centerX] = true;
					
					if (LOG_ENABLED) {
						log("y=" + y + ",x=" + x + " estan dentro de la zona central, donde sus coordenadas son y=" + centerY + ",x=" + centerX);
					}
					IPieze pieze = model.getPieze(centerX, centerY);
					if (pieze != null) {
						// La pieza ha sido definida correctamente en el modelo,
						if (pieze.getImageElement(piezePixelsEnum) == null) {
							drawEmptyPieze(startX, startY, centerX, centerY);
						} else {
							drawPieze(piezePixelsEnum,startX, startY, pieze);
						}
					} else {
						// La pieza todavia no ha sido definida en el modelo 
						// (posiblemente todavia no esta procesada la imagen) 
						drawEmptyPieze(startX, startY, centerX, centerY);
					}
				}
			}
		}
	}
	
	@Override
	public void drawPieze(PiezePixelsEnum piezePixelsEnum,int startX, int startY, IPieze pieze) {
		if (LOG_ENABLED) {
			log("drawPieze(y=" + pieze.getY() + ",x=" + pieze.getX() + ")");
		}
		
		double xOffsite = (pieze.getX() * PIEZE_CENTER_X_SIDE) + (MODEL_CENTER_START_X - startX) * PIEZE_MAIN_HALF_DIAGONAL;
		double yOffsite = (pieze.getY() * PIEZE_CENTER_Y_SIDE) + (MODEL_CENTER_START_Y - startY) * PIEZE_MAIN_HALF_DIAGONAL;
		
		// Obtenemos la imagen
		ImageElement imageElement = pieze.getImageElement(piezePixelsEnum);
		// Se almazena el contexto grafico.
		canvas.saveContext();
		// Trasladamos el origen
		canvas.translate(xOffsite, yOffsite);
		// Dibujamos la pieza
		canvas.drawImage( imageElement, 0, 0, PIEZE_CENTER_X_SIDE, PIEZE_CENTER_Y_SIDE);
		// Se recupera el contexto grafico.
		canvas.restoreContext();
	}
	
	protected void drawEmptyPieze(int startX, int startY, int x, int y) {
		if (LOG_ENABLED) {
			log("drawEmptyPieze(y=" + y + ",x=" + x + ")");
		}
		
		double xOffsite = (x * PIEZE_CENTER_X_SIDE) + (MODEL_CENTER_START_X - startX) * PIEZE_MAIN_HALF_DIAGONAL;
		double yOffsite = (y * PIEZE_CENTER_Y_SIDE) + (MODEL_CENTER_START_Y - startY) * PIEZE_MAIN_HALF_DIAGONAL;
		
		// Se almazena el contexto grafico.
		canvas.saveContext();
		// Trasladamos el origen
		canvas.translate(xOffsite, yOffsite);
		// Dibujamos la pieza
		canvas.setStrokeStyle(new Color("#A29481"));
		canvas.setFillStyle(Color.GREY);
		canvas.setLineWidth(2);
		canvas.fillRect(0, 0, PIEZE_CENTER_X_SIDE, PIEZE_CENTER_Y_SIDE);
		canvas.strokeRect(0, 0, PIEZE_CENTER_X_SIDE, PIEZE_CENTER_Y_SIDE);
		// Se recupera el contexto grafico.
		canvas.restoreContext();
	}
	
	@Override
	public IPieze getPickedPiezeUrl(ZoomModeEnum zoomModeEnum, int startX, int startY, int xPixel, int yPixel) {
		
		// Coordenadas en pixeles a coordenadas en milimetros.
		int xMainMillimeter = Util.pixelToMillimeterX(zoomModeEnum, xPixel);
		int yMainMillimeter = Util.pixelToMillimeterY(zoomModeEnum, yPixel);
		
		// Coordenadas el milimetros a coordenadas en unidades del cuadrante.
		int xMainCoord = Util.millimeterToCoordX(xMainMillimeter);
		int yMainCoord = Util.millimeterToCoordY(yMainMillimeter);
		
		// Se calculan las coordenas absolutas
		xMainCoord += startX;
		yMainCoord += startY;
		
		// Para poder determinar que pieza es hay que verificar que ha pinchado dentro de la zona central.
		if (xMainCoord >= MODEL_CENTER_START_X && xMainCoord < MODEL_CENTER_END_X &&
			yMainCoord >= MODEL_CENTER_START_Y && yMainCoord < MODEL_CENTER_END_Y) {
			// Hay que convertir los datos en coordenadas validas para la zona central.
			int xCenterMillimeter = (int) (xMainMillimeter + (startX * PIEZE_MAIN_HALF_DIAGONAL) - (MODEL_CENTER_START_X * PIEZE_MAIN_HALF_DIAGONAL));
			int yCenterMillimeter = (int) (yMainMillimeter + (startY * PIEZE_MAIN_HALF_DIAGONAL) - (MODEL_CENTER_START_Y * PIEZE_MAIN_HALF_DIAGONAL));
			int xCenterCoord = (int) (xCenterMillimeter / PIEZE_CENTER_X_SIDE);
			int yCenterCoord = (int) (yCenterMillimeter / PIEZE_CENTER_Y_SIDE);
			return ModelDirectory.getCenterModel().getPieze(xCenterCoord, yCenterCoord);
		} else {
			return null;
		}
	}
	
	@Override
	public Model getModel() {
		return ModelDirectory.getCenterModel();
	}

	public void log(String msg) {
		GWT.log("CenterView: " + msg);
	}

}
