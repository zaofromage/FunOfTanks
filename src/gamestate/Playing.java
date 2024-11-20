package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import map.Obstacle;
import utils.Calcul;
import player.Bullet;
import player.Player;
import player.PlayerMode;
import player.Stats;
import player.TypeShot;
import client.GamePanel;
import input.PlayerInputs;

public class Playing implements Statemethods {

	private GamePanel panel;

	private Player player;
	private ArrayList<Player> players;
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private ArrayList<Obstacle> obsToAdd = new ArrayList<Obstacle>();
	private ArrayList<Obstacle> obsToRemove = new ArrayList<Obstacle>();

	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> bulletsToAdd = new ArrayList<Bullet>();
	private ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();

	private HashMap<Player, Stats> leaderBoard = new HashMap<>();

	protected int isFinish = 0;

	private boolean drawLeaderBoard;

	public Playing(GamePanel panel, Player player, ArrayList<Player> players) {
		this.panel = panel;
		this.player = player;
		this.players = players;
		setUpWalls();
		for (Player p : players) {
			p.createTank(Calcul.r.nextInt(100, (int) panel.getDimension().getWidth() - 150),
					Calcul.r.nextInt(100, (int) panel.getDimension().getHeight() - 150));
			leaderBoard.put(p, new Stats());
		}
	}

	@Override
	public void update() {
		for (Player p : players) {
			p.updatePlayer(getObstacles(), players);
		}
		for (Bullet b : bullets.stream().filter(b -> b.getPlayer().equals(player)).collect(Collectors.toList())) {
			b.update(obstacles);
		}
		if (isFinish != 0) {
			GameState.state = GameState.FINISH;
			panel.getGame().setFinish(new Finish(isFinish,
					players.stream().filter(p -> p.getTeam() == isFinish).findAny().orElse(null).tankColor,
					panel.getGame(), players));
		}
	}

	@Override
	public void draw(Graphics g) {
		for (Obstacle o : getObstacles()) {
			o.drawObstacle(g);
		}
		if (!obsToAdd.isEmpty()) {
			obstacles.addAll(obsToAdd);
			obsToAdd.clear();
		}
		if (!obsToRemove.isEmpty()) {
			obstacles.removeAll(obsToRemove);
			obsToRemove.clear();
		}
		for (Player p : players) {
			p.drawPlayer(g);
		}
		for (Bullet b : bullets) {
			b.draw(g);
		}
		if (!bulletsToAdd.isEmpty()) {
			bullets.addAll(bulletsToAdd);
			bulletsToAdd.clear();
		}
		if (!bulletsToRemove.isEmpty()) {
			bullets.removeAll(bulletsToRemove);
			bulletsToRemove.clear();
		}
		player.drawSkills(g);
		// draw leaderBoard
		if (drawLeaderBoard) {
			drawLeaderBoard(g);
		}
	}
	
	public void drawLeaderBoard(Graphics g) {
		int i = 0;
		g.setColor(new Color(255, 0, 0, 50));
		g.fillRect(GamePanel.dimension.width / 2 - ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, 100, Stats.STAT_HEIGHT);
		g.fillRect(
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 2
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
		g.fillRect(
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 3
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
		g.fillRect(
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 4
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawRect(GamePanel.dimension.width / 2 - ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, 100, Stats.STAT_HEIGHT);
		g.drawRect(
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 2
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
		g.drawRect(
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 3
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
		g.drawRect(
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 4
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
				175 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawString("PLAYER",
				GamePanel.dimension.width / 2 - ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + 50
						- g.getFontMetrics().stringWidth("PLAYER") / 2,
				175 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
		g.drawString("KILLS",
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 2
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + Stats.STAT_WIDTH / 2
						- g.getFontMetrics().stringWidth("KILLS") / 2,
				175 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
		g.drawString("DEATH",
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 3
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + Stats.STAT_WIDTH / 2
						- g.getFontMetrics().stringWidth("DEATH") / 2,
				175 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
		g.drawString("RATIO",
				GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 4
						- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + Stats.STAT_WIDTH / 2
						- g.getFontMetrics().stringWidth("RATIO") / 2,
				175 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
		for (Map.Entry<Player, Stats> l : leaderBoard.entrySet()) {
			g.setColor(new Color(255, 0, 0, 50));
			g.fillRect(GamePanel.dimension.width / 2 - ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, 100, Stats.STAT_HEIGHT);
			g.fillRect(
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 2
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
			g.fillRect(
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 3
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
			g.fillRect(
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 4
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(GamePanel.dimension.width / 2 - ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, 100, Stats.STAT_HEIGHT);
			g.drawRect(
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 2
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
			g.drawRect(
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 3
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
			g.drawRect(
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 4
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2),
					200 + Stats.STAT_HEIGHT * i, Stats.STAT_WIDTH, Stats.STAT_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawString(l.getKey().getName(),
					GamePanel.dimension.width / 2 - ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + 50
							- g.getFontMetrics().stringWidth(l.getKey().getName()) / 2,
					200 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
			g.drawString("" + l.getValue().kills,
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 2
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + Stats.STAT_WIDTH / 2
							- g.getFontMetrics().stringWidth("" + l.getValue().kills) / 2,
					200 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
			g.drawString("" + l.getValue().death,
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 3
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + Stats.STAT_WIDTH / 2
							- g.getFontMetrics().stringWidth("" + l.getValue().death) / 2,
					200 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
			g.drawString("" + l.getValue().ratio(),
					GamePanel.dimension.width / 2 + Stats.STAT_WIDTH * 4
							- ((100 + Stats.STAT_WIDTH * Stats.STATS_NUMBER) / 2) + Stats.STAT_WIDTH / 2
							- g.getFontMetrics().stringWidth("" + l.getValue().ratio()) / 2,
					200 + Stats.STAT_HEIGHT*i+Stats.STAT_HEIGHT/2+g.getFontMetrics().getAscent()/2);
			i++;
		}
	}
	
	public void deleteBullet(Bullet b) {
		if (b != null) {
			b.die(player);
			bulletsToRemove.add(b);		
		}
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public HashMap<Player, Stats> getLeaderBoard() {
		return leaderBoard;
	}

	public ArrayList<Bullet> getBulletsToAdd() {
		return bulletsToAdd;
	}

	public ArrayList<Bullet> getBulletsToRemove() {
		return bulletsToRemove;
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
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		handleInputsPressed(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		handleInputsReleased(e.getButton());
		if (player.getTank() != null &&  player.getTank().getMode() == PlayerMode.BLOC && e.getButton() == PlayerInputs.build) {			
			player.getTank().dropObstacle(Calcul.limitRange(e.getX(), player.getTank().getX()), Calcul.limitRange(e.getY(), player.getTank().getY()), true, players, getObstacles());
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

	}

	@Override
	public void keyPressed(KeyEvent e) {
		handleInputsPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		handleInputsReleased(e.getKeyCode());
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
	
	public void handleInputsPressed(int keyCode) {
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
			if (player.getTank() != null && player.getSkill1() != null) {
				player.getSkill1().launch();
			}
		} else if (keyCode == PlayerInputs.skill2) {
			if (player.getTank() != null && player.getSkill2() != null) {
				player.getSkill2().launch();
			}
		} else if (keyCode == PlayerInputs.skill3) {
			if (player.getTank() != null && player.getSkill3() != null) {
				player.getSkill3().launch();
			}
		} else if (keyCode == PlayerInputs.leaderBoard) {
			drawLeaderBoard = true;
		}
		if (player.getTank() != null) {
			if (keyCode == PlayerInputs.aim) {
				if (player.getTank().getMode() == PlayerMode.BASE && player.getTank().getCannon().canFire()) {
					player.getTank().switchMode(PlayerMode.AIM);
					if (player.getTank().getCannon().getShot() == TypeShot.GRENADE) {
						player.getTank().fireDir();
						player.getTank().switchMode(PlayerMode.BASE);
					}
				}
			} else if (keyCode == PlayerInputs.blocMode) {
				player.getTank().switchMode(PlayerMode.BLOC);
			} else if (keyCode == PlayerInputs.grabMode) {
				player.getTank().switchMode(PlayerMode.GRAB);
			}
		}
	}
	
	public void handleInputsReleased(int keyCode) {
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
		if (player.getTank() != null) {
			switch (player.getTank().getMode()) {
			case AIM:
				if (keyCode == PlayerInputs.aim) {
					player.getTank().fire();						
					player.getTank().switchMode(PlayerMode.BASE);
				}
				break;
			case BLOC:
				if (keyCode == PlayerInputs.blocMode) {
					player.getTank().switchMode(PlayerMode.BASE);
				}
				break;
			case GRAB:
				if (keyCode == PlayerInputs.grabMode) {
					if (player.getTank().getGrabed() != null) {
						player.getTank().getGrabed().setHolding(null);
						player.getTank().setGrabed(null);
					}
					player.getTank().switchMode(PlayerMode.BASE);
				}
			default:
				break;
			}
		}
	}

}
