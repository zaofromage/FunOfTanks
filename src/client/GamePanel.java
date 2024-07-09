package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import input.KeyboardInput;
import input.MouseInput;

public class GamePanel extends JPanel{
	
	private final Dimension dimension;
	private Game game;
	
	private final int tileSize = 50;
	
	public GamePanel(Game game) {
		this.game = game;
		MouseInput mouse = new MouseInput(this);
		dimension = new Dimension(1250, 800);
		setPanelSize();
		addKeyListener(new KeyboardInput(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	/**
	 * Graphic loop
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void setPanelSize() {
		setPreferredSize(dimension);
	}

	public Dimension getDimension() {
		return dimension;
	}

	public int getTileSize() {
		return tileSize;
	}

	public Game getGame() {
		return game;
	}
	
	
}
