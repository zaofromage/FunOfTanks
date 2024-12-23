package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Game;

public class Server implements Runnable {
	public static final int PORT = 4551;

	private ArrayList<ClientHandler> clients;
	private ServerSocket server;

	private ExecutorService pool;
	
	private boolean running = true;
	
	private Game game;

	public Server(Game game) throws IOException {
		clients = new ArrayList<>();
		this.game = game;
		server = new ServerSocket(PORT, 0, InetAddress.getByName("0.0.0.0"));
		pool = Executors.newFixedThreadPool(8);
		pool.execute(this);
	}

	@Override
	public void run() {
		while (running) {
			try {
				Socket client = server.accept();
				ClientHandler ch = new ClientHandler(client, clients, game);
				clients.add(ch);
				pool.execute(ch);
			} catch (IOException e) {
				if (running) {
					e.printStackTrace();					
				} else {
					System.out.println("Serveur arreté proprement");
				}
			}
		}
	}
	
	public void close() {
		running = false;
	    try {
	        if (server != null && !server.isClosed()) {
	            server.close();
	        }
	        for (ClientHandler ch : clients) {
	            ch.close();
	        }
	        pool.shutdownNow();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
