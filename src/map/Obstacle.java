package map;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Obstacle {
	
	private Rectangle hitbox;
	private int size;
	private Color color;
	private Color border;
	private boolean destructible;
	
	public Obstacle(int x, int y, boolean destructible) {
		size = 50;
		color = new Color(204, 102, 0);
		border = Color.black;
		hitbox = new Rectangle(x, y, size, size);
		this.destructible = destructible;
	}
	
	public boolean isDestructible() {
		return destructible;
	}

	public void drawObstacle(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.draw(hitbox);
		g2.fill(hitbox);
		g2.setColor(border);
		g2.draw(hitbox);
	}
	
	//getters setters
	public Rectangle getHitbox() {
		return hitbox;
	}
}
