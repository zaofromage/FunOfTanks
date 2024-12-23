package model.player;

import java.util.Objects;
import java.util.UUID;

import model.IModel;

public class Player implements IModel {
	
	public static final int RESPAWN = 5000;
	public static final int MAX_LIVES = 3;
	
	private UUID ID;
	
	private String name;
	private Tank tank;
	private boolean ready  = false;
	
// en faire un decorateur
//	private Skill skill1;
//	private Skill skill2;
//	private Skill skill3;
	
	private int team = 1;
	
	public Player(String name, UUID id) {
		this.name = name;
		this.ID = id;
		this.tank = new Tank(500, 500);
	}
	
	@Override
	public void update() {
		if (tank != null) {
			tank.update();			
		}
	}
	
	public void createTank(int x, int y) {
		tank = new Tank(x, y);
	}
	
	public void destroyTank() {
		tank = null;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public UUID getID() {
		return ID;
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
	
	public boolean isReady() {
		return ready;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(ID, other.ID) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Player [ID=" + ID + ", name=" + name + ", team=" + team + "]";
	}
	
}
