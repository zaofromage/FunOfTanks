package utils;

import java.util.ArrayList;

import client.Game;
import map.Obstacle;
import player.Bullet;
import player.Player;

public final class DataManager {

	//clients
	/**
	 * stringify a list of players that goals to turn into a list of Player
	 * @param list
	 * @return a string that contains at index 0 the purpose of the message and at index 1 the length of the list
	 */
	public static String stringifyPlayers(ArrayList<Player> list) {
		String res = "players";
		for (Player p : list) {
			res += ";" + p.getName();
		}
		return res;
	}
	
	/*public static ArrayList<Player> parsePlayers(String players) {
		String[] list = players.split(";");
		ArrayList<Player> res = new ArrayList<Player>();
		for (int i = 2; i < Integer.parseInt(list[1]); i++) {
			String[] player = list[i].split(",");
			res.add(new Player(player[0], Integer.parseInt(player[1]), Integer.parseInt(player[2]), Double.parseDouble(player[3])));
		}
		return res;
	}*/
	
	public static String stringifyPlayer(Player p) {
		return p.getName();
	}
	
	public static Player parsePlayer(String str) {
		return null;
	}
	
	
	public static String stringifyObstacles(ArrayList<Obstacle> list) {
		String res = "obstacles;" + list.size();
		for (Obstacle o : list) {
			res += ";" + o.getHitbox().getX() + "," + o.getHitbox().getY() + "," + o.isDestructible();
		}
		return res;
	}
	
	public static ArrayList<Obstacle> parseObstacles(String obs) {
		String[] list = obs.split(";");
		ArrayList<Obstacle> res = new ArrayList<Obstacle>();
		for (int i = 2; i < Integer.parseInt(list[1]); i++) {
			String[] o = list[i].split(",");
			res.add(new Obstacle(Integer.parseInt(o[0]), Integer.parseInt(o[1]), Boolean.parseBoolean(o[2])));
		}
		return res;
	}
	
	public static String stringifyBullets(ArrayList<Bullet> list) {
		String res = "bullets;" + list.size();
		for (Bullet o : list) {
			res += ";" + o.getHitbox().getX() + "," + o.getHitbox().getY() + "," + o.getOrientation() + "," + o.getOwner().getOwner().getOwner().getName();
		}
		return res;
	}
	
	/*public static ArrayList<Bullet> parseBullets(String bullets) {
		String[] list = bullets.split(";");
		ArrayList<Bullet> res = new ArrayList<Bullet>();
		for (int i = 2; i < Integer.parseInt(list[1]); i++) {
			String[] b = list[i].split(",");
			res.add(new Bullet(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Double.parseDouble(b[2]), b[3]));
		}
		return res;
	}*/
	
	
	
	//server
	public static String stringifyServerPlayers(ArrayList<String> list) {
		String res = "players";
		for (String p : list) {
			res += ";" + p;
		}
		return res;
	}
	/*
	public static ArrayList<Player> parseServerPlayers(String players, Game game) {
		String[] list = players.split(";");
		ArrayList<Player> res = new ArrayList<Player>();
		for (int i = 2; i < Integer.parseInt(list[1]); i++) {
			String[] player = list[i].split(",");
			//res.add(Finder.findPlayer(player[0], game.));
		}
		return res;
	}
	
	
	public static String stringifyServerObstacles(ArrayList<Obstacle> list) {
		String res = "obstacles;" + list.size();
		for (Obstacle o : list) {
			res += ";" + o.getHitbox().getX() + "," + o.getHitbox().getY() + "," + o.isDestructible();
		}
		return res;
	}
	
	public static ArrayList<Obstacle> parseServerObstacles(String obs) {
		String[] list = obs.split(";");
		ArrayList<Obstacle> res = new ArrayList<Obstacle>();
		for (int i = 2; i < Integer.parseInt(list[1]); i++) {
			String[] o = list[i].split(",");
			res.add(new Obstacle(Integer.parseInt(o[0]), Integer.parseInt(o[1]), Boolean.parseBoolean(o[2])));
		}
		return res;
	}
	
	public static String stringifyServerBullets(ArrayList<Bullet> list) {
		String res = "bullets;" + list.size();
		for (Bullet o : list) {
			res += ";" + o.getHitbox().getX() + "," + o.getHitbox().getY() + "," + o.getOrientation() + "," + o.getOwner().getOwner().getOwner().getName();
		}
		return res;
	}
	
	public static ArrayList<Bullet> parseServerBullets(String bullets) {
		String[] list = bullets.split(";");
		ArrayList<Bullet> res = new ArrayList<Bullet>();
		for (int i = 2; i < Integer.parseInt(list[1]); i++) {
			String[] b = list[i].split(",");
			res.add(new Bullet(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Double.parseDouble(b[2]), b[3]));
		}
		return res;
	}*/
	
}
