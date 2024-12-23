package view;

import java.awt.Graphics;

import model.Game;
import model.gamestate.GameState;
import view.gamestate.PlayingView;

public class GameView implements IView, Runnable {
	
	public static final int FPS = 120;
	private Thread viewLoop;
	
	
	private GameWindow window;
	private GamePanel panel;
	
	// game state
	private PlayingView playing;
	
	public GameView() {
		panel = new GamePanel(this);
		window = new GameWindow(panel);
		viewLoop = new Thread(this);
		viewLoop.start();
	}

	@Override
	public void draw(Graphics g) {
		switch (GameState.state) {
		case MENU:
			break;
		case PLAYING:
			//playing.draw(g);
			break;
		case FINISH:
			break;
		}
	}
	
	@Override
	public void run() {
		double timePerFrame = 1_000_000_000.0 / FPS;
		
		long previousTime = System.nanoTime();
		
		int frames = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0.;
		double deltaF = 0.;
		
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if (deltaF >= 1) {
				panel.repaint();
				frames++;
				deltaF--;
			}
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("FPS : " + frames);
				frames = 0;
			}
		}
	}
	
	public GamePanel getPanel() {
		return panel;
	}

}
