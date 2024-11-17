package serverClass;

import java.awt.Color;
import java.awt.Graphics;

import client.Game;
import player.Grenade;
import player.Player;

public class ServerGrenade extends ServerBullet {

	private double diam = 15;
	private boolean ascend = true;
	private int bounce = 0;
	private double redIndex = 0;
	private double greenIndex = 255;
	
	public ServerGrenade(int x, int y, double o, String owner, int id) {
		super(x, y, o, owner, id);
		color = Color.GREEN;
	}
	
	@Override
	public void update(int x, int y, boolean holding) {
		super.update(x, y, holding);
		redIndex += redIndex+(double)255/((Grenade.timeToBlowUp/1000)*Game.FPS) < 255 ? (double)255/((Grenade.timeToBlowUp/1000)*Game.FPS):0;
		greenIndex -= greenIndex-(double)255/((Grenade.timeToBlowUp/1000)*Game.FPS) > 0 ? (double)255/((Grenade.timeToBlowUp/1000)*Game.FPS)/2:0;
		color = new Color((int)redIndex, (int)greenIndex, 0);
		if (!holding) {
			if (ascend) {
				diam+=0.5;
				if (diam > (75 - (bounce*26))) {
					ascend = false;
				}
			} else {
				if (diam >= 25) {
					diam-=0.5;			
				} else {
					ascend = true;
					bounce++;
				}
			}			
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)x, (int)y, (int)diam, (int)diam);
		g.setColor(Color.gray);
		g.fillRect((int)(x+diam/2), (int)(y), (int) (diam/5), (int) (diam/2));
	}
	
	@Override
	public void updateHitbox() {
		hitbox.setBounds(x, y, (int)diam, (int)diam);
	}
	
	@Override
	public void die(Player p) {
		p.blowup(x, y, 1);
	}

}
