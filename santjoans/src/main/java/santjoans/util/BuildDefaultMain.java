package santjoans.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import santjoans.client.util.IConfiguration;

public class BuildDefaultMain implements IConfiguration {

	final String OUTPUT_FILE_NAME = "src/santjoans/public/piezes/default_main.txt";
	final String IMAGE_FILE_NAME = "flor";
	final int DETAIL_ROTATION = 45;
	
	int[] marmolCoords = {
		2,1,
		1,2,
		3,2,
		2,3,
		
		52,1,
		51,2,
		53,2,
		52,3,
		
		2,23,
		1,24,
		3,24,
		2,25,
		
		52,23,
		51,24,
		53,24,
		52,25
	};


	public static void main(String[] args) throws FileNotFoundException {
		BuildDefaultMain main = new BuildDefaultMain();
		
		main.open();
		main.generate();
		main.close();
	}
	
	private File outputFile;
	private PrintStream outputPrint;

	public void open() throws FileNotFoundException {
		outputFile = new File(OUTPUT_FILE_NAME);
		outputPrint = new PrintStream(new FileOutputStream(outputFile));
	}
	
	public void close() {
		outputPrint.close();
	}
	
	public boolean isInMarmol(int x, int y) {
		for (int i = 0; i < marmolCoords.length; i += 2) {
			if (x == marmolCoords[i] && y == marmolCoords[i + 1]) {
				return true;
			}
		}
		return false;
	}
	
	public void generate() {
		
		for (int x = 0; x <= MODEL_MAIN_MAX_COORD_X; x++) {
			for (int y = 0; y <= MODEL_MAIN_MAX_COORD_Y; y++) {
				if (!isInMarmol(x, y)) {
					if ((isEven(y) && isOdd(x)) || (isOdd(y) && isEven(x))) {
						if (y <= 3 || y >= 23) {
							outputPrint.format("%s%d.jpg,%d,%d,%d,%d\n", IMAGE_FILE_NAME, getNumber(), x, y, getRotation(), DETAIL_ROTATION);
						} else {
							if (x <= 3 || x >= 51) {
								outputPrint.format("%s%d.jpg,%d,%d,%d,%d\n", IMAGE_FILE_NAME, getNumber(), x, y, getRotation(), DETAIL_ROTATION);
							}
						}
					}
				}
			}
		}
	}
	
	public int getRotation() {
		double r = Math.random();
		if (r < 0.25f) {
			return 45;
		} else if (r < 0.50) {
			return 225;
		} else if (r < 0.75f) {
			return 315;
		} else {
			return 135;
		}
	}
	
	private double[] numberLimits = new double[] {0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f};
	
	public int getNumber() {
		double r = Math.random();
		for (int i = 0; i < numberLimits.length; i++) {
			if (r < numberLimits[i]) {
				return i;
			}
		}
		return numberLimits.length;
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
	
}
