package player;

import java.util.ArrayList;
import java.util.Objects;

import client.Game;
import serverHost.*;
import utils.Delay;

import java.awt.Graphics;
import java.io.IOException;
import java.net.InetAddress;

import map.Obstacle;

public class Player {

	private Role role;
	private Server server;
	private Client client;
	private boolean main;

	private String name;

	private Tank tank;

	public Player(String name, Role role, Game game, boolean isMain) {
		this.name = name;
		this.role = role;
		this.main = isMain;
		if (this.role == Role.HOST) {
			try {
				server = new Server();
				client = new Client(InetAddress.getLocalHost().toString().split("/")[1], Server.PORT, game);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		createTank(500, 500);
	}

	public Player(String name, Role role, String ip, int port, Game game, boolean isMain) {
		this.name = name;
		this.role = role;
		this.main = isMain;
		try {
			client = new Client(ip, port, game);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// test
		client.send(name);
		createTank(500, 500);
	}

	public void createTank(int x, int y) {
		tank = new Tank(x, y, this);
		if (client != null)
			client.send("newtank;" + x + ";" + y + ";" + tank.getOrientation() + ";"
					+ Integer.toString(tank.getColor().getRGB()) + ";" + name);
	}

	public void deleteTank() {
		tank = null;
		new Delay(5000, () -> createTank(200, 200));
	}

	public void drawPlayer(Graphics g) {
		if (tank != null) {
			tank.drawTank(g);
		}
	}

	public void updatePlayer(ArrayList<Obstacle> obs, ArrayList<Player> players) {
		if (tank != null) {
			tank.updateTank(obs, players, this);
		}
	}

	public Tank getTank() {
		return this.tank;
	}

	public String getName() {
		return name;
	}
	
	public boolean isMain() {
		return main;
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

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return name.equals(other.name);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
