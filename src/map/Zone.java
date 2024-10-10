package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import player.Player;
import player.Tank;
import serverHost.Role;

import java.util.HashSet;

import gamestate.Playing;

public class Zone {
	private Ellipse2D hitbox;
	
	private Color color;
	
	private HashSet<Player> present = new HashSet<>();
	
	private Playing playing;
	
	private int point = 0;
	
	public Zone(double x, double y, double w, double h, Playing p) {
		hitbox = new Ellipse2D.Double(x, y, w, h);
		playing = p;
	}
	
	public void update() {
		for (Player p : playing.getPlayers()) {
			if (p.getTank() != null && hitbox.contains(p.getTank().getHitbox())) {
				present.add(p);
				p.getTank().setInZone(true);
			} else {
				present.remove(p);
				if (p.getTank() != null) {
					p.getTank().setInZone(false);					
				}
			}
			color = mixColor();
		}
		point += present.stream()
				.map(p -> p.getTeam())
				.reduce(0, (sub, t) -> sub + (t == 1 ? 1:-1));
		if (playing.getPlayer().getRole() == Role.HOST) {
			playing.getPlayer().getClient().send("point;"+point);		
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)hitbox.getX(), (int)hitbox.getY(), (int)hitbox.getWidth(), (int)hitbox.getHeight());
	}
	
	private Color mixColor() {
		int r = 0;
		int g = 0;
		int b = 0;
		for (Player t : present) {
			if (t.getTank() != null) {
				r += t.getTank().getColor().getRed();
				g += t.getTank().getColor().getGreen();
				b += t.getTank().getColor().getBlue();	
			}
		}
		if (present.size() == 0) {
			return new Color(128, 128, 128, 50);
		}
		r /= present.size();
		g /= present.size();
		b /= present.size();
		return new Color(r, g, b, 50);
	}
	
	public HashSet<Player> getPresent() {
		return present;
	}
	
	public int getPoints() {
		return point;
	}
	
	public void setPoints(int p) {
		point = p;
	}
}
