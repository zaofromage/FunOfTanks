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

	private double x, y;
	private int id;
	private Rectangle hitbox;
	private Vector vector;
	private int targetX, targetY;
	private double orientation;
	private int width = 20;
	private int height = 10;
	private int displayOffset = height/2;

	private int blowOffset = 7;

	private double speed = 4;
	
	private boolean friendlyFire = false;

	private Color color = Color.gray;
	
	private boolean isBertha;

	private Cannon owner;

	public Bullet(int x, int y, int targetX, int targetY, double orientation, Cannon owner, boolean bertha) {
		this.x = x;
		this.y = y;
		this.id = counter;
		counter++;
		this.targetX = targetX;
		this.targetY = targetY;
		hitbox = new Rectangle(x+displayOffset, y-displayOffset, width, height);
		double[] nvect = Calcul.normalizeVector(targetX - x, targetY - y);
		vector = new Vector(nvect[0], nvect[1]);
		this.orientation = orientation;
		this.owner = owner;
		this.isBertha = bertha;
		new Delay(1000, () -> friendlyFire = true);
	}

	public void drawBullet(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (isBertha) {
			g2.setColor(Color.BLUE);
		} else {
			g2.setColor(color);			
		}
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);

		Shape rotated = transform.createTransformedShape(hitbox);

		g2.draw(rotated);
		g2.fill(rotated);
	}

	public void updateHitbox() {
		hitbox.setBounds((int) x+displayOffset, (int) y-displayOffset, width, height);
	}

	public void updateBullet() {
		x += vector.x * speed;
		y += vector.y * speed;
		updateHitbox();
		if (owner.getOwner().getOwner() != null) {
			owner.getOwner().getOwner().getClient().sendUDP(
					"updatebullet;" + id + ";" + (int) x + ";" + (int) y + ";" + owner.getOwner().getOwner().getName());
		}
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
		if (owner.getOwner().getOwner().getClient() != null) {
			owner.getOwner().getOwner().getClient().send("deleteobstacle;" + (int) o.getHitbox().getX() + ";" + (int) o.getHitbox().getY());
		}
		return obs.remove(o);
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

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public boolean isBertha() {
		return isBertha;
	}

	public double getOrientation() {
		return orientation;
	}

	public Cannon getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return "x : " + x + " y : " + y + " targetX : " + targetX + " targetY : " + targetY;
	}
}
