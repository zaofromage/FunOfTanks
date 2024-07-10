package player;

import java.util.ArrayList;
import serverHost.*;
import java.awt.Graphics;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import map.Obstacle;

public class Player {

	private Role role;
	private Server server;
	private Client client;

	private String name;

	private Tank tank;

	public Player(String name, Role role) {
		this.name = name;
		this.role = role;
		if (this.role == Role.HOST) {
			try {
				server = new Server();
				client = new Client(InetAddress.getLocalHost().toString().split("/")[1], Server.PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		createTank(500, 500);
	}

	public Player(String name, Role role, String ip, int port) {
		this.name = name;
		this.role = role;
		try {
			client = new Client(ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// test
		client.send(name);
		createTank(500, 500);
	}

	public void createTank(int x, int y) {
		tank = new Tank(x, y);
	}

	public void drawPlayer(Graphics g) {
		tank.drawTank(g);
	}

	public void updatePlayer(ArrayList<Obstacle> obs, ArrayList<Player> players, ArrayList<Player> toRemove) {
		tank.updateTank(obs, players, this, toRemove);
	}

	public Tank getTank() {
		return this.tank;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
