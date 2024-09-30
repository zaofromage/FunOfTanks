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
	
	public ParticleSystem(Particle particle, int nb) {
		spawnSize = particle.getSize();
		particles = new ArrayList<>(nb);
		for (int i = 0; i < nb; i++) {
			particles.add(new Particle(particle));
		}
	}
	
	public void emit(int x, int y, int min, int max) {
		double alpha = Math.toRadians(min);
		double beta  = Math.toRadians(max);
		for (Particle p : particles) {   
			double angle = alpha + r.nextDouble() * (beta - alpha);
			p.reset(x, y, new Vector(Math.cos(angle), Math.sin(angle)), spawnSize);
		}
	}
	
	public void update() {
		if (particles.stream().filter(p -> !p.isDead()).count() > 0) {
			for (Particle p : particles) {
				p.update();
			}			
		}
	}
	
	public void draw(Graphics g) {
		for (Particle p : particles) {
			p.draw(g);
		}
	}
	
	public void setTemplate(Particle p, int nb) {
		particles.clear();
		for (int i = 0; i < nb; i++) {
			particles.add(new Particle(p));
		}
	}
	
	public void setLifetime(double lifetime) {
		for (Particle p : particles)  {
			p.setLifetime(spawnSize, lifetime);
		}
	}
}
