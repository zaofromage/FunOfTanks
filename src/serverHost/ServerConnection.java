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
import player.Skill;
import player.TypeShot;
import serverClass.*;
import utils.Finder;

public class ServerConnection implements Runnable {

	private Socket server;
	private BufferedReader in;

	private Game game;
	
	private boolean running = true;

	public ServerConnection(Socket s, Game game) throws IOException {
		this.game = game;
		server = s;
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}

	@Override
	public void run() {
		String serverResponse;
		try {
			while (running) {
				serverResponse = in.readLine();
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
							Skill.saveSkills(game.getPlayer());
							game.getPlayer().setReady(false);
							game.getPlayer().getClient().send("ready;" + game.getPlayer().getName() + ";" + game.getPlayer().isReady());
							GameState.state = GameState.PLAYING;
							game.setMenu(null);
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
						} else if (header.equals("cancel")) {
							Player p = Finder.findPlayer(body[0], menu.getPlayers());
							if (body[1].equals("HOST")) {
								menu.setHostMenu(null);
								menu.setJoinMenu(null);
								menu.setSettings(null);
								menu.setActiveCancelButton(false);
								if (game.getPlayer() != null) {
									game.getPlayer().close();
									game.setPlayer(null);
								}
								menu.getPlayers().clear();								
							} else {
								menu.getPlayers().remove(p);
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
						} else if (header.equals("deletetank")) {
							Player p = Finder.findPlayer(body[0], play.getPlayers());
							Player killer = Finder.findPlayer(body[1], play.getPlayers());								
							if (p != null) {
								p.deleteTank();
								if (game != null) {
									game.getPlaying().getLeaderBoard().get(p).death++;
									game.getPlaying().getLeaderBoard().get(killer).kills++;
								}
							}
						} else if (header.equals("newbullet")) {
							ServerBullet b = null;
							switch (TypeShot.parseTypeShot(body[5])) {
							case NORMAL:
								b = new ServerBullet(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
										Double.parseDouble(body[2]), body[3], Integer.parseInt(body[4]));
								break;
							case BERTHA:
								b = new ServerBertha(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
										Double.parseDouble(body[2]), body[3], Integer.parseInt(body[4]));
								break;
							case GRENADE:
								b = new ServerGrenade(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
										Double.parseDouble(body[2]), body[3], Integer.parseInt(body[4]));
								break;
							case TRIPLE:
								b = new ServerTriple(Integer.parseInt(body[0]), Integer.parseInt(body[1]),
										Double.parseDouble(body[2]), body[3], Integer.parseInt(body[4]));
								break;
							}
							play.getEnemiesBullets()
									.add(b);
						} else if (header.equals("deletebullet")) {
							ServerBullet bullet = Finder.findServerBullet(body[0], Integer.parseInt(body[1]),
									play.getEnemiesBullets());
							if (bullet != null) {
								play.deleteBullet(bullet);
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
						}
						break;
					case FINISH:
						break;
					}
				}
			}
		} catch (IOException e) {
			if (running) {
				System.out.println("pas normal server conn");
			} else {
				System.out.println("server fermé");
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		running = false;
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
