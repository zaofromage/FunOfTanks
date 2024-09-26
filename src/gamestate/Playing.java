package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import map.Obstacle;
import serverClass.ServerBullet;
import utils.Calcul;
import utils.Delay;
import player.Player;
import player.PlayerMode;
import client.GamePanel;
import effect.Particle;
import effect.ParticleSystem;
import effect.Shape;
import input.PlayerInputs;
import utils.Vector;

public class Playing implements Statemethods {

	private GamePanel panel;

	private Player player;
	private ArrayList<Player> players;
	private ArrayList<Obstacle> obstacles;

	private ArrayList<ServerBullet> enemiesBullets;
	
	private Particle p = new Particle(20, Shape.RECTANGLE, 0.5, new Vector(), Color.RED);
	private ParticleSystem ps = new ParticleSystem(p, 20);

	public Playing(GamePanel panel, Player player, ArrayList<Player> players) {
		this.panel = panel;
		this.player = player;
		this.players = players;
		obstacles = new ArrayList<>();
		enemiesBullets = new ArrayList<ServerBullet>();
		setUpWalls();
	}

	@Override
	public void update() {
		for (Player p : players) {
			p.updatePlayer(getObstacles(), players);
		}
	}

	@Override
	public void draw(Graphics g) {
		for (Obstacle o : getObstacles()) {
			o.drawObstacle(g);
		}

		for (Player p : players) {
			p.drawPlayer(g);
		}

		for (ServerBullet b : enemiesBullets) {
			b.drawBullet(g);
		}
		
		player.drawSkills(g);
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<ServerBullet> getEnemiesBullets() {
		return enemiesBullets;
	}

	private void setUpWalls() {
		for (int i = 0; i < panel.getDimension().getWidth(); i += panel.getTileSize()) {
			getObstacles().add(new Obstacle(i, 0, false));
			getObstacles().add(new Obstacle(i, (int) panel.getDimension().getHeight() - panel.getTileSize(), false));
		}
		for (int i = panel.getTileSize(); i < panel.getDimension().getHeight() - panel.getTileSize(); i += panel
				.getTileSize()) {
			getObstacles().add(new Obstacle(0, i, false));
			getObstacles().add(new Obstacle((int) panel.getDimension().getWidth() - panel.getTileSize(), i, false));
		}
	}

	// inputs
	@Override
	public void mouseDragged(MouseEvent e) {
		if (player.getTank() != null) {
			player.getTank().setTargetX(e.getX());
			player.getTank().setTargetY(e.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (player.getTank() != null) {
			player.getTank().setTargetX(e.getX());
			player.getTank().setTargetY(e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (player.getTank() != null) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (player.getTank().getMode() == PlayerMode.BASE && player.getTank().getCannon().canFire()) {
					player.getTank().switchMode(PlayerMode.AIM);
				}
			}
		    else if (e.getButton() == MouseEvent.BUTTON3) {
				player.getTank().switchMode(PlayerMode.BLOC);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (player.getTank() != null) {
			switch (player.getTank().getMode()) {
			case AIM:
				if (e.getButton() == MouseEvent.BUTTON1) {
					player.getTank().fire();
					player.getTank().switchMode(PlayerMode.BASE);
				}
				break;
			case BLOC:
				if (e.getButton() == MouseEvent.BUTTON1) {
					player.getTank().dropObstacle(Calcul.limitRange(e.getX(), player.getTank().getX()), Calcul.limitRange(e.getY(), player.getTank().getY()), true, players, getObstacles());
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					player.getTank().switchMode(PlayerMode.BASE);
				}
				break;
			default:
				break;
			}
		}
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
		int keyCode = e.getKeyCode();
		if (keyCode == PlayerInputs.up) {
			if (player.getTank() != null) {
				player.getTank().setUp(true);
			}
		} else if (keyCode == PlayerInputs.left) {
			if (player.getTank() != null) {
				player.getTank().setLeft(true);
			}
		} else if (keyCode == PlayerInputs.down) {
			if (player.getTank() != null) {
				player.getTank().setDown(true);
			}
		} else if (keyCode == PlayerInputs.right) {
			if (player.getTank() != null) {
				player.getTank().setRight(true);
			}
		} else if (keyCode == PlayerInputs.dash) {
			player.getTank().dash(obstacles);
		} else if (keyCode == PlayerInputs.skill1) {
			player.getSkill1().launch();
		} else if (keyCode == PlayerInputs.skill2) {
			player.getSkill2().launch();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == PlayerInputs.up) {
			if (player.getTank() != null) {
				player.getTank().setUp(false);
			}
		} else if (keyCode == PlayerInputs.left) {
			if (player.getTank() != null) {
				player.getTank().setLeft(false);
			}
		} else if (keyCode == PlayerInputs.down) {
			if (player.getTank() != null) {
				player.getTank().setDown(false);
			}
		} else if (keyCode == PlayerInputs.right) {
			if (player.getTank() != null) {
				player.getTank().setRight(false);
			}
		} else if (keyCode == PlayerInputs.escape) {
			GameState.state = GameState.MENU;
		}
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

}
