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

	// idees
	/*
	 * ui pour les capacités et les cooldown de dash etc...
	 * ecran de fin de partie
	 * cooldown pour poser des murs
	 * ajouter un systeme de capacité
	 *    - dash a travers les murs
	 *    - rendre les balles rebondissantes
	 *    - quand on drag la souris ça fait une sorte de mur temporaire qui renvoit les tires
	 *    - reduire le cooldown de tir et dash
	 *  X - augmenter la vitesse temporairement
	 *    - full counter (si tu le cale tu gagnes instant)
	 *    - augmenter le nombre de mur posés par pose de mur
	 *    - faire descendre un soldat qui tire des petites balles tout seul
	 *    - tirer trois balles en un coup
	 *    - 
	 * ajouter une mitrailleuse
	 * ajouter une grenade
	 * son du jeu
	 * systeme qui detruit une partie du tank quand tu prend une explosion (operation booleene tah blender) et tu meurt quand ton tank ne contient plus de pixel
	 */
	private Role role;
	private Server server;
	private Client client;
	private boolean main;
	
	private int lives;

	private String name;

	private Tank tank;
	
	private boolean ready;

	private Skill skill1;
	private Skill skill2;
	private Skill skill3;
	
	public Player(String name, Role role, Game game, boolean isMain) {
		this.name = name;
		this.role = role;
		this.main = isMain;
		this.lives = 3;
		this.skill1 = Skill.speedUp(this);
		this.skill2 = Skill.dashThrough(this);
		if (this.role == Role.HOST) {
			try {
				server = new Server();
				client = new Client(InetAddress.getLocalHost().toString().split("/")[1], Server.PORT, game);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setReady(false);
		createTank(500, 500);
	}

	public Player(String name, Role role, String ip, int port, Game game, boolean isMain) {
		this.name = name;
		this.role = role;
		this.main = isMain;
		try {
			client = new Client(ip, port, game);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setReady(false);
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
		lives--;
		new Delay(5000, () -> createTank(200, 200));
	}
	
	public void close() {
		if (server != null) {
			server.close();
			server = null;
		}
		if (client != null) {
			client.close();
			client = null;
		}
	}

	public void drawPlayer(Graphics g) {
		if (tank != null) {
			tank.drawTank(g);
		}
	}
	
	public void drawSkills(Graphics g) {
		skill1.draw(g, 25, 725);
		skill2.draw(g, 100, 725);
	}

	public void updatePlayer(ArrayList<Obstacle> obs, ArrayList<Player> players) {
		if (tank != null) {
			tank.updateTank(obs, players, this);
		}
		//ui
		skill1.update();
		skill2.update();
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
	
	public int getLives() {
		return lives;
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

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public Skill getSkill1() {
		return skill1;
	}
	
	public Skill getSkill2() {
		return skill2;
	}

}
