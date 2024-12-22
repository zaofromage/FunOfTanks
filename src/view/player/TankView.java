package view.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import map.Obstacle;
import model.player.Tank;
import player.PlayerMode;
import utils.Vector;
import view.IView;

public class TankView implements IView {
	
	private Tank model;
	
	private Color color = Color.red;
	private int size;
	
//	private Obstacle possibleObstacle;
	
	//private IView cannon;
	
//	private BufferedImage crosshair;
//	private Vector aim;
		
	public TankView(Tank tank) {
		this.model = tank;
		size = model.getSize();
//		try {
//			crosshair = ImageIO.read(new File("res/images/crosshair.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void draw(Graphics g) {
		int x = model.getX();
		int y = model.getY();
		g.setColor(color);
		g.fillRect(x, y, size, size);
		g.setColor(Color.black);
		//if (model.isInZone()) {
		//	g.drawRect(x - displayOffset, y - displayOffset, size, size);
		//}
		g.drawString(model.getPlayer().getName(), x, y - 10);
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
}
