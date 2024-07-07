package client;

import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import input.KeyboardInput;
import input.MouseInput;

public class GamePanel extends JPanel{
	
	private MouseInput mouse;
	private int posX = 150, posY = 100;
	private int xDir = 1, yDir = 1;
	
	public GamePanel() {
		mouse = new MouseInput(this);
		addKeyListener(new KeyboardInput(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		updateRectangle();
		g.fillRect(posX, posY, 200, 50);
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void updateRectangle() {
		posX += xDir;
		posY += yDir;
		if (posX > 400 || posX < 0) {
			xDir *= -1;
		}
		if (posY > 400 || posY < 0) {
			yDir *= -1;
		}
	}
	
	public void moveX(int delta) {
		posX += delta;
	}
	public void moveY(int delta) {
		posY += delta;
	}
	
	public void setRectPos(int x, int y) {
		posX = x;
		posY = y;
	}
}
