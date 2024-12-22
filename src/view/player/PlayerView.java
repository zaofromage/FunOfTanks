package view.player;

import java.awt.Graphics;

import model.player.Player;
import view.IView;

public class PlayerView implements IView {
	
	private Player model;
	
	private TankView tank;
	
	public PlayerView(Player player) {
		this.model = player;
		if (model.getTank() != null) {
			tank = new TankView(model.getTank());
		}
	}

	@Override
	public void draw(Graphics g) {
		tank.draw(g);
	}

}
