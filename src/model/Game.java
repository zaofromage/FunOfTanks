package model;

import model.gamestate.GameState;
import model.gamestate.Playing;
import model.player.Player;

public class Game implements IModel, Runnable {
	
	public static final int UPS = 200;
	
	private Thread logicLoop;
	
	private Player player;
	
	// game states
	private Playing playing;
	
	public Game(Player player) {
		this.player = player;
		playing = new Playing(this);
		logicLoop = new Thread(this);
		logicLoop.start();
	}
	
	@Override
	public void update() {
		switch (GameState.state) {
		case MENU:
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
	

	public Player getPlayer() {
		return player;
	}

	public Playing getPlaying() {
		return playing;
	}
}
