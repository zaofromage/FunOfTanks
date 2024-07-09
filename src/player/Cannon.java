package player;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
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
	
	public Cannon() {
		color = Color.black;
		width = 50;
		height = 20;
		cooldown = 1500;
		displayOffset = height/2;
		bullets = new ArrayList<>();
	}
	
	public void drawCannon(Graphics g, int x, int y, double orientation) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		Rectangle rect = new Rectangle(x + displayOffset, y - displayOffset, width, height);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);
		
	    Shape rotated = transform.createTransformedShape(rect);
		g2d.draw(rotated);
		g2d.fill(rotated);
		for (Bullet b : bullets) {
			b.drawBullet(g);
		}
	}
	
	public void updateCannon(ArrayList<Obstacle> obs) {
		for (Bullet b : bullets) {
			b.updateBullet();
		}
		bullets.removeIf(b -> b.hasReachLimit(obs));
		bullets.removeIf(b -> b.destroyObstacle(obs));
	}
	
	public void fire(int x, int y, int targetX, int targetY, double orientation) {
		if (canFire) {
			bullets.add(new Bullet(x, y, targetX, targetY, orientation));
			canFire = false;
			new Delay(cooldown, () -> canFire = true);
		}
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
