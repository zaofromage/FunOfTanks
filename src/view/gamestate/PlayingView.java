package view.gamestate;

import java.awt.Graphics;

import model.gamestate.Playing;
import view.IView;
import view.player.PlayerView;

public class PlayingView implements IView {
	
	private Playing model;
	
	private PlayerView player;
	
	public PlayingView(Playing playing) {
		this.model = playing;
		if (model.getPlayer() != null) {
			player = new PlayerView(model.getPlayer());
		}
	}

	@Override
	public void draw(Graphics g) {
		player.draw(g);
	}

}
