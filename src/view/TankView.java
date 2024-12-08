package view;

import java.awt.Color;
import java.awt.Graphics;

import player.PlayerMode;

public class TankView {
	
	private int x, y;
	private Color color;
	private final int size = 50;
	private final int displayOffset = size/2;
	private boolean isInZone = false;
	private double orientation;
	
	private String name;
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x - displayOffset, y - displayOffset, size, size);
		g.setColor(Color.black);
		if (isInZone) {
			g.drawRect(x - displayOffset, y - displayOffset, size, size);
		}
		g.drawString(owner.getName(), x - displayOffset, y - displayOffset - 10);
		cannon.draw(g, x, y, orientation);
		if (possibleObstacle != null) {
			possibleObstacle.drawObstacle(g);
		}
		if (mode == PlayerMode.AIM) {
			g.drawImage(crosshair, (int) aim.x - crosshair.getWidth()/2, (int) aim.y - crosshair.getHeight()/2, null);
		}
		if (mode == PlayerMode.GRAB) {
			g.setColor(new Color(125, 125, 125, 125));
			g.fillOval((int) grabHitbox.getX(), (int) grabHitbox.getY(), (int) grabRange, (int) grabRange);		
		}
		g.setColor(Color.white);
		g.drawString(""+owner.getTeam(), x, y);
	}
}
