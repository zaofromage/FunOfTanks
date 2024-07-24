package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Delay;

public class Skill {
	/*
	* ajouter un systeme de capacité
	 *    - dash a travers les murs
	 *    - rendre les balles rebondissantes
	 *    - quand on drag la souris ça fait une sorte de mur temporaire qui renvoit les tires
	 *    - reduire le cooldown de tir et dash
	 *    - augmenter la vitesse temporairement
	 *    - full counter (si tu le cale tu gagnes instant
	 *    - augmenter le nombre de mur posés par pose de mur
	 *    - faire descendre un soldat qui tire des petites balles tout seul
	 *    - tirer trois balles en un coup
	*/
	private String title;
	private int cooldown;
	private boolean inCooldown;
	private BufferedImage img;
	private Runnable effect;
	
	private Color cooldownColor = new Color(128, 128, 128, 200);
	private int cooldownOffset;
	private long endOfTimer;

	public Skill(String title, int cooldown, BufferedImage img, Runnable effect) {
		this.title = title;
		this.cooldown = cooldown;
		this.inCooldown = false;
		this.img = img;
		this.effect = effect;
		this.cooldownOffset = 0;
	}
	
	public void launch() {
		if (!inCooldown) {
			effect.run();
			inCooldown = true;
			endOfTimer = System.currentTimeMillis() + cooldown;
			new Delay(cooldown, () -> {
				inCooldown = false;
				cooldownOffset = 0;
			});
		}
	}
	
	public void update() {
		long timeBeforeEnable = endOfTimer - System.currentTimeMillis();
		if (timeBeforeEnable > 0) {
			cooldownOffset = 50 - (int) (timeBeforeEnable * 100 / cooldown) * 50 / 100;			
		}
	}
	
	public void draw(Graphics g, int x, int y) {
		g.drawImage(img, x, y, null);
		if (inCooldown) {
			g.setColor(cooldownColor);
			g.fillRect(x, y + cooldownOffset, 50, 50 - cooldownOffset);
		}
	}
	
	public static Skill speedUp(Player player) {
		try {
			return new Skill("Speed up", 10000, ImageIO.read(Skill.class.getResource("/images/speedup.png")), () -> {
				if (player.getTank() != null) {
					player.getTank().setSpeed(player.getTank().BASE_SPEED * 3);
					new Delay(3000, () -> player.getTank().setSpeed(player.getTank().BASE_SPEED));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Skill dashThrough(Player player) {
		try {
			return new Skill("Dash through walls", 5000, ImageIO.read(Skill.class.getResource("/images/dashthrough.png")), () -> {
				if (player.getTank() != null) {
					player.getTank().setCanDashThrough(true);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
