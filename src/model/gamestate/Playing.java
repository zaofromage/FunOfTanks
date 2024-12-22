package model.gamestate;

import model.Game;
import model.IModel;
import model.player.Player;

public class Playing implements IModel {
	
	private Game game;
	
	private Player player;
	
	public Playing(Game game) {
		this.game = game;
		this.player = game.getPlayer();
	}

	@Override
	public void update() {
		player.update();
	}

	public Player getPlayer() {
		return player;
	}
}
