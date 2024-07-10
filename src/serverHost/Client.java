package serverHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader keyboard;
	
	private ServerConnection serverConn;

	public Client(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		serverConn = new ServerConnection(socket);
		out = new PrintWriter(socket.getOutputStream(), true);
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		new Thread(serverConn).start();
	}
	
	public void send(String msg) {
		out.println(msg);
	}
}
