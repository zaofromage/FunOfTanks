package client;

import controller.GameController;
import model.Game;
import model.player.Player;
import serverHost.Role;
import view.GameView;

public class Main {

	public static void main(String[] args) {
		Player player = new Player("justin", Role.HOST);
		Game game = new Game(player);
		GameView view = new GameView(game);
		GameController controller = new GameController(game , view);
	}
}
