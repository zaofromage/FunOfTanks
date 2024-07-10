package serverHost;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	private ArrayList<ClientHandler> clients;
	
	public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
		client = clientSocket;
		this.clients = clients;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				String request = in.readLine();
				System.out.println("Client says : " + request);
				if (request.startsWith("say")) {
					sendToAll(request);
				}
				out.println(request);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.close();
		}
	}
	
	private void sendToAll(String msg) {
		ArrayList<ClientHandler> others = new ArrayList<>(clients);
		others.remove(this);
		for (ClientHandler c : others) {
			c.out.println(msg);
		}
	}

}
