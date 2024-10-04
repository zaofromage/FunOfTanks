package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import player.Player;
import player.Tank;
import java.util.HashSet;

import gamestate.Playing;

public class Zone {
	private Ellipse2D hitbox;
	
	private Color color;
	
	private HashSet<Tank> present = new HashSet<>();
	
	private Playing playing;
	
	public Zone(double x, double y, double w, double h, Playing p) {
		hitbox = new Ellipse2D.Double(x, y, w, h);
		playing = p;
	}
	
	public void update() {
		for (Player p : playing.getPlayers()) {
			if (hitbox.contains(p.getTank().getHitbox())) {
				present.add(p.getTank());
			} else {
				present.remove(p.getTank());
			}
		}
		color = mixColor();
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)hitbox.getX(), (int)hitbox.getY(), (int)hitbox.getWidth(), (int)hitbox.getHeight());
	}
	
	private Color mixColor() {
		int r = 0;
		int g = 0;
		int b = 0;
		for (Tank t : present) {
			r += t.getColor().getRed();
			g += t.getColor().getGreen();
			b += t.getColor().getBlue();
		}
		if (present.size() == 0) {
			return Color.gray;
		}
		r /= present.size();
		g /= present.size();
		b /= present.size();
		return new Color(r, g, b);
	}
}
