package santjoans.client.util;

public enum ScreenTypeEnum {
	
	SVGA() {
		@Override
		public int getScreenWidth() {
			return 800;
		}
		
		@Override
		public int getScreenHeight() {
			return 600;
		}
		
		@Override
		public int getImageViewerToolMaxWidthLandscape() {
			return 400;
		}
		
		@Override
		public int getImageViewerToolMaxWidthPortrait() {
			return 400;
		}
		
		@Override
		public int getImageViewerToolMaxWidthMultiImage() {
			return 120;
		}
		
		@Override
		public int getCanvasX() {
			return 450;
		}
		
		@Override
		public int getPiezeViewerHeight() {
			return 400;
		}
		
		@Override
		public int getPiezeViewerWidth() {
			return 400;
		}
		
		@Override
		public int getPiezeViewerSide() {
			return 270;
		}
		
		@Override
		public PiezePixelsEnum getPiezePixelsForZoom(ZoomModeEnum zoomMode) {
			switch (zoomMode) {
			case MODE_100:
			case MODE_150:
			case MODE_200:
			case MODE_400:
				return PiezePixelsEnum.PP60;
			case MODE_800:
			case MODE_1600:
				return PiezePixelsEnum.PP360;
			default:
				return PiezePixelsEnum.PP60;
			}
		}

		public PiezePixelsEnum getPiezePixelsForDetail() {
			return PiezePixelsEnum.PP360;
		}

	},

	XGA() {
		@Override
		public int getScreenWidth() {
			return 1024;
		}
		
		@Override
		public int getScreenHeight() {
			return 768;
		}
		
		@Override
		public int getImageViewerToolMaxWidthLandscape() {
			return 500;
		}
		
		@Override
		public int getImageViewerToolMaxWidthPortrait() {
			return 500;
		}
		
		@Override
		public int getImageViewerToolMaxWidthMultiImage() {
			return 170;
		}
		
		@Override
		public int getCanvasX() {
			return 820;
		}
		
		@Override
		public int getPiezeViewerHeight() {
			return 550;
		}
		
		@Override
		public int getPiezeViewerWidth() {
			return 550;
		}
		
		@Override
		public int getPiezeViewerSide() {
			return 350;
		}
		
		@Override
		public PiezePixelsEnum getPiezePixelsForZoom(ZoomModeEnum zoomMode) {
			switch (zoomMode) {
			case MODE_100:
			case MODE_150:
			case MODE_200:
				return PiezePixelsEnum.PP60;
			case MODE_400:
			case MODE_800:
			case MODE_1600:
				return PiezePixelsEnum.PP360;
			default:
				return PiezePixelsEnum.PP60;
			}
		}
		
		public PiezePixelsEnum getPiezePixelsForDetail() {
			return PiezePixelsEnum.PP360;
		}

	},
	
	SXGA() {
		@Override
		public int getScreenWidth() {
			return 1280;
		}
		
		@Override
		public int getScreenHeight() {
			return 1024;
		}
		
		@Override
		public int getImageViewerToolMaxWidthLandscape() {
			return 600;
		}
		
		@Override
		public int getImageViewerToolMaxWidthPortrait() {
			return 600;
		}
		
		@Override
		public int getImageViewerToolMaxWidthMultiImage() {
			return 200;
		}
		
		@Override
		public int getCanvasX() {
			return 1200;
		}
		
		@Override
		public int getPiezeViewerHeight() {
			return 720;
		}
		
		@Override
		public int getPiezeViewerWidth() {
			return 720;
		}
		
		@Override
		public int getPiezeViewerSide() {
			return 500;
		}
		
		@Override
		public PiezePixelsEnum getPiezePixelsForZoom(ZoomModeEnum zoomMode) {
			switch (zoomMode) {
			case MODE_100:
			case MODE_150:
				return PiezePixelsEnum.PP60;
			case MODE_200:
			case MODE_400:
			case MODE_800:
			case MODE_1600:
				return PiezePixelsEnum.PP360;
			default:
				return PiezePixelsEnum.PP60;
			}
		}

		public PiezePixelsEnum getPiezePixelsForDetail() {
			return PiezePixelsEnum.PP550;
		}

	},
	
	FULLHD() {
		@Override
		public int getScreenWidth() {
			return 1920;
		}
		
		@Override
		public int getScreenHeight() {
			return 1080;
		}
		
		@Override
		public int getImageViewerToolMaxWidthLandscape() {
			return 650;
		}
		
		@Override
		public int getImageViewerToolMaxWidthPortrait() {
			return 650;
		}
		
		@Override
		public int getImageViewerToolMaxWidthMultiImage() {
			return 220;
		}
		
		@Override
		public int getCanvasX() {
			return 1450;
		}
		
		@Override
		public int getPiezeViewerHeight() {
			return 740;
		}
		
		@Override
		public int getPiezeViewerWidth() {
			return 740;
		}
		
		@Override
		public int getPiezeViewerSide() {
			return 530;
		}
		
		@Override
		public PiezePixelsEnum getPiezePixelsForZoom(ZoomModeEnum zoomMode) {
			switch (zoomMode) {
			case MODE_100:
			case MODE_150:
				return PiezePixelsEnum.PP60;
			case MODE_200:
			case MODE_400:
			case MODE_800:
				return PiezePixelsEnum.PP360;
			case MODE_1600:
				return PiezePixelsEnum.PP550;
			default:
				return PiezePixelsEnum.PP60;
			}
		}

		public PiezePixelsEnum getPiezePixelsForDetail() {
			return PiezePixelsEnum.PP550;
		}

	},
	
	UXGA() {
		@Override
		public int getScreenWidth() {
			return 1600;
		}
		
		@Override
		public int getScreenHeight() {
			return 1200;
		}
		
		@Override
		public int getImageViewerToolMaxWidthLandscape() {
			return 650;
		}
		
		@Override
		public int getImageViewerToolMaxWidthPortrait() {
			return 650;
		}
		
		@Override
		public int getImageViewerToolMaxWidthMultiImage() {
			return 220;
		}
		
		@Override
		public int getCanvasX() {
			return 1500;
		}
		
		@Override
		public int getPiezeViewerHeight() {
			return 800;
		}
		
		@Override
		public int getPiezeViewerWidth() {
			return 800;
		}
		
		@Override
		public int getPiezeViewerSide() {
			return 530;
		}
		
		@Override
		public PiezePixelsEnum getPiezePixelsForZoom(ZoomModeEnum zoomMode) {
			switch (zoomMode) {
			case MODE_100:
			case MODE_150:
				return PiezePixelsEnum.PP60;
			case MODE_200:
			case MODE_400:
			case MODE_800:
				return PiezePixelsEnum.PP360;
			case MODE_1600:
				return PiezePixelsEnum.PP550;
			default:
				return PiezePixelsEnum.PP60;
			}
		}

		public PiezePixelsEnum getPiezePixelsForDetail() {
			return PiezePixelsEnum.PP550;
		}

	};
	
	abstract public int getScreenHeight();
	abstract public int getScreenWidth();
	abstract public int getImageViewerToolMaxWidthLandscape();
	abstract public int getImageViewerToolMaxWidthPortrait();
	abstract public int getImageViewerToolMaxWidthMultiImage();
	abstract public int getCanvasX();

	abstract public int getPiezeViewerHeight();
	abstract public int getPiezeViewerWidth();
	abstract public int getPiezeViewerSide();
	
	abstract public PiezePixelsEnum getPiezePixelsForZoom(ZoomModeEnum zoomMode);
	abstract public PiezePixelsEnum getPiezePixelsForDetail();
	
	public int getCanvasY() {
		return 	(int) ((getCanvasX() * IConfiguration.MODEL_RATIO_X_Y) + 1);
	}
	
	public boolean isComplatible(int w, int h) {
		return (w >= getScreenWidth() && h >= getScreenHeight());
	}
	
}
