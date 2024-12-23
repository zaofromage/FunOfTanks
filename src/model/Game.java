package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.gamestate.GameState;
import model.gamestate.Menu;
import model.gamestate.Playing;
import model.player.Player;
import network.Server;
import network.UDPServer;

public class Game implements IModel, Runnable {
	
	public static final int UPS = 200;
	
	private Thread logicLoop;
	
	private Map<UUID, Player> players = new HashMap<UUID, Player>();
		
	// game states
	private Menu menu;
	private Playing playing;
	
	public Game() {
		try {
			new Server(this);
			new UDPServer(this);
		} catch (IOException e) {
			System.err.println("server failed to start");
		}
		logicLoop = new Thread(this);
		menu = new Menu(this);
		logicLoop.start();
	}
	
	@Override
	public void update() {
		switch (GameState.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case FINISH:
			break;
		}
	}
	
	@Override
	public void run() {
		double timePerUpdate = 1_000_000_000.0 / UPS;
		
		long previousTime = System.nanoTime();
		
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0.;
		
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			previousTime = currentTime;
			
			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("UPS : " + updates);
				updates = 0;
			}
		}
	}
	
	public void switchState(GameState s) {
		menu = null;
		playing = null;
		switch (s) {
		case MENU:
			menu = new Menu(this);
			break;
		case PLAYING:
			playing = new Playing(this);
			break;
		case FINISH:
			break;
		}
		GameState.state = s;
	}
	
	public Map<UUID, Player> getPlayers() {
		return players;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
}
