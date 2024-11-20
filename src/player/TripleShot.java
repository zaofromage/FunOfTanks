package player;

import utils.Vector;

public class TripleShot extends Bullet {

	public TripleShot(double x, double y, Vector target, double orientation, Player owner) {
		super(x, y, target, orientation, owner);
		if (player.equals(player.getGame().getPlayer()) && player.getClient() != null) {
			double dist = Math.sqrt((target.x-x)*(target.x-x) + (target.y-y)*(target.y-y));
			player.getClient().send("newbullet;" + x + ";" + y + ";" + (int)(x+(dist*Math.cos(Math.toRadians(orientation)+Math.toRadians(15)))) + ";" + (int)(y+(dist*Math.sin(Math.toRadians(orientation)+Math.toRadians(15)))) + ";" + (orientation+15) + ";"
					+ player.getName() + ";" + TypeShot.NORMAL);
			player.getClient().send("newbullet;" + x + ";" + y + ";" + (int)(x+(dist*Math.cos(Math.toRadians(orientation)-Math.toRadians(15)))) + ";" + (int)(y+(dist*Math.sin(Math.toRadians(orientation)-Math.toRadians(15)))) + ";" + (orientation-15) + ";"
					+ player.getName() + ";" + TypeShot.NORMAL);			
		}
	}
}
