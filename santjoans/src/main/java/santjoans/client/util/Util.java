package santjoans.client.util;

import com.google.gwt.core.client.GWT;

public class Util implements IConfiguration {
	
	static public int getIndex(int x, int y) {
		return y * 100 + x;
	}
	
	static public double getRadians(int rotation) {
		return (2 * Math.PI * rotation) / 360;
	}
	
	static public boolean isOdd(int n) {
		if (n % 2 !=0)
			return true;
		else
			return false;
	}

	static public boolean isEven(int n) {
		if (n % 2 ==0)
			return true;
		else
			return false;
	}
	
	static public boolean isValidMainCoord(int x, int y) {
		// Las coodenadas X pares van con las Y impares 
		// y las X impares con las Y pares. 
		if ((isEven(y) && isOdd(x)) || (isOdd(y) && isEven(x))) {
			// Si la coordenada cae dentro del cuadro central, tampoco
			// hay que tenerlo en cuenta.
			if (x > MODEL_CENTER_START_X && 
				x < MODEL_CENTER_END_X &&
				y > MODEL_CENTER_START_Y && 
				y < MODEL_CENTER_END_Y ) {
					return false;
				} else {
					return true;
				}
		} else {
			return false;
		}
	}

	static public boolean isIntoCenterCoord(int x, int y) {
		if (x >= MODEL_CENTER_START_X && 
			x < MODEL_CENTER_END_X &&
			y >= MODEL_CENTER_START_Y && 
			y < MODEL_CENTER_END_Y ) {
			return true;
		} else {
			return false;
		}
	}

	// 21 -> 0, 35 -> 10 == 0 -> 0, 14 -> 10
	static final Double MAIN_X_TO_CENTER_X = 0.7142857142857143;
	
	static public int mainXtoCenterX(int mainX) {
		return (int) ((mainX - MODEL_CENTER_START_X) * MAIN_X_TO_CENTER_X);
	}
	
	// 7 -> 0, 17 -> 7 == 0 -> 0, 10 -> 7
	static final Double MAIN_Y_TO_CENTER_Y = 0.7;
	
	static public int mainYtoCenterY(int mainY) {
		return (int) ((mainY - MODEL_CENTER_START_Y) * MAIN_Y_TO_CENTER_Y);
	}
	
	static public int pixelToMillimeterX(ZoomModeEnum zoomModeEnum, int pixel) {
		return ((pixel * zoomModeEnum.getMillimetersWidth()) / Util.getCurrentScreenType().getCanvasX());
	}
	
	static public int pixelToMillimeterY(ZoomModeEnum zoomModeEnum, int pixel) {
		return ((pixel * zoomModeEnum.getMillimetersHeight()) / Util.getCurrentScreenType().getCanvasY());
	}
	
	static public int millimeterToCoordX(int millimeters) {
		return (int) (millimeters / PIEZE_MAIN_HALF_DIAGONAL);
	}

	static public int millimeterToCoordY(int millimeters) {
		return (int) (millimeters / PIEZE_MAIN_HALF_DIAGONAL);
	}

	static public int millimeterToCoordXRest(int millimeters) {
		return (int) (millimeters % PIEZE_MAIN_HALF_DIAGONAL);
	}

	static public int millimeterToCoordYRest(int millimeters) {
		return (int) (millimeters % PIEZE_MAIN_HALF_DIAGONAL);
	}

	public static native int getScreenWidth() /*-{ 
    	return $wnd.screen.width;
 	}-*/;

	public static native int getScreenHeight() /*-{ 
		return $wnd.screen.height;
	}-*/;
	
	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;

	static public ScreenTypeEnum screenType;
	
	static public ScreenTypeEnum getCurrentScreenType() {
		if (screenType == null) {
			// Se determina el tipo de pantalla.
			if (ScreenTypeEnum.FULLHD.isComplatible(getScreenWidth(), getScreenHeight()) ) {
				screenType =  ScreenTypeEnum.FULLHD;
			} else if (ScreenTypeEnum.UXGA.isComplatible(getScreenWidth(), getScreenHeight())) {
				screenType = ScreenTypeEnum.UXGA;
			} else if (ScreenTypeEnum.SXGA.isComplatible(getScreenWidth(), getScreenHeight())) {
				screenType = ScreenTypeEnum.SXGA;
			} else if (ScreenTypeEnum.XGA.isComplatible(getScreenWidth(), getScreenHeight())) {
				screenType = ScreenTypeEnum.XGA;
			} else if (ScreenTypeEnum.SVGA.isComplatible(getScreenWidth(), getScreenHeight())) {
				screenType = ScreenTypeEnum.SVGA;
			} else {
				// Si no hay ninguna definicion adecuada simplemente utilizamos la mas pequeña
				screenType = ScreenTypeEnum.SVGA;
			}
			
			// La primera vez se muestra la informacion de la pantalla.
			String desc = screenType.toString() + ", w=" + getScreenWidth() + ", h=" + getScreenHeight();
			//Window.alert(desc);
			GWT.log(desc);
			
		}
		return screenType;
	}
	
}
