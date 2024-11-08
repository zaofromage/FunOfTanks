package player;

public class Stats {
	
	public static final int STATS_NUMBER = 3;
	public static final int STAT_WIDTH = 50;
	public static final int STAT_HEIGHT = 25;
	
	public int kills;
	public int death;
	
	public Stats() {
		kills = 0;
		death = 0;
	}
	
	public double ratio() {
		return death == 0 ? kills:(double)kills/(double)death;
	}
	
	@Override
	public String toString() {
		return "[k="+kills+",d="+death+",r="+ratio()+"]";
	}
}
