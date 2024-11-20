package utils;

public class Vector {
	
	public double x, y;
	
	public Vector() {
		x = 0;
		y = 0;
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double norm() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public void normalize() {
	    double n = norm();
	    if (n != 0) {
	       x /= n;
	       y /= n;
	    }
	}
	
	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + "]";
	}
	
}
