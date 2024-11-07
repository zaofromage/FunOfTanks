package serverHost;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.Game;

public class UDPServerConnection implements Runnable {

	private DatagramSocket socket;
	
	private Game game;
	
	public UDPServerConnection(Game g) {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.game = g;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);
				String res = new String(packet.getData(), 0, packet.getLength());
				System.out.println("rep du serveur : " + res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void send(String mes, InetAddress ip) {
		byte[] toSend = mes.getBytes();
		DatagramPacket packet = new DatagramPacket(toSend, toSend.length, ip, UDPServer.PORT);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
