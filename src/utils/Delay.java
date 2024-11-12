package utils;


public class Delay implements Runnable {
	
	private long millisBeforePerformAction;
	private Runnable function;
	private Thread sideTime;
	private int repeat;
	
	public Delay(long time, Runnable runnable) {
		millisBeforePerformAction = time;
		function = runnable;
		repeat = 1;
		start();
	}
	
	public Delay(long time, int repeat, Runnable runnable) {
		millisBeforePerformAction = time;
		function = runnable;
		this.repeat = repeat;
		start();
	}
	
	private void start() {
		sideTime = new Thread(this);
		sideTime.start();
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < repeat; i++) {				
				Thread.sleep(millisBeforePerformAction);
				function.run();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
