package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import serverHost.Role;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Scanner;

import UI.Button;
import client.Game;
import player.Player;

public class Menu implements Statemethods {

	private Scanner sc = new Scanner(System.in);

	private Role choosenRole;
	private Game game;

	private ArrayList<Button> buttons;

	private String playersPresent;
	private ArrayList<Player> players;

	public Menu(Game game) {
		this.game = game;
		this.buttons = new ArrayList<>();
		this.players = new ArrayList<Player>();
		buttons.add(new Button(700, 200, 200, 50, Color.cyan, "CREATE PLAYER", () -> {
			System.out.println(game.getPlayer());

			if (choosenRole == null) {
				System.out.println("Choisis un role !");
				return;
			}
			System.out.print("Nom d'utilisateur : ");
			String name = sc.nextLine();
			if (choosenRole == Role.HOST) {
				if (game.getPlayer() != null) {
					// game.getPlayer().getClient().stopConnection();
					// game.getPlayer().getServer().stop();
				}
				Player p = new Player(name, choosenRole, game, true);
				game.setPlayer(p);
				players.add(p);
			} else if (choosenRole == Role.GUEST) {
				if (game.getPlayer() != null) {
					// game.getPlayer().getClient().stopConnection();
				}
				System.out.print("Entrer l'adresse ip du host : ");
				String ip = sc.nextLine();
				System.out.print("Entrer le port : ");
				String port = sc.nextLine();
				Player p = new Player(name, choosenRole, ip, Integer.parseInt(port), game, true);
				game.setPlayer(p);
				players.add(p);
			}
			game.getPlayer().getClient().send("newplayer;" + game.getPlayer().getName());
			System.out.println("Joueur crée !");
		}));

		buttons.add(new Button(200, 200, 200, 50, Color.red, "HOST", () -> {
			choosenRole = Role.HOST;
			if (game.getPlayer() != null) {
				game.getPlayer().setRole(choosenRole);
			}
			System.out.println("Tu deviens le host de la game");
		}));
		buttons.add(new Button(200, 400, 200, 50, Color.blue, "JOIN", () -> {
			choosenRole = Role.GUEST;
			if (game.getPlayer() != null) {
				game.getPlayer().setRole(choosenRole);
			}
			System.out.println("Tu rejoins une game");
		}));
		buttons.add(new Button(400, 200, 200, 50, Color.green, "PLAY", () -> {
			if (game.getPlayer() != null) {
				game.setPlaying(new Playing(game.getPanel(), game.getPlayer(), players));
			} else {
				System.out.println("crée un joueur stp soit pas con");
			}
			GameState.state = GameState.PLAYING;
		}));
	}

	@Override
	public void update() {
		for (Button b : buttons) {
			b.update();
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawString("MENU", 500, 500);
		if (playersPresent != null) {
			g.drawString(playersPresent, 100, 100);
		}
		for (Button b : buttons) {
			b.draw(g);
		}
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
		for (Button b : buttons) {
			b.onClick(e);
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

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
