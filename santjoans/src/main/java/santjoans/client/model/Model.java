package santjoans.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.util.PiezeClassEnum;
import santjoans.client.util.PiezePixelsEnum;
import santjoans.client.util.SantjoansMessages;
import santjoans.client.util.Util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;

abstract public class Model {
	
	protected SantjoansMessages messages = GWT.create(SantjoansMessages.class);
	
	private PiezeClassEnum piezeClassEnum;
	private Map<Integer,IModelEntry> map;
	private Cache cache;
	
	public Model(PiezeClassEnum piezeClassEnum) {
		this.piezeClassEnum = piezeClassEnum;
		map = new HashMap<Integer, IModelEntry>();
		cache = new Cache(piezeClassEnum);
	}
	
	public IPieze getPieze(int x, int y) {
		return map.get(Util.getIndex(x, y));
	}
	
	void addPieze(String name, int x, int y, int miniatureRotation, int detailRotation) {
		ModelEntry pieze = new ModelEntry(name, x, y, miniatureRotation, detailRotation);
		map.put(Util.getIndex(pieze.getX(), pieze.getY()), pieze);
	}
	
	public Collection<IModelEntry> getModelEntries() {
		return map.values();
	}
	
	abstract public List<IModelEntry> queryByContext(Collection<IModelEntry> entries, IControllerViewerContext context);

	
	public String getName() {
		return piezeClassEnum.toString();
	}
	
	public class ModelEntry implements IModelEntry {
		
		private String name;
		private int x;
		private int y;
		private int miniatureRotation;
		private int detailRotation;
		
		private ICacheEntry cacheEntry;
		
		public ModelEntry(String name, int x, int y, int miniatureRotation, int detailRotation) {
			this.name = name;
			this.x = x;
			this.y = y;
			this.miniatureRotation = miniatureRotation;
			this.detailRotation = detailRotation;
			this.cacheEntry = cache.getCacheEntry(this);
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public int getX() {
			return x;
		}
		
		@Override
		public int getY() {
			return y;
		}
		
		@Override
		public int getMiniatureRotation() {
			return miniatureRotation;
		}
		
		@Override
		public int getDetailRotation() {
			return detailRotation;
		}
		
		@Override
		public double getMiniatureRadians() {
			return Util.getRadians(getMiniatureRotation());
		}
		
		@Override
		public double getDetailRadians() {
			return Util.getRadians(getDetailRotation());
		}
		
		@Override
		public String getImageDetailedUrl() {
			return GWT.getModuleBaseURL() + "piezes/" + Util.getCurrentScreenType().getPiezePixelsForDetail().getPath() + "/" + piezeClassEnum.getPath() + "/" + getName();
		}

		@Override
		public ImageElement getImageElement(PiezePixelsEnum piezePixelsEnum) {
			return cacheEntry.getImageElement(piezePixelsEnum);
		}
		
		@Override
		public ICacheEntry getCacheEntry() {
			return cacheEntry;
		}

		@Override
		public String toString() {
			return messages.piezeToString(name, x, y, miniatureRotation, detailRotation);
		}

	}

}
