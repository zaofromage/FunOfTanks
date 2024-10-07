package gamestate;

import java.awt.Graphics;
import java.util.ArrayList;

import client.GamePanel;
import map.Zone;
import player.Player;

public class Domination extends Playing {
	
	private Zone zone;

	public Domination(GamePanel panel, Player player, ArrayList<Player> players) {
		super(panel, player, players);
		zone = new Zone(panel.getDimension().getWidth()/2-200, panel.getDimension().getHeight()/2-200, 400, 400, this);
	}
	
	@Override
	public void update() {
		zone.update();
		super.update();
	}
	
	@Override
	public void draw(Graphics g) {
		zone.draw(g);
		super.draw(g);
	}
 
}
