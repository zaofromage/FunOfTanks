package UI;

import java.awt.geom.RoundRectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

public class Button {

	private String text;
	private RoundRectangle2D hitbox;
	private Color color;

	private boolean enabled;

	private Runnable action;
	
	public Button(int x, int y, int width, int height, Color color, String text, Runnable action) {
		this.text = text;
		this.hitbox = new RoundRectangle2D.Double(x, y, width, height, 10, 10);
		this.action = action;
		this.color = color;
		this.enabled = true;
	}

	public void update() {

	}

	public void draw(Graphics g) {
		if (enabled) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(color);
			g2.draw(hitbox);
			g2.fill(hitbox);
			g2.setColor(Color.black);
			g2.setFont(new Font("SansSerif", Font.PLAIN, 35));
			g2.drawString(text,
					(int) (hitbox.getX() + hitbox.getWidth() / 2 - g2.getFontMetrics().stringWidth(text) / 2),
					(int) (hitbox.getY() + (int) hitbox.getHeight() / 2 + g2.getFontMetrics().getAscent() / 2));
		}
	}

	public void onClick(MouseEvent e) {
		if (hitbox.contains(e.getX(), e.getY()) && enabled) {
			action.run();
		}
	}
	
	public void setEnabled(boolean e) {
		this.enabled = e;
	}
	
	public void setColor(Color c) {
		color = c;
	}

}
