package client;

import player.Player;
import utils.Delay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gamestate.*;

public class Game implements Runnable {
	
	private GameWindow window;
	private GamePanel panel;
	private Thread gameLoop;
	public static final int FPS = 120;
	public static final int UPS = 200;
	
	private Player player;
	
	private Menu menu;
	private Playing playing;
	private Finish finish;
	
	private static String errorMessage;
	private static String message;
	private Font errorMessageFont;
	
	public Game() {
		
		panel = new GamePanel(this);
		menu = new Menu(this);
		window = new GameWindow(panel);
		errorMessageFont = new Font("SansSerif", Font.PLAIN, 30);
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
		case FINISH:
			finish.update();
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
		case FINISH:
			finish.draw(g);
			break;
		}
		if (errorMessage != null) {
			g.setColor(Color.red);
			g.setFont(errorMessageFont);
			g.drawString(errorMessage, panel.getDimension().width/2 - g.getFontMetrics().stringWidth(errorMessage)/2, panel.getDimension().height/4 * 3);
		}
		if (message != null) {
			g.setColor(Color.black);
			g.setFont(errorMessageFont);
			g.drawString(message, panel.getDimension().width/2 - g.getFontMetrics().stringWidth(message)/2, panel.getDimension().height/4 * 3);
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
	
	public static void printErrorMessage(String msg) {
		errorMessage = msg;
		new Delay(3000, () -> errorMessage = null);
	}
	
	public static void printMessage(String msg) {
		message = msg;
		new Delay(3000, () -> message = null);
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
	
	public void setFinish(Finish f) {
		finish = f;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
