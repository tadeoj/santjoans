package santjoans.client.util;

import com.google.gwt.i18n.client.Messages;

public interface SantjoansMessages extends Messages {
	public String piezesDirectoryUnavailable(String name);
	public String mainPiezeAlreadyExist(int x, int y);
	public String mainPiezeAdd(int x, int y);
	public String invalidMainPieze(String name, String x, String y, String miniatureRotation, String detailRotation);
	public String piezeToString(String name, int x, int y, int miniatureRotation, int detailRotation);
	public String unexpectedRequestStatus(int status);
}
