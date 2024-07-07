package player;

public class Player {
	private Tank tank;
	
	public Player() {
		tank = new Tank();
	}
	
	public Tank getTank() { return this.tank; }
}
