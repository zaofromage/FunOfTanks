package client;

public class Game implements Runnable {
	
	private GameWindow window;
	private GamePanel panel;
	private Thread gameLoop;
	private final int FPS = 120;
	
	public Game() {
		panel = new GamePanel();
		window = new GameWindow(panel);
		panel.requestFocus();
		startGameLoop();
	}
	
	private void startGameLoop() {
		gameLoop = new Thread(this);
		gameLoop.start();
	}

	@Override
	public void run() {
		
		double timePerFrame = 1_000_000_000.0 / FPS;
		long lastFrame = System.nanoTime();
		long now = System.nanoTime();
		
		int frames = 0;
		long lastCheck = System.currentTimeMillis();
		
		while(true) {
			now = System.nanoTime();
			
			if (now - lastFrame >= timePerFrame) {
				panel.repaint();
				lastFrame = now;
				frames++;
			}
			
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("FPS : " + frames);
				frames = 0;
			}
		}
	}
}
