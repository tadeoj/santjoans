package santjoans.client.transaction.async;

import java.util.Vector;

public class TaskChain {

	private ITaskLink currentLink;
	private Vector<ITaskLink> headTaskLinks;

	public void cancel() {
		// Si hay una cadena en curso se para.
		if (currentLink != null) {
			currentLink = null;
			headTaskLinks = null;
		}
	}
	
	public void addHeadLink(ITaskLink headTaskLink) {
		if (headTaskLinks == null) {
			headTaskLinks = new Vector<ITaskLink>();
		}
		headTaskLinks.add(headTaskLink);
	}
	
	public void execute() {
		// Se tiene que haber incluido alguna cadena para poder ejecutarse.
		if (headTaskLinks != null && headTaskLinks.size() > 0) {
			// Se crea un contexto para proporcionarselo al primer
			// eslabon de la cadena.
			TaskContextImpl startTaskContext = new TaskContextImpl();
			// Se lanza el primer eslabon de la cadena.
			startTaskContext.executeFirst();
		}
	}
	
	public class TaskContextImpl implements ITaskContext {
		
		public void executeFirst() {
			currentLink = headTaskLinks.remove(0);
			if (currentLink != null) {
				currentLink.execute(this);
			}
		}
		
		@Override
		public void execute(ITaskLink taskLink) {
			// Se currentLink == null quiere decir que la cadena ha sido cancelada.
			if (currentLink != null) {
				if (taskLink != null) {
					// Se se ha proporcinado el siguiente eslabon continuamos.
					currentLink = taskLink;
				} else {
					// Si la subcadena ha terminado hay que mirar por si hay mas subcadenas
					if (headTaskLinks.size() > 0) {
						// Todavia quedan mas subcadenas
						currentLink = headTaskLinks.remove(0);
					} else {
						// Ya no queda nada mas que hacer.
						currentLink = null;
					}
				}
				// Si hay algo pendiente se continua
				if (currentLink != null) {
					currentLink.execute(new TaskContextImpl());
				}
			}
		}

	}
	
}
