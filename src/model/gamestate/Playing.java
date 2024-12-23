package model.gamestate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import model.Game;
import model.IModel;
import model.bullet.Bullet;
import model.player.Dir;
import model.player.Player;
import model.player.PlayerMode;
import model.player.Tank;
import utils.Vector;

public class Playing implements IModel {
	
	private Game game;
		
	private Map<UUID, Player> players;
	
	private List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();
	
	/**
	 * association bulletsID / playerID pour retrouver à qui appartient les balles
	 */
	private Map<UUID, UUID> owners = new HashMap<UUID, UUID>();
	
	public Playing(Game game) {
		this.game = game;
		players = game.getPlayers();
	}

	@Override
	public void update() {
		for (Player p : players.values()) {
			p.update();
		}
		
		for (Bullet b : bullets) {
			b.update();
		}
		for (Bullet b : bullets.stream().filter(b -> b.toRemove()).collect(Collectors.toList())) {
			bullets.remove(b);
			owners.remove(b.getID());
		}
	}
	
	public void moveTank(UUID id, Dir dir) {
		Tank tank = players.get(id).getTank();
		if (tank != null) {
			tank.move(dir);
		}
	}
	
	public void updateOrientation(UUID id, double x, double y) {
		Tank tank = players.get(id).getTank();
		if (tank != null) {
			tank.setOrientation(Math.atan2(y - tank.getY(), x - tank.getX()));
		}
	}
	
	public void fire(UUID id, Vector target) {
		UUID r = UUID.randomUUID();
		Tank tank = players.get(id).getTank();
		if (tank != null) {
			bullets.add(new Bullet(r, tank.getX(), tank.getY(), target, players));
			owners.put(r, id);
		}
	}
	
	public void switchMode(UUID id, PlayerMode mode) {
		Tank tank = players.get(id).getTank();
		if (tank != null) {
			tank.setMode(mode);
		}
	}
	
	
	public Map<UUID, UUID> getOwners() {
		return owners;
	}
	
	public List<Bullet> getBullets() {
		return bullets;
	}

	public Map<UUID, Player> getPlayers() {
		return players;
	}
}
