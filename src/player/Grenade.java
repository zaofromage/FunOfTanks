package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import client.Game;
import map.Obstacle;
import utils.Calcul;
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
	
	public Grenade(int x, int y, int targetX, int targetY, Cannon owner) {
		super(x, y, targetX, targetY, 0, owner);
		speed = 1.5;
		color = Color.green;
		new Delay(timeToBlowUp, () -> blowup());
	}
	
	@Override
	public void update(ArrayList<Obstacle> obs) {
		redIndex += redIndex+(double)255/((timeToBlowUp/1000)*Game.FPS) < 255 ? (double)255/((timeToBlowUp/1000)*Game.FPS):0;
		greenIndex -= greenIndex-(double)255/((timeToBlowUp/1000)*Game.FPS) > 0 ? (double)255/((timeToBlowUp/1000)*Game.FPS)/2:0;
		color = new Color((int)redIndex, (int)greenIndex, 0);
		if (owner.getOwner().getOwner() != null) {
			String holdingName = holding != null ? holding.getOwner().getName():"null";
			owner.getOwner().getOwner().getClient().sendUDP(
					"updatebullet;" + id + ";" + (int) x + ";" + (int) y + ";" + owner.getOwner().getOwner().getName() + ";" + holdingName);
		}
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
			x = owner.getOwner().getX();
			y = owner.getOwner().getY();
			double[] nvect = Calcul.normalizeVector((int)(owner.getOwner().getTarget().x - x), (int) (owner.getOwner().getTarget().y - y));
			vector.x = nvect[0];
			vector.y = nvect[1];
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)x, (int)y, (int)diam, (int)diam);
		g.setColor(Color.gray);
		g.fillRect((int)(x+diam/2), (int)(y), (int) (diam/5), (int) (diam/2));
		//g.setColor(new Color(125, 125, 125, 125));
		//g.fillOval((int) (x-blowDiam/2), (int) (y-blowDiam/2), blowDiam, blowDiam);
	}
	
	private void blowup() {
		ArrayList<Obstacle> obs = owner.getOwner().getOwner().getGame().getPlaying().getObstacles();
		Ellipse2D aoe = new Ellipse2D.Double((int) (x-blowDiam/2), (int) (y-blowDiam/2), blowDiam, blowDiam);
		for (Player p : players) {
			if (p.getTank() != null && aoe.intersects(p.getTank().getHitbox()) && !p.getTank().isInvinsible()) {
				owner.getOwner().getOwner().getClient().send("deletetank;" + p.getName() + ";" + owner.getOwner().getOwner().getName());
			}
		}
		for (Obstacle o : obs) {
			if (aoe.intersects(o.getHitbox()) && o.isDestructible()) {
				if (owner.getOwner().getOwner().getClient() != null) {
					owner.getOwner().getOwner().getClient().send("deleteobstacle;" + (int) o.getHitbox().getX() + ";" + (int) o.getHitbox().getY());
				}
			}
		}
		owner.getOwner().getOwner().blowup((int) x, (int) y, 2);
		remove = true;
		owner.getOwner().getOwner().getClient().send("deletebullet;" + owner.getOwner().getOwner().getName() + ";" + id);
	}
	
	@Override
	public void updateHitbox() {
		hitbox.setBounds((int) x, (int) y, (int)diam, (int)diam);
	}
	
}
