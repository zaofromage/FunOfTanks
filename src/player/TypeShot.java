package player;

public enum TypeShot {
	NORMAL, BERTHA, GRENADE, TRIPLE;
	
	public static TypeShot parseTypeShot(String str) {
		switch (str) {
		case "NORMAL":return NORMAL;
		case "BERTHA":return BERTHA;
		case "GRENADE":return GRENADE;
		case "TRIPLE":return TRIPLE;
		default: return null;
		}
	}
}
