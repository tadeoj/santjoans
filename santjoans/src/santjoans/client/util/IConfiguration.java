package santjoans.client.util;

public interface IConfiguration {
	
	public static boolean LOG_ENABLED = false;
	
	final double PIEZE_MAIN_SIDE = 200f; // mm
	final double PIEZE_MAIN_DIAGONAL = 282.842712474619f; // mm
	final double PIEZE_MAIN_HALF_DIAGONAL = 141.4213562373095f; // mm
	
	final double MODEL_RATIO_X_Y = 0.4814814814814815f;
	
	final int MODEL_MAIN_MAX_COORD_X = 54; 
	final int MODEL_MAIN_MAX_COORD_Y = 26;

	final int MODEL_CENTER_MAX_COORD_X = 9; 
	final int MODEL_CENTER_MAX_COORD_Y = 6;

	final int PREVIEW_X = 200;
	final int PREVIEW_Y = (int) ((PREVIEW_X * MODEL_RATIO_X_Y) + 1);
	
	final int MODEL_CENTER_START_X = 21;
	final int MODEL_CENTER_START_Y =  7;
	final int MODEL_CENTER_END_X = 35;
	final int MODEL_CENTER_END_Y = 17;

	final double PIEZE_CENTER_X_SIDE = 197.9898987322333; // mm
	final double PIEZE_CENTER_Y_SIDE = 202.0305089104421; // mm
	
}
