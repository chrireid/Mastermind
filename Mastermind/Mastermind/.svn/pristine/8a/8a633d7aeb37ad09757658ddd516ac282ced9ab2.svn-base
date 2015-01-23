package com.group05.mastermind.business;

import java.net.*;
import java.io.*;

import com.group05.mastermind.data.OPCode;
import com.group05.mastermind.networking.MMPacket;

public class GameClient {

	private Socket socket;
	private String serverAddress;
	private int serverPort;
	private MMPacket connection;

	/**
	 * Default constructor (for testing)
	 */
	public GameClient() {
		super();
		serverAddress = "";
		serverPort = 50000;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void connect() throws IOException {

		System.out.print("Connected to server... ");
		socket = new Socket(serverAddress, serverPort);

		// Initialize the connection to server
		connection = new MMPacket(socket);

		System.out.println("Connected.");
	}

	/**
	 * Sends a game start to the server (OPCode)
	 * 
	 * @param hint
	 */
	public byte gamestart() throws IOException {

		byte[] message = prepareMessage(OPCode.CLIENT_GAMESTART.getOPCode(),
				null);

		connection.sendPacket(message);

		message = connection.receivePacket();
		return (getOPCode(message));
	}

	/**
	 * Sends a forfeit to the server (OPCode)
	 * 
	 * @param hint
	 */
	public void forfeit() throws IOException {

		byte[] message = prepareMessage(OPCode.CLIENT_FORFEIT.getOPCode(), null);

		connection.sendPacket(message);
	}

	/**
	 * Sends a guess to the server (OPCode + Guess)
	 * 
	 * @param hint
	 */
	public void guess(byte[] guess) throws IOException {

		byte[] message = prepareMessage(OPCode.CLIENT_GUESS.getOPCode(), guess);

		connection.sendPacket(message);
	}

	public byte[] hint() throws IOException {

		byte[] hint = connection.receivePacket();

		return hint;
	}

	/**
	 * Sends the server EXIT code and closes the socket (if required) 
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {

		if (socket != null) {
			if (!socket.isClosed()) {
				disconnectRequest();
				socket.close();
			}
		}
	}

	/**
	 * Sends an disconnect request to the server (OPCode)
	 * 
	 * @param hint
	 */
	private void disconnectRequest() throws IOException {

		byte[] message = prepareMessage(OPCode.CLIENT_DISCONNECT.getOPCode(),
				null);

		connection.sendPacket(message);
	}

	/**
	 * Retrieves the OPCode from the message
	 * 
	 * @param message
	 * @return The OPCode
	 */
	private byte getOPCode(byte[] message) {
		return message[0];
	}

	/**
	 * Retrieves the hint by stripping the OPCode out of the message
	 * 
	 * @param message
	 * 
	 * @return The array of bytes containing the hint
	 */
	public byte[] getHint(byte[] message) {

		byte[] hint = new byte[message.length - 1];
		for (int i = 0; i < hint.length; i++) {
			hint[i] = message[i + 1];
		}
		return hint;
	}

	/**
	 * Receives an Operation Code with byte array and sends it to the client
	 * 
	 * @param code
	 * @param b
	 * @return The message containing the operation code and hint/solution
	 */
	private byte[] prepareMessage(byte code, byte[] b) {

		byte[] message = new byte[MMPacket.BUFSIZE];
		message[0] = code;

		if (b == null) {
			byte[] zero = { 0, 0, 0, 0 };
			b = zero;
		}

		if (b.length == MMPacket.BUFSIZE - 1) {
			for (int i = 0; i < b.length; i++)
				message[i + 1] = b[i];
		} else {
			for (int i = 0; i < MMPacket.BUFSIZE - 1; i++)
				message[i + 1] = 0;
		}
		return message;
	}
}