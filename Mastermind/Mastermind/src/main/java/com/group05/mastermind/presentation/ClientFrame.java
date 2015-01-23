package com.group05.mastermind.presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

import com.group05.mastermind.business.GameClient;
import com.group05.mastermind.data.CCode;
import com.group05.mastermind.data.OPCode;

import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {

	private JPanel contentPane;
	private GameClient gameClient;
	private JMenuItem mntmConnect, mntmGameStart, mntmForfeit, mntmDisconnect,
			mntmExit;
	private JLabel[][] guesses, hints;
	private JLabel[] solution;
	private JLabel result;
	private JButton[] selection, colors;
	private JButton send;
	private int turn;
	private byte[] guess;
	private byte colorSelected;
	private boolean gameInProgress;

	/**
	 * Create the frame
	 */
	public ClientFrame() {

		// Initialize everything
		gameClient = new GameClient();
		guesses = new JLabel[10][4];
		hints = new JLabel[10][4];
		solution = new JLabel[4];
		selection = new JButton[4];
		colors = new JButton[8];
		guess = new byte[4];

		// this block of code prevents server from crashing when the user
		// presses the 'x' to close the game
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					gameClient.disconnect();
				} catch (IOException ioe) {
					System.out.println("IOException: " + ioe.getMessage());
				}
				System.exit(0);
			}
		});
		setTitle("Mastermind Client");
		setSize(300, 600);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		setContentPane(contentPane);

		// Menu

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnGame = new JMenu("Game");
		menuBar.add(mnGame);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmConnect = new JMenuItem("Connect...");
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					String ip = (String) JOptionPane.showInputDialog(
							new JFrame(), "Please enter the IP of the server:",
							"Connect...", JOptionPane.PLAIN_MESSAGE);
					if (ip != null && ip.length() > 0) {
						if (gameInProgress) {
							resetGame();
							gameInProgress = false;
							enableButtons(false);
						}
						
						gameClient.disconnect();
						gameClient.setServerAddress(ip);
						gameClient.connect();
					}
				} catch (IOException e) {

					System.out.println("Connection failed.");

					JOptionPane.showMessageDialog(contentPane,
							"No response from server: " + e.getMessage(),
							"Could not connect to server...",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnFile.add(mntmConnect);

		mntmDisconnect = new JMenuItem("Disconnect...");
		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					if (gameInProgress) {
						resetGame();
						gameInProgress = false;
						enableButtons(false);
					}
					gameClient.disconnect();
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
				}
			}
		});
		mnFile.add(mntmDisconnect);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					gameClient.disconnect();
					System.exit(0);
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
				}
			}
		});
		mnFile.add(mntmExit);

		mntmGameStart = new JMenuItem("Start new game...");
		mntmGameStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					if (gameInProgress) {
						gameClient.forfeit();
						byte[] hint = gameClient.hint();

						byte[] answer = new byte[4];
						gameOver(false, answer);
					}
					
					byte reply = gameClient.gamestart();

					if (reply == OPCode.SERVER_GAMESTART.getOPCode()) {
						resetGame();
						enableButtons(true);
						gameInProgress = true;
					} else {
						throw new IOException(
								"Failed to start game, received wrong op code");
					}
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
				}
			}
		});
		mnGame.add(mntmGameStart);

		mntmForfeit = new JMenuItem("Forfeit");
		mntmForfeit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					gameClient.forfeit();
					byte[] hint = gameClient.hint();

					byte[] answer = new byte[4];
					// extract answer:
					for (int i = 0; i < 4; i++) {
						answer[i] = hint[i + 1];
					}
					gameOver(false, answer);

				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
				}
			}
		});
		mnGame.add(mntmForfeit);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane
						.showMessageDialog(
								contentPane,
								"Connect and play Mastermind!\n\n"
										+ "To play, you need to connect to a server and start a game.\n\n"
										+ "Connection:\n"
										+ "- To connect to a server, click on [File] > [Connect...] "
										+ "and enter the IP of the server you wish to connect to.\n"
										+ "- To disconnect, click on [File] > [Disconnect...].\n\n"
										+ "Game:\n"
										+ "- To start a game, once you are connected to a server, "
										+ "click on [Game] > [Start new game...].\n"
										+ "- To forfeit a game and see the answer, "
										+ "click on [Game] > [Forfeit]. ",
								"Mastermind Game Client",
								JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);

		// Panels

		// --- NORTH PANEL ---
		// This panel contains the solution set that is sent from the server

		JPanel solutionPanel = new JPanel();
		solutionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));
		solutionPanel.setBackground(Color.LIGHT_GRAY);
		solutionPanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		// The four solution placeholder labels

		solution[0] = new JLabel("");
		solution[0].setPreferredSize(new Dimension(30, 30));
		solution[0].setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		solution[1] = new JLabel("");
		solution[1].setPreferredSize(new Dimension(30, 30));
		solution[1].setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		solution[2] = new JLabel("");
		solution[2].setPreferredSize(new Dimension(30, 30));
		solution[2].setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		solution[3] = new JLabel("");
		solution[3].setPreferredSize(new Dimension(30, 30));
		solution[3].setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		// Add the four solution placeholder labels

		solutionPanel.add(solution[0]);
		solutionPanel.add(solution[1]);
		solutionPanel.add(solution[2]);
		solutionPanel.add(solution[3]);

		result = new JLabel("");
		result.setPreferredSize(new Dimension(40, 30));

		// Add the result placeholder label

		solutionPanel.add(result);

		// Add the solutionPanel panel to the contentPane panel

		contentPane.add(solutionPanel, BorderLayout.NORTH);

		// --- CENTER PANEL ---
		// This panel contains all the guesses and hints from the server

		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(10, 1));
		// resultsPanel.setBackground(Color.GRAY);

		// These panels contain each guess combination
		for (int i = 9; i >= 0; i--) {

			JPanel aResultPanel = new JPanel();
			aResultPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));
			// aResultPanel.setBackground(Color.GRAY);

			// The four solution placeholder labels

			for (int j = 0; j < 4; j++) {
				JLabel guess = new JLabel("");
				guess.setPreferredSize(new Dimension(30, 30));
				guess.setBorder(BorderFactory.createLineBorder(Color.GRAY));

				guesses[i][j] = guess;

				aResultPanel.add(guess);
			}
			resultsPanel.add(aResultPanel);

		}

		contentPane.add(resultsPanel, BorderLayout.CENTER);

		// --- EAST PANEL ---
		// This panel contains the server hints

		JPanel hintsPanel = new JPanel();
		hintsPanel.setLayout(new GridLayout(10, 1));
		// hintsPanel.setBackground(Color.GRAY);

		for (int i = 9; i >= 0; i--) {

			JPanel hintPanel = new JPanel();
			hintPanel.setLayout(new GridLayout(2, 2));
			// hintPanel.setBackground(Color.GRAY);

			for (int j = 0; j < 4; j++) {
				JLabel hint = new JLabel("");
				hint.setPreferredSize(new Dimension(20, 20));
				hints[i][j] = hint;

				hintPanel.add(hint);
			}

			hintsPanel.add(hintPanel);
		}

		contentPane.add(hintsPanel, BorderLayout.EAST);

		// // --- WEST PANEL ---
		// // This panel contains the turn number
		//
		// JPanel turnNumberPanel = new JPanel();
		// turnNumberPanel.setLayout(new GridLayout(10,1));
		// //turnNumberPanel.setBackground(Color.GRAY);
		//
		// for (int i=9; i>=0; i--) {
		// JLabel turnOne = new JLabel("" + (i+1), JLabel.CENTER);
		// turnOne.setForeground(Color.GRAY);
		// turnOne.setFont(new Font("Tahoma", Font.PLAIN, 20));
		// turnOne.setPreferredSize(new Dimension(40,40));
		//
		// turnNumberPanel.add(turnOne);
		// }
		//
		// contentPane.add(turnNumberPanel, BorderLayout.WEST);

		// --- SOUTH PANEL ---
		// This panel contains all the components that the user interacts with,
		// including the four input selections, the color section pane and the
		// send button.

		JPanel userInteractionPanel = new JPanel();
		userInteractionPanel.setLayout(new GridLayout(2, 1));
		userInteractionPanel.setBackground(Color.LIGHT_GRAY);
		userInteractionPanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));

		// This panel contains the four input selections and the send button.

		JPanel userSelectionPanel = new JPanel();
		userSelectionPanel.setLayout(new FlowLayout());
		userSelectionPanel.setBackground(Color.LIGHT_GRAY);

		// The four input buttons

		selection[0] = new JButton("");
		selection[0].setPreferredSize(new Dimension(30, 30));
		selection[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click selection[" + 0 + "]: " + arg0);
				colorPlaced(0);
			}
		});

		selection[1] = new JButton("");
		selection[1].setPreferredSize(new Dimension(30, 30));
		selection[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click selection[" + 1 + "]: " + arg0);
				colorPlaced(1);
			}
		});

		selection[2] = new JButton("");
		selection[2].setPreferredSize(new Dimension(30, 30));
		selection[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click selection[" + 2 + "]: " + arg0);
				colorPlaced(2);
			}
		});

		selection[3] = new JButton("");
		selection[3].setPreferredSize(new Dimension(30, 30));
		selection[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click selection[" + 3 + "]: " + arg0);
				colorPlaced(3);
			}
		});

		// Add the four input buttons to the panel

		userSelectionPanel.add(selection[0]);
		userSelectionPanel.add(selection[1]);
		userSelectionPanel.add(selection[2]);
		userSelectionPanel.add(selection[3]);

		// The send button

		send = new JButton("Send");
		send.setPreferredSize(new Dimension(75, 30));
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [send]: " + arg0);
				sendGuess();
			}
		});

		// Add the send button to the panel

		userSelectionPanel.add(send);

		// This panel contains the color selection pane.

		JPanel colorSelectionPanel = new JPanel();
		colorSelectionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));
		colorSelectionPanel.setBackground(Color.LIGHT_GRAY);

		// The eight color buttons

		colors[CCode.RED.getCCode()] = new JButton("");

		colors[CCode.RED.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_red.png")));
		colors[CCode.RED.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.RED.getCCode()].setBorderPainted(false);
		colors[CCode.RED.getCCode()].setContentAreaFilled(false);
		colors[CCode.RED.getCCode()].setFocusPainted(false);
		colors[CCode.RED.getCCode()]
				.setRolloverIcon(new ImageIcon(ClientFrame.class
						.getResource("/images/color_red_rollover.png")));
		colors[CCode.RED.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [red]: " + arg0);
				colorChosen(CCode.RED.getCCode());
			}
		});

		colors[CCode.MAGENTA.getCCode()] = new JButton("");
		colors[CCode.MAGENTA.getCCode()].setIcon(new ImageIcon(
				ClientFrame.class.getResource("/images/color_magenta.png")));
		colors[CCode.MAGENTA.getCCode()]
				.setPreferredSize(new Dimension(30, 30));
		colors[CCode.MAGENTA.getCCode()].setBorderPainted(false);
		colors[CCode.MAGENTA.getCCode()].setContentAreaFilled(false);
		colors[CCode.MAGENTA.getCCode()].setFocusPainted(false);
		colors[CCode.MAGENTA.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_magenta_rollover.png")));
		colors[CCode.MAGENTA.getCCode()]
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						System.out.println("Click [magenta]: " + arg0);
						colorChosen(CCode.MAGENTA.getCCode());
					}
				});

		colors[CCode.PURPLE.getCCode()] = new JButton("");
		colors[CCode.PURPLE.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_purple.png")));
		colors[CCode.PURPLE.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.PURPLE.getCCode()].setBorderPainted(false);
		colors[CCode.PURPLE.getCCode()].setContentAreaFilled(false);
		colors[CCode.PURPLE.getCCode()].setFocusPainted(false);
		colors[CCode.PURPLE.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_purple_rollover.png")));
		colors[CCode.PURPLE.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [purple]: " + arg0);
				colorChosen(CCode.PURPLE.getCCode());
			}
		});

		colors[CCode.BLUE.getCCode()] = new JButton("");
		colors[CCode.BLUE.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_blue.png")));
		colors[CCode.BLUE.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.BLUE.getCCode()].setBorderPainted(false);
		colors[CCode.BLUE.getCCode()].setContentAreaFilled(false);
		colors[CCode.BLUE.getCCode()].setFocusPainted(false);
		colors[CCode.BLUE.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_blue_rollover.png")));
		colors[CCode.BLUE.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [blue]: " + arg0);
				colorChosen(CCode.BLUE.getCCode());
			}
		});

		colors[CCode.CYAN.getCCode()] = new JButton("");
		colors[CCode.CYAN.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_cyan.png")));
		colors[CCode.CYAN.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.CYAN.getCCode()].setBorderPainted(false);
		colors[CCode.CYAN.getCCode()].setContentAreaFilled(false);
		colors[CCode.CYAN.getCCode()].setFocusPainted(false);
		colors[CCode.CYAN.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_cyan_rollover.png")));
		colors[CCode.CYAN.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [cyan]: " + arg0);
				colorChosen(CCode.CYAN.getCCode());
			}
		});

		colors[CCode.GREEN.getCCode()] = new JButton("");
		colors[CCode.GREEN.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_green.png")));
		colors[CCode.GREEN.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.GREEN.getCCode()].setBorderPainted(false);
		colors[CCode.GREEN.getCCode()].setContentAreaFilled(false);
		colors[CCode.GREEN.getCCode()].setFocusPainted(false);
		colors[CCode.GREEN.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_green_rollover.png")));
		colors[CCode.GREEN.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [green]: " + arg0);
				colorChosen(CCode.GREEN.getCCode());
			}
		});

		colors[CCode.YELLOW.getCCode()] = new JButton("");
		colors[CCode.YELLOW.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_yellow.png")));
		colors[CCode.YELLOW.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.YELLOW.getCCode()].setBorderPainted(false);
		colors[CCode.YELLOW.getCCode()].setContentAreaFilled(false);
		colors[CCode.YELLOW.getCCode()].setFocusPainted(false);
		colors[CCode.YELLOW.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_yellow_rollover.png")));
		colors[CCode.YELLOW.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [yellow]: " + arg0);
				colorChosen(CCode.YELLOW.getCCode());
			}
		});

		colors[CCode.ORANGE.getCCode()] = new JButton("");
		colors[CCode.ORANGE.getCCode()].setIcon(new ImageIcon(ClientFrame.class
				.getResource("/images/color_orange.png")));
		colors[CCode.ORANGE.getCCode()].setBorderPainted(false);
		colors[CCode.ORANGE.getCCode()].setContentAreaFilled(false);
		colors[CCode.ORANGE.getCCode()].setFocusPainted(false);
		colors[CCode.ORANGE.getCCode()].setRolloverIcon(new ImageIcon(
				ClientFrame.class
						.getResource("/images/color_orange_rollover.png")));
		colors[CCode.ORANGE.getCCode()].setPreferredSize(new Dimension(30, 30));
		colors[CCode.ORANGE.getCCode()].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("Click [orange]: " + arg0);
				colorChosen(CCode.ORANGE.getCCode());
			}
		});

		// Add the eight color buttons to the color selection panel

		colorSelectionPanel.add(colors[CCode.RED.getCCode()]);
		colorSelectionPanel.add(colors[CCode.MAGENTA.getCCode()]);
		colorSelectionPanel.add(colors[CCode.PURPLE.getCCode()]);
		colorSelectionPanel.add(colors[CCode.BLUE.getCCode()]);
		colorSelectionPanel.add(colors[CCode.CYAN.getCCode()]);
		colorSelectionPanel.add(colors[CCode.GREEN.getCCode()]);
		colorSelectionPanel.add(colors[CCode.YELLOW.getCCode()]);
		colorSelectionPanel.add(colors[CCode.ORANGE.getCCode()]);

		// Add the panels to the userInteraction panel.

		userInteractionPanel.add(userSelectionPanel);
		userInteractionPanel.add(colorSelectionPanel);

		// Add the userInteraction panel to the contentPane panel

		contentPane.add(userInteractionPanel, BorderLayout.SOUTH);

		resetGame();
		enableButtons(false);
		gameInProgress = false;
	}

	private void colorChosen(byte selected) {
		// if (colorSelected != -1) {
		// switch (colorSelected) {
		//
		// }
		// }
		colorSelected = selected;
	}

	private void colorPlaced(int position) {

		if (colorSelected != -1) {

			guess[position] = colorSelected;
			changeColor(selection[position], colorSelected);

		}
	}

	private void sendGuess() {

		// If all guess positions are chosen
		if (guess[0] != -1 && guess[1] != -1 && guess[2] != -1
				&& guess[3] != -1) {
			byte[] hint;

			try {
				gameClient.guess(guess);
				hint = gameClient.hint();

				for (int i = 0; i < 4; i++) {
					changeColor(guesses[turn][i], guess[i]);
				}

				if (getOPCode(hint) == OPCode.SERVER_LOSE.getOPCode()) {
					byte[] answer = new byte[4];
					// extract answer:
					for (int i = 0; i < 4; i++) {
						answer[i] = hint[i + 1];
					}
					gameOver(false, answer);
					// you lose

				} else {
					for (int i = 0; i < 4; i++) {
						changeHint(hints[turn][i], hint[i + 1]);
						System.out.println(hint[i + 1]);
					}

					if (getOPCode(hint) == OPCode.SERVER_WIN.getOPCode()) {

						gameOver(true, guess);
					}
				}
				turn++;

			} catch (IOException e) {
				System.out.println("sendGuess(): " + e.getMessage());
			}
		}

	}

	private void changeColor(JButton button, byte color) {

		switch (color) {

		case 0:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_red.png")));
			break;
		case 1:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_magenta.png")));
			break;
		case 2:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_purple.png")));
			break;
		case 3:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_blue.png")));
			break;
		case 4:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_cyan.png")));
			break;
		case 5:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_green.png")));
			break;
		case 6:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_yellow.png")));
			break;
		case 7:
			button.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_orange.png")));
			break;

		} // end switch
	}

	private void changeColor(JLabel label, byte color) {

		switch (color) {

		case 0:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_red.png")));
			break;
		case 1:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_magenta.png")));
			break;
		case 2:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_purple.png")));
			break;
		case 3:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_blue.png")));
			break;
		case 4:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_cyan.png")));
			break;
		case 5:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_green.png")));
			break;
		case 6:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_yellow.png")));
			break;
		case 7:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/color_orange.png")));
			break;

		} // end switch
	}

	private void changeHint(JLabel label, byte color) {
		switch (color) {
		case 1:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/hint_white.png")));
			break;
		case 2:
			label.setIcon(new ImageIcon(ClientFrame.class
					.getResource("/images/hint_black.png")));
			break;
		}
	}

	private byte getOPCode(byte[] message) {
		return message[0];
	}

	private void gameOver(boolean victory, byte[] answer) {

		revealAnswer(answer);

		if (victory) {
			result.setText("WIN");
		} else {
			result.setText("LOSE");
		}
		enableButtons(false);
		gameInProgress = false;
	}

	private void revealAnswer(byte[] answer) {

		for (int i = 0; i < answer.length; i++) {
			changeColor(solution[i], answer[i]);
		}

	}

	private void resetGame() {

		result.setText("");
		turn = 0;
		colorSelected = -1;

		for (int i = 0; i < 4; i++) {
			solution[i].setIcon(null);
			selection[i].setIcon(null);
			guess[i] = -1;
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 4; j++) {
				guesses[i][j].setIcon(null);
				hints[i][j].setIcon(null);
			}
		}
	}

	private void enableButtons(boolean enabled) {

		mntmForfeit.setEnabled(enabled);

		for (int i = 0; i < 4; i++) {
			selection[i].setEnabled(enabled);
		}
		for (int i = 0; i < 8; i++) {
			colors[i].setEnabled(enabled);
		}
		send.setEnabled(enabled);
	}

}
