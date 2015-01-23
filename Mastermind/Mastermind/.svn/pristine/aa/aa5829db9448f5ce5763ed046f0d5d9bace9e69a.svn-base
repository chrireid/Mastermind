package com.group05.mastermind.business;

import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.util.ArrayList;
import java.io.*; // for IOException and Input/OutputStream

import com.group05.mastermind.data.OPCode;
import com.group05.mastermind.game.GameBoard;
import com.group05.mastermind.networking.*;

public class GameServer implements Runnable {

	private int servPort;
	private ServerSocket servSock;
	private ArrayList<Session> sessions;
	private Thread thread;

	/**
	 * Default Constructor
	 */
	public GameServer() {
		super();
		this.servPort = 50000;

	}

	/**
	 * Non-default Constructor
	 * 
	 * @param servPort
	 */
	public GameServer(int servPort) {
		super();
		this.servPort = servPort;
	}

	/**
	 * Returns the ip address of the server machine
	 * 
	 * @return The IP address of the GameServer
	 * @throws IOException
	 */
	public String getIPAddress() throws IOException {

		String ip;
		if (servSock != null) {
			ip = InetAddress.getLocalHost().getHostAddress();
		} else {
			ip = "-";
		}
		return ip;
	}

	/**
	 * Returns the port of the server machine
	 * 
	 * @return The port of the GameServer
	 * @throws IOException
	 */
	public String getPortNumber() throws IOException {

		String port;
		if (servSock != null) {
			port = Integer.toString(servSock.getLocalPort());
		} else {
			port = "-";
		}

		return port;
	}

	/**
	 * Returns the server status
	 * 
	 * @return The status of the GameServer
	 * @throws IOException
	 */
	public String getServerStatus() throws IOException {

		String status;
		if (servSock != null) {
			if (!servSock.isClosed()) {
				status = "Online";
			} else {
				status = "Offline";
			}
		} else {
			status = "Offline";
		}
		return status;
	}

	/**
	 * Required to implement Runnable, the new thread starts accepting
	 * connections
	 */
	@Override
	public void run() {

		try {
			acceptConnections();
		} catch (IOException e) {
			System.out.println("Thread Exception: " + e.getMessage());
		}
	}

	/**
	 * Initializes the server socket and starts accepting incoming connections
	 * 
	 * @throws IOException
	 */
	public void perform() throws IOException {

		// Initialize socket and display connection info
		initializeConnection();

		// Create a new, second thread for accepting connections
		thread = new Thread(this);
		thread.start();

	}

	/**
	 * Closes the server socket
	 * 
	 * @throws IOException
	 */
	public void closeAllConnections() throws IOException {

		if (servSock != null) {
			if (!servSock.isClosed()) {
				servSock.close();
				// close all the client sockets now
				for (Session s : sessions) {
					s.getSocket().close();
				}
			}
		}
	}

	/**
	 * Establishes a server socket for incoming connections
	 * 
	 * @throws IOException
	 */
	private void initializeConnection() throws IOException {
		// Create a server socket to accept client connection requests
		servSock = new ServerSocket(servPort);

		// Server connection information
		System.out.println("Server online");
		System.out.println("IP=[" + InetAddress.getLocalHost().getHostAddress()
				+ "], Port=[" + servSock.getLocalPort() + "]");
		System.out.println();
	}

	/**
	 * Accepts and services client connections, forever
	 * 
	 * @throws IOException
	 */
	private void acceptConnections() throws IOException {

		// Instantiate ArrayList of sessions
		sessions = new ArrayList<Session>();

		// Run forever, accepting and servicing connections
		for (;;) {
			System.out.println("Accepting client connections...");

			// Get client connection
			Socket clientSocket = servSock.accept();

			// Display client information
			System.out.println("Handling client at "
					+ clientSocket.getInetAddress().getHostAddress()
					+ " on port " + clientSocket.getPort());

			// Create the session
			sessions.add(new Session(clientSocket));

		}
		// Not reached
	}
	

}
