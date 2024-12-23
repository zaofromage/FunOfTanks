package model.bullet;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Map;
import java.util.UUID;

import model.IModel;
import model.player.Player;
import model.player.Tank;
import utils.Vector;

public class Bullet implements IModel {
	
	private UUID id;
	
	protected double x, y;
	private int width = 20;
	private int height = 10;
	protected Shape hitbox;
	private Vector target;
	private Vector vector;
	private double orientation;
	
	private Map<UUID, Player> players;
	
	private UUID owner;
	
	private int blowOffset = 7;
	
	protected double speed = 4;
	
	private boolean remove = false;
	
	public Bullet(UUID id, UUID owner, double x, double y, Vector target, Map<UUID, Player> players) {
		this.id = id;
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.target = target;
		this.players = players;
		this.orientation = Math.atan2(target.y - y, target.x - x);
		this.vector = new Vector(target.x - x, target.y - y);
		vector.normalize();
		hitbox = new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public void update() {
		move();
		Player p = detectPlayer();
		if (p != null) {
			p.destroyTank();
			remove = true;
		} else if (hasReachLimit()) {
			remove = true;
		}
		updateHitbox();
	}
	
	public void updateHitbox() {
		Rectangle rect = new Rectangle((int)x, (int)y, width, height);
		AffineTransform transform = new AffineTransform();
		transform.rotate(orientation, x+width/2, y+height/2);
		hitbox = transform.createTransformedShape(rect);
	}
	
	public void move() {
		x += vector.x * speed;
		y += vector.y * speed;
	}
	
	public Player detectPlayer() {
		for (Player p : players.values()) {
			Tank tank = p.getTank();
			if (tank != null && !p.getID().equals(owner)) {
				if (hitbox.intersects(tank.getHitbox())) {
					return p;
				}
			}
		}
		return null;
	}
	
	public boolean hasReachLimit() {
		return (target.x - blowOffset < (int) x && (int) x < target.x + blowOffset && target.y - blowOffset < (int) y
				&& (int) y < target.y + blowOffset);
	}
	
	public boolean toRemove() {
		return remove;
	}
	
	public UUID getID() {
		return id;
	}

}
