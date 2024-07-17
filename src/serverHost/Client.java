package serverHost;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import client.Game;

public class Client {

	private Socket socket;
	private PrintWriter out;
	
	private ServerConnection serverConn;

	public Client(String ip, int port, Game game) throws IOException {
		socket = new Socket(ip, port);
		serverConn = new ServerConnection(socket, game);
		out = new PrintWriter(socket.getOutputStream(), true);
		new Thread(serverConn).start();
	}
	
	public void send(String msg) {
		out.println(msg);
	}
	
	public void close() {
		try {
			socket.shutdownOutput();
			out.close();
			serverConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
