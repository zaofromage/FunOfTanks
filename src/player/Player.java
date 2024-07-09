package player;

import java.util.ArrayList;
import java.awt.Graphics;

import map.Obstacle;

public class Player {
	private Tank tank;
	
	public Player(int x, int y) {
		tank = new Tank(x, y);
	}
	
	public void drawPlayer(Graphics g) {
		tank.drawTank(g);
	}
	
	public void updatePlayer(ArrayList<Obstacle> obs, ArrayList<Player> players, ArrayList<Player> toRemove) {
		tank.updateTank(obs, players, this, toRemove);
	}
	
	public Tank getTank() { return this.tank; }
}
