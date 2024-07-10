package serverHost;

import java.net.*;
import java.io.*;

public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream in;

	private int port;

	private Thread th;

	public Server(int port) {
		this.port = port;
		start();
	}

	public void start() {
		th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void listen() throws IOException {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("serveur is listening on " + InetAddress.getLocalHost() + ":" + port);
			while (true) {
				clientSocket = serverSocket.accept();
				System.out.println("new connection " + clientSocket);
				out = new DataOutputStream(clientSocket.getOutputStream());
				in = new DataInputStream(clientSocket.getInputStream());

				System.out.println("Assigning a new thread for this client");
				
				Thread t = new ClientHandler(in, out, clientSocket);
				t.start();
			}

		} catch (IOException e) {
			clientSocket.close();
			e.printStackTrace();
		}

	}

	public void stop() {
		try {
			clientSocket.close();
			System.out.println("client closed");
			serverSocket.close();
			System.out.println("server closed");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public class ClientHandler extends Thread {
		private DataInputStream in;
		private DataOutputStream out;
		private Socket socket;
		
		public ClientHandler(DataInputStream in, DataOutputStream out, Socket socket) {
			this.in = in;
			this.out = out;
			this.socket = socket;
		}
		
		public void run() {
			String received;
			while (true) {
				try {
					out.writeUTF("Send a message to this server (Exit to exit)");
					received = in.readUTF();
					if (received.equals("Exit")) {
						//this.socket.close();
						System.out.println("Connection closed");
						break;
					}
					out.writeUTF(received);
					System.out.println("Response : " + received);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
