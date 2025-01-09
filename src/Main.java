import java.io.IOException;

import network.Client;
import network.Server;
import network.UDPServer;

public class Main {
	
	public static void main(String[] args) {
		if (args[0].equals("serve")) {
			new Server();
			new UDPServer();			
		}
	}
}
