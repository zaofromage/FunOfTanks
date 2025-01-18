package model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.gamestate.GameState;
import model.gamestate.Menu;
import model.gamestate.Playing;
import model.player.Player;

public class Game implements IModel, Runnable {
	
	public static final int UPS = 200;
	
	private UUID id;
	
	private Thread logicLoop;
	
	private Map<UUID, Player> players = new HashMap<UUID, Player>();
	
	private GameState state = GameState.MENU;
		
	// game states
	private Menu menu;
	private Playing playing;
	
	public Game() {
		id = UUID.randomUUID();
		logicLoop = new Thread(this);
		menu = new Menu(this);
		logicLoop.start();
	}
	
	@Override
	public void update() {
		switch (state) {
		case MENU:
			if (menu != null) {
				menu.update();				
			}
			break;
		case PLAYING:
			if (playing != null) {
				playing.update();				
			}
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
		state = s;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Map<UUID, Player> getPlayers() {
		return players;
	}
	
	public GameState getState() {
		return state;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
}
