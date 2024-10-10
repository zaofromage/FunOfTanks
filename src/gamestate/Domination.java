package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import client.Game;
import client.GamePanel;
import map.Zone;
import player.Player;
import utils.Delay;

public class Domination extends Playing {
	
	public static final int FINISH_POINTS = 10000;
	
	private Zone zone;

	public Domination(GamePanel panel, Player player, ArrayList<Player> players) {
		super(panel, player, players);
		zone = new Zone(panel.getDimension().getWidth()/2-200, panel.getDimension().getHeight()/2-200, 400, 400, this);
	}
	
	@Override
	public void update() {
		zone.update();
		super.update();
		if (zone.getPoints() >= FINISH_POINTS) {
			new Delay(3000, () -> {
				isFinish = 1;				
			});
			Game.printMessage("Fin de partie");
		} else if (zone.getPoints() <= -1*FINISH_POINTS) {
			new Delay(3000, () -> {
				isFinish = 2;				
			});
			Game.printMessage("Fin de partie");
		}
	}
	
	@Override
	public void draw(Graphics g) {
		zone.draw(g);
		super.draw(g);
		g.setColor(Color.RED);
		g.fillRect(310, 10, 640, 30);
		g.setColor(Color.BLUE);
		g.fillRect(310, 10, 320+(zone.getPoints()*320/FINISH_POINTS), 30);
	}
	
	public Zone getZone() {
		return zone;
	}
 
}
