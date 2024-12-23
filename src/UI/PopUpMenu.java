package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import client.Game;
import client.GamePanel;
import model.gamestate.GameMode;
import player.Player;
import player.Skill;
import utils.Finder;

public abstract class PopUpMenu {
	
	protected int x, y, width, height;
	private Color backgroundColor;
	protected Game game;
	protected ArrayList<Button> buttons = new ArrayList<>();
	protected SkillMenu skillMenu = null;
	protected int previousLength;
	protected ArrayList<Player> players;
	
	public PopUpMenu(int x, int y, int width, int height, Color bcolor, Game game, ArrayList<Player> players) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = bcolor;
		this.players = players;
		buttons.add(new Button(x+width, y+height/2-25, 50, 50, Color.magenta, ">", () -> {
			if (skillMenu == null) {
				skillMenu = new SkillMenu(x+width+100, y+100, game.getPlayer(), Skill.skills);
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
		if (players != null && skillMenu == null) {
			g.setFont(HostMenu.playerFont);
			formatPlayers(players, g);
		} else if (skillMenu != null) {
			skillMenu.draw(g);
		}
		g.setColor(Color.black);
		if (game.getMenu() != null && game.getMenu().getSettings() == null) {
			g.drawString(GameMode.gameMode.toString(), x+width + 250, 750);			
		}
		for (Button b : buttons) {
			b.draw(g);
		}			
	}
	
	public void formatPlayers(ArrayList<Player> players, Graphics g) {
		int i = 1;
		for (Player p : players) {
			if (p != null) {
				String ready = p.isReady()?"V":"X";
				if (GameMode.gameMode == GameMode.FFA) {
					g.drawString(p.getName() + "  " + ready, x+width+100, 75+50 * i);
				} else {
					g.drawString(p.getName() + "  " + ready, p.getTeam() == 1 ? x+width+100:x+width+400, 75+50 * i);
				}
			}
			i++;
		}
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (skillMenu != null) {
			skillMenu.mouseClicked(e);
		}
		for (Button b : buttons) {
			b.onClick(e);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (skillMenu != null) {
			skillMenu.keyPressed(e);
		}
	}
}
