package player;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.Shape;

import utils.Calcul;
import utils.Delay;

public class Cannon {

	private Color color;
	private int width, height;
	private int displayOffset;

	private boolean canFire = true;
	private long cooldown;

	private Tank owner;

	// skill
	private TypeShot shot = TypeShot.NORMAL;

	public Cannon(Tank owner) {
		color = Color.black;
		width = 50;
		height = 20;
		cooldown = 750;
		displayOffset = height / 2;
		this.owner = owner;
	}

	public void draw(Graphics g, int x, int y, double orientation) {
		Graphics2D g2d = (Graphics2D) g;

		switch (shot) {
		case NORMAL:
			width = 50;
			height = 20;
			displayOffset = height/2;
			color = Color.black;
			break;
		case GRENADE:
			g.setColor(Color.green);
			g.fillOval(x+20, y+20, 15, 15);
			g.setColor(Color.gray);
			g.fillRect((int)(x+20+15/2), (int)(y+20), (int) (15/5), (int) (15/2));
			break;
		case BERTHA:
			height = 30 + Calcul.r.nextInt(10)-5;
			displayOffset = height/2;
			color = new Color(125, 0, 0);
			break;
		case TRIPLE:
			g2d.setColor(color);
			Rectangle haut = new Rectangle(x + displayOffset, y - displayOffset, (int)(width/1.5), height);
			AffineTransform h = new AffineTransform();
			h.rotate(Math.toRadians(orientation+15), x, y);
			Shape hr = h.createTransformedShape(haut);
			Rectangle bas = new Rectangle(x + displayOffset, y - displayOffset, (int)(width/1.5), height);
			AffineTransform b = new AffineTransform();
			b.rotate(Math.toRadians(orientation-15), x, y);
			Shape br = b.createTransformedShape(bas);
			g2d.fill(hr);
			g2d.fill(br);
			break;
		}
		Rectangle rect = new Rectangle(x + displayOffset, y - displayOffset, width, height);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);
		
		Shape rotated = transform.createTransformedShape(rect);
		g2d.setColor(color);
		g2d.draw(rotated);
		g2d.fill(rotated);
	}

	public void update() {
		
	}

	public void fire(int x, int y, int targetX, int targetY, double orientation) {
		if (canFire) {
			canFire = false;
			Bullet b = null;
			switch (shot) {
			case NORMAL:
				b = new Bullet(x, y, targetX, targetY, orientation, owner.getOwner());
				break;
			case BERTHA:
				b = new Bertha(x, y, targetX, targetY, orientation, owner.getOwner());
				break;
			case GRENADE:
				b = new Grenade(x, y, targetX, targetY, owner.getOwner());
				break;
			case TRIPLE:
				b = new TripleShot(x, y, targetX, targetY, orientation, owner.getOwner());
				break;
			default:
				b = new Bullet(x, y, targetX, targetY, orientation, owner.getOwner());
				break;
			}
			owner.getOwner().getClient().send("newbullet;" + x + ";" + y + ";" + targetX + ";" + targetY + ";" + orientation + ";"
					+ owner.getOwner().getName() + ";" + b.getId() + ";" + shot);
			new Delay(cooldown, () -> canFire = true);
			shot = TypeShot.NORMAL;
		}
	}

	public Tank getOwner() {
		return owner;
	}

	public boolean canFire() {
		return canFire;
	}
	
	public TypeShot getShot() {
		return shot;
	}

	public void setShot(TypeShot s) {
		shot = s;
	}
	
	public void setFire(boolean fire) {
		canFire = fire;
	}
}
