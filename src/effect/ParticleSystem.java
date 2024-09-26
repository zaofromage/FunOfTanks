package effect;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utils.Vector;

public class ParticleSystem {

	protected ArrayList<Particle> particles;
	private double spawnSize;
	private Random r = new Random();
	public int x, y;
	
	public ParticleSystem(Particle p, int nb) {
		x = 0;
		y = 0;
		particles = new ArrayList<>(Collections.nCopies(nb, p));
	}
	
	public void emit(int min, int max) {
		for (Particle p : particles) {
			double norm = r.nextDouble();
			int angle = r.nextInt(min, max);
			Vector v = new Vector(norm*Math.cos(angle*Math.PI/180), norm*Math.cos(angle*Math.PI/180));
			p.reset(x, y, v, spawnSize);
		}
	}
	
	public void update() {
		for (Particle p : particles) {
			p.update();
		}
	}
	
	public void draw(Graphics g) {
		
	}
}
