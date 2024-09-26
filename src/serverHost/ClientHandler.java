package serverHost;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


import map.Obstacle;
import serverClass.*;
import utils.Finder;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	private ArrayList<ClientHandler> clients;
	private Server server;
	
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
			while (true) {
				String request = in.readLine();
				//System.out.println("Client says : " + request);
				String header = getHeader(request);
				String[] body = getBody(request);
				if (header.equals("newplayer")) {
					server.getPlaying().getPlayers().add(body[0]);
					server.getPlaying().getPlayersReady().add(false);
					sendToAll(stringifyServerPlayers(server.getPlaying().getPlayers()));
				} else if (header.equals("newtank")) {
					server.getPlaying().getTanks().add(new ServerTank(Integer.parseInt(body[0]), Integer.parseInt(body[1]), Double.parseDouble(body[2]), body[3], body[4]));
				    sendToAllOthers("newtank;" + body[4] + ";" + body[0] + ";" + body[1]);
				} else if (header.equals("updatetank")) {
					ServerTank tank = Finder.findServerTank(body[0], server.getPlaying().getTanks());
					if (tank != null) {
						tank.x = Integer.parseInt(body[1]);
						tank.y = Integer.parseInt(body[2]);
						tank.orientation = Double.parseDouble(body[3]);
						sendToAllOthers(request);						
					}
				} else if (header.equals("deletetank")) {
					ServerTank tank = Finder.findServerTank(body[0], server.getPlaying().getTanks());
					if (tank != null) {
						server.getPlaying().getTanks().remove(tank);
						sendToAllOthers(request);						
					}
				} else if (header.equals("newbullet")) {
					server.getPlaying().getBullets().add(new ServerBullet(Integer.parseInt(body[0]), Integer.parseInt(body[1]), Double.parseDouble(body[2]), body[3], Integer.parseInt(body[4])));
					sendToAllOthers("newbullet;" + body[0] + ";" + body[1] + ";" + body[2] + ";" + body[3] + ";" + body[4]);
				} else if (header.equals("updatebullet")) {
					ServerBullet bullet = Finder.findServerBullet(body[3],Integer.parseInt(body[0]), server.getPlaying().getBullets());
					if (bullet != null) {
						bullet.update(Integer.parseInt(body[1]), Integer.parseInt(body[2]));
						sendToAllOthers(request);
					}
				} else if (header.equals("deletebullet")) {
					ServerBullet bullet = Finder.findServerBullet(body[0],Integer.parseInt(body[1]), server.getPlaying().getBullets());
					if (bullet != null) {
						server.getPlaying().getBullets().remove(bullet);
						sendToAllOthers(request);
					}
				} else if (header.equals("newobstacle")) {
					server.getPlaying().getObstacles().add(new Obstacle(Integer.parseInt(body[0]), Integer.parseInt(body[1]), Boolean.parseBoolean(body[2])));
					sendToAllOthers(request);
				} else if (header.equals("deleteobstacle")) {
					Obstacle o = Finder.findObstacle(Integer.parseInt(body[0]), Integer.parseInt(body[1]), server.getPlaying().getObstacles());
					if (o != null) {
						server.getPlaying().getObstacles().remove(o);
						sendToAllOthers(request);
					}
				} else if (header.equals("ready")) {
					server.getPlaying().getPlayersReady().set(Finder.findIndexPlayer(body[0], server.getPlaying().getPlayers()), Boolean.parseBoolean(body[1]));
					sendToAll(request);
				} else if (header.equals("play")) {
					sendToAllOthers(request);
				}
				else {
					out.println("wrong request");
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
			out.close();
		}
	}
	
	public void close() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String stringifyServerPlayers(ArrayList<String> players) {
		String res = "players";
		for (String name : players) {
			res += ";" + name;
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
