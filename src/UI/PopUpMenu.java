package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import client.Game;
import client.GamePanel;
import gamestate.GameMode;
import player.Player;
import utils.Finder;

public abstract class PopUpMenu {
	
	protected int x, y, width, height;
	private Color backgroundColor;
	protected Game game;
	protected ArrayList<Button> buttons = new ArrayList<>();
	protected SkillMenu skillMenu = null;
	protected int previousLength;
	
	public PopUpMenu(int x, int y, int width, int height, Color bcolor, Game game) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = bcolor;
		buttons.add(new Button(x+width, y+height/2-25, 50, 50, Color.magenta, ">", () -> {
			if (skillMenu == null) {
				skillMenu = new SkillMenu(x+width+100, y+100, game.getPlayer());
				buttons.get(0).setText("<");
			} else {
				skillMenu = null;
				buttons.get(0).setText(">");
			}
		}));
		previousLength = buttons.size();
	}
	
	public void update() {
		for (Button b : buttons) {
			b.update();
		}
		buttons.get(0).setEnabled(game.getPlayer() != null);
		if (skillMenu != null) {
			skillMenu.update();
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.dimension.width, GamePanel.dimension.height);
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		if (game.getMenu().getPlayersPresent() != null && skillMenu == null) {
			g.setFont(HostMenu.playerFont);
			formatPlayers(game.getMenu().getPlayersPresent(), g);
		} else if (skillMenu != null) {
			skillMenu.draw(g);
		}
		g.setColor(Color.black);
		g.drawString(GameMode.gameMode.toString(), x+width + 250, 750);
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
					g.drawString(p[i] + "  " + ready, x+width+100, 75+50 * i);
				} else {
					g.drawString(p[i] + "  " + ready, pl.getTeam() == 1 ? x+width+100:x+width+400, 75+50 * i);
				}
			}
		}
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (skillMenu != null) {
			skillMenu.mouseClicked(e);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (skillMenu != null) {
			skillMenu.keyPressed(e);
		}
	}
}
