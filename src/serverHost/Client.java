package serverHost;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import client.Game;

public class Client {

	private Socket socket;
	private PrintWriter out;
	
	private ServerConnection serverConn;

	public Client(String ip, int port, Game game) throws IOException {
		socket = new Socket(ip, port);
		try {
			socket.connect(new InetSocketAddress(ip, port), 5000);
		} catch (SocketTimeoutException e) {
			throw new IOException("Connexion au serveur impossible après 5 secondes", e);
		}
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
