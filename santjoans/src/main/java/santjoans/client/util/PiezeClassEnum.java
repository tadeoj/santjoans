package santjoans.client.util;

public enum PiezeClassEnum {
	
	MAIN() {
		@Override
		public String getPath() {
			return "main";
		}
	},
	CENTER() {
		@Override
		public String getPath() {
			return "center";
		}
	};
	
	abstract public String getPath();
	

}
