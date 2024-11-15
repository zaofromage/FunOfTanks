package serverHost;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import client.Game;

public class Client {

	private Socket socket;
	private PrintWriter out;
	private InetAddress serverIP;
	
	private ServerConnection serverConn;
	private UDPServerConnection udpConn;

	public Client(String ip, int port, Game game) throws IOException {
		socket = new Socket(ip, port);
		serverIP = InetAddress.getByName(ip);
		serverConn = new ServerConnection(socket, game);
		out = new PrintWriter(socket.getOutputStream(), true);
		udpConn = new UDPServerConnection(game);
		new Thread(serverConn).start();
		new Thread(udpConn).start();
	}
	
	public void send(String msg) {
		out.println(msg);
	}
	
	public void sendUDP(String msg) {
		udpConn.send(msg, serverIP);
	}
	
	public void close() {
		try {
			socket.close();
			out.close();
			serverConn.close();
			udpConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
