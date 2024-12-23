package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import map.Obstacle;
import player.Bullet;
import model.player.Player;
import serverClass.ServerBullet;
import serverClass.ServerPlayer;
import serverClass.ServerTank;

public final class Finder {

	public static Player findPlayer(UUID id, ArrayList<Player> players) {
		return players.stream().filter(p -> p.getID().equals(id)).findFirst().orElse(null);
	}
	
	public static ServerPlayer findServerPlayer(String name, ArrayList<ServerPlayer> players) {
		return players.stream().filter(p -> p.name.equals(name)).findAny().orElse(null);
	}

	public static ServerTank findServerTank(String name, ArrayList<ServerTank> tanks) {
		return tanks.stream().filter(t -> t.owner.equals(name)).findAny().orElse(null);
	}
	
	public static Bullet findBullet(Player player, int id, List<Bullet> bullets) {
		return bullets.stream().filter(b -> b.getPlayer().equals(player) && b.getId() == id).findAny().orElse(null);
	}

	public static ServerBullet findServerBullet(String name, int id, ArrayList<ServerBullet> bullets) {
		return bullets.stream().filter(b -> b.owner.equals(name) && b.id == id).findAny().orElse(null);
	}

	public static Obstacle findObstacle(int x, int y, List<Obstacle> obs) {
		return obs.stream().filter(o -> o.getHitbox().getX() == x && o.getHitbox().getY() == y).findAny().orElse(null);
	}
}
