package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	public static final Dimension dimension = new Dimension(1250, 800);
	private GameView view;
	
	public static final int tileSize = 50;
	
	public GamePanel(GameView game) {
		this.view = game;
		setPanelSize();
		setFocusable(true);
		requestFocusInWindow();
		setFocusTraversalKeysEnabled(false);
	}
	
	/**
	 * Graphic loop
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		view.draw(g);
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

	public GameView getView() {
		return view;
	}
}
