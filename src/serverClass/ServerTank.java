package serverClass;

public class ServerTank {
	public int x, y;
	public double orientation;
	
	public String color;
	
	public String owner;
	
	public ServerTank(int x, int y, double o, String c, String owner) {
		this.x = x;
		this.y = y;
		this.orientation = o;
		this.color = c;
		this.owner = owner;
	}
}
