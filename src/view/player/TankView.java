package view.player;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.player.Tank;
import view.IView;

public class TankView implements IView, PropertyChangeListener {
	
	private Tank model;
	
	private int x, y;
	
	private Color color = Color.red;
	private int size;
	
//	private Obstacle possibleObstacle;
	
	//private IView cannon;
	
//	private BufferedImage crosshair;
//	private Vector aim;
		
	public TankView(Tank tank) {
		this.model = tank;
		x = model.getX();
		y = model.getY();
		size = model.getSize();
//		try {
//			crosshair = ImageIO.read(new File("res/images/crosshair.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, size, size);
		g.setColor(Color.black);
		//if (model.isInZone()) {
		//	g.drawRect(x - displayOffset, y - displayOffset, size, size);
		//}
		//g.drawString(model.getPlayer().getName(), x, y - 10);
		//cannon.draw(g);
//		if (possibleObstacle != null) {
//			possibleObstacle.drawObstacle(g);
//		}
//		if (mode == PlayerMode.AIM) {
//			g.drawImage(crosshair, (int) aim.x - crosshair.getWidth()/2, (int) aim.y - crosshair.getHeight()/2, null);
//		}
//		if (mode == PlayerMode.GRAB) {
//			g.setColor(new Color(125, 125, 125, 125));
//			g.fillOval((int) grabHitbox.getX(), (int) grabHitbox.getY(), (int) grabRange, (int) grabRange);		
//		}
		//g.setColor(Color.white);
		//g.drawString(""+model.getTeam(), x, y);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String s = e.getPropertyName();
		switch (s) {
		case "x":
			x = (int)(double)e.getNewValue();
			break;
		case "y":
			y = (int)(double)e.getNewValue();
			break;
		}
	}
}
