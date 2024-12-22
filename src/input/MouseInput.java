package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import client.Game;
import model.gamestate.GameState;

public class MouseInput implements MouseListener, MouseMotionListener {
	
	private Game game;
	
	public MouseInput(Game game) {
		this.game = game;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mouseDragged(e);
			break;
		case PLAYING:
			game.getPlaying().mouseDragged(e);
			break;
		case FINISH:
			game.getFinish().mouseDragged(e);
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mouseMoved(e);
			break;
		case PLAYING:
			game.getPlaying().mouseMoved(e);
			break;
		case FINISH:
			game.getFinish().mouseMoved(e);
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mouseClicked(e);
			break;
		case PLAYING:
			game.getPlaying().mouseClicked(e);
			break;
		case FINISH:
			game.getFinish().mouseClicked(e);
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mousePressed(e);
			break;
		case PLAYING:
			game.getPlaying().mousePressed(e);
			break;
		case FINISH:
			game.getFinish().mousePressed(e);
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mouseReleased(e);
			break;
		case PLAYING:
			game.getPlaying().mouseReleased(e);
			break;
		case FINISH:
			game.getFinish().mouseReleased(e);
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mouseEntered(e);
			break;
		case PLAYING:
			game.getPlaying().mouseEntered(e);
			break;
		case FINISH:
			game.getFinish().mouseEntered(e);
			break;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenu().mouseExited(e);
			break;
		case PLAYING:
			game.getPlaying().mouseExited(e);
			break;
		case FINISH:
			game.getFinish().mouseExited(e);
			break;
		}
	}

}