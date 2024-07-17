package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import client.Game;
import gamestate.GameState;
import gamestate.Playing;
import player.Player;
import serverHost.Role;

public class JoinMenu extends PopUpMenu {
	
	private TextInput name;
	private TextInput ip;
	private TextInput port;
	private ArrayList<Button> buttons;

	private Game game;
	
	public JoinMenu(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow);
		buttons = new ArrayList<Button>();
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 350, 300, 75, Color.cyan,
				"CREATE PLAYER", () -> {
					Player p = new Player(name.getText(), Role.GUEST, ip.getIP(), port.getPort(), game, true);
					game.setPlayer(p);
					game.getMenu().getPlayers().add(p);
					game.getPlayer().getClient().send("newplayer;" + game.getPlayer().getName());
				}));
		buttons.add(
				new Button(game.getPanel().getDimension().width / 2 - 150, 450, 300, 75, Color.green, "PLAY", () -> {
					game.getPlayer().setReady(true);
				}));
		name = new TextInput(x + 50, y + 50, 180, 30, "name ", new Font("SansSerif", Font.PLAIN, 20), 15);
		ip = new TextInput(x + 50, y + 100, 200, 30, "ip ", new Font("SansSerif", Font.PLAIN, 20), 16);
		port = new TextInput(x + 50, y + 150, 180, 30, "port ", new Font("SansSerif", Font.PLAIN, 20), 4);
		this.game = game;
	}
	
	@Override
	public void update() {
		name.update();
		ip.update();
		port.update();
	}

	@Override
	public void draw(Graphics g) {
		superDraw(g);
		for (Button b : buttons) {
			b.draw(g);
		}
		if (game.getMenu().getPlayers().stream().filter(p -> p.isReady()).count() == game.getMenu().getPlayers()
				.size()) {
			buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 600, 300, 75, Color.green, "PLAY",
					() -> {
						if (game.getPlayer() != null) {
							game.setPlaying(
									new Playing(game.getPanel(), game.getPlayer(), game.getMenu().getPlayers()));
							GameState.state = GameState.PLAYING;
						} else {
							Game.printErrorMessage("crée un joueur stp soit pas con");
						}
					}));
		}
		name.draw(g);
		ip.draw(g);
		port.draw(g);
	}

	public void mouseClicked(MouseEvent e) {
		name.onClick(e);
		ip.onClick(e);
		port.onClick(e);
	}

	public void keyPressed(KeyEvent e) {
		name.keyPressed(e);
		ip.keyPressed(e);
		port.keyPressed(e);
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
}
