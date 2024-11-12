 package effect;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;

import utils.Calcul;
import utils.Delay;
import utils.Vector;

public class ParticleSystem {

	protected ArrayList<Particle> particles;
	private double spawnSize;
	private int offset;
	
	public ParticleSystem(Particle particle, int nb) {
		spawnSize = particle.getSize();
		particles = new ArrayList<>(nb);
		for (int i = 0; i < nb; i++) {
			particles.add(new Particle(particle));
		}
		offset = 0;
	}
	
	public ParticleSystem(Particle particle, int nb, int offset) {
		spawnSize = particle.getSize();
		particles = new ArrayList<>(nb);
		for (int i = 0; i < nb; i++) {
			particles.add(new Particle(particle));
		}
		this.offset = offset;
	}
	
	public void emit(int x, int y, int min, int max) {
		double alpha = Math.toRadians(min);
		double beta  = Math.toRadians(max);
		for (int i = 0; i < particles.size(); i++) {
			double angle = alpha + Calcul.r.nextDouble() * (beta - alpha);
			Particle p = particles.get(i);
			// faut faire gaffe je sais pas a quelle point c'est gourmand de recreer un thread a chaque fois, a revoir si le jeu est lent
			new Delay(offset*i, () -> p.reset(x, y, new Vector(Math.cos(angle), Math.sin(angle)), spawnSize));
		}
	}
	
	public void interval(int x, int y, int min, int max) {
		double alpha = Math.toRadians(min);
		double beta  = Math.toRadians(max);
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if (p.isDead()) {				
				double angle = alpha + Calcul.r.nextDouble() * (beta - alpha);
				new Delay(offset*i, () -> p.reset(x, y, new Vector(Math.cos(angle), Math.sin(angle)), spawnSize));
			}
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
