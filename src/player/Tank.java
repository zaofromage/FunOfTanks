package player;

import java.awt.Color;
import java.awt.Graphics;

public class Tank {
	private int x, y;
	private int size;
	private double orientation;
	private int speed;
	private Color color;
	private boolean up, down, left, right;
	private int targetX, targetY;
	
	
	public Tank() {
		x = 0;
		y = 0;
		size = 50;
		orientation = 0;
		speed = 1;
		color = Color.red;
		up = false; 
		down = false;
		left = false;
		right = false;
		targetX = 0;
		targetY = 0;
	}
	
	public void moveX(int dir) {
		x += dir * speed;
	}
	
	public void moveY(int dir) {
		y += dir * speed;
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
			orientation = (Math.atan(vectorY / vectorX) * (180/Math.PI) + delta);
		}
	}
	
	public void updateTank() {
		if (up) {
			moveY(-1);
		}
		if (down) {
			moveY(1);
		}
		if (left) {
			moveX(-1);
		}
		if (right) {
			moveX(1);
		}
		findOrientation();
	}
	
	public void drawTank(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, size, size);
	}
	
	
	
	//getters setters
	public double getOrientation() {
		return orientation;
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
