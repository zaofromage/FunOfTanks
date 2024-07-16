package map;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import utils.Calcul;

public class Obstacle {
	
	public static Color normal = new Color(204, 102, 0);
	public static Color possible = new Color(204, 102, 0, 100);
	public static Color possibleWrong = new Color(255, 0, 0, 100);
	
	private Rectangle hitbox;
	private int size;
	private Color color;
	private Color border;
	private boolean destructible;
	
	public Obstacle(int x, int y, boolean destructible) {
		size = 50;
		color = normal;
		border = Color.black;
		hitbox = new Rectangle(Calcul.roundToTile(x), Calcul.roundToTile(y), size, size);
		this.destructible = destructible;
	}
	
	public boolean isDestructible() {
		return destructible;
	}
	
	public void updateObstacle(int x, int y) {
		hitbox.setBounds(Calcul.roundToTile(x), Calcul.roundToTile(y), size, size);
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
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
}
