package gamestate;

import java.util.ArrayList;

import client.GamePanel;
import player.Player;

public class TeamMode extends Playing {

	public TeamMode(GamePanel panel, Player player, ArrayList<Player> players) {
		super(panel, player, players);
	}
	
	@Override
	public void update() {
		super.update();
		System.out.println("prout");
	}

}
