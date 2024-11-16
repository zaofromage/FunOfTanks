package serverClass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import player.Player;
import player.TypeShot;

public class ServerBullet {
	
	public int id;
	public int x, y;
	public double orientation;
	public Color color = Color.gray;
	
	public Rectangle hitbox;
	
	public String owner;
	
	public ServerBullet(int x, int y, double o, String owner, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.orientation = o;
		this.owner = owner;
		hitbox = new Rectangle(x, y, 20, 10);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(orientation), x, y);
		
	    Shape rotated = transform.createTransformedShape(hitbox);
	    
		g2.draw(rotated);
		g2.fill(rotated);
	}
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
		updateHitbox();
	}
	
	public void updateHitbox() {
		hitbox.setBounds((int)x, (int)y, 20, 10);
	}
	
	public void die(Player p) {
		p.blowup(x, y, .2);
	}
}
