package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import map.Obstacle;
import player.Player;
import client.GamePanel;

public class Playing implements Statemethods {

	private GamePanel panel;

	private Player player;
	private ArrayList<Player> players;
	private ArrayList<Player> playersToRemove;
	private ArrayList<Obstacle> obstacles;

	public Playing(GamePanel panel) {
		this.panel = panel;

		player = new Player(500, 500);
		players = new ArrayList<>();
		playersToRemove = new ArrayList<>();
		players.add(player);

		// test
		players.add(new Player(200, 700));
		players.add(new Player(225, 700));
		players.add(new Player(400, 700));
		players.add(new Player(425, 700));

		obstacles = new ArrayList<>();
		setUpWalls();
	}

	@Override
	public void update() {
		for (Player p : players) {
			p.updatePlayer(obstacles, players, playersToRemove);
		}
		players.removeAll(playersToRemove);
		playersToRemove.clear();
	}

	@Override
	public void draw(Graphics g) {
		for (Player p : players) {
			p.drawPlayer(g);
		}

		for (Obstacle o : obstacles) {
			o.drawObstacle(g);
		}
	}

	public Player getPlayer() {
		return player;
	}

	private void setUpWalls() {
		for (int i = 0; i < panel.getDimension().getWidth(); i += panel.getTileSize()) {
			obstacles.add(new Obstacle(i, 0, true));
			obstacles.add(new Obstacle(i, (int) panel.getDimension().getHeight() - panel.getTileSize(), true));
		}
		for (int i = panel.getTileSize(); i < panel.getDimension().getHeight() - panel.getTileSize(); i += panel
				.getTileSize()) {
			obstacles.add(new Obstacle(0, i, false));
			obstacles.add(new Obstacle((int) panel.getDimension().getWidth() - panel.getTileSize(), i, false));
		}
	}

	// inputs
	@Override
	public void mouseDragged(MouseEvent e) {
		player.getTank().setTargetX(e.getX());
		player.getTank().setTargetY(e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		player.getTank().setTargetX(e.getX());
		player.getTank().setTargetY(e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		player.getTank().fire(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			player.getTank().setUp(true);
			break;
		case KeyEvent.VK_Q:
			player.getTank().setLeft(true);
			break;
		case KeyEvent.VK_S:
			player.getTank().setDown(true);
			break;
		case KeyEvent.VK_D:
			player.getTank().setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.getTank().dash();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			player.getTank().setUp(false);
			break;
		case KeyEvent.VK_Q:
			player.getTank().setLeft(false);
			break;
		case KeyEvent.VK_S:
			player.getTank().setDown(false);
			break;
		case KeyEvent.VK_D:
			player.getTank().setRight(false);
			break;
		//escape	
		case KeyEvent.VK_ESCAPE:
			GameState.state = GameState.MENU;
		}
	}

}
