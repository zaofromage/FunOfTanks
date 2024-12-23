package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.Game;
import model.gamestate.GameState;
import model.gamestate.Playing;
import model.player.Dir;

public class UDPServer implements Runnable {
	
	public static final int PORT = 4552;
	
	private HashMap<InetAddress, Integer> clients = new HashMap<>();
	private DatagramSocket serverSocket;
	private byte[] data = new byte[1024];
	private boolean running = true;
	private Game game;
	
	public UDPServer(Game game) {
		this.game = game;
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
				switch (GameState.state) {
				case MENU:
					break;
				case PLAYING:
					Playing playing = game.getPlaying();
					switch (header) {
					case "moveTank":
						playing.moveTank(UUID.fromString(body[0]), Dir.fromString(body[1]));
						break;
					case "updateOrientation":
						playing.updateOrientation(UUID.fromString(body[0]), Integer.parseInt(body[1]), Integer.parseInt(body[2]));
						break;
					}
					break;
				case FINISH:
					break;
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
