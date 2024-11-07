package gamestate;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import map.Obstacle;
import serverClass.ServerBullet;
import utils.Calcul;
import player.Player;
import player.PlayerMode;
import client.GamePanel;
import input.PlayerInputs;

public class Playing implements Statemethods {

	private GamePanel panel;

	private Player player;
	private ArrayList<Player> players;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Obstacle> obsToAdd = new ArrayList<Obstacle>();
	private ArrayList<Obstacle> obsToRemove = new ArrayList<Obstacle>();

	private ArrayList<ServerBullet> enemiesBullets;
	
	private HashMap<Player, Integer> leaderBoard = new HashMap<>();
	
	protected int isFinish = 0;

	private boolean drawLeaderBoard;
	

	public Playing(GamePanel panel, Player player, ArrayList<Player> players) {
		this.panel = panel;
		this.player = player;
		this.players = players;
		obstacles = new ArrayList<>();
		enemiesBullets = new ArrayList<ServerBullet>();
		setUpWalls();
		for (Player p : players) {
			p.createTank(Calcul.r.nextInt(100, (int)panel.getDimension().getWidth()-150), Calcul.r.nextInt(100, (int)panel.getDimension().getHeight()-150));
			leaderBoard.put(p, 0);
		}
	}

	@Override
	public void update() {
		for (Player p : players) {
			p.updatePlayer(getObstacles(), players);
		}
		if (!obsToAdd.isEmpty()) {
			obstacles.addAll(obsToAdd);
			obsToAdd.clear();
		}
		if (!obsToRemove.isEmpty()) {
			obstacles.removeAll(obsToRemove);
			obsToRemove.clear();
		}
		if (isFinish != 0) {
			panel.getGame().setFinish(new Finish(isFinish, players.stream().filter(p -> p.getTeam() == isFinish).findAny().orElse(null).tankColor));				
			GameState.state = GameState.FINISH;
		}
	}

	@Override
	public void draw(Graphics g) {
		for (Obstacle o : getObstacles()) {
			o.drawObstacle(g);
		}
		for (ServerBullet b : enemiesBullets) {
			b.drawBullet(g);
		}
		for (Player p : players) {
			p.drawPlayer(g);
		}
		player.drawSkills(g);
		if (drawLeaderBoard) {
			System.out.println(leaderBoard);
		}
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
	
	public HashMap<Player, Integer> getLeaderBoard() {
		return leaderBoard;
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
		player.getClient().sendUDP("X : " + e.getX() + " Y : " + e.getY());
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
			if (player.getTank() != null) {
				player.getTank().dash(obstacles);				
			}
		} else if (keyCode == PlayerInputs.skill1) {
			if (player.getTank() != null) {
				player.getSkill1().launch();				
			}
		} else if (keyCode == PlayerInputs.skill2) {
			if (player.getTank() != null) {
				player.getSkill2().launch();				
			}
		} else if (keyCode == PlayerInputs.skill3) {
			if (player.getTank() != null) {
				player.getSkill3().launch();				
			}
		} else if (keyCode == PlayerInputs.leaderBoard) {
			drawLeaderBoard = true;
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
		} else if (keyCode == PlayerInputs.leaderBoard) {
			drawLeaderBoard = false;
		}
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public ArrayList<Obstacle> getObsToRemove() {
		return obsToRemove;
	}
	
	public ArrayList<Obstacle> getObsToAdd() {
		return obsToAdd;
	}

}
