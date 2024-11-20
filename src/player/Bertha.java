package player;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import map.Obstacle;
import utils.Delay;
import utils.Vector;

public class Bertha extends Bullet {
	
	private Ellipse2D aoe = new Ellipse2D.Double(0, 0, 200, 200);

	public Bertha(double x, double y, Vector target, double orientation, Player owner) {
		super(x, y, target, orientation, owner);
		owner.getTank().getCannon().setFire(true);
		color = Color.blue;
	}
	
	@Override
	public void update(ArrayList<Obstacle> obs) {
		super.update(obs);
		if (remove) {
			aoe.setFrame((int) (x - aoe.getWidth() / 2),
					(int) (y - aoe.getHeight() / 2), (int) aoe.getWidth(),
					(int) aoe.getHeight());
			player.blowup((int) x, (int) y, 1);
			destroyAoe(obs);
		}
	}
	
	private void destroyAoe(ArrayList<Obstacle> obs) {
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
	}

}
