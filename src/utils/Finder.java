package utils;

import java.util.ArrayList;

import map.Obstacle;
import serverClass.*;

import player.Player;

public final class Finder {

	public static Player findPlayer(String name, ArrayList<Player> players) {
		return players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
	}

	public static ServerTank findServerTank(String name, ArrayList<ServerTank> tanks) {
		return tanks.stream().filter(t -> t.owner.equals(name)).findAny().orElse(null);
	}

	public static ServerBullet findServerBullet(String name, int id, ArrayList<ServerBullet> bullets) {
		return bullets.stream().filter(b -> b.owner.equals(name) && b.id == id).findAny().orElse(null);
	}

	public static Obstacle findObstacle(int x, int y, ArrayList<Obstacle> obs) {
		return obs.stream().filter(o -> o.getHitbox().getX() == x && o.getHitbox().getY() == y).findAny().orElse(null);
	}
}
