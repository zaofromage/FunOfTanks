package serverHost;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.net.*;

public class Server implements Runnable {
	public static final int PORT = 4551;

	private ArrayList<ClientHandler> clients;
	private ServerSocket server;

	private ExecutorService pool;
	
	
	//Game
	private ServerPlaying playing;

	public Server() throws IOException {
		clients = new ArrayList<>();
		String localIP = InetAddress.getLocalHost().getHostAddress();
		server = new ServerSocket(PORT, 0, InetAddress.getByName("0.0.0.0"));
		server.setSoTimeout(100000);
		pool = Executors.newFixedThreadPool(4);
		playing = new ServerPlaying();
		
		pool.execute(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				ClientHandler ch = new ClientHandler(client, clients, this);
				clients.add(ch);
				pool.execute(ch);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		try {
			server.close();
			for (ClientHandler ch : clients) {
				ch.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerPlaying getPlaying() {
		return playing;
	}
}
