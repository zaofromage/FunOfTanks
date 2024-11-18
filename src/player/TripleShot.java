package player;

public class TripleShot extends Bullet {
	
	private Bullet haut;
	private Bullet bas;

	public TripleShot(int x, int y, int targetX, int targetY, double orientation, Player owner) {
		super(x, y, targetX, targetY, orientation, owner);
		double dist = Math.sqrt((targetX-x)*(targetX-x) + (targetY-y)*(targetY-y));
		haut = new Bullet(x, y, (int) (x+(dist*Math.cos(Math.toRadians(orientation)+Math.toRadians(15)))), (int) (y+(dist*Math.sin(Math.toRadians(orientation)+Math.toRadians(15)))), orientation+15, owner);
		bas = new Bullet(x, y, (int) (x+(dist*Math.cos(Math.toRadians(orientation)-Math.toRadians(15)))), (int) (y+(dist*Math.sin(Math.toRadians(orientation)-Math.toRadians(15)))), orientation-15, owner);
		player.getClient().send("newbullet;" + x + ";" + y + ";" + targetX + ";" + targetY + ";" + (orientation+15) + ";"
				+ player.getName() + ";" + haut.getId() + ";" + player.getTank().getCannon().getShot());
		player.getClient().send("newbullet;" + x + ";" + y + ";" + targetX + ";" + targetY + ";" + (orientation-15) + ";"
				+ player.getName() + ";" + bas.getId() + ";" + player.getTank().getCannon().getShot());
	}
}
