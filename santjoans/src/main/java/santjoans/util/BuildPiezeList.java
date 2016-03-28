package santjoans.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class BuildPiezeList {
	
	final String IMAGES_DIR = "src/santjoans/public/piezes/60";
	final String MAIN_LIST_FILE_NAME = "src/santjoans/public/piezes/main.txt";
	final String CENTER_LIST_FILE_NAME = "src/santjoans/public/piezes/center.txt";
	final String OUTPUT_FILE_NAME = "src/santjoans/public/piezes/piezes.xml";

	public static void main(String[] args) throws IOException {
		BuildPiezeList build = new BuildPiezeList();
		build.print();
		build.close();
	}

	// Para el resultado
	private File outputFile;
	private PrintStream outputPrint;
	
	// Directorios finales desde donede podemos obtener la lista de imagenes.
	private File mainImageDir;
	private File centerImageDir;
	
	// Ficheros de donde obtener la lista de imagenes.
	private File mainListFile;
	private File centerListFile;
	private BufferedReader mainReader;
	private BufferedReader centerReader;
	
	// Contadores de las piezas
	private int mainPiezes = 0;
	private int centerPiezes = 0;
	
	private BuildPiezeList() throws FileNotFoundException {
		
		// Preparacion para la salida del resultado.
		outputFile = new File(OUTPUT_FILE_NAME);
		outputPrint = new PrintStream(new FileOutputStream(outputFile));

		// El directorio base de las imagenes tiene que existir
		File baseDir = new File(IMAGES_DIR);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			System.err.println("ERROR: No existe el directorio base de las imagenes.");
			System.exit(1);
		}

		// Los disrectorios finales de las imagenes tambien tienen que existir
		mainImageDir = new File(baseDir, "main");
		centerImageDir = new File(baseDir, "center");
		if (!mainImageDir.exists() || !mainImageDir.isDirectory() || !centerImageDir.exists() || !centerImageDir.isDirectory()) {
			System.err.println("ERROR: No existe algunos de los directorios finales de las imagenes.");
			System.exit(1);
		}
		
		// Se prepara la lectura desde los ficheros con listas de imagenes
		mainListFile = new File(MAIN_LIST_FILE_NAME);
		if (mainListFile.exists()) {
			if (mainListFile.isFile()) {
				mainReader = new BufferedReader(new FileReader(mainListFile));
			} else {
				System.err.println("ERROR: El fichero con la lista de imagenes MAIN no existe o no es un fichero.");
				System.exit(1);
			}
		}
		centerListFile = new File(CENTER_LIST_FILE_NAME);
		if (centerListFile.exists()) {
			if (centerListFile.isFile()) {
				centerReader = new BufferedReader(new FileReader(centerListFile));
			} else {
				System.err.println("ERROR: El fichero con la lista de imagenes CENTER no existe o no es un fichero.");
				System.exit(1);
			}
		}
	}
	
	private void close() {
		outputPrint.flush();
		outputPrint.close();
	}
	
	private void print() throws IOException {
		printHeader();
		printMainPiezes();
		printCenterPiezes();
		printFooter();
		System.out.format("Main=%d, Center=%d.\n", mainPiezes, centerPiezes);
	}
	
	private void printMainPiezes() throws IOException {
		outputPrint.println("  <main>");
		printMainFromImageList();
		printMainFromImageDir();
		outputPrint.println("  </main>");
	}
	
	private void printCenterPiezes() throws IOException {
		outputPrint.println("  <center>");
		printCenterFromImageList();
		printCenterFromImageDir();
		outputPrint.println("  </center>");
	}
	
	private void printMainFromImageList() throws IOException {
		if (mainReader != null) {
			while(true) {
				String buffer = mainReader.readLine();
				if (buffer == null) {
					return;
				} else {
					if (!isBlankLine(buffer)) {
						PiezeInfo pi = PiezeInfo.parseFromFile(buffer);
						if (pi != null) {
							mainPiezes++;
							outputPrint.println(pi.toString("mainpieze"));
						} else {
							System.err.println("Invalid entry in list");
						}
					}
				}
			}
		}
	}
	
	private void printMainFromImageDir() {
		for (File file: mainImageDir.listFiles()) {
			if (file.isFile()) {
				PiezeInfo pi = PiezeInfo.parseFromDir(file.getName());
				if (pi != null) {
					mainPiezes++;
					outputPrint.println(pi.toString("mainpieze"));
				}
			}
		}
	}
	
	private void printCenterFromImageList() throws IOException {
		if (centerReader != null) {
			while(true) {
				String buffer = centerReader.readLine();
				if (buffer == null) {
					return;
				} else {
					if (!isBlankLine(buffer)) {
						PiezeInfo pi = PiezeInfo.parseFromFile(buffer);
						if (pi != null) {
							centerPiezes++;
							outputPrint.println(pi.toString("centerpieze"));
						} else {
							System.err.println("Invalid entry in list");
						}
					}
				}
			}
		}
	}
	
	private boolean isBlankLine(String buffer) {
		String line = buffer.trim();
		return (line.length() == 0 || line.startsWith("//"));
	}
	
	private void printCenterFromImageDir() {
		for (File file: centerImageDir.listFiles()) {
			if (file.isFile()) {
				PiezeInfo pi = PiezeInfo.parseFromDir(file.getName());
				if (pi != null) {
					centerPiezes++;
					outputPrint.println(pi.toString("centerpieze"));
				}
			}
		}
	}
	
	private void printHeader() {
		outputPrint.println("<?xml version=\"1.0\" ?>");
		outputPrint.println("<!DOCTYPE layout [");
		outputPrint.println("<!ELEMENT layout (main+,center+)>");  
		outputPrint.println("<!ELEMENT main (mainpieze*)>");
		outputPrint.println("<!ELEMENT mainpieze EMPTY>");
		outputPrint.println("<!ATTLIST mainpieze ");
		outputPrint.println("    name CDATA #REQUIRED"); 
		outputPrint.println("    x CDATA #REQUIRED");
		outputPrint.println("    y CDATA #REQUIRED"); 
		outputPrint.println("    miniature_rotation CDATA #REQUIRED"); 
		outputPrint.println("    detail_rotation CDATA #REQUIRED>"); 
		outputPrint.println("<!ELEMENT center (centerpieze*)>"); 
		outputPrint.println("<!ELEMENT centerpieze EMPTY>");
		outputPrint.println("<!ATTLIST centerpieze");
		outputPrint.println("    name CDATA #REQUIRED"); 
		outputPrint.println("    x CDATA #REQUIRED");
		outputPrint.println("    y CDATA #REQUIRED"); 
		outputPrint.println("    miniature_rotation CDATA #REQUIRED"); 
		outputPrint.println("    detail_rotation CDATA #REQUIRED>"); 
		outputPrint.println("]>");
		outputPrint.println("<layout>");
	}
	
	private void printFooter() {
		outputPrint.println("</layout>");
	}
	
	static private class PiezeInfo {
		
		static PiezeInfo parseFromDir(String fileName) {
			StringTokenizer stDot = new StringTokenizer(fileName, ".");
			if (stDot.countTokens() == 2) {
				StringTokenizer stUnder = new StringTokenizer(stDot.nextToken(), "_");
				if (stUnder.countTokens() == 3) {
					return new PiezeInfo(
						fileName, 
						Integer.parseInt(stUnder.nextToken()), 
						Integer.parseInt(stUnder.nextToken()), 
						45,
						Integer.parseInt(stUnder.nextToken()) 
					);
				}
			}
			return null;
		}
		
		static PiezeInfo parseFromFile(String line) {
			StringTokenizer stUnder = new StringTokenizer(line, ",");
			if (stUnder.countTokens() == 5) {
				return new PiezeInfo(
					stUnder.nextToken(), 
					Integer.parseInt(stUnder.nextToken().trim()), 
					Integer.parseInt(stUnder.nextToken().trim()), 
					Integer.parseInt(stUnder.nextToken().trim()), 
					Integer.parseInt(stUnder.nextToken().trim()) 
				);
			}
			return null;
		}
		
		private String name;
		private int x;
		private int y;
		private int miniatureRotation;
		private int detailRotation;
		
		public PiezeInfo(String name, int x, int y, int miniatureRotation, int detailRotation) {
			this.name = name;
			this.x = x;
			this.y = y;
			this.miniatureRotation = miniatureRotation;
			this.detailRotation = detailRotation;
		}
		
		public String getName() {
			return name;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getMiniatureRotation() {
			return miniatureRotation;
		}
		
		public int getDetailedRotation() {
			return detailRotation;
		}
		
		public String toString(String type) {
			return String.format("    <%s name=\"%s\" x=\"%d\" y=\"%d\" miniature_rotation=\"%d\"  detail_rotation=\"%d\" />", 
					type, getName(), getX(), getY(), getMiniatureRotation(), getDetailedRotation());

		}
		
	}
	

}
