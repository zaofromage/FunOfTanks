package client;

import gamestates.*;
import player.Player;

import java.awt.Graphics;

public class Game implements Runnable {
	
	private GameWindow window;
	private GamePanel panel;
	private Thread gameLoop;
	private final int FPS = 120;
	private final int UPS = 200;
	
	private Player player;
	
	private Menu menu;
	private Playing playing;
	
	public Game() {
		menu = new Menu(this);
		panel = new GamePanel(this);
		window = new GameWindow(panel);
		panel.requestFocus();
		startGameLoop();
	}
	
	private void startGameLoop() {
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	public void update() {
		switch (GameState.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		}
	}
	
	public void render(Graphics g) {
		switch (GameState.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		}
	}

	@Override
	public void run() {
		
		double timePerFrame = 1_000_000_000.0 / FPS;
		double timePerUpdate = 1_000_000_000.0 / UPS;
		
		long previousTime = System.nanoTime();
		
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0.;
		double deltaF = 0.;
		
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if (deltaF >= 1) {
				panel.repaint();
				frames++;
				deltaF--;
			}
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("FPS : " + frames + " | UPS : " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public GamePanel getPanel() {
		return panel;
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}
	
	public void setPlaying(Playing p) {
		playing = p;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
