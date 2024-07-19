package serverHost;

import java.util.ArrayList;

import map.Obstacle;
import serverClass.*;


public class ServerPlaying {
	
	ArrayList<String> players;
	
	ArrayList<Boolean> playersReady;
	
	ArrayList<ServerTank> tanks;
	
	ArrayList<ServerBullet> bullets;
	
	ArrayList<Obstacle> obstacles;
	
	public ServerPlaying() {
		players = new ArrayList<String>();
		playersReady = new ArrayList<Boolean>();
		tanks = new ArrayList<ServerTank>();
		bullets = new ArrayList<ServerBullet>();
		obstacles = new ArrayList<Obstacle>();
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public ArrayList<Boolean> getPlayersReady() {
		return playersReady;
	}
	
	public ArrayList<ServerTank> getTanks() {
		return tanks;
	}
	
	public ArrayList<ServerBullet> getBullets() {
		return bullets;
	}
	
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}
}






