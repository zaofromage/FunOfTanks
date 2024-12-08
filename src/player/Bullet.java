package player;

import java.awt.Rectangle;
import utils.Vector;
import java.util.ArrayList;
import java.util.List;

import map.Obstacle;
import serverHost.Client;
import utils.Delay;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class Bullet {

	public static int counter = 0;

	protected double x, y;
	protected int id;
	protected Rectangle hitbox;
	protected Vector vector;
	private Vector target;
	private double orientation;
	private int width = 20;
	private int height = 10;
	private int displayOffset = height/2;

	private int blowOffset = 7;

	protected double speed = 1;
	
	private boolean friendlyFire = false;

	protected Color color = Color.gray;

	protected Player player;
	
	protected Tank holding;
	
	protected boolean remove = false;
	
	protected ArrayList<Player> players;
	
	protected Client host;

	public Bullet(double x, double y, Vector target, double orientation, Player owner) {
		this.x = x;
		this.y = y;
		this.id = counter;
		counter++;
		this.target = target;
		hitbox = new Rectangle((int)x+displayOffset, (int)y-displayOffset, width, height);
		vector = new Vector((target.x - x), (target.y - y));
		vector.normalize();
		this.orientation = orientation;
		players = owner.getGame().getPlaying().getPlayers();
		player = owner;
		host = player.getGame().getPlayer().getClient();
		new Delay(500, () -> friendlyFire = true);
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);

		Shape rotated = transform.createTransformedShape(hitbox);

		g2.draw(rotated);
		g2.fill(rotated);
	}

	public void updateHitbox() {
		hitbox.setBounds((int) (x+displayOffset), (int) (y-displayOffset), width, height);
	}

	public void update(List<Obstacle> obs) {
		//x += vector.x * speed;
		//y += vector.y * speed;
		updateHitbox();
		if (host != null) {
			String holdingName = holding != null ? holding.getOwner().getName():"null";
			//c'est la faut probablement pas cast en int
			host.sendUDP(
					"updatebullet;" + id + ";" + (x+vector.x * speed) + ";" + (y+vector.y * speed) + ";" + player.getName() + ";" + holdingName);
		}
		Player p = detectPlayer(players, player);
		if (p != null && !p.getTank().isInvinsible() && p.getTeam() != player.getTeam()) {
			remove = true;
			host.send("deletebullet;" + player.getName() + ";" + id);
			host.send("deletetank;" + p.getName() + ";" + player.getName());
			player.debris(x, y, orientation - 50,
					orientation + 50);
			player.blowup((int) x, (int) y, 0.20);
		} else if (hasReachLimit(obs)) {
			remove = true;
			host.send("deletebullet;" + player.getName() + ";" + id);
			player.blowup((int) x, (int) y, 0.20);
		} else if (destroyObstacle(obs)) {
			remove = true;
			host.send("deletebullet;" + player.getName() + ";" + id);
			player.debris(x, y, orientation - 50,
					orientation + 50);
		}
		if (holding != null && holding.getGrabed() == null) {
			holding = null;
		}
	}
	
	public void update(double x, double y) {
		System.out.println("guest");
		this.x = x;
		this.y = y;
		updateHitbox();
		if (holding != null && holding.getGrabed() == null) {
			holding = null;
		}
	}

	

	public boolean hasReachLimit(List<Obstacle> obs) {
		return (target.x - blowOffset < (int) x && (int) x < target.x + blowOffset && target.y - blowOffset < (int) y
				&& (int) y < target.y + blowOffset);
	}

	public Obstacle detectObstacle(List<Obstacle> obs) {
		for (Obstacle o : obs) {
			if (o.getHitbox().intersects(hitbox))
				return o;
		}
		return null;
	}

	public Player detectPlayer(ArrayList<Player> players, Player player) {
		ArrayList<Player> enemies = new ArrayList<Player>(players);
		if (!friendlyFire) {
			enemies.remove(player);		
		}
		for (Player p : enemies) {
			if (p.getTank() != null) {
				if (p.getTank().getHitbox().intersects(hitbox))
					return p;
			}
		}
		return null;
	}

	public boolean destroyObstacle(List<Obstacle> obs) {
		Obstacle o = detectObstacle(obs);
		if (o == null)
			return false;
		if (!o.isDestructible())
			return true;
		if (player.getClient() != null) {
			player.getClient().send("deleteobstacle;" + (int) o.getHitbox().getX() + ";" + (int) o.getHitbox().getY());
		}
		return true;
	}
	
	public void die(Player p) {
		p.blowup((int)x, (int)y, .2);
	}
	
	public void bounceX() {
		vector.x *= -1;
	}
	
	public void bounceY() {
		vector.y *= -1;
	}

	// getters setters

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public Vector getTarget() {
		return target;
	}

	public int getId() {
		return id;
	}
	
	public boolean toRemove() {
		return remove;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public double getOrientation() {
		return orientation;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Tank getHolding() {
		return holding;
	}
	
	public void setHolding(Tank holding) {
		this.holding = holding;
	}

	@Override
	public String toString() {
		return "id : " + id + " player : " + player + " holding : " + holding;
	}
}
