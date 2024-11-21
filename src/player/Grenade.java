package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import client.Game;
import map.Obstacle;
import utils.Delay;
import utils.Vector;

public class Grenade extends Bullet {
	
	private double diam = 15;
	private boolean ascend = true;
	private int bounce = 0;
	public static final int timeToBlowUp = 4000;
	private double redIndex = 0;
	private double greenIndex = 255;
	
	private final int blowDiam = 300;
	
	private boolean colliding = false;
	
	public Grenade(double x, double y, Vector target, Player owner) {
		super(x, y, target, 0, owner);
		speed = 1.5;
		color = Color.green;
		if (owner.equals(owner.getGame().getPlayer())) {
			new Delay(timeToBlowUp, () -> blowup());			
		}
	}
	
	@Override
	public void update(ArrayList<Obstacle> obs) {
		redIndex += redIndex+(double)255/((timeToBlowUp/1000)*Game.FPS) < 255 ? (double)255/((timeToBlowUp/1000)*Game.FPS):0;
		greenIndex -= greenIndex-(double)255/((timeToBlowUp/1000)*Game.FPS) > 0 ? (double)255/((timeToBlowUp/1000)*Game.FPS)/2:0;
		color = new Color((int)redIndex, (int)greenIndex, 0);
		updateHitbox();
		if (holding == null) {
			x += vector.x * speed;
			y += vector.y * speed;
			if (ascend) {
				diam+=0.5;
				if (diam > (75 - (bounce*26))) {
					ascend = false;
				}
			} else {
				if (diam >= 25) {
					diam-=0.5;			
				} else {
					ascend = true;
					bounce++;
				}
			}
			if (diam <= 50) {
				speed -= speed - 0.003 > 0 ? 0.003:0.;
				int c = 0;
				for (Obstacle o : obs) {
					if (hitbox.intersects(o.getHitbox())) {
						++c;
						if (!colliding) {
							double deltaX = hitbox.getCenterX() - o.getHitbox().getCenterX();
							double deltaY = hitbox.getCenterY() - o.getHitbox().getCenterY();
							double semiWidth  = (hitbox.width + o.getHitbox().width)/2;
							double semiHeight = (hitbox.height + o.getHitbox().height)/2;
							double overlapX = semiWidth - Math.abs(deltaX);
							double overlapY = semiHeight - Math.abs(deltaY);
							if (overlapX < overlapY) {
								bounceX();
							} else {
								bounceY();
							}
							colliding = true;
							break;
						}
					}
				}
				if (c == 0) {
					colliding = false;
				}
			}
		} else {
			x = holding.getX();
			y = holding.getY();
			vector.set(holding.getTarget().x - x, holding.getTarget().y - y).normalize();
			speed = 1.5;
		}
		if (player.getClient() != null) {
			String holdingName = holding != null ? holding.getOwner().getName():"null";
			player.getClient().sendUDP(
					"updatebullet;" + id + ";" + x + ";" + y + ";" + player.getName() + ";" + holdingName);
		}
	}
	
	@Override
	public void update(double x, double y) {
		this.x = x;
		this.y = y;
		redIndex += redIndex+(double)255/((timeToBlowUp/1000)*Game.FPS) < 255 ? (double)255/((timeToBlowUp/1000)*Game.FPS):0;
		greenIndex -= greenIndex-(double)255/((timeToBlowUp/1000)*Game.FPS) > 0 ? (double)255/((timeToBlowUp/1000)*Game.FPS)/2:0;
		color = new Color((int)redIndex, (int)greenIndex, 0);
		updateHitbox();
		if (holding == null) {
			if (ascend) {
				diam+=0.5;
				if (diam > (75 - (bounce*26))) {
					ascend = false;
				}
			} else {
				if (diam >= 25) {
					diam-=0.5;			
				} else {
					ascend = true;
					bounce++;
				}
			}
			if (diam <= 50) {
				speed -= speed - 0.003 > 0 ? 0.003:0.;
			}			
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)x, (int)y, (int)diam, (int)diam);
		g.setColor(Color.gray);
		g.fillRect((int)(x+diam/2), (int)(y), (int) (diam/5), (int) (diam/2));
		g.setColor(new Color(125, 125, 125, 125));
		g.fillOval((int) (x-blowDiam/2), (int) (y-blowDiam/2), blowDiam, blowDiam);
	}
	
	private void blowup() {
		ArrayList<Obstacle> obs = player.getGame().getPlaying().getObstacles();
		Ellipse2D aoe = new Ellipse2D.Double((int) (x-blowDiam/2), (int) (y-blowDiam/2), blowDiam, blowDiam);
		for (Player p : players) {
			if (p.getTank() != null && aoe.intersects(p.getTank().getHitbox()) && !p.getTank().isInvinsible()) {
				player.getClient().send("deletetank;" + p.getName() + ";" + player.getName());
			}
		}
		for (Obstacle o : obs) {
			if (aoe.intersects(o.getHitbox()) && o.isDestructible()) {
				if (player.getClient() != null) {
					player.getClient().send("deleteobstacle;" + (int) o.getHitbox().getX() + ";" + (int) o.getHitbox().getY());
				}
			}
		}
		player.blowup((int) x, (int) y, 2);
		remove = true;
		player.getClient().send("deletebullet;" + player.getName() + ";" + id);
	}
	
	@Override
	public void updateHitbox() {
		hitbox.setBounds((int) x, (int) y, (int)diam, (int)diam);
	}
	
	@Override
	public void die(Player p) {
		p.blowup((int)x, (int)y, 2);
	}
	
	public void setVector(Vector v) {
		vector = v;
	}
}
