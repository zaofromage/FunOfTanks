package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Client {

	private Socket socket;
	private PrintWriter out;
	private InetAddress serverIP;
	
	private ServerConnection serverConn;
	private UDPServerConnection udpConn;

	public Client(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		serverIP = InetAddress.getByName(ip);
		serverConn = new ServerConnection(socket);
		out = new PrintWriter(socket.getOutputStream(), true);
		udpConn = new UDPServerConnection();
		new Thread(serverConn).start();
		new Thread(udpConn).start();
	}
	
	public void send(String[] msg) {
		out.print(join(msg));
		out.flush();
	}
	
	public void sendUDP(String[] msg) {
		udpConn.send(join(msg), serverIP);
	}
	
	private String join(String[] tab) {
		return Arrays.stream(tab).collect(Collectors.joining(";"));
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


