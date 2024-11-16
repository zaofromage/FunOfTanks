package player;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import map.Obstacle;
import utils.Delay;

public class Bertha extends Bullet {
	
	private Ellipse2D aoe = new Ellipse2D.Double(0, 0, 200, 200);

	public Bertha(int x, int y, int targetX, int targetY, double orientation, Cannon owner) {
		super(x, y, targetX, targetY, orientation, owner);
		owner.setFire(true);
		color = Color.blue;
	}
	
	@Override
	public void update(ArrayList<Obstacle> obs) {
		super.update(obs);
		if (remove) {
			aoe.setFrame((int) (x - aoe.getWidth() / 2),
					(int) (y - aoe.getHeight() / 2), (int) aoe.getWidth(),
					(int) aoe.getHeight());
			owner.getOwner().getOwner().blowup((int) x, (int) y, 1);
			destroyAoe(obs);
		}
	}
	
	private void destroyAoe(ArrayList<Obstacle> obs) {
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
	}

}
