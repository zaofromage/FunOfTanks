package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import client.Game;
import gamestate.GameState;
import gamestate.Playing;
import player.Player;
import serverHost.Role;
import serverHost.Server;
import utils.Finder;

public class HostMenu extends PopUpMenu {

	private TextInput name;
	private ArrayList<Button> buttons;

	private String ip;
	
	public static Font playerFont = new Font("SansSerif", Font.BOLD, 30);
	private Font ipFont;

	public HostMenu(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow);
		buttons = new ArrayList<Button>();
		ipFont = new Font("SansSerif", Font.PLAIN, 25);
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
						buttons.get(1).setEnabled(true);
					} else {
						Game.printErrorMessage("Please enter a name !");
					}
				}));
		buttons.add(
				new Button(game.getPanel().getDimension().width / 2 - 150, 350, 300, 75, Color.red, "READY", () -> {
					game.getPlayer().setReady(!game.getPlayer().isReady());
					game.getPlayer().getClient().send("ready;" + game.getPlayer().getName() + ";" + game.getPlayer().isReady());
					buttons.get(1).setColor(game.getPlayer().isReady() ? Color.green : Color.red);
				}));
		buttons.get(1).setEnabled(false);
		buttons.add(new Button(game.getPanel().getDimension().width / 2 - 150, 500, 300, 75, Color.green, "PLAY",
				() -> {
					if (game.getPlayer() != null) {
						game.getPlayer().getClient().send("play;");
						game.setPlaying(
								new Playing(game.getPanel(), game.getPlayer(), game.getMenu().getPlayers()));
						GameState.state = GameState.PLAYING;
					} else {
						Game.printErrorMessage("crée un joueur stp soit pas con");
					}
				}));
		name = new TextInput(x + 50, y + 50, 180, 30, "name ", new Font("SansSerif", Font.PLAIN, 20), 15);
		this.game = game;
	}

	@Override
	public void update() {
		name.update();
		if (game.getMenu().getPlayers().size() > 0 && game.getMenu().getPlayers().stream().filter(p -> p.isReady()).count() == game.getMenu().getPlayers()
				.size()) {
			buttons.get(2).setEnabled(true);
		} else {
			buttons.get(2).setEnabled(false);
		}
	}

	@Override
	public void draw(Graphics g) {
		superDraw(g);
		for (Button b : buttons) {
			b.draw(g);
		}
		name.draw(g);
		if (game.getMenu().getPlayersPresent() != null) {
			g.setFont(playerFont);
			formatPlayers(game.getMenu().getPlayersPresent(), g);
		}
		if (ip != null) {
			g.setFont(ipFont);
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
