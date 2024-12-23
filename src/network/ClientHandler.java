package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import model.Game;
import model.gamestate.GameMode;
import model.gamestate.GameState;
import model.gamestate.Menu;
import model.gamestate.Playing;
import model.player.PlayerMode;
import utils.Vector;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	private ArrayList<ClientHandler> clients;
	
	private Game game;
	
	private boolean running = true;
	
	public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients, Game game) throws IOException {
		client = clientSocket;
		this.clients = clients;
		this.game = game;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		out.println("Connected to server");
	}

	@Override
	public void run() {
		try {
			while (running) {
				String request = in.readLine();
				if (request != null) {
					String header = getHeader(request);
					String[] body = getBody(request);
					switch (GameState.state) {
					case MENU:
						Menu menu = game.getMenu();
						switch (header) {
						case "addPlayer":
							menu.addPlayer(body[0], UUID.fromString(body[1]));
							break;
						case "removePlayer":
							menu.removePlayer(UUID.fromString(body[0]));
							break;
						case "setReady":
							menu.setReady(UUID.fromString(body[0]), Boolean.parseBoolean(body[1]));
							break;
						case "setMode":
							menu.setMode(GameMode.fromString(body[0]));
							break;
						case "play":
							menu.play();
							break;
						}
						break;
					case PLAYING:
						Playing playing = game.getPlaying();
						switch (header) {
						case "fire":
							playing.fire(UUID.fromString(body[0]), new Vector(Double.parseDouble(body[1]), Double.parseDouble(body[2])));
							break;
						case "switchMode":
							playing.switchMode(UUID.fromString(body[0]), PlayerMode.fromString(body[1]));
							break;
						}
						break;
					case FINISH:
						break;
					}
				}
				
			}
		} catch (IOException e) {
			if (running) {
				System.out.println("client handler pas normal");
			} else {
				System.out.println("server fermé");
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.close();
		}
	}
	
	public void close() {
		running = false;
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendToAll(String msg) {
		for (ClientHandler c : clients) {
			c.out.println(msg);
		}
	}
	
	public static String getHeader(String req) {
		return req.split(";")[0];
	}
	
	public static String[] getBody(String req) {
		String[] split = req.split(";");
		String[] body = new String[split.length  - 1];
		for (int i = 1; i < split.length; i++) {
			body[i - 1] = split[i];
		}
		return body;
	}

}
