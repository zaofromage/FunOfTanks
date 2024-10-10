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
			hostMenu = null;
			joinMenu = null;
			settings = null;
			activeCancelButton = false;
			if (game.getPlayer() != null) {
				game.getPlayer().close();
				game.setPlayer(null);
			}
		});
		activeCancelButton = false;
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 200, 300, 75, Color.red, "HOST A GAME", () -> {
			activeCancelButton = true;
			hostMenu = new HostMenu(game.getPanel().getDimension().width / 2 - 500/2, 50, game);
		}));
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 400, 300, 75, Color.blue, "JOIN A GAME", () -> {
			activeCancelButton = true;
			joinMenu = new JoinMenu(game.getPanel().getDimension().width / 2 - 500/2, 50, game);
		}));
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 550, 300, 75, Color.darkGray, "Settings", () -> {
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
			settings.update();
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
			for (Button b : hostMenu.getButtons()) {
				b.onClick(e);
			}
			hostMenu.mouseClicked(e);
		} else if (joinMenu != null) {
			for (Button b : joinMenu.getButtons()) {
				b.onClick(e);
			}
			joinMenu.mouseClicked(e);
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

}
