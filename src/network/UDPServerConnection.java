package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class UDPServerConnection implements Runnable {

	private DatagramSocket socket;
	
	private boolean running = true;
	
	public UDPServerConnection() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);
				String res = new String(packet.getData(), 0, packet.getLength());
				if (res != null) {
					String header = ClientHandler.getHeader(res);
					String[] body = ClientHandler.getBody(res);
					
				}
			} catch (IOException e) {
				if (running) {
					System.out.println("udp conn pas normal");
				} else {
					System.out.println("server fermé");
				}
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
	
	public void close() {
		running = false;
		socket.close();
	}
	
}


