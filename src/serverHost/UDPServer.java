package serverHost;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;

import serverClass.ServerBullet;
import serverClass.ServerTank;
import utils.Finder;

public class UDPServer implements Runnable {
	
	public static final int PORT = 4552;
	
	private HashMap<InetAddress, Integer> clients = new HashMap<>();
	private DatagramSocket serverSocket;
	private Server server;
	private byte[] data = new byte[1024];
	
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
		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				serverSocket.receive(packet);
				clients.put(packet.getAddress(), packet.getPort());
				String request = new String(packet.getData(), 0, packet.getLength());
				String header = ClientHandler.getHeader(request);
				String[] body = ClientHandler.getBody(request);
				if (header.equals("updatetank")) {
					ServerTank tank = Finder.findServerTank(body[0], server.getPlaying().getTanks());
					if (tank != null) {
						tank.x = Integer.parseInt(body[1]);
						tank.y = Integer.parseInt(body[2]);
						tank.orientation = Double.parseDouble(body[3]);
						sendToAll(request);						
					}
				} else if (header.equals("updatebullet")) {
					ServerBullet bullet = Finder.findServerBullet(body[3],Integer.parseInt(body[0]), server.getPlaying().getBullets());
					if (bullet != null) {
						bullet.update(Integer.parseInt(body[1]), Integer.parseInt(body[2]));
						sendToAll(request);
					}
				} else if (header.equals("point")) {
					sendToAll(request);
				} else {
					sendToAll("wrong request");
				}
			} catch (IOException e) {
				e.printStackTrace();
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
}
