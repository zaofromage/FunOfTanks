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
		System.out.println("IP address : " + InetAddress.getLocalHost().getHostAddress());
		String localIP = InetAddress.getLocalHost().getHostAddress();
		server = new ServerSocket(PORT, 0, InetAddress.getByName(localIP));
		server.setSoTimeout(100000);
		pool = Executors.newFixedThreadPool(4);
		playing = new ServerPlaying();
		
		pool.execute(this);
	}

	@Override
	public void run() {
		System.out.println("Méthode run() du serveur démarrée");
	    System.out.println("Adresse du serveur : " + server.getInetAddress() + ", Port : " + server.getLocalPort());
		while (true) {
			try {
				System.out.println("En attente de connexions...");
				Socket client = server.accept();
				System.out.println("apres d'accepter");
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
