package santjoans.client.piezes.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;

import santjoans.client.model.IPieze;
import santjoans.client.model.Model;
import santjoans.client.model.ModelDirectory;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.PiezePixelsEnum;
import santjoans.client.util.Util;
import santjoans.client.util.ZoomModeEnum;

public class MainView implements IConfiguration, IView {
	
	private Boolean LOG_ENABLED = false;
	
	private Canvas canvas;
	private Model model;
	
	public MainView(Canvas canvas) {
		this.canvas = canvas;
		this.model = ModelDirectory.getMainModel();
	}
	
	@Override
	public void updateFromModel(PiezePixelsEnum piezePixelsEnum, int startX, int startY, int stopX, int stopY) {
		if (LOG_ENABLED) {
			log("=== UPDATE_FROM_MODEL ===");
		}
		// Obtenemos todos las piezas del modelo
		// que se encuentran en la ventana de vision 
		// (pueden haber coordenadas vacias en el modelo)
		if (LOG_ENABLED) {
			log("startX=" + startX + ",startY=" + startY + ",stopX=" + stopX + ",stopY=" + stopY);
		}
		for (int y = startY; y <= stopY; y++) {
			for (int x = startX; x <= stopX; x++) {
				if (Util.isValidMainCoord(x, y)) {
					IPieze pieze = model.getPieze(x, y);
					if (pieze != null) {
						// La pieza ha sido definida correctamente en el modelo,
						if (pieze.getImageElement(piezePixelsEnum) == null) {
							drawEmptyPieze(startX, startY, x, y);
						} else {
							drawPieze(piezePixelsEnum, startX, startY, pieze);
						}
					} else {
						// La pieza todavia no ha sido definida en el modelo 
						// (posiblemente todavia no esta procesada la imagen) 
						drawEmptyPieze(startX, startY, x, y);
					}
				} else {
					if (LOG_ENABLED) {
						log("InvalidCoord(x=" + x + ",y=" + y + ")");
					}
				}
			}
		}
	}
	
	@Override
	public void drawPieze(PiezePixelsEnum piezePixelsEnum, int startX, int startY, IPieze pieze) {
		if (LOG_ENABLED) {
			log("drawPieze(y=" + pieze.getY() + ",x=" + pieze.getX() + ")");
		}
		
		// Guardamos el contexto de dibujo
		canvas.getCanvasElement().getContext2d().save();
		
		int tmpX = pieze.getX() - startX;
		int tmpY = pieze.getY() - startY;
		
		// Trasladamos el origen
		canvas.getCanvasElement().getContext2d().translate(tmpX * PIEZE_MAIN_HALF_DIAGONAL, tmpY * PIEZE_MAIN_HALF_DIAGONAL);
		
		// Obtenemos la imagen
		ImageElement imageElement = pieze.getImageElement(piezePixelsEnum);
		
		// Definimos la rotacion
		canvas.getCanvasElement().getContext2d().rotate(pieze.getMiniatureRadians());
		
		// Dibujamos la pieza
		canvas.getCanvasElement().getContext2d().drawImage( imageElement, -(PIEZE_MAIN_SIDE / 2), -(PIEZE_MAIN_SIDE / 2), PIEZE_MAIN_SIDE, PIEZE_MAIN_SIDE);
		
		// Restauramos el contexto de dibujo
		canvas.getCanvasElement().getContext2d().restore();
		
	}

	protected void drawEmptyPieze(int startX, int startY, int x, int y) {
		if (LOG_ENABLED) {
			log("drawEmptyPieze(y=" + y + ",x=" + x + ")");
		}
		
		// Guardamos el contexto de dibujo
		canvas.getCanvasElement().getContext2d().save();
		
		int tmpX = x - startX;
		int tmpY = y - startY;
		
		// Trasladamos el origen
		canvas.getCanvasElement().getContext2d().translate(tmpX * PIEZE_MAIN_HALF_DIAGONAL, tmpY * PIEZE_MAIN_HALF_DIAGONAL);
		
		// Definimos la rotacion
		canvas.getCanvasElement().getContext2d().rotate(Util.getRadians(45));
		
		// Dibujamos la pieza
		canvas.getCanvasElement().getContext2d().setStrokeStyle(CssColor.make("#A29481"));
		canvas.getCanvasElement().getContext2d().setFillStyle(CssColor.make("#A9A9A9"));
		canvas.getCanvasElement().getContext2d().setLineWidth(2);
		canvas.getCanvasElement().getContext2d().fillRect(-(PIEZE_MAIN_SIDE / 2), -(PIEZE_MAIN_SIDE / 2), PIEZE_MAIN_SIDE, PIEZE_MAIN_SIDE);
		canvas.getCanvasElement().getContext2d().strokeRect(-(PIEZE_MAIN_SIDE / 2), -(PIEZE_MAIN_SIDE / 2), PIEZE_MAIN_SIDE, PIEZE_MAIN_SIDE);
		
		// Restauramos el contexto de dibujo
		canvas.getCanvasElement().getContext2d().restore();
	}
	
	@Override
	public IPieze getPickedPiezeUrl(ZoomModeEnum zoomModeEnum, int startX, int startY, int xPixel, int yPixel) {
		
		// Coordenadas en pixeles a coordenadas en milimetros.
		int xMainMillimeter = Util.pixelToMillimeterX(zoomModeEnum, xPixel);
		int yMainMillimeter = Util.pixelToMillimeterY(zoomModeEnum, yPixel);
		
		// Coordenadas el milimetros a coordenadas en unidades del cuadrante.
		int xMainCoord = Util.millimeterToCoordX(xMainMillimeter);
		int yMainCoord = Util.millimeterToCoordY(yMainMillimeter);

		// Se camculan las coordenadas globales
		xMainCoord += startX;
		yMainCoord += startY;
		
		// Tiene que seleccionar dentro de la zona Main.
		if (Util.isIntoCenterCoord(xMainCoord, yMainCoord)) {
			return null;
		}

		// Se claculan los milimetros que se supera el punto de la coordenada.
		int xMilliRest = Util.millimeterToCoordXRest(xMainMillimeter);
		int yMilliRest= Util.millimeterToCoordYRest(yMainMillimeter);
		
		// Se calcula la inclinacion de la recta separadora de las piezas
		boolean goUp = (Util.isEven(yMainCoord) && Util.isOdd(xMainCoord)) || (Util.isOdd(yMainCoord) && Util.isEven(xMainCoord)) ? true : false;
		
		// Se calcula si se selecciona la parte izquierda o la parte derecha de la recta separadora de las piezas.
		boolean left = false;
		if (goUp) {
			if (xMilliRest> PIEZE_MAIN_HALF_DIAGONAL - yMilliRest) {
				left = false;
			} else {
				left = true;
			}
		} else {
			if (xMilliRest> yMilliRest) {
				left = false;
			} else {
				left = true;
			}
		}

		// Se resuelve que pieza es.
		int xPieze = 0;
		int yPieze = 0;
		if (Util.isEven(yMainCoord)) {
			// La coordenada Y es PAR (0,2,4,6,..)
			if (Util.isEven(xMainCoord)) {
				// La coordenada X es PAR
				if (left) {
					yPieze = yMainCoord + 1;
					xPieze = xMainCoord;
				} else{
					yPieze = yMainCoord;
					xPieze = xMainCoord + 1;
				}
			} else {
				// La coordenada X es IMPAR
				if (left) {
					yPieze = yMainCoord;
					xPieze = xMainCoord;
				} else{
					yPieze = yMainCoord + 1;
					xPieze = xMainCoord + 1;
				}
			}
		} else {
			// La coordenada Y es IMPAR (1,3,5,..)
			if (Util.isEven(xMainCoord)) {
				// La coordenada X es PAR
				if (left) {
					yPieze = yMainCoord;
					xPieze = xMainCoord;
				} else{
					yPieze = yMainCoord + 1;
					xPieze = xMainCoord + 1;
				}
			} else {
				// La coordenada X es IMPAR
				if (left) {
					yPieze = yMainCoord + 1;
					xPieze = xMainCoord;
				} else{
					yPieze = yMainCoord;
					xPieze = xMainCoord + 1;
				}
			}
		}
		
		//GWT.log("x:" + xPieze + ", y:" + yPieze);
		//GWT.log((goUp ? "SUBE" : "BAJA") + ", x:" + xMilliRest + ", y:" + yMilliRest + ",  " + (left ? "IZQUIERDA" : "DERECHA"));
		
		return ModelDirectory.getMainModel().getPieze(xPieze, yPieze);
	}
	
	@Override
	public Model getModel() {
		return ModelDirectory.getMainModel();
	}
	
	public void log(String msg) {
		GWT.log("MainView: " + msg);
	}

}
