package serverHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import client.Game;
import gamestate.Domination;
import gamestate.GameMode;
import gamestate.GameState;
import gamestate.Menu;
import gamestate.Playing;
import gamestate.TeamMode;
import map.Obstacle;
import player.Player;
import serverClass.ServerBullet;
import utils.Finder;

public class ServerConnection implements Runnable {

	private Socket server;
	private BufferedReader in;

	private Game game;

	public ServerConnection(Socket s, Game game) throws IOException {
		this.game = game;
		server = s;
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}

	@Override
	public void run() {
		String serverResponse;
		try {
			while (true) {
				serverResponse = in.readLine();
				// System.out.println("Server says : " + serverResponse);
				if (serverResponse != null) {
					String header = ClientHandler.getHeader(serverResponse);
					String[] body = ClientHandler.getBody(serverResponse);
					switch (GameState.state) {
					case MENU:
						Menu menu = game.getMenu();
						if (header.equals("players")) {
							menu.setPlayersPresent(serverResponse);
							ArrayList<Player> toAdd = new ArrayList<>();
							for (String name : body) {
								Player p = Finder.findPlayer(name, menu.getPlayers());
								if (p == null) {
									toAdd.add(new Player(name, Role.GUEST, game, false));
								}
							}
							if (GameMode.gameMode == GameMode.FFA) {
								int i = 1;
								for (Player p : game.getMenu().getPlayers()) {
									p.setTeam(i);
									i++;
								}								
							}
							menu.getPlayers().addAll(toAdd);
						} else if (header.equals("ready")) {
							System.out.println(serverResponse);
							Player p = Finder.findPlayer(body[0], menu.getPlayers());
							if (p != null) {
								p.setReady(Boolean.parseBoolean(body[1]));
							}
						} else if (header.equals("play")) {
							switch(GameMode.gameMode) {
							case FFA:game.setPlaying(new Playing(game.getPanel(), game.getPlayer(), game.getMenu().getPlayers()));break;
							case TEAM:game.setPlaying(new TeamMode(game.getPanel(), game.getPlayer(), game.getMenu().getPlayers()));break;
							case DOMINATION:game.setPlaying(new Domination(game.getPanel(), game.getPlayer(), game.getMenu().getPlayers()));break;
							}
							GameState.state = GameState.PLAYING;
						} else if (header.equals("team")) {
							Player p = Finder.findPlayer(body[0], menu.getPlayers());
							p.setTeam(Integer.parseInt(body[1]));
						} else if (header.equals("mode")) {
							GameMode.gameMode = GameMode.toMode(body[0]);
							if (body[0].equals("ffa")) {
								int i = 1;
								for (Player p : game.getMenu().getPlayers()) {
									p.setTeam(i);
									i++;
								}
							} else {
								for (Player p : game.getMenu().getPlayers()) {
									p.setTeam(1);
								}
							}
						}
						break;
					case PLAYING:
						Playing play = game.getPlaying();
						if (header.equals("newtank")) {
							Player p = Finder.findPlayer(body[0], play.getPlayers());
							if (p != null) {
								p.createTank(Integer.parseInt(body[1]), Integer.parseInt(body[2]));
							}
						} else if (header.equals("updatetank")) {
							Player p = Finder.findPlayer(body[0], play.getPlayers());
							if (p != null) {
								if (p.getTank() != null) {
									p.getTank().setX(Integer.parseInt(body[1]));
									p.getTank().setY(Integer.parseInt(body[2]));
									p.getTank().setOrientation(Double.parseDouble(body[3]));
								}
							}
						} else if (header.equals("deletetank")) {
							System.out.println(serverResponse);
							Player p = Finder.findPlayer(body[0], play.getPlayers());
							if (p != null) {
								play.getLeaderBoard().merge(p, 1, Integer::sum);
								p.deleteTank();
							}
						} else if (header.equals("newbullet")) {
							play.getEnemiesBullets()
									.add(new ServerBullet(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
											Double.parseDouble(body[2]), body[3], Integer.parseInt(body[4]), Boolean.parseBoolean(body[5])));
						} else if (header.equals("updatebullet")) {
							ServerBullet b = Finder.findServerBullet(body[3], Integer.parseInt(body[0]),
									play.getEnemiesBullets());
							if (b != null) {
								b.update(Integer.parseInt(body[1]), Integer.parseInt(body[2]));
							}
						} else if (header.equals("deletebullet")) {
							ServerBullet bullet = Finder.findServerBullet(body[0], Integer.parseInt(body[1]),
									play.getEnemiesBullets());
							if (bullet != null) {
								play.getPlayer().blowup(bullet.x, bullet.y, 0.2);
								play.getEnemiesBullets().remove(bullet);
							}
						} else if (header.equals("newobstacle")) {
							play.getObsToAdd().add(new Obstacle(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
									Boolean.parseBoolean(body[2])));
						} else if (header.equals("deleteobstacle")) {
							Obstacle o = Finder.findObstacle(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
									play.getObstacles());
							if (o != null) {
								play.getObsToRemove().add(o);
							}
						} else if (header.equals("point")) {
							if (play instanceof Domination) {
								Domination dom = (Domination) play;
								dom.getZone().setPoints(Integer.parseInt(body[0]));
							}
						}
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
