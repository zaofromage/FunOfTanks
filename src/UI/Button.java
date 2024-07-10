package UI;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class Button {
	
	private String text;
	private Rectangle hitbox;
	private Color color;
	
	private Runnable action;
	
	public Button(int x, int y, int width, int height, Color color, String text, Runnable action) {
		this.text = text;
		this.hitbox = new Rectangle(x, y, width, height);
		this.action = action;
		this.color = color;
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.draw(hitbox);
		g2.fill(hitbox);
		g2.drawString(text, (int) hitbox.getX(), (int) hitbox.getY());
	}
	
	public void onClick(MouseEvent e) {
		if (hitbox.contains(e.getX(), e.getY())) {
			action.run();
		}
	}
	
}
