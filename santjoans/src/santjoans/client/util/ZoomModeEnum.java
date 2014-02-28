package santjoans.client.util;


public enum ZoomModeEnum implements IConfiguration {

	MODE_100() {
		@Override
		public int getUnitWidth() {
			return 54;
		}
		@Override
		public int getUnitHeight() {
			return 26;
		}
		@Override
		public int getEndX(int startX) {
			return startX + getUnitWidth();
		}
		@Override
		public int getEndY(int startY) {
			return startY + getUnitHeight();
		}
		@Override
		public int getMillimetersWidth() {
			return (int) (getUnitWidth() * PIEZE_MAIN_HALF_DIAGONAL);
		}
		@Override
		public int getMillimetersHeight() {
			return (int) (getMillimetersWidth() * MODEL_RATIO_X_Y);
		}
		@Override
		public int getSteepX() {
			return 0;
		}
		@Override
		public int getSteepY() {
			return 0;
		}
		@Override
		public PiezePixelsEnum getPiezePixels() {
			return Util.getCurrentScreenType().getPiezePixelsForZoom(this);
		}
		@Override
		public String toString() {
			return "100%";
		}
		public ZoomModeEnum getZoomIn() {
			return MODE_150;
		}
		public ZoomModeEnum getZoomOut() {
			return this;
		}
	},
	MODE_150() {
		@Override
		public int getUnitWidth() {
			return 40;
		}
		@Override
		public int getUnitHeight() {
			//return (int) ((getX() * MODEL_RATIO_X_Y) + 2);
			return 20;
		}
		@Override
		public int getEndX(int startX) {
			return startX + getUnitWidth();
		}
		@Override
		public int getEndY(int startY) {
			return startY + getUnitHeight();
		}
		@Override
		public int getMillimetersWidth() {
			return (int) (getUnitWidth() * PIEZE_MAIN_HALF_DIAGONAL);
		}
		@Override
		public int getMillimetersHeight() {
			return (int) (getMillimetersWidth() * MODEL_RATIO_X_Y);
		}
		@Override
		public int getSteepX() {
			return 2;
		}
		@Override
		public int getSteepY() {
			return 2;
		}
		@Override
		public PiezePixelsEnum getPiezePixels() {
			return Util.getCurrentScreenType().getPiezePixelsForZoom(this);
		}
		@Override
		public String toString() {
			return "150%";
		}
		public ZoomModeEnum getZoomIn() {
			return MODE_200;
		}
		public ZoomModeEnum getZoomOut() {
			return MODE_100;
		}
	},
	MODE_200() {
		@Override
		public int getUnitWidth() {
			return 26;
		}
		@Override
		public int getUnitHeight() {
			//return (int) ((getX() * MODEL_RATIO_X_Y) + 1);
			return 13;
		}
		@Override
		public int getEndX(int startX) {
			return startX + getUnitWidth();
		}
		@Override
		public int getEndY(int startY) {
			return startY + getUnitHeight();
		}
		@Override
		public int getMillimetersWidth() {
			return (int) (getUnitWidth() * PIEZE_MAIN_HALF_DIAGONAL);
		}
		@Override
		public int getMillimetersHeight() {
			return (int) (getMillimetersWidth() * MODEL_RATIO_X_Y);
		}
		@Override
		public int getSteepX() {
			return 1;
		}
		@Override
		public int getSteepY() {
			return 1;
		}
		@Override
		public PiezePixelsEnum getPiezePixels() {
			return Util.getCurrentScreenType().getPiezePixelsForZoom(this);
		}
		public String toString() {
			return "200%";
		}
		public ZoomModeEnum getZoomIn() {
			return MODE_400;
		}
		public ZoomModeEnum getZoomOut() {
			return MODE_150;
		}
	},
	MODE_400() {
		@Override
		public int getUnitWidth() {
			return 14;
		}
		@Override
		public int getUnitHeight() {
			//return (int) ((getX() * MODEL_RATIO_X_Y) + 1);
			return 7;
		}
		@Override
		public int getEndX(int startX) {
			return startX + getUnitWidth();
		}
		@Override
		public int getEndY(int startY) {
			return startY + getUnitHeight();
		}
		@Override
		public int getMillimetersWidth() {
			return (int) (getUnitWidth() * PIEZE_MAIN_HALF_DIAGONAL);
		}
		@Override
		public int getMillimetersHeight() {
			return (int) (getMillimetersWidth() * MODEL_RATIO_X_Y);
		}
		@Override
		public int getSteepX() {
			return 1;
		}
		@Override
		public int getSteepY() {
			return 1;
		}
		@Override
		public PiezePixelsEnum getPiezePixels() {
			return Util.getCurrentScreenType().getPiezePixelsForZoom(this);
		}
		public String toString() {
			return "400%";
		}
		public ZoomModeEnum getZoomIn() {
			return MODE_800;
		}
		public ZoomModeEnum getZoomOut() {
			return MODE_200;
		}
	},
	MODE_800() {
		@Override
		public int getUnitWidth() {
			return 8;
		}
		@Override
		public int getUnitHeight() {
			//return (int) ((getX() * MODEL_RATIO_X_Y) + 1);
			return 5;
		}
		@Override
		public int getEndX(int startX) {
			return startX + getUnitWidth();
		}
		@Override
		public int getEndY(int startY) {
			return startY + getUnitHeight();
		}
		@Override
		public int getMillimetersWidth() {
			return (int) (getUnitWidth() * PIEZE_MAIN_HALF_DIAGONAL);
		}
		@Override
		public int getMillimetersHeight() {
			return (int) (getMillimetersWidth() * MODEL_RATIO_X_Y);
		}
		@Override
		public int getSteepX() {
			return 2;
		}
		@Override
		public int getSteepY() {
			return 2;
		}
		@Override
		public PiezePixelsEnum getPiezePixels() {
			return Util.getCurrentScreenType().getPiezePixelsForZoom(this);
		}
		public String toString() {
			return "800%";
		}
		public ZoomModeEnum getZoomIn() {
			return MODE_1600;
		}
		public ZoomModeEnum getZoomOut() {
			return MODE_400;
		}
	},
	MODE_1600() {
		@Override
		public int getUnitWidth() {
			return 5;
		}
		@Override
		public int getUnitHeight() {
			//return (int) ((getX() * MODEL_RATIO_X_Y) + 1);
			return 3;
		}
		@Override
		public int getEndX(int startX) {
			return startX + getUnitWidth();
		}
		@Override
		public int getEndY(int startY) {
			return startY + getUnitHeight();
		}
		@Override
		public int getMillimetersWidth() {
			return (int) (getUnitWidth() * PIEZE_MAIN_HALF_DIAGONAL);
		}
		@Override
		public int getMillimetersHeight() {
			return (int) (getMillimetersWidth() * MODEL_RATIO_X_Y);
		}
		@Override
		public int getSteepX() {
			return 1;
		}
		@Override
		public int getSteepY() {
			return 1;
		}
		@Override
		public PiezePixelsEnum getPiezePixels() {
			return Util.getCurrentScreenType().getPiezePixelsForZoom(this);
		}
		public String toString() {
			return "1600%";
		}
		public ZoomModeEnum getZoomIn() {
			return MODE_1600;
		}
		public ZoomModeEnum getZoomOut() {
			return MODE_800;
		}
	};
	
	abstract public int getUnitWidth();
	abstract public int getUnitHeight();
	abstract public int getEndX(int startX);
	abstract public int getEndY(int startY);
	abstract public int getMillimetersWidth();
	abstract public int getMillimetersHeight();
	abstract public int getSteepX();
	abstract public int getSteepY();
	abstract public PiezePixelsEnum getPiezePixels();
	abstract public ZoomModeEnum getZoomIn();
	abstract public ZoomModeEnum getZoomOut();
	
}
