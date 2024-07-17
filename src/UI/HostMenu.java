package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import client.Game;
import gamestate.GameState;
import gamestate.Playing;
import player.Player;
import serverHost.Role;
import serverHost.Server;

public class HostMenu extends PopUpMenu {

	private TextInput name;
	private ArrayList<Button> buttons;

	private Game game;

	private String ip;

	public HostMenu(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow);
		buttons = new ArrayList<Button>();
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 200, 300, 75, Color.cyan,
				"CREATE PLAYER", () -> {
					if (name.getText().length() > 0) {
						Player p = new Player(name.getText(), Role.HOST, game, true);
						game.setPlayer(p);
						game.getMenu().getPlayers().add(p);
						try {
							ip = "IP : " + InetAddress.getLocalHost().toString().split("/")[1] + " PORT : "
									+ Server.PORT;
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						game.getPlayer().getClient().send("newplayer;" + game.getPlayer().getName());
					} else {
						Game.printErrorMessage("Please enter a name !");
					}
				}));
		buttons.add(
				new Button(game.getPanel().getDimension().width / 2 - 150, 350, 300, 75, Color.green, "READY", () -> {
					game.getPlayer().setReady(true);
				}));
		name = new TextInput(x + 50, y + 50, 180, 30, "name ", new Font("SansSerif", Font.PLAIN, 20), 15);
		this.game = game;
	}

	@Override
	public void update() {
		name.update();
	}

	@Override
	public void draw(Graphics g) {
		superDraw(g);
		for (Button b : buttons) {
			b.draw(g);
		}
		if (game.getMenu().getPlayers().stream().filter(p -> p.isReady()).count() == game.getMenu().getPlayers()
				.size()) {
			buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 500, 300, 75, Color.green, "PLAY",
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
		if (ip != null) {
			g.drawString(ip, game.getPanel().getDimension().width / 2 - g.getFontMetrics().stringWidth(ip) / 2, 700);
		}
	}

	public void mouseClicked(MouseEvent e) {
		name.onClick(e);
	}

	public void keyPressed(KeyEvent e) {
		name.keyPressed(e);
	}

	public ArrayList<Button> getButtons() {
		return buttons;
	}
}
