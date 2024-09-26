package effect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import effect.Shape;

import client.Game;
import utils.Vector;

public class Particle {
	
	private double size;
	private double decrement;
	private Shape shape;
	private Vector dir;
	public double x, y;
	private Color color;

	public Particle(int size, Shape shape, double lifetime, Vector dir, Color c) {
		this.size = size;
		this.decrement = size/(Game.FPS * lifetime);
		this.shape = shape;
		this.dir = dir;
		this.x = 0;
		this.y = 0;
		this.color = c;
	}
	
	private void disapear() {
		size -= decrement;
        if (size <= 0.1f) {
           size = 0;
        }
	}
	
	private void move() {
		x += dir.x;
		y += dir.y;
	}
	
	public boolean isDead() {
		return size <= 0.;
	}
	
	public void reset(int x, int y, Vector newdir, double s) {
		this.x = x;
		this.y = y;
		this.dir = newdir;
		size = s;
	}
	
	public void update() {
		disapear();
		move();
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		switch (shape) {
		case RECTANGLE: g.fillRect((int) x, (int) y, (int) size, (int) size);break;
		case CIRCLE: g.fillOval((int) x, (int) y, (int) size, (int) size);break;
		}
	}
	
	public double getSize() {
		return size;
	}
	
	public Vector getDir() {
		return dir;
	}
}
