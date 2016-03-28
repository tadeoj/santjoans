package santjoans.client.model;

import java.util.ArrayList;
import java.util.List;

public class ModelDirectory {
	
	static private Model mainModel;
	static private Model centerModel;
	
	static private List<Model> models;
	
	static {
		mainModel = new MainModel();
		centerModel = new CenterModel();
		models = new ArrayList<Model>();
		models.add(mainModel);
		models.add(centerModel);
	}
	
	static public Model getMainModel() {
		return mainModel;
	}
	
	static public Model getCenterModel() {
		return centerModel;
	}
	
	static public List<Model> getModelList() {
		return models;
	}
	
}
