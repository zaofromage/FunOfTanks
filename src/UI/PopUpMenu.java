package UI;

import java.awt.Color;
import java.awt.Graphics;

import client.Game;
import player.Player;
import utils.Finder;

public abstract class PopUpMenu {
	
	private int x, y, width, height;
	private Color backgroundColor;
	protected Game game;
	
	public PopUpMenu(int x, int y, int width, int height, Color bcolor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = bcolor;
	}
	
	public void update() {}
	
	public void draw(Graphics g) {
		superDraw(g);
	}
	
	protected void superDraw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
	}
	
	public void formatPlayers(String players, Graphics g) {
		String[] p = players.split(";");
		for (int i = 1; i < p.length; i++) {
			Player pl = Finder.findPlayer(p[i], game.getMenu().getPlayers());
			if (pl != null) {
				String ready = pl.isReady()?"V":"X";
				g.drawString(p[i] + "  " + ready, 950, 100 + 50 * i);
			}
		}
	}
}
