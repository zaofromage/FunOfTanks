package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.GamePanel;
import gamestate.GameState;

public class KeyboardInput implements KeyListener {
	
	private GamePanel panel;
	
	public KeyboardInput(GamePanel panel) {
		this.panel = panel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().keyTyped(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().keyTyped(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().keyReleased(e);
		}
	}

}