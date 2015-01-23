package com.group05.mastermind.networking;

import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.io.*; // for IOException and Input/OutputStream

public class MMPacket {

	public static final int BUFSIZE = 5;
	private Socket socket;
	
	public MMPacket(Socket socket) {
		super();
		this.socket = socket;
	}
	
	/**
	 * Sends an array of bytes to the socket via OutputStream.
	 * 
	 * @param message	the message being sent
	 */
	public void sendPacket(byte [] message) throws IOException {
		
		OutputStream out = socket.getOutputStream();
		out.write(message);
	}
	
	/**
	 * Receives an array of bytes from the socket via InputStream.
	 * 
	 * @return 			the message being received
	 */
	public byte[] receivePacket() throws IOException {
		
		InputStream in = socket.getInputStream();
		byte[] message = new byte[BUFSIZE];

		in.read(message);
		
		return message;
	}

}
