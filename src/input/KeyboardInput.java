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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			panel.moveY(-1);
			break;
		case KeyEvent.VK_Q:
			panel.moveX(-1);
			break;
		case KeyEvent.VK_S:
			panel.moveY(1);
			break;
		case KeyEvent.VK_D:
			panel.moveX(1);
			break;
		}
		
	}

}