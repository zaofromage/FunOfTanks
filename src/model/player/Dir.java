package model.player;


public enum Dir {
	RIGHT, LEFT, UP, DOWN;

	public static String toString(Dir d) {
		switch (d) {
		case RIGHT: return "right";
		case LEFT: return "left";
		case UP: return "up";
		case DOWN: return "down";
		default: return null;
		}
	}
	
	public static Dir fromString(String d) {
		switch (d) {
		case "right": return RIGHT;
		case "left": return LEFT;
		case "up": return UP;
		case "down": return DOWN;
		default: return null;
		}
	}
}
