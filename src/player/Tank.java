package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import utils.Delay;
import map.Obstacle;
import utils.Calcul;

public class Tank {
	private int x, y;
	private int size;
	private double orientation;

	private int speed;
	private int dashSpeed;
	private boolean canDash = true;
	private long cooldownDash;

	private int displayOffset;

	private Color color;

	private boolean up, down, left, right;

	private int targetX, targetY;
	private Cannon cannon;

	private Rectangle hitbox;

	private PlayerMode mode;

	private Player owner;
	
	private Obstacle possibleObstacle;

	public Tank(int x, int y, Player owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
		cannon = new Cannon(this);
		size = 50;
		orientation = 0;
		speed = 1;
		dashSpeed = 20;
		cooldownDash = 1000;
		color = Color.red;
		up = false;
		down = false;
		left = false;
		right = false;
		targetX = 0;
		targetY = 0;
		displayOffset = size / 2;
		mode = PlayerMode.FIRE;
		hitbox = new Rectangle(x - displayOffset, y - displayOffset, size, size);
	}

	public void move(int deltaX, int deltaY, ArrayList<Obstacle> obs) {
		for (Obstacle o : obs) {
			if (detectObstacle(o, x + deltaX * speed, y + deltaY * speed)) {
				return;
			}
		}
		x += deltaX * speed;
		y += deltaY * speed;
	}

	public void dash() {
		if (canDash) {
			speed *= dashSpeed;
			canDash = false;
			new Delay(50, () -> speed /= dashSpeed);
			new Delay(cooldownDash, () -> canDash = true);
		}

	}

	private void findOrientation() {
		double vectorX = (targetX - x), vectorY = (targetY - y) * -1;
		if (vectorX != 0) {
			double delta = 0.;
			if (vectorX > 0 && vectorY < 0) {
				delta = 360.;
			} else if (vectorX < 0) {
				delta = 180.;
			}
			orientation = (Math.atan(vectorY / vectorX) * (180 / Math.PI) + delta) * -1;
		}
	}

	public void updateHitbox() {
		hitbox.setBounds(x - displayOffset, y - displayOffset, size, size);
	}

	private boolean detectObstacle(Obstacle obstacle, int x, int y) {
		return obstacle.getHitbox().intersects(new Rectangle(x - displayOffset, y - displayOffset, size, size));
	}

	public void fire(int targetX, int targetY) {
		cannon.fire(x, y, targetX, targetY, orientation);
	}

	public void dropObstacle(int x, int y, boolean destructible, ArrayList<Player> players, ArrayList<Obstacle> obstacles) {
		if (mode == PlayerMode.BLOC) {
			Obstacle o = new Obstacle(x, y, destructible);
			for (Player p : players) {
				if (o.getHitbox().intersects(p.getTank().getHitbox())) {
					return;
				}
			}
			for (Obstacle obs : obstacles) {
				if (o.getHitbox().intersects(obs.getHitbox())) {
					return;
				}
			}
			obstacles.add(o);
			if (owner.getClient() != null) {
				owner.getClient().send("newobstacle;" + x + ";" + y + ";" + destructible);
			}
			switchMode(PlayerMode.FIRE);
		}
	}

	public void updateTank(ArrayList<Obstacle> obs, ArrayList<Player> players, Player player) {
		if (up) {
			move(0, -1, obs);
		}
		if (down) {
			move(0, 1, obs);
		}
		if (left) {
			move(-1, 0, obs);
		}
		if (right) {
			move(1, 0, obs);
		}
		if (owner.isMain()) {
			findOrientation();
		}
		updateHitbox();
		cannon.updateCannon(obs, players, player);
		if (possibleObstacle != null) {
			possibleObstacle.updateObstacle(targetX, targetY);
			if (possibleObstacle.getHitbox().intersects(hitbox)) {
				possibleObstacle.setColor(Obstacle.possibleWrong);
			} else {
				possibleObstacle.setColor(Obstacle.possible);
			}
		}
		if (owner.getClient() != null)
			owner.getClient().send("updatetank;" + owner.getName() + ";" + x + ";" + y + ";" + orientation);
	}

	public void drawTank(Graphics g) {
		g.setColor(color);
		g.fillRect(x - displayOffset, y - displayOffset, size, size);
		g.setColor(Color.black);
		g.drawString(owner.getName(), x - displayOffset, y - displayOffset - 10);
		cannon.drawCannon(g, x, y, orientation);
		if (possibleObstacle != null) {
			possibleObstacle.drawObstacle(g);
		}
	}

	// getters setters
	public double getOrientation() {
		return orientation;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public Cannon getCannon() {
		return cannon;
	}

	public Player getOwner() {
		return owner;
	}

	public Color getColor() {
		return color;
	}

	public PlayerMode getMode() {
		return mode;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public void switchMode(PlayerMode mode) {
		this.mode = mode;
		if (mode == PlayerMode.BLOC) {
			possibleObstacle = new Obstacle(targetX, targetY, false);
		} else if (mode == PlayerMode.FIRE) {
			possibleObstacle = null;
		}
	}
	
	//fait que quand je suis en mode constru les blocs ne peuvent etre posé que devant le joueur

	public Obstacle getPossibleObstacle() {
		return possibleObstacle;
	}

	public void setPossibleObstacle(Obstacle possibleObstacle) {
		this.possibleObstacle = possibleObstacle;
	}
	
	
}
