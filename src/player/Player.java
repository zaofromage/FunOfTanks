package player;

import java.util.ArrayList;
import java.awt.Graphics;

import map.Obstacle;

public class Player {
	private Tank tank;
	
	public Player() {
		tank = new Tank();
	}
	
	public void drawPlayer(Graphics g) {
		tank.drawTank(g);
	}
	
	public void updatePlayer(ArrayList<Obstacle> obs) {
		tank.updateTank(obs);
	}
	
	public Tank getTank() { return this.tank; }
}
