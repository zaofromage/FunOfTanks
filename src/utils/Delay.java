package utils;


public class Delay implements Runnable {
	
	private long millisBeforePerformAction;
	private Runnable function;
	private Thread sideTime;
	
	public Delay(long time, Runnable runnable) {
		millisBeforePerformAction = time;
		function = runnable;
		start();
	}
	
	private void start() {
		sideTime = new Thread(this);
		sideTime.start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(millisBeforePerformAction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		function.run();
	}
}
