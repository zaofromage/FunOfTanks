package serverClass;

public class ServerPlayer {

	public String name;
	public boolean ready;
	public int team;
	
	public ServerPlayer(String n, boolean r, int t) {
		name = n;
		ready = r;
		team = t;
	}
}
