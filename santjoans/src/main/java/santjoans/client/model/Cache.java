package santjoans.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import santjoans.client.util.PiezeClassEnum;
import santjoans.client.util.PiezePixelsEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;

public class Cache {

	private Map<String,CacheEntry> map;
	private PiezeClassEnum piezeClassEnum;
	
	public Cache(PiezeClassEnum piezeClassEnum) {
		this.piezeClassEnum = piezeClassEnum;
		this.map = new HashMap<String, CacheEntry>();
	}
	
	private String normalizeImageName(String imageName) {
		return imageName.toLowerCase();
	}
	
	public ICacheEntry getCacheEntry(IModelEntry modelEntry) {
		CacheEntry cacheEntry = map.get(normalizeImageName(modelEntry.getName()));
		if (cacheEntry == null) {
			// Todavia no existe una entrada de la cache para esta imagen
			cacheEntry = new CacheEntry(modelEntry.getName());
			map.put(cacheEntry.getImageName(), cacheEntry);
		}
		cacheEntry.addReference(modelEntry);
		return cacheEntry;
	}
	
	private class CacheEntry implements ICacheEntry {
		
		private String imageName;
		private Map<PiezePixelsEnum,ImageElement> images;
		private List<IModelEntry> references;
		
		public CacheEntry(String imageName) {
			this.imageName = imageName;
			images = new HashMap<PiezePixelsEnum, ImageElement>();
			references = new ArrayList<IModelEntry>(); 
		}
		
		public String getImageName() {
			return normalizeImageName(imageName);
		}

		@Override
		public String getImageUrl(PiezePixelsEnum piezePixelsEnum) {
			return GWT.getModuleBaseURL() + "piezes/" + piezePixelsEnum.getPath() + "/" + piezeClassEnum.getPath() + "/" + getImageName();
		}

		@Override
		public ImageElement getImageElement(PiezePixelsEnum piezePixelsEnum) {
			return images.get(piezePixelsEnum);
		}

		@Override
		public void setImageElement(PiezePixelsEnum piezePixelsEnum, ImageElement imageElement) {
			images.put(piezePixelsEnum, imageElement);
		}

		@Override
		public void addReference(IModelEntry modelEntry) {
			references.add(modelEntry);
		}

		@Override
		public List<IModelEntry> getReferences() {
			return references;
		}
		
		
	}
}
