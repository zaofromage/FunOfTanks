package client;

import controller.GameController;
import model.Game;
import view.GameView;

public class Main {

	public static void main(String[] args) {
		if (args[0].equals("server")) {
			Game game = new Game();			
		} else {
			GameView view = new GameView();
			GameController controller = new GameController(view);			
		}
	}
}
