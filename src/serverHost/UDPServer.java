package serverHost;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashSet;

public class UDPServer implements Runnable {
	
	public static final int PORT = 4552;
	
	private HashSet<InetAddress> clients = new HashSet<>();
	private DatagramSocket server;
	private byte[] data = new byte[1024];
	
	public UDPServer() {
		try {
			server = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				server.receive(packet);
				clients.add(packet.getAddress());
				String mes = new String(packet.getData(), 0, packet.getLength());
				System.out.println("message recu : " + mes);
				byte[] response = "proutresponse".getBytes();
				DatagramPacket toSend = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
				server.send(toSend);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void sendToAll(String msg) {
		for (InetAddress a : clients) {
			
		}
	}
	
	private void sendToAllOthers(String msg) {
		
	}
}
