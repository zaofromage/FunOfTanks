package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;

import input.KeyboardInput;
import input.MouseInput;
import player.Player;
import map.Obstacle;

public class GamePanel extends JPanel{
	
	private MouseInput mouse;
	private final Dimension dimension;
	
	private final int tileSize = 50;
	
	private Player player;
	private ArrayList<Player> players;
	private ArrayList<Obstacle> obstacles;
	
	public GamePanel() {
		mouse = new MouseInput(this);
		dimension = new Dimension(1250, 800);
		setPanelSize();
		addKeyListener(new KeyboardInput(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		player = new Player();
		players = new ArrayList<>();
		players.add(player);
		obstacles = new ArrayList<>();		
		setUpWalls();
	}
	
	/**
	 * Logic loop
	 */
	public void updateGame() {
		for (Player p : players) {
			p.updatePlayer(obstacles);
		}
	}
	
	/**
	 * Graphic loop
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Player p : players) {
			p.drawPlayer(g);
		}
		
		for (Obstacle o : obstacles) {
			o.drawObstacle(g);
		}
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	
	
	public Player getPlayer() { return player; }
	
	private void setPanelSize() {
		setPreferredSize(dimension);
	}
	
	private void setUpWalls() {
		for (int i = 0; i < dimension.getWidth(); i+=tileSize) {
			obstacles.add(new Obstacle(i, 0, true));
			obstacles.add(new Obstacle(i, (int) dimension.getHeight() - tileSize, true));
		}
		for (int i = tileSize; i < dimension.getHeight() - tileSize; i+=tileSize) {
			obstacles.add(new Obstacle(0, i, false));
			obstacles.add(new Obstacle((int) dimension.getWidth() - tileSize, i, false));
		}
	}
}
