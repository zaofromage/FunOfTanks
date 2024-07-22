package utils;

public class Calcul {
	
	public final static int MAX_RANGE = 200;
	
	public static int roundToTile(int x) {
		return x - x%50;
	}
	
    public static int limitRange(int target, int pos) {
    	int range = target - pos;
		if (range > MAX_RANGE) {
			return pos + MAX_RANGE;			
		} else if (range < MAX_RANGE *-1) {
			return pos - MAX_RANGE;
		} else {
			return target;
		}
	}
    
    public static double[] normalizeVector(int x, int y) {
		double norme = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double[] res = new double[2];
		res[0] = ((double) x / norme);
		res[1] = ((double) y / norme);
		return res;
	}
}
