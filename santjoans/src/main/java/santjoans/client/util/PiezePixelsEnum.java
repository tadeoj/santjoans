package santjoans.client.util;

public enum PiezePixelsEnum {
	
	PP550() {
		@Override
		public String getPath() {
			return "550";
		}
	},
	PP360() {
		@Override
		public String getPath() {
			return "360";
		}
	},
	PP60() {
		@Override
		public String getPath() {
			return "60";
		}
	};
	
	abstract public String getPath();
	

}
