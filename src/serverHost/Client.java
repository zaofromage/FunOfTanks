package serverHost;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private Scanner sc = new Scanner(System.in);

	private String ip;
	private int port;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
		startConnection();
		try {
			socketLoop();
		} catch (Exception e) {
			System.out.println("prout");
		}
	}

	private void startConnection() {
		try {
			socket = new Socket(ip, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			System.out.println("Client connected");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void socketLoop() throws Exception {
		while (true) {
			System.out.println(in.readUTF());
			String tosend = sc.nextLine();
			if (tosend.equals("Exit")) {
				//socket.close();
				break;
			}
			out.writeUTF(tosend);
			String received = in.readUTF();
			System.out.println(received);
		}
		sc.close();
		in.close();
		out.close();
	}

	public void stopConnection() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
