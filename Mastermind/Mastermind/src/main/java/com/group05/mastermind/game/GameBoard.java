package com.group05.mastermind.game;

public class GameBoard {

	private byte[] solution;

	public GameBoard() {
		super();
	}
	
	public byte[] getSolution() {
		return solution;
	}

	// For testing purposes, allows user to set custom solution
	public void setSolution(byte[] solution) {
		this.solution = solution;
	}

	/**
	 * Receives an array of bytes (guess) from the client, determines how many
	 * are the CORRECT value & the CORRECT position as well as how many are the
	 * CORRECT value & the INCORRECT position. Returns the appropriate hint.
	 * 
	 * @param guess
	 *            The guess sent by the client
	 * @return The hint to send to the client
	 */
	public byte[] generateHint(byte[] guess) {

		// Duplicate variables for possible modification
		byte[] guessCopy = guess.clone();
		byte[] solutionCopy = solution.clone();
		byte[] hint = { 0, 0, 0, 0}; // Instantiate the hint to send to client
		int counter = 0;

		// Determine if any values from the guess are the:
		// CORRECT VALUE & CORRECT POSITION
		for (int i = 0; i < 4; i++) {
			if (guessCopy[i] == solutionCopy[i]) { // Matching pair
				
				guessCopy[i] = -1;
				solutionCopy[i] = -1;
				hint[counter] = 1;
				
				counter++;
			}
		}

		// Determine if any remaining values from the guess are the:
		// CORRECT VALUE & INCORRECT POSITION
		for (int i = 0; i < 4; i++) {
			if (solutionCopy[i] != -1) { // Skip if already matched
				for (int j = 0; j < solution.length; j++) {
					if (guessCopy[j] != -1) { // Skip if already matched
						if (solutionCopy[i] == guessCopy[j]) { // Matching pair
							
							guessCopy[j] = -1;
							solutionCopy[i] = -1;
							hint[counter] = 2;
							
							counter++;
						}
					}
				} // End for (j)
			}
		} // End for (i)

		return hint;
	}

	/*
	 * Generates a random solution
	 */
	public void generateSolution() {

		// Initialize the array of bytes
		solution = new byte[4];

		for (int i = 0; i < 4; i++) {
			// Generate a random byte (0-7)
			byte random = (byte) (Math.random() * 8);
			// System.out.println(random);
			solution[i] = random;
		}
	}

}
