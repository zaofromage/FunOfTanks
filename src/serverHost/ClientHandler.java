package serverHost;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import gamestate.GameMode;
import map.Obstacle;
import player.TypeShot;
import serverClass.*;
import utils.Finder;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	private ArrayList<ClientHandler> clients;
	private Server server;
	
	private boolean running = true;
	
	public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients, Server server) throws IOException {
		client = clientSocket;
		this.server = server;
		this.clients = clients;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		out.println("Connected to server");
	}

	@Override
	public void run() {
		try {
			while (running) {
				String request = in.readLine();
				if (request != null) {
					String header = getHeader(request);
					String[] body = getBody(request);
					if (header.equals("newplayer")) {
						server.getPlaying().getPlayers().add(new ServerPlayer(body[0], false, 1));
						sendToAll(stringifyServerPlayers(server.getPlaying().getPlayers()));
						for (ServerPlayer p : server.getPlaying().getPlayers()) {
							sendToAll("ready;"+p.name+";"+p.ready);
							sendToAll("team;"+p.name+";"+p.team);
						}
						sendToAll("mode;"+GameMode.gameMode.toString());
					} else if (header.equals("newtank")) {
						server.getPlaying().getTanks().add(new ServerTank(Integer.parseInt(body[0]), Integer.parseInt(body[1]), Double.parseDouble(body[2]), body[3], body[4]));
						sendToAllOthers("newtank;" + body[4] + ";" + body[0] + ";" + body[1]);
					} else if (header.equals("deletetank")) {
						ServerTank tank = Finder.findServerTank(body[0], server.getPlaying().getTanks());
						if (tank != null) {
							server.getPlaying().getTanks().remove(tank);
							sendToAll(request);						
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
						server.getPlaying().getBullets().add(b);
						sendToAllOthers(request);
					} else if (header.equals("deletebullet")) {
						ServerBullet bullet = Finder.findServerBullet(body[0],Integer.parseInt(body[1]), server.getPlaying().getBullets());
						if (bullet != null) {
							server.getPlaying().getBullets().remove(bullet);
							sendToAllOthers(request);
						}
					} else if (header.equals("newobstacle")) {
						server.getPlaying().getObstacles().add(new Obstacle(Integer.parseInt(body[0]), Integer.parseInt(body[1]), Boolean.parseBoolean(body[2])));
						sendToAll(request);
					} else if (header.equals("deleteobstacle")) {
						Obstacle o = Finder.findObstacle(Integer.parseInt(body[0]), Integer.parseInt(body[1]), server.getPlaying().getObstacles());
						if (o != null) {
							server.getPlaying().getObstacles().remove(o);
							sendToAll(request);
						}
					} else if (header.equals("ready")) {
						ServerPlayer p = Finder.findServerPlayer(body[0], server.getPlaying().getPlayers());
						if (p != null) p.ready = Boolean.parseBoolean(body[1]);
						sendToAll(request);
					} else if (header.equals("team")) {
						sendToAll(request);
					} else if (header.equals("mode")) {
						sendToAll(request);
					} else if (header.equals("play")) {
						sendToAllOthers(request);
					} else if (header.equals("cancel")) {
						ServerPlayer p = server.getPlaying().getPlayers().stream().filter(player -> player.name.equals(body[0])).findAny().orElse(null);
						if (p != null) {
							server.getPlaying().getPlayers().remove(p);
						}
						sendToAll(request);
					}
					else {
						out.println("wrong request");
					}	
				}
				
			}
		} catch (IOException e) {
			if (running) {
				System.out.println("client handler pas normal");
			} else {
				System.out.println("server fermé");
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.close();
		}
	}
	
	public void close() {
		running = false;
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String stringifyServerPlayers(ArrayList<ServerPlayer> players) {
		String res = "players";
		for (ServerPlayer p : players) {
			res += ";" + p.name;
		}
		return res;
	}

	private void sendToAll(String msg) {
		for (ClientHandler c : clients) {
			c.out.println(msg);
		}
	}
	
	private void sendToAllOthers(String msg) {
		ArrayList<ClientHandler> others = new ArrayList<>(clients);
		others.remove(this);
		for (ClientHandler c : others) {
			c.out.println(msg);
		}
	}
	
	public static String getHeader(String req) {
		return req.split(";")[0];
	}
	
	public static String[] getBody(String req) {
		String[] split = req.split(";");
		String[] body = new String[split.length  - 1];
		for (int i = 1; i < split.length; i++) {
			body[i - 1] = split[i];
		}
		return body;
	}

}
