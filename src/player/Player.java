package player;

import java.util.ArrayList;
import java.util.Objects;

import client.Game;
import client.GamePanel;
import effect.ParticleSystem;
import effect.Shape;
import effect.Particle;
import serverHost.*;
import utils.Calcul;
import utils.Delay;
import utils.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.InetAddress;

import map.Obstacle;

public class Player {

	// idees
	/*
	 * ui pour les capacités et les cooldown de dash etc...
	 * ecran de fin de partie
	 * ajouter un systeme de capacité
	 *  X - dash a travers les murs
	 *    - rendre les balles rebondissantes
	 *    - quand on drag la souris ça fait une sorte de mur temporaire qui renvoit les tires
	 *    - reduire le cooldown de tir et dash
	 *  X - augmenter la vitesse temporairement
	 *    - full counter (si tu le cale tu gagnes instant)
	 *    - augmenter le nombre de mur posés par pose de mur
	 *    - faire descendre un soldat qui tire des petites balles tout seul
	 *    - tirer trois balles en un coup
	 *    - portail qui tp le tank et les balles
	 *    - forreuse (genkidama)
	 * ajouter une mitrailleuse
	 * ajouter une grenade
	 * son du jeu
	 * systeme qui detruit une partie du tank quand tu prend une explosion (operation booleene tah blender) et tu meurt quand ton tank ne contient plus de pixel
	 * IMPORTANT REFACTORING il faut que a terme chaque feature soit serverside ça veut dire que quand je tire l'action coté client c'est juste 
	 * "server tire une balle mais moi je fais rien"
	 * et surtout pour etre sur que tout soit comme ça il faut que le serveur utilise seulement sendToAll et pas sendToAllOthers 
	 */
	public static final int RESPAWN = 5000;
	public static final int MAX_LIVES = 3;
	
	private Game game;
	
	private Role role;
	private Server server;
	private UDPServer udpServer;
	private Client client;
	private boolean main;
	
	private int lives;

	private String name;

	private Tank tank;
	public Color tankColor;
	
	private boolean ready;
	
	private ParticleSystem blowup = new ParticleSystem(new Particle(50, Shape.RECTANGLE, 0.75, new Vector(), Color.RED), 20);
    private ParticleSystem debris = new ParticleSystem(new Particle(50, Shape.RECTANGLE, 0.75, new Vector(), Color.ORANGE), 10);
    private ParticleSystem trainee = new ParticleSystem(new Particle(35, Shape.RECTANGLE, 0.3, new Vector(), Color.GRAY), 3, 30);
	
	private Skill skill1;
	private Skill skill2;
	private Skill skill3;
	
	private int team;
	
	public Player(String name, Role role, Game game, boolean isMain) {
		this.game = game;
		this.name = name;
		this.role = role;
		this.main = isMain;
		this.lives = MAX_LIVES;
		this.team = 1;
		this.skill1 = null;
		this.skill2 = null;
		this.skill3 = null;
		if (this.role == Role.HOST) {
			try {
				server = new Server();
				udpServer = new UDPServer(server);
				client = new Client(InetAddress.getLocalHost().toString().split("/")[1], Server.PORT, game);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setReady(false);
	}

	public Player(String name, Role role, String ip, int port, Game game, boolean isMain) {
		this.game = game;
		this.name = name;
		this.role = role;
		this.main = isMain;
		this.lives = 3;
		this.team = 1;
		this.skill1 = Skill.speedUp(this);
		this.skill2 = Skill.dashThrough(this);
		this.skill3 = Skill.grosseBertha(this);
		try {
			client = new Client(ip, port, game);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setReady(false);
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
		new Delay(5000, () -> createTank(Calcul.r.nextInt(51, GamePanel.dimension.width - 100), Calcul.r.nextInt(51, GamePanel.dimension.height - 100)));
	}
	
	public void blowup(int x, int y, double lifetime) {
		blowup.setLifetime(lifetime);
		blowup.emit(x, y, -180, 180);
	}
	
	public void debris(double x, double y, double min, double max) {
		debris.emit((int) x, (int) y, (int) min, (int) max);
	}
	
	public void trainee(double x, double y) {
		trainee.interval((int)x, (int)y, -180, 180);
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

	public void updatePlayer(ArrayList<Obstacle> obs, ArrayList<Player> players) {
		if (tank != null) {
			tank.updateTank(obs, players, this);
		}
		//ui
		if (main) {
			if (skill1 != null)
				skill1.update();
			if (skill2 != null)
				skill2.update();
			if (skill3 != null)
				skill3.update();
		}
		blowup.update();
		debris.update();
		trainee.update();
	}
	
	public void drawPlayer(Graphics g) {
		trainee.draw(g);
		if (tank != null) {
			tank.drawTank(g);
		}
		blowup.draw(g);
		debris.draw(g);
	}
	
	public void drawSkills(Graphics g) {
		if (main) {
			if (skill1 != null)
				skill1.draw(g, 25, 725);
			if (skill2 != null)
				skill2.draw(g, 100, 725);
			if (skill3 != null)
				skill3.draw(g, 175, 725);
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
	
	public Game getGame() {
		return game;
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
	
	public void setSkill1(Skill s) {
		skill1 = s;
	}
	
	public Skill getSkill2() {
		return skill2;
	}
	
	public void setSkill2(Skill s) {
		skill2 = s;
	}
	
	public Skill getSkill3() {
		return skill3;
	}
	
	public void setSkill3(Skill s) {
		skill3 = s;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}

}
