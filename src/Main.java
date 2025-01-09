import java.io.IOException;
import java.util.Scanner;

import network.Client;
import network.Server;
import network.UDPServer;

public class Main {
	
	public static void main(String[] args) {
		if (args[0].equals("serve")) {
			new Server();
			new UDPServer();			
		} else if (args[0].equals("client")) {
			try {
				Client client = new Client("192.168.1.106", Server.PORT);
				Scanner sc = new Scanner(System.in);
				boolean running = true;
				while (running) {
					System.out.println("Message a envoyé au serveur : ");
					String msg = sc.nextLine();
					if (msg.equals("quit")) {
						break;
					}
					client.send(new String[] {msg});
				}
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
