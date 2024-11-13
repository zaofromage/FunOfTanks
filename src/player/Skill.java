package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

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
	
	public void draw(Graphics g, int x, int y, int size) {
		g.drawImage(img, x, y, size, size, null);
		if (inCooldown) {
			g.setColor(cooldownColor);
			g.fillRect(x, y + cooldownOffset, 50, 50 - cooldownOffset);
		}
	}
	
	public static Skill[] getAllSkills(Player p) {
		Skill[] res = new Skill[4];
		res[0] = speedUp(p);
		res[1] = dashThrough(p);
		res[2] = grosseBertha(p);
		res[3] = tripleShot(p);
		return res;
	}
	
	public static Skill speedUp(Player player) {
		try {
			return new Skill("Speed up", 20000, ImageIO.read(Skill.class.getResource("/images/speedup.png")), () -> {
				if (player.getTank() != null) {
					player.getTank().setSpeed(player.getTank().BASE_SPEED * 3);
					new Delay(3000, () -> {
						if (player.getTank() != null) {
							player.getTank().setSpeed(player.getTank().BASE_SPEED);						
						}
					});
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Skill dashThrough(Player player) {
		try {
			return new Skill("Dash through walls", 10000, ImageIO.read(Skill.class.getResource("/images/dashthrough.png")), () -> {
				if (player.getTank() != null) {
					player.getTank().setCanDashThrough(true);
					player.getTank().setCanDash(true);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Skill grosseBertha(Player player) {
		try {
			return new Skill("Next shot goes hard", 10000, ImageIO.read(Skill.class.getResource("/images/bertha.png")), () -> {
				if (player.getTank() != null) {
					player.getTank().getCannon().setCanBertha(true);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Skill tripleShot(Player player) {
		try {
			return new Skill("la plante vs zombie la", 4000, ImageIO.read(Skill.class.getResource("/images/tripleshot.png")), () -> {
				if (player.getTank() != null) {
					player.getTank().getCannon().setCanTripleShot(true);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cooldown, cooldownColor, cooldownOffset, endOfTimer, inCooldown, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Skill other = (Skill) obj;
		return cooldown == other.cooldown && Objects.equals(cooldownColor, other.cooldownColor)
				&& cooldownOffset == other.cooldownOffset && endOfTimer == other.endOfTimer
				&& inCooldown == other.inCooldown && Objects.equals(title, other.title);
	}
	
	
}
