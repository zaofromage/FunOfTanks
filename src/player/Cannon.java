package player;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.Shape;

import utils.Delay;
import map.Obstacle;

public class Cannon {
	
	private Color color;
	private int width, height;
	private int displayOffset;
	private ArrayList<Bullet> bullets;
	
	private boolean canFire = true;
	private long cooldown;
	
	private Tank owner;
	
	//skill
	private boolean canBertha = false;
	private Ellipse2D bertha = new Ellipse2D.Double(0, 0, 200, 200);
	private boolean activeBertha = false;
	
	public Cannon(Tank owner) {
		color = Color.black;
		width = 50;
		height = 20;
		cooldown = 750;
		displayOffset = height/2;
		bullets = new ArrayList<>();
		this.owner = owner;
	}
	
	public void drawCannon(Graphics g, int x, int y, double orientation) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle rect = new Rectangle(x + displayOffset, y - displayOffset, width, height);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);
		
	    Shape rotated = transform.createTransformedShape(rect);
	    
	    if (activeBertha) {
	    	//g.fillOval((int)bertha.getX(), (int)bertha.getY(), (int)bertha.getWidth(), (int)bertha.getHeight());
	    }
	    
		for (Bullet b : bullets) {
			b.drawBullet(g);
		}
		g2d.setColor(color);
		g2d.draw(rotated);
		g2d.fill(rotated);
	}
	
	public void updateCannon(ArrayList<Obstacle> obs, ArrayList<Player> players, Player player) {
		Bullet haveToRemove = null;
		for (Bullet b : bullets) {
			b.updateBullet();
			Player p = b.detectPlayer(players, player);
			if (p != null) {
				haveToRemove = b;
				p.deleteTank();
				owner.getOwner().getClient().send("deletebullet;" + owner.getOwner().getName() + ";" + b.getId());
				owner.getOwner().getClient().send("deletetank;" + p.getName());
				owner.getOwner().debris(haveToRemove.getX(), haveToRemove.getY(), haveToRemove.getOrientation() - 30, haveToRemove.getOrientation() + 30);
			} else if (b.hasReachLimit(obs)) {
				haveToRemove = b;
				owner.getOwner().getClient().send("deletebullet;" + owner.getOwner().getName() + ";" + b.getId());
				owner.getOwner().blowup((int) haveToRemove.getX(), (int) haveToRemove.getY(), 0.20);
			} else if (b.destroyObstacle(obs)) {
				haveToRemove = b;
				owner.getOwner().getClient().send("deletebullet;" + owner.getOwner().getName() + ";" + b.getId());
				owner.getOwner().debris(haveToRemove.getX(), haveToRemove.getY(), haveToRemove.getOrientation() - 30, haveToRemove.getOrientation() + 30);
			}
		}
		if (haveToRemove != null)  {
			if (haveToRemove.isBertha()) {
				bertha.setFrame((int) (haveToRemove.getX()-bertha.getWidth()/2), (int) (haveToRemove.getY()-bertha.getHeight()/2), (int)bertha.getWidth(), (int)bertha.getHeight());
				activeBertha = true;
				owner.getOwner().blowup((int) haveToRemove.getX(), (int) haveToRemove.getY(), 1);
				new Delay(200, () -> activeBertha = false);
			}
			bullets.remove(haveToRemove);
		}
		if (activeBertha) {
			for (Player p : players) {
				if (p.getTank() != null && bertha.intersects(p.getTank().getHitbox())) {
					p.deleteTank();
					owner.getOwner().getClient().send("deletetank;" + p.getName());
				}
			}
			destroyObstacle(obs);
		}
	}
	
	public void fire(int x, int y, int targetX, int targetY, double orientation) {
		if (canFire) {
			Bullet b = new Bullet(x, y, targetX, targetY, orientation, this, canBertha);
			canFire = false;
			if (canBertha) {
				canBertha = false;
				canFire = true;
			}
			bullets.add(b);
			owner.getOwner().getClient().send("newbullet;" + x + ";" + y + ";" + orientation + ";" + owner.getOwner().getName() + ";" + b.getId() + ";" + b.isBertha());
			new Delay(cooldown, () -> canFire = true);
		}
	}
	
	public Obstacle detectObstacle(ArrayList<Obstacle> obs) {
		for (Obstacle o : obs) {
			if (bertha.intersects(o.getHitbox()))
				return o;
		}
		return null;
	}
	
	public boolean destroyObstacle(ArrayList<Obstacle> obs) {
		Obstacle o = detectObstacle(obs);
		if (o == null)
			return false;
		if (!o.isDestructible())
			return true;
		if (owner.getOwner().getClient() != null) {
			owner.getOwner().getClient().send("deleteobstacle;" + (int) o.getHitbox().getX() + ";" + (int) o.getHitbox().getY());
		}
		return obs.remove(o);
	}
	
	public Tank getOwner() {
		return owner;
	}
	
	public boolean canFire() {
		return canFire;
	}
	
	public void setCanBertha(boolean bertha) {
		canBertha = bertha;
	}
	
	public void setFire(boolean fire) {
		canFire = fire;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
