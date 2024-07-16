package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import client.GamePanel;
import gamestate.GameState;

public class MouseInput implements MouseListener, MouseMotionListener {
	
	private GamePanel panel;
	
	public MouseInput(GamePanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mouseDragged(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mouseDragged(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mouseMoved(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mouseClicked(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mouseReleased(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mouseEntered(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mouseEntered(e);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			panel.getGame().getMenu().mouseExited(e);
			break;
		case PLAYING:
			panel.getGame().getPlaying().mouseExited(e);
		}
	}

}