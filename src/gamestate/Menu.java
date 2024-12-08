package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import UI.Button;
import UI.HostMenu;
import UI.JoinMenu;
import UI.Settings;
import client.Game;
import client.GamePanel;
import player.Player;

public class Menu implements Statemethods {

	private Game game;

	private ArrayList<Button> buttons;

	private String playersPresent;
	private ArrayList<Player> players;
	
	private Button cancelButton;
	private boolean activeCancelButton;
	
	private HostMenu hostMenu;
	private JoinMenu joinMenu;
	private Settings settings;

	public Menu(Game game) {
		this.game = game;
		this.buttons = new ArrayList<>();
		this.players = new ArrayList<Player>();
		cancelButton = new Button(50, 50, 50, 50, Color.red, "X", () -> {
			Game.fade();
			hostMenu = null;
			joinMenu = null;
			settings = null;
			activeCancelButton = false;
			if (game.getPlayer() != null) {
				if (game.getPlayer().getClient() != null) {
					game.getPlayer().getClient().send("cancel;" + game.getPlayer().getName() + ";" + game.getPlayer().getRole());
				}
				game.getPlayer().close();
				game.setPlayer(null);
			}
			players.clear();
		});
		activeCancelButton = false;
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 200, 300, 75, Color.red, "HOST A GAME", () -> {
			Game.fade();
			activeCancelButton = true;
			hostMenu = new HostMenu(game.getPanel().getDimension().width / 2 - 500, 50, game);
		}));
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 350, 300, 75, Color.blue, "JOIN A GAME", () -> {
			Game.fade();
			activeCancelButton = true;
			joinMenu = new JoinMenu(game.getPanel().getDimension().width / 2 - 500, 50, game);
		}));
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 600, 300, 75, Color.darkGray, "Settings", () -> {
			Game.fade();
			activeCancelButton = true;
			settings = new Settings(game.getPanel().getDimension().width / 2 - 500/2, 50, game);
		}));
	}

	@Override
	public void update() {
		for (Button b : buttons) {
			b.update();
		}
		if (hostMenu != null) {
			hostMenu.update();
		}
		if (joinMenu != null) {
			joinMenu.update();
		}
		if (settings != null) {
			settings.update();
		}
		if (activeCancelButton) cancelButton.update();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.dimension.width, GamePanel.dimension.height);
		g.setColor(Color.lightGray);
		g.fillRect(game.getPanel().getDimension().width / 2 - 500/2, 50, 500, 700);
		g.setColor(Color.black);
		g.setFont(new Font("SansSerif", Font.BOLD, 40));
		g.drawString("MENU", game.getPanel().getDimension().width / 2 - g.getFontMetrics().stringWidth("MENU")/2, 100);
		for (Button b : buttons) {
			b.draw(g);
		}
		if (hostMenu != null) {
			hostMenu.draw(g);
		}
		if (joinMenu != null) {
			joinMenu.draw(g);
		}
		if (settings != null) {
			settings.draw(g);
		}
		if (activeCancelButton) cancelButton.draw(g);
	}

	// inputs
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (hostMenu != null) {
			hostMenu.mouseClicked(e);
		} else if (joinMenu != null) {
			joinMenu.mouseClicked(e);
		} else if (settings != null) {
			settings.mouseClicked(e);
		}
		else {
			for (Button b : buttons) {
				b.onClick(e);
			}
		}
		if (activeCancelButton) cancelButton.onClick(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (hostMenu != null) {
			hostMenu.keyPressed(e);
		} else if (joinMenu != null) {
			joinMenu.keyPressed(e);
		} else if (settings != null) {
			settings.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public Game getGame() {
		return game;
	}

	public String getPlayersPresent() {
		return playersPresent;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayersPresent(String playersPresent) {
		this.playersPresent = playersPresent;
	}

	public HostMenu getHostMenu() {
		return hostMenu;
	}

	public void setHostMenu(HostMenu hostMenu) {
		this.hostMenu = hostMenu;
	}

	public JoinMenu getJoinMenu() {
		return joinMenu;
	}

	public void setJoinMenu(JoinMenu joinMenu) {
		this.joinMenu = joinMenu;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public boolean isActiveCancelButton() {
		return activeCancelButton;
	}

	public void setActiveCancelButton(boolean activeCancelButton) {
		this.activeCancelButton = activeCancelButton;
	}

}
