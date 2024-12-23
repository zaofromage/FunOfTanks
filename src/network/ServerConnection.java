package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ServerConnection implements Runnable {

	private Socket server;
	private BufferedReader in;

	private boolean running = true;

	public ServerConnection(Socket s) throws IOException {
		server = s;
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}

	@Override
	public void run() {
		String serverResponse;
		try {
			while (running) {
				serverResponse = in.readLine();
				if (serverResponse != null) {
					String header = ClientHandler.getHeader(serverResponse);
					String[] body = ClientHandler.getBody(serverResponse);
					
				}
			}
		} catch (IOException e) {
			if (running) {
				System.out.println("pas normal server conn");
			} else {
				System.out.println("server fermé");
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		running = false;
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


