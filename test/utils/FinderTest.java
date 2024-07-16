package utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import client.Game;
import player.Player;
import serverHost.Role;

class FinderTest {

	@Test
	void testFindPlayer() {
		Game game = new Game();
		ArrayList<Player> players = new ArrayList<Player>();
		Player p = new Player("corentin", Role.GUEST, game, false);
		players.add(p);
		players.add(new Player("phillipe", Role.GUEST, game, false));
		assertEquals(p, Finder.findPlayer("corentin", players));
		assertEquals(null, Finder.findPlayer("carole", players));
	}

}
