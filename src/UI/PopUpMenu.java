package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import client.Game;
import gamestate.GameMode;
import player.Player;
import utils.Finder;

public abstract class PopUpMenu {
	
	private int x, y, width, height;
	private Color backgroundColor;
	protected Game game;
	protected ArrayList<Button> buttons = new ArrayList<>();
	
	public PopUpMenu(int x, int y, int width, int height, Color bcolor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = bcolor;
	}
	
	public void update() {
		for (Button b : buttons) {
			b.update();
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawString(GameMode.gameMode.toString(), 100, 700);
		for (Button b : buttons) {
			b.draw(g);
		}
	}
	
	public void formatPlayers(String players, Graphics g) {
		String[] p = players.split(";");
		for (int i = 1; i < p.length; i++) {
			Player pl = Finder.findPlayer(p[i], game.getMenu().getPlayers());
			if (pl != null) {
				String ready = pl.isReady()?"V":"X";
				if (GameMode.gameMode == GameMode.FFA) {
					g.drawString(p[i] + "  " + ready, 100, 100 + 50 * i);
				} else {
					g.drawString(p[i] + "  " + ready, pl.getTeam() == 1 ? 100:950, 100 + 50 * i);
				}
			}
		}
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
}
