package network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import model.gamestate.GameMode;
import model.player.Dir;
import model.player.PlayerMode;

public class ClientRequestTest {
	
	public static final String IP = "192.168.1.146";

	// menu
	
	@Test
	public void testAddPlayer() {
		Server server = new Server();
		sleep();
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
			System.out.println(server.getGame().getPlayers().get(r));
			assertEquals(server.getGame().getPlayers().get(r).getID(), r);
		}
		server.shutdown();
	}
	
	@Test
	public void testRemovePlayer() {
		Server server = new Server();
		sleep();
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
			assertEquals(server.getGame().getPlayers().get(r), null);
		}
		server.shutdown();
	}
	
	@Test
	public void testSetReady() {
		Server server = new Server();
		sleep();
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
			assertFalse(server.getGame().getPlayers().get(testID).isReady());
			cl.send(new String[] {"setReady", testID.toString(), Boolean.TRUE.toString()});
			sleep();
			assertTrue(server.getGame().getPlayers().get(testID).isReady());
		}
		server.shutdown();
	}
	
	@Test
	public void testSetMode() {
		Server server = new Server();
		sleep();
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
		server.shutdown();
	}
	
	@Test
	public void testPlay() {
		Server server = new Server();
		sleep();
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
			assertEquals(server.getGame().getPlaying(), null);
			cl.send(new String[] {"setReady", r.toString(), Boolean.TRUE.toString()});
			sleep();
			cl.send(new String[] {"play"});
			sleep();
			assertTrue(server.getGame().getPlaying() != null);
			assertEquals(server.getGame().getMenu(), null);
		}
		server.shutdown();
	}
	
	// playing
	
	@Test
	public void testMoveTank() {
		Server server = new Server();
		sleep();
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
			double old = server.getGame().getPlayers().get(r).getTank().getX();
			for (int i = 0; i < 20; i++) {
				cl.sendUDP(new String[] {"moveTank", r.toString(), Dir.toString(Dir.RIGHT)});
				sleep();
			}
			double newPos = server.getGame().getPlayers().get(r).getTank().getX();
			assertTrue(newPos > old + 19 && newPos < old + 21);
		}
		server.shutdown();
	}
	
	@Test
	public void testUpdateOrientation() {
		Server server = new Server();
		sleep();
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
			double old = server.getGame().getPlayers().get(r).getTank().getOrientation();
			for (Integer i = 0; i < 20; i++) {
				Integer offset = 500 + i;
				cl.sendUDP(new String[] {"updateOrientation", r.toString(), "600", offset.toString()});
				sleep();
			}
			double newPos = server.getGame().getPlayers().get(r).getTank().getOrientation();
			System.out.println(old + " " + newPos);
			assertTrue(newPos > old + 0.17 && newPos < old + 0.19);
		}
		server.shutdown();
	}
	
	@Test
	public void testFire() {
		Server server = new Server();
		sleep();
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
			cl.send(new String[] {"fire", r.toString(), "1000", "500"});
			sleep();
			assertTrue(server.getGame().getPlaying().getOwners().containsValue(r));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			assertFalse(server.getGame().getPlaying().getOwners().containsValue(r));
		}
		server.shutdown();
	}
	
	@Test
	public void testSwitchMode() {
		Server server = new Server();
		sleep();
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
			assertEquals(server.getGame().getPlayers().get(r).getTank().getMode(), PlayerMode.BASE);
			cl.send(new String[] {"switchMode", r.toString(), PlayerMode.toString(PlayerMode.AIM)});
			sleep();
			assertEquals(server.getGame().getPlayers().get(r).getTank().getMode(), PlayerMode.AIM);
		}
		server.shutdown();
	}
	
	// utils
	
	private void sleep() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
