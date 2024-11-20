package serverHost;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;
import serverClass.ServerTank;
import utils.Finder;

public class UDPServer implements Runnable {
	
	public static final int PORT = 4552;
	
	private HashMap<InetAddress, Integer> clients = new HashMap<>();
	private DatagramSocket serverSocket;
	private Server server;
	private byte[] data = new byte[1024];
	private boolean running = true;
	
	public UDPServer(Server s) {
		server = s;
		try {
			serverSocket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (running) {
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				serverSocket.receive(packet);
				clients.put(packet.getAddress(), packet.getPort());
				String request = new String(packet.getData(), 0, packet.getLength());
				String header = ClientHandler.getHeader(request);
				String[] body = ClientHandler.getBody(request);
				if (header.equals("updatetank")) {
					sendToAll(request);
				} else if (header.equals("updatebullet")) {
					sendToAll(request);
				} else if (header.equals("point")) {
					sendToAll(request);
				} else if (header.equals("trainee")) {
					sendToAll(request);
				} else {
					sendToAll("wrong request");
				}
			} catch (IOException e) {
				if (running) {
					System.out.println("pas normal udp");
				} else {
					System.out.println("udp bien fermé");
				}
			}
			
		}
	}

	private void sendToAll(String msg) {
		byte[] response = msg.getBytes();
		for (Map.Entry<InetAddress, Integer> a : clients.entrySet()) {
			DatagramPacket toSend = new DatagramPacket(response, response.length, a.getKey(), a.getValue());
			try {
				serverSocket.send(toSend);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void close() {
		running = false;
		serverSocket.close();
	}
}
