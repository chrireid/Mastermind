package com.group05.mastermind.business;

import java.io.IOException;
import java.net.Socket;

import com.group05.mastermind.data.OPCode;
import com.group05.mastermind.game.GameBoard;
import com.group05.mastermind.networking.MMPacket;

public class Session implements Runnable {
	
	private Socket socket;
	private Thread thread;
	private static final int NUM_OF_TURNS = 9;
	
	public Session (Socket socket) {
		
		this.socket = socket;
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		try {
			play();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	private void play() throws IOException {
		
		byte[] clientMsg = new byte[MMPacket.BUFSIZE];
		byte[] serverMsg = new byte[MMPacket.BUFSIZE];
		MMPacket connection = new MMPacket(socket);
		GameBoard board = new GameBoard();
		;

		do { // Do while clientSocket is open...

			System.out.println("SERVER: Waiting for client packet...");
			clientMsg = connection.receivePacket();

			// Client wants to start game
			if (getOPCode(clientMsg) == OPCode.CLIENT_GAMESTART.getOPCode() ||
					getOPCode(clientMsg) == OPCode.CLIENT_TESTING.getOPCode()) {
				
				if (getOPCode(clientMsg) == OPCode.CLIENT_GAMESTART.getOPCode()) {
					
					System.out.println("\nSERVER: Starting regular game...");
					
					// Draw game board
					board.generateSolution();
					
				} else {
					System.out.println("\nSERVER: Starting test game...");
					
					byte[] solution = getBody(clientMsg);
					System.out.print("SERVER: Solution (sent by client): ");
					checkMessage(solution);
					
					board.setSolution(solution);
					
				}
				// Solution to game
				System.out.print("Solution: ");
				checkMessage(board.getSolution());

				// Gameboard instantiated, ready to start, inform client...
				serverMsg = prepareMessage(OPCode.SERVER_GAMESTART.getOPCode(),
						null);
				connection.sendPacket(serverMsg);
				
				// Initialize turn counter
				int counter = 0;
				
				// Play until turn counter exceeds limit
				while (counter <= NUM_OF_TURNS) {

					// Receive client message
					clientMsg = connection.receivePacket();

					
					if (getOPCode(clientMsg) == OPCode.CLIENT_GUESS.getOPCode()) {

						// Client sent a guess to server

						byte[] guess = getBody(clientMsg);
						byte[] hint = board.generateHint(guess);

						// Determine if the guess was correct

						if (checkIfWon(hint)) {

							// Client guess was correct

							System.out.println("SERVER: Correct, you win!");

							// Send client WIN code with solution
							serverMsg = prepareMessage(
									OPCode.SERVER_WIN.getOPCode(), hint);

							connection.sendPacket(serverMsg);
							break;

						} else {

							// Client guess was incorrect

							if (counter == NUM_OF_TURNS) {

								// Client has run out of guesses

								System.out.println("SERVER: Incorrect... "
										+ "you lose...");

								// Send client LOSE code with solution
								serverMsg = prepareMessage(
										OPCode.SERVER_LOSE.getOPCode(),
										board.getSolution());

								connection.sendPacket(serverMsg);

								break;

							} else {
								counter++;

								System.out.println("SERVER: Incorrect... "
										+ "sending guess...");

								// Send client HINT code with hint
								serverMsg = prepareMessage(
										OPCode.SERVER_HINT.getOPCode(), hint);

								connection.sendPacket(serverMsg);
							}
						}

					} else if (getOPCode(clientMsg) == OPCode.CLIENT_DISCONNECT
							.getOPCode()) {

						clientDisconnect(socket);
						break;

					} else if (getOPCode(clientMsg) == OPCode.CLIENT_FORFEIT
							.getOPCode()) {

						System.out.println("SERVER: Forfeit by client...");

						// Send client LOSE code with solution
						serverMsg = prepareMessage(
								OPCode.SERVER_LOSE.getOPCode(),
								board.getSolution());

						connection.sendPacket(serverMsg);
						break;

					}

				} // End while (counter <= NUM_OF_TURNS)

			} else if (getOPCode(clientMsg) == OPCode.CLIENT_DISCONNECT
					.getOPCode()) {

				clientDisconnect(socket);

			}

		} while (!socket.isClosed());

		System.out.println("SERVER: Client disconnected.");
	}

	/**
	 * Receives an Operation Code with the hint/answer to the GameBoard and
	 * sends it to the client
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

		if (b.length == MMPacket.BUFSIZE - 1 && b != null) {
			for (int i = 0; i < b.length; i++)
				message[i + 1] = b[i];
		} else {
			for (int i = 0; i < MMPacket.BUFSIZE - 1; i++)
				message[i + 1] = 0;
		}

		return message;
	}

	/**
	 * Retrieves the OPCode from the message
	 * 
	 * @param message
	 * 
	 * @return The Operation Code
	 */
	private byte getOPCode(byte[] message) {
		return message[0];
	}

	/**
	 * Retrieves the message body by stripping the out the OPCode
	 * 
	 * @param message
	 * 
	 * @return The array of bytes containing the guess
	 */
	private byte[] getBody(byte[] message) {

		byte[] guess = new byte[message.length - 1];
		for (int i = 0; i < guess.length; i++) {
			guess[i] = message[i + 1];
		}
		return guess;
	}

	/**
	 * Checks the hint to see if all values are 1
	 * 
	 * @param hint
	 * 
	 * @return True if win - false if lose
	 */
	private boolean checkIfWon(byte[] hint) {
		boolean won;

		if (hint[0] == 1 && hint[1] == 1 && hint[2] == 1 && hint[3] == 1)
			won = true;
		else
			won = false;

		return won;
	}

	private void clientDisconnect(Socket clientSocket) throws IOException {

		System.out.println("SERVER: "
				+ "Exit request by client...  closing socket.");
		clientSocket.close();

	}
	
	private void clientGuess() {
		
	}

	// For testing, please ignore
	private void checkMessage(byte[] message) {
		// -------- DEBUGGING CODE -------- //
		// Display the packet to console
		String messageStr = "";
		for (int i = 0; i < message.length; i++) {
			// messageStr += "byte[" + i + "]: " + message[i] + "\n";
			messageStr += message[i] + " ";
		}
		System.out.println(messageStr);
		// -------- DEBUGGING CODE -------- //

	}
	
}
