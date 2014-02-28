package santjoans.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.util.PiezeClassEnum;

public class MainModel extends Model {

	public MainModel() {
		super(PiezeClassEnum.MAIN);
	}

	@Override
	public List<IModelEntry> queryByContext(Collection<IModelEntry> entries, IControllerViewerContext context) {
		List<IModelEntry> piezes = new ArrayList<IModelEntry>();
		for (IModelEntry entry : entries) {
			if (	entry.getX() >= context.getStartX() && 
					entry.getY() >= context.getStartY() &&  
					entry.getX() <= context.getEndX() &&
					entry.getY() <= context.getEndY()) {
				piezes.add(entry);
			}
		}
		return piezes;
	}
	
}
