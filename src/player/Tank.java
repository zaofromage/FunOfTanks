package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import utils.Delay;
import map.Obstacle;

public class Tank {
	private int x, y;
	private int size;
	private double orientation;
	
	private int speed;
	private int dashSpeed;
	private boolean canDash = true;
	private long cooldownDash;
	
	private int displayOffset;
	
	private Color color;
	
	private boolean up, down, left, right;
	
	private int targetX, targetY;
	private Cannon cannon;
	
	private Rectangle hitbox;
	
	public Tank() {
		x = 500;
		y = 500;
		cannon = new Cannon();
		size = 50;
		orientation = 0;
		speed = 1;
		dashSpeed = 20;
		cooldownDash = 1000;
		color = Color.red;
		up = false; 
		down = false;
		left = false;
		right = false;
		targetX = 0;
		targetY = 0;
		displayOffset = size / 2;
		hitbox = new Rectangle(x - displayOffset, y - displayOffset, size, size);
	}
	
	public void move(int deltaX, int deltaY, ArrayList<Obstacle> obs) {
		for (Obstacle o : obs) {
			if (detectObstacle(o, x + deltaX * speed, y + deltaY * speed)) {
				return;
			}
		}
		x += deltaX * speed;
		y += deltaY * speed;
	}
	
	public void dash() {
		if (canDash) {
			speed *= dashSpeed;
			canDash = false;
		    new Delay(50, () -> speed /= dashSpeed);
		    new Delay(cooldownDash, () -> canDash = true);
		}
		
	}
	
	private void findOrientation() {
		double vectorX = (targetX - x), vectorY = (targetY - y) * -1;
		if (vectorX != 0) {
			double delta = 0.;
			if (vectorX > 0 && vectorY < 0) {
				delta = 360.;
			} else if (vectorX < 0) {
				delta = 180.;
			}
			orientation = (Math.atan(vectorY / vectorX) * (180/Math.PI) + delta) * -1;
		}
	}
	
	public void updateHitbox() {
		hitbox.setBounds(x - displayOffset, y - displayOffset, size, size);
	}
	
	private boolean detectObstacle(Obstacle obstacle, int x, int y) {
		return obstacle.getHitbox().intersects(new Rectangle(x - displayOffset, y - displayOffset, size, size));
	}
	
	public void fire(int targetX, int targetY) {
		cannon.fire(x, y, targetX, targetY, orientation);
	}
	
	public void updateTank(ArrayList<Obstacle> obs) {
		if (up) {
			move(0, -1, obs);
		}
		if (down) {
			move(0, 1, obs);
		}
		if (left) {
			move(-1, 0, obs);
		}
		if (right) {
			move(1, 0, obs);
		}
		findOrientation();
		updateHitbox();
		cannon.updateCannon(obs);
	}
	
	public void drawTank(Graphics g) {
		g.setColor(color);
		g.fillRect(x - displayOffset, y - displayOffset, size, size);
		cannon.drawCannon(g, x, y, orientation);
	}
	
	
	
	//getters setters
	public double getOrientation() {
		return orientation;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}

	public Cannon getCannon() {
		return cannon;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}
}
