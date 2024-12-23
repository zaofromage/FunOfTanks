package model.player;

import java.awt.Point;
import java.awt.Rectangle;

import model.IModel;
import utils.Vector;

public class Tank implements IModel {
	
	public final int BASE_SPEED = 1;
		
	
	private double x, y;
	private int size = 50;
	private double orientation = 0;
	
	private int speed = BASE_SPEED;
	private int dashSpeed = 20;
	private boolean inDash;
	private boolean canDash = true;
	private long cooldownDash = 1000;
	
	private boolean up, down, left, right = false;
	
	private Vector target = new Vector();
	private Vector aim;
	private double aimDistance = 0.;
	private double maxRange = 600.;
	private double aimSpeed = 5;
	private Cannon cannon;
	
	private Rectangle hitbox = new Rectangle((int)x, (int)y, size, size);
	private Point center = new Point((int)hitbox.getCenterX(), (int)hitbox.getCenterY());
	
	private PlayerMode mode = PlayerMode.BASE;

	private boolean invinsible = false;
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
		cannon = new Cannon();
		aim = new Vector(x, y);
	}
	
	@Override
	public void update() {
		updateHitbox();
	}
	
	public void updateHitbox() {
		hitbox.setBounds((int)x,  (int)y,  size,  size);
		center.setLocation((int)hitbox.getCenterX(), (int)hitbox.getCenterY());
	}

	public void move(Dir dir) {
		switch (dir) {
		case LEFT:
			x -= speed;
			break;
		case RIGHT:
			x += speed;
			break;
		case UP:
			y -= speed;
			break;
		case DOWN:
			y += speed;
			break;
		}
	}

	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}

	public int getSize() {
		return size;
	}
	
	public double getOrientation() {
		return orientation;
	}
	
	public void setOrientation(double o) {
		orientation = o;
	}
	
	public PlayerMode getMode() {
		return mode;
	}
	
	public void setMode(PlayerMode mode) {
		this.mode = mode;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

}
