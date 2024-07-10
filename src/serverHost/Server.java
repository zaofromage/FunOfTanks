package serverHost;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.net.*;

public class Server implements Runnable {
	public static final int PORT = 4550;

	private ArrayList<ClientHandler> clients;
	private ServerSocket server;

	private Thread th;

	private ExecutorService pool;

	public Server() throws IOException {
		clients = new ArrayList<>();
		server = new ServerSocket(PORT);
		pool = Executors.newFixedThreadPool(4);
		th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				ClientHandler ch = new ClientHandler(client, clients);
				clients.add(ch);
				pool.execute(ch);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
