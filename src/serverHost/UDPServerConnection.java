package serverHost;

import java.io.IOException;

import gamestate.Domination;
import gamestate.GameState;
import gamestate.Playing;
import player.Bullet;
import player.Player;
import player.TypeShot;
import serverClass.ServerBullet;
import utils.Finder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.Game;

public class UDPServerConnection implements Runnable {

	private DatagramSocket socket;
	
	private Game game;
	
	private boolean running = true;
	
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
		while (running) {
			try {
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);
				String res = new String(packet.getData(), 0, packet.getLength());
				if (res != null) {
					String header = ClientHandler.getHeader(res);
					String[] body = ClientHandler.getBody(res);
					switch (GameState.state) {
					case MENU:break;
					case PLAYING:
						Playing play = game.getPlaying();
						if (header.equals("updatetank")) {
							Player p = Finder.findPlayer(body[0], play.getPlayers());
							if (p != null) {
								if (p.getTank() != null) {
									p.getTank().setX(Integer.parseInt(body[1]));
									p.getTank().setY(Integer.parseInt(body[2]));
									p.getTank().setOrientation(Double.parseDouble(body[3]));
									if (!game.getPlayer().equals(p)) {
										p.getTank().getCannon().setShot(TypeShot.parseTypeShot(body[4]));										
									}
								}
							}
						} else if (header.equals("updatebullet")) {
							Player owner = Finder.findPlayer(body[3], play.getPlayers());
							Bullet b = Finder.findBullet(owner, Integer.parseInt(body[0]),
									play.getBullets());
							if (b != null) {
								if (!owner.equals(play.getPlayer())) {
									b.update(Double.parseDouble(body[1]), Double.parseDouble(body[2]));									
								}
								if (!body[4].equals("null")) {
									Player hold = Finder.findPlayer(body[4], play.getPlayers());
									if (hold.getTank() != null) {
										b.setHolding(hold.getTank());										
									}
								}
							}
						} else if (header.equals("point")) {
							if (play instanceof Domination) {
								Domination dom = (Domination) play;
								dom.getZone().setPoints(Integer.parseInt(body[0]));
							}
						} else if (header.equals("trainee")) {
							play.getPlayer().trainee(Integer.parseInt(body[0]), Integer.parseInt(body[1]));
						}
						break;
					case FINISH:
						break;
					}
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
