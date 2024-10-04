package gamestate;

public enum GameMode {
	FFA, TEAM;
	public static GameMode gameMode = FFA;
	
	public String toString() {
		switch (gameMode) {
		case FFA: return "ffa";
		case TEAM: return "team";
		default: return "";
		}
	}
	
	public static GameMode toMode(String mode) {
		switch (mode) {
		case "ffa": return FFA;
		case "team": return TEAM;
		default: return FFA;
		}
	}
}
