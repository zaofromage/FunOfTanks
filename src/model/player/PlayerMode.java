package model.player;


public enum PlayerMode {
	BASE, AIM, BLOC, GRAB;
	
	public static String toString(PlayerMode m) {
		switch (m) {
		case BASE: return "base";
		case AIM: return "aim";
		case BLOC: return "bloc";
		case GRAB: return "grab";
		default: return "ffa";
		}
	}
	
	public static PlayerMode fromString(String m) {
		switch (m) {
		case "base": return BASE;
		case "aim" : return AIM;
		case "bloc": return BLOC;
		case "grab": return GRAB;
		default: return null;
		}
	}
}
