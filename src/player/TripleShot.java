package player;

public class TripleShot extends Bullet {
	
	private Bullet haut;
	private Bullet bas;

	public TripleShot(int x, int y, int targetX, int targetY, double orientation, Cannon owner) {
		super(x, y, targetX, targetY, orientation, owner);
		double dist = Math.sqrt((targetX-x)*(targetX-x) + (targetY-y)*(targetY-y));
		haut = new Bullet(x, y, (int) (x+(dist*Math.cos(Math.toRadians(orientation)+Math.toRadians(15)))), (int) (y+(dist*Math.sin(Math.toRadians(orientation)+Math.toRadians(15)))), orientation+15, owner);
		bas = new Bullet(x, y, (int) (x+(dist*Math.cos(Math.toRadians(orientation)-Math.toRadians(15)))), (int) (y+(dist*Math.sin(Math.toRadians(orientation)-Math.toRadians(15)))), orientation-15, owner);
		owner.getOwner().getOwner().getClient().send("newbullet;" + x + ";" + y + ";" + (orientation+15) + ";"
				+ owner.getOwner().getOwner().getName() + ";" + haut.getId() + ";" + owner.getShot());
		owner.getOwner().getOwner().getClient().send("newbullet;" + x + ";" + y + ";" + (orientation-15) + ";"
				+ owner.getOwner().getOwner().getName() + ";" + bas.getId() + ";" + owner.getShot());
		owner.getBullets().add(haut);
		owner.getBullets().add(bas);
	}

}
