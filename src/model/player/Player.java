package model.player;

import model.IModel;
import serverHost.Client;
import serverHost.Role;
import serverHost.Server;
import serverHost.UDPServer;

public class Player implements IModel {
	
	public static final int RESPAWN = 5000;
	public static final int MAX_LIVES = 3;
	
	private Role role;
	private Client client;
	private String name;
	private Tank tank;
	private boolean ready  = false;
	
// en faire un decorateur
//	private Skill skill1;
//	private Skill skill2;
//	private Skill skill3;
	
	private int team = 1;
	
	private Server server;
	private UDPServer udpServer;
	
	public Player(String name, Role role) {
		this.name = name;
		this.role = role;
		this.tank = new Tank(500, 500, this);
	}
	
	@Override
	public void update() {
		tank.update();
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public int getTeam() {
		return team;
	}
	
	public Tank getTank() {
		return tank;
	}
	
	public String getName() {
		return name;
	}
}
