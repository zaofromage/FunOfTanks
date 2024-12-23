package model.gamestate;

import java.util.UUID;

import model.Game;
import model.IModel;
import model.player.Player;

public class Menu implements IModel {
	
	private Game game;
	
	public Menu(Game game) {
		this.game = game;
	}

	@Override
	public void update() {
		
	}
	
	public void addPlayer(String name, UUID id) {
		game.getPlayers().put(id, new Player(name, id));
	}
	
	public void removePlayer(UUID id) {
		game.getPlayers().remove(id);
	}
	
	public void setReady(UUID id, boolean ready) {
		game.getPlayers().get(id).setReady(ready);
	}
	
	public void setMode(GameMode m) {
		GameMode.gameMode = m;
	}
	
	public void play() {
		if (game.getPlayers().values().stream().allMatch(Player::isReady)) {
			game.switchState(GameState.PLAYING);
		}
	}

}
