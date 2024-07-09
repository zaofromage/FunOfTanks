package player;

import java.awt.Rectangle;
import java.util.ArrayList;

import map.Obstacle;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class Bullet {
	
	private double x, y;
	private Rectangle hitbox;
	private double vectorX, vectorY;
	private int targetX, targetY;
	private double orientation;
	private int width, height;
	
	private int blowOffset;
	
	private double speed;
	
	private Color color;
	
	public Bullet(int x, int y, int targetX, int targetY, double orientation) {
		this.x = x;
		this.y = y;
		this.targetX = targetX;
		this.targetY = targetY;
		width = 20;
		height = 10;
		hitbox = new Rectangle(x, y, width, height);
		double[] nvect = normalizeVector(targetX - x, targetY - y);
		vectorX = nvect[0];
		vectorY = nvect[1];
		speed = 2;
		blowOffset = 5;
		this.orientation = orientation;
		color = Color.gray;
	}
	
	public void drawBullet(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);
		
	    Shape rotated = transform.createTransformedShape(hitbox);
	    
		g2.draw(rotated);
		g2.fill(rotated);
	}
	
	public void updateHitbox() {
		hitbox.setBounds((int)x, (int)y, width, height);
	}
	
	public void updateBullet() {
		x += vectorX * speed;
		y += vectorY * speed;
		updateHitbox();
	}
	
	private double[] normalizeVector(int x, int y) {
		double norme = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double[] res = new double[2];
		res[0] =((double)x / norme);
		res[1] =((double)y / norme);
		return res;
	}
	
	public boolean hasReachLimit(ArrayList<Obstacle> obs) {
		return( targetX - blowOffset < (int) x && (int) x < targetX + blowOffset && targetY - blowOffset < (int) y && (int) y < targetY + blowOffset);
	}
	
	private Obstacle detectObstacle(ArrayList<Obstacle> obs) {
		for (Obstacle o : obs) {
			if (o.getHitbox().intersects(hitbox)) return o;
		}
		return null;
	}
	
	public boolean destroyObstacle(ArrayList<Obstacle> obs) {
		Obstacle o = detectObstacle(obs);
		if (o == null) return false;
		if (!o.isDestructible()) return true;
		return obs.remove(o);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	@Override
	public String toString() {
		return "x : " + x + " y : " + y + " targetX : " + targetX + " targetY : " + targetY;
	}
}
