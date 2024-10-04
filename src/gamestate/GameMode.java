package gamestate;

public enum GameMode {
	FFA, TEAM, DOMINATION;
	public static GameMode gameMode = FFA;
	
	public String toString() {
		switch (gameMode) {
		case FFA: return "ffa";
		case TEAM: return "team";
		case DOMINATION: return "domination";
		default: return "ffa";
		}
	}
	
	public static GameMode toMode(String mode) {
		switch (mode) {
		case "ffa": return FFA;
		case "team": return TEAM;
		case "domination": return DOMINATION;
		default: return FFA;
		}
	}
}
