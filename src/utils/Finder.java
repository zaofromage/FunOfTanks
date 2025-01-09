package utils;

import java.util.ArrayList;
import java.util.UUID;

import model.player.Player;

public final class Finder {

	public static Player findPlayer(UUID id, ArrayList<Player> players) {
		return players.stream().filter(p -> p.getID().equals(id)).findFirst().orElse(null);
	}
}
