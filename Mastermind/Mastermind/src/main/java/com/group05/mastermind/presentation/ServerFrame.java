package com.group05.mastermind.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.group05.mastermind.business.GameServer;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame {

	private GameServer gameServer;
	private JPanel contentPane;
	private JMenuItem mntmAcceptConnections,mntmCloseConnections;
	private JLabel lblServerStatusValue, lblServerIPValue, lblServerPortValue;

	/**
	 * Create the frame
	 */
	public ServerFrame() {
		gameServer = new GameServer();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Mastermind Server");
		setSize(250, 200);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Menu

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmAcceptConnections = new JMenuItem("Accept Connections");
		mntmAcceptConnections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					gameServer.perform();
					lblServerStatusValue.setText(gameServer.getServerStatus());
					lblServerStatusValue.setForeground(new Color(35, 140, 35)); // green
					lblServerIPValue.setText(gameServer.getIPAddress());
					lblServerPortValue.setText(gameServer.getPortNumber());
					mntmAcceptConnections.setEnabled(false);
					mntmCloseConnections.setEnabled(true);
				} catch (IOException e) {
					System.out.println("Accept failed: " + e.getMessage());
				}
			}
		});
		mnFile.add(mntmAcceptConnections);

		mntmCloseConnections = new JMenuItem("Close Connections");
		mntmCloseConnections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					gameServer.closeAllConnections();
					lblServerStatusValue.setText(gameServer.getServerStatus());
					lblServerStatusValue.setForeground(new Color(255, 0, 0)); // red
					lblServerIPValue.setText(gameServer.getIPAddress());
					lblServerPortValue.setText(gameServer.getPortNumber());
					mntmAcceptConnections.setEnabled(true);
					mntmCloseConnections.setEnabled(false);
				} catch (IOException e) {
					System.out.println("Close failed: " + e.getMessage());
				}
			}
		});
		mntmCloseConnections.setEnabled(false);
		mnFile.add(mntmCloseConnections);

		mnFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					gameServer.closeAllConnections();
				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());
				}
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane
						.showMessageDialog(
								contentPane,
								"This program allows machines running Game Clients "
								+ "to connect and play Mastermind!\n\n"
								+ "The server status, server IP and server Port numbers are "
								+ "displayed in the main window.\n\n"
								+ "- To establish a ServerSocket and start accepting connections, "
								+ "click on [File] > [Accept Connections].\n"
								+ "- To close the ServerSocket and terminate all sessions, "
								+ "click on [File] > [Close Connections].",
								"Mastermind Game Server",
								JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);

		// Panel

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(panel, BorderLayout.CENTER);

		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatusValue = new JLabel("Offline");
		lblServerStatusValue.setForeground(new Color(255, 0, 0)); // red

		JLabel lblServerIP = new JLabel("IP Address:");
		lblServerIPValue = new JLabel("-");

		JLabel lblServerPort = new JLabel("Port #:");
		lblServerPortValue = new JLabel("-");

		panel.add(lblServerStatus);
		panel.add(lblServerStatusValue);
		panel.add(lblServerIP);
		panel.add(lblServerIPValue);
		panel.add(lblServerPort);
		panel.add(lblServerPortValue);

	}

}
