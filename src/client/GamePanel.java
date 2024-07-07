package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import input.KeyboardInput;
import input.MouseInput;
import player.Player;

public class GamePanel extends JPanel{
	
	private MouseInput mouse;
	private Player player;
	
	public GamePanel() {
		mouse = new MouseInput(this);
		setPanelSize();
		addKeyListener(new KeyboardInput(this));
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		player = new Player();
	}
	
	/**
	 * Logic loop
	 */
	public void updateGame() {
		player.getTank().updateTank();
	}
	
	/**
	 * Graphic loop
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		player.getTank().drawTank(g);

		Toolkit.getDefaultToolkit().sync();
	}
	
	
	
	public Player getPlayer() { return player; }
	
	private void setPanelSize() {
		setPreferredSize(new Dimension(1280, 800));
	}
}
