package player;

import java.awt.Rectangle;
import utils.Vector;
import java.util.ArrayList;

import map.Obstacle;
import utils.Calcul;
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
	private int targetX, targetY;
	private double orientation;
	private int width = 20;
	private int height = 10;
	private int displayOffset = height/2;

	private int blowOffset = 7;

	protected double speed = 4;
	
	private boolean friendlyFire = false;

	protected Color color = Color.gray;

	protected Player player;
	
	protected Tank holding;
	
	protected boolean remove = false;
	
	protected ArrayList<Player> players;

	public Bullet(int x, int y, int targetX, int targetY, double orientation, Player owner) {
		this.x = x;
		this.y = y;
		this.id = counter;
		counter++;
		this.targetX = targetX;
		this.targetY = targetY;
		hitbox = new Rectangle(x+displayOffset, y-displayOffset, width, height);
		double[] nvect = Calcul.normalizeVector(targetX - x, targetY - y);
		vector = new Vector(nvect[0], nvect[1]);
		System.out.println((targetX - x) + " " + (targetY - y));
		this.orientation = orientation;
		players = owner.getGame().getPlaying().getPlayers();
		player = owner;
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
		hitbox.setBounds((int) x+displayOffset, (int) y-displayOffset, width, height);
	}

	public void update(ArrayList<Obstacle> obs) {
		x += vector.x * speed;
		y += vector.y * speed;
		updateHitbox();
		if (player != null) {
			String holdingName = holding != null ? holding.getOwner().getName():"null";
			player.getClient().sendUDP(
					"updatebullet;" + id + ";" + (int) x + ";" + (int) y + ";" + player.getName() + ";" + holdingName);
		}
		Player p = detectPlayer(players, player);
		if (p != null && !p.getTank().isInvinsible() && p.getTeam() != player.getTeam()) {
			remove = true;
			player.getClient().send("deletebullet;" + player.getName() + ";" + id);
			player.getClient().send("deletetank;" + p.getName() + ";" + player.getName());
			player.debris(x, y, orientation - 50,
					orientation + 50);
			player.blowup((int) x, (int) y, 0.20);
		} else if (hasReachLimit(obs)) {
			remove = true;
			player.getClient().send("deletebullet;" + player.getName() + ";" + id);
			player.blowup((int) x, (int) y, 0.20);
		} else if (destroyObstacle(obs)) {
			remove = true;
			player.getClient().send("deletebullet;" + player.getName() + ";" + id);
			player.debris(x, y, orientation - 50,
					orientation + 50);
		}
	}
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
		updateHitbox();
	}

	

	public boolean hasReachLimit(ArrayList<Obstacle> obs) {
		return (targetX - blowOffset < (int) x && (int) x < targetX + blowOffset && targetY - blowOffset < (int) y
				&& (int) y < targetY + blowOffset);
	}

	public Obstacle detectObstacle(ArrayList<Obstacle> obs) {
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

	public boolean destroyObstacle(ArrayList<Obstacle> obs) {
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
	
	public int getTargetX() {
		return targetX;
	}
	
	public int getTargetY() {
		return targetY;
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

	@Override
	public String toString() {
		return "id : " + id + " player : " + player;
	}
}
