package network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import model.Game;
import model.gamestate.GameMode;
import model.player.Dir;
import model.player.PlayerMode;

public class ClientRequestTest {
	
	public static final String IP = "192.168.1.42";

	//menu
	
	@Test
	public void testAddPlayer() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[]{"addPlayer", "corentin", r.toString()});
			sleep();
			System.out.println(r);
			System.out.println(game.getPlayers().get(r));
			assertEquals(game.getPlayers().get(r).getID(), r);
		}
	}
	
	@Test
	public void testRemovePlayer() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[] {"addPlayer", "corentin", r.toString()});
			sleep();
			cl.send(new String[] {"removePlayer", r.toString()});
			sleep();
			assertEquals(game.getPlayers().get(r), null);
		}
	}
	
	@Test
	public void testSetReady() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID testID = UUID.randomUUID();
			cl.send(new String[]{"addPlayer", "corentin", testID.toString()});
			sleep();
			assertFalse(game.getPlayers().get(testID).isReady());
			cl.send(new String[] {"setReady", testID.toString(), Boolean.TRUE.toString()});
			sleep();
			assertTrue(game.getPlayers().get(testID).isReady());
		}
	}
	
	@Test
	public void testSetMode() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			assertEquals(GameMode.gameMode, GameMode.FFA);
			cl.send(new String[] {"setMode", GameMode.toString(GameMode.DOMINATION)});
			sleep();
			assertEquals(GameMode.gameMode, GameMode.DOMINATION);
		}
	}
	
	@Test
	public void testPlay() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[] {"addPlayer", "simbadlemarin", r.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			assertEquals(game.getPlaying(), null);
			cl.send(new String[] {"setReady", r.toString(), Boolean.TRUE.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			assertTrue(game.getPlaying() != null);
			assertEquals(game.getMenu(), null);
		}
	}
	
	// playing
	
	@Test
	public void testMoveTank() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[] {"addPlayer", "simbadlemarin", r.toString()});
			sleep();
			cl.send(new String[] {"setReady", r.toString(), Boolean.TRUE.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			double old = game.getPlayers().get(r).getTank().getX();
			for (int i = 0; i < 20; i++) {
				cl.sendUDP(new String[] {"moveTank", r.toString(), Dir.toString(Dir.RIGHT)});
				sleep();
			}
			double newPos = game.getPlayers().get(r).getTank().getX();
			assertTrue(newPos > old + 19 && newPos < old + 21);
		}
	}
	
	@Test
	public void testUpdateOrientation() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[] {"addPlayer", "simbadlemarin", r.toString()});
			sleep();
			cl.send(new String[] {"setReady", r.toString(), Boolean.TRUE.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			double old = game.getPlayers().get(r).getTank().getOrientation();
			for (Integer i = 0; i < 20; i++) {
				Integer offset = 500+i;
				cl.sendUDP(new String[] {"updateOrientation", r.toString(), "600", offset.toString()});
				sleep();
			}
			double newPos = game.getPlayers().get(r).getTank().getOrientation();
			assertTrue(newPos > old + 0.17 && newPos < old + 0.19);
		}
	}
	
	@Test
	public void testFire() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[] {"addPlayer", "simbadlemarin", r.toString()});
			sleep();
			cl.send(new String[] {"setReady", r.toString(), Boolean.TRUE.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			cl.send(new String[] {"fire", r.toString(), "600", "500"});
			sleep();
			assertTrue(game.getPlaying().getOwners().containsKey(r));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			assertFalse(game.getPlaying().getOwners().containsKey(r));
		}
	}
	
	@Test
	public void testSwitchMode() {
		Game game = new Game();
		Client cl = null;
		try {
			 cl = new Client(IP, 4551);
		} catch (IOException e) {
			System.err.println("client pas connecté");
		}
		if (cl != null) {
			UUID r = UUID.randomUUID();
			cl.send(new String[] {"addPlayer", "simbadlemarin", r.toString()});
			sleep();
			cl.send(new String[] {"setReady", r.toString(), Boolean.TRUE.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			assertEquals(game.getPlayers().get(r).getTank().getMode(), PlayerMode.BASE);
			cl.send(new String[] {"switchMode", r.toString(), PlayerMode.toString(PlayerMode.AIM)});
			sleep();
			assertEquals(game.getPlayers().get(r).getTank().getMode(), PlayerMode.AIM);
		}
	}
	
	
	// utils
	
	private void sleep() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
