package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.GamePanel;

public class KeyboardInput implements KeyListener {
	
	private GamePanel panel;
	public KeyboardInput(GamePanel panel) {
		this.panel = panel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			panel.getPlayer().getTank().setUp(true);
			break;
		case KeyEvent.VK_Q:
			panel.getPlayer().getTank().setLeft(true);
			break;
		case KeyEvent.VK_S:
			panel.getPlayer().getTank().setDown(true);
			break;
		case KeyEvent.VK_D:
			panel.getPlayer().getTank().setRight(true);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			panel.getPlayer().getTank().setUp(false);
			break;
		case KeyEvent.VK_Q:
			panel.getPlayer().getTank().setLeft(false);
			break;
		case KeyEvent.VK_S:
			panel.getPlayer().getTank().setDown(false);
			break;
		case KeyEvent.VK_D:
			panel.getPlayer().getTank().setRight(false);
			break;
		}
	}

}