package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import utils.Vector;

import javax.imageio.ImageIO;

import client.GamePanel;
import gamestate.GameMode;
import utils.Calcul;
import utils.Delay;
import map.Obstacle;

public class Tank {

	public final int BASE_SPEED = 1;

	private int x, y;
	private int size = 50;
	private double orientation = 0;

	private int speed = BASE_SPEED;
	private int dashSpeed = 20;
	private boolean inDash;
	private boolean canDash = true;
	private long cooldownDash = 1000;

	private int displayOffset = size / 2;

	private Color color;

	private boolean up, down, left, right = false;

	private Vector target = new Vector();
	private Vector aim;
	private double aimDistance = 0.;
	private double maxRange = 600.;
	private double aimSpeed = 5;
	private Cannon cannon;

	private Rectangle hitbox = new Rectangle(x - displayOffset, y - displayOffset, size, size);

	private PlayerMode mode = PlayerMode.BASE;

	private Player owner;

	private Obstacle possibleObstacle;

	private BufferedImage crosshair;
	
	private boolean invinsible;
	
	// skills
	private boolean canDashThrough = false;

	public Tank(int x, int y, Player owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
		invinsible = true;
		color = Color.green;
		//se lance a la creation du joueur et pas du jeu
		new Delay(2000, () -> {
			invinsible = false;
			if(owner.getName() == "ChickenJoe") {
				invinsible = true;
			}
			if (GameMode.gameMode == GameMode.FFA) {
				if (owner.isMain()) color = Color.blue;
				else color = Color.red;				
			} else {
				switch (owner.getTeam()) {
				case 1: color = Color.blue;break;
				case 2: color = Color.red;break;
				case 3: color = Color.cyan;break;
				case 4: color = Color.orange;break;
				}
			}
		});
		cannon = new Cannon(this);
		aim = new Vector(x, y);
		try {
			crosshair = ImageIO.read(new File("res/images/crosshair.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void move(int deltaX, int deltaY, ArrayList<Obstacle> obs) {
		if (inDash) {
			if (!canDashThrough) {
				for (Obstacle o : obs) {
					if (detectObstacle(o, x + deltaX * dashSpeed, y + deltaY * dashSpeed)) {
						return;
					}
				}
			} else {
				for (Obstacle o : obs) {
					if (detectObstacle(o, x + deltaX * dashSpeed, y + deltaY * dashSpeed) && !o.isDestructible()) {
						return;
					}
				}
			}
			x += deltaX * dashSpeed;
			y += deltaY * dashSpeed;
		} else {
			for (Obstacle o : obs) {
				if (detectObstacle(o, x + deltaX * speed, y + deltaY * speed)) {
					return;
				}
			}
			x += deltaX * speed;
			y += deltaY * speed;
		}
	}

	public void dash(ArrayList<Obstacle> obs) {
		if (canDash) {
			inDash = true;
			canDash = false;
			new Delay(50, () -> {
				inDash = false;
				canDashThrough = false;
			});
			new Delay(cooldownDash, () -> canDash = true);
		}

	}

	private void findOrientation() {
		Vector vector = new Vector((target.x - x), (target.y - y) * -1);
		if (vector.x != 0) {
			double delta = 0.;
			if (vector.x > 0 && vector.y < 0) {
				delta = 360.;
			} else if (vector.x < 0) {
				delta = 180.;
			}
			orientation = (Math.atan(vector.y / vector.x) * (180 / Math.PI) + delta) * -1;
		}
	}

	public void updateHitbox() {
		hitbox.setBounds(x - displayOffset, y - displayOffset, size, size);
	}

	private boolean detectObstacle(Obstacle obstacle, int x, int y) {
		return obstacle.getHitbox().intersects(new Rectangle(x - displayOffset, y - displayOffset, size, size));
	}

	public void fire() {
		if (!invinsible) {
			cannon.fire(x, y, (int) aim.x, (int) aim.y, orientation);			
		}
	}

	public void dropObstacle(int x, int y, boolean destructible, ArrayList<Player> players,
			ArrayList<Obstacle> obstacles) {
		if (mode == PlayerMode.BLOC) {
			Obstacle o = new Obstacle(x, y, destructible);
			for (Player p : players) {
				if (p.getTank() != null) {
					if (o.getHitbox().intersects(p.getTank().getHitbox())) {
						return;
					}
				}
			}
			for (Obstacle obs : obstacles) {
				if (o.getHitbox().intersects(obs.getHitbox())) {
					return;
				}
			}
			if (possibleObstacle.getHitbox().getX() >= GamePanel.dimension.width - GamePanel.tileSize * 2
					|| possibleObstacle.getHitbox().getX() <= GamePanel.tileSize
					|| possibleObstacle.getHitbox().getY() >= GamePanel.dimension.height - GamePanel.tileSize * 2
					|| possibleObstacle.getHitbox().getY() <= GamePanel.tileSize) {
				return;
			}
			obstacles.add(o);
			if (owner.getClient() != null) {
				owner.getClient().send("newobstacle;" + x + ";" + y + ";" + destructible);
			}
		}
	}

	public void updateTank(ArrayList<Obstacle> obs, ArrayList<Player> players, Player player) {
		//System.out.println(invinsible);
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
			possibleObstacle.updateObstacle(Calcul.limitRange((int)target.x, x), Calcul.limitRange((int)target.y, y));
			if (possibleObstacle.getHitbox().intersects(hitbox)
					|| possibleObstacle.getHitbox().getX() >= GamePanel.dimension.width - GamePanel.tileSize * 2
					|| possibleObstacle.getHitbox().getX() <= GamePanel.tileSize
					|| possibleObstacle.getHitbox().getY() >= GamePanel.dimension.height - GamePanel.tileSize * 2
					|| possibleObstacle.getHitbox().getY() <= GamePanel.tileSize) {
				possibleObstacle.setColor(Obstacle.possibleWrong);
			} else {
				possibleObstacle.setColor(Obstacle.possible);
			}
		}
		// si t'es bloqué dans un obstacle quand tu dash through
		for (Obstacle o : obs) {
			while (detectObstacle(o, x, y)) {
				if (up)
					y -= 1;
				else if (left)
					x -= 1;
				else if (right)
					x += 1;
				else if (down)
					y += 1;
				else
					y += 1;
			}
		}
		//aim calculation
		if (mode == PlayerMode.AIM) {
			if (aimDistance < maxRange) {
				aimDistance += aimSpeed;				
			}
			aim.x = hitbox.getX() + Math.cos(orientation*(Math.PI/180.0)) * aimDistance;
			aim.y = hitbox.getY() + Math.sin(orientation*(Math.PI/180.0)) * aimDistance;
		} else {
			aimDistance = 0.;
			aim.x = hitbox.getX();
			aim.y = hitbox.getY();
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
		if (mode == PlayerMode.AIM) {
			g.drawImage(crosshair, (int) aim.x, (int) aim.y, null);
		}
		g.setColor(Color.white);
		g.drawString(""+owner.getTeam(), x, y);
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

	public int getSpeed() {
		return speed;
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
		this.target.x = targetX;
	}

	public void setTargetY(int targetY) {
		this.target.y = targetY;
	}
	
	public void setCanDash(boolean dash) {
		canDash = dash;
	}

	public void switchMode(PlayerMode mode) {
		this.mode = mode;
		if (mode == PlayerMode.BLOC) {
			possibleObstacle = new Obstacle((int)target.x, (int)target.y, false);
		} else if (mode != PlayerMode.BLOC) {
			possibleObstacle = null;
		}
	}
	
	public boolean isInvinsible() {
		return invinsible;
	}

	public Obstacle getPossibleObstacle() {
		return possibleObstacle;
	}

	public void setPossibleObstacle(Obstacle possibleObstacle) {
		this.possibleObstacle = possibleObstacle;
	}

	public void setCanDashThrough(boolean canDashThrough) {
		this.canDashThrough = canDashThrough;
	}
	
	@Override
	public String toString() {
		return owner.getName();
	}
}
