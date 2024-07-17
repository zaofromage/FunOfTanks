package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class PopUpMenu {
	
	private int x, y, width, height;
	private Color backgroundColor;
	
	public PopUpMenu(int x, int y, int width, int height, Color bcolor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backgroundColor = bcolor;
	}
	
	public void update() {}
	
	public void draw(Graphics g) {
		superDraw(g);
	}
	
	protected void superDraw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);

	}
}
