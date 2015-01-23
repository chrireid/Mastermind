package com.group05.mastermind.presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.group05.mastermind.data.CCode;

public class ColorSelectionPanel extends JPanel {

	private JPanel panel;
	private JButton[] colors;
	byte colorSelected;

	public ColorSelectionPanel() {

		// Instantiate variables
		panel = new JPanel();
		colors = new JButton[8];
		
		place();

	}
	
	public JButton[] getColors() {
		return colors;
	}

	private void place() {

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));
		panel.setBackground(Color.LIGHT_GRAY);

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
				colorSelected = CCode.RED.getCCode();
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
						colorSelected = CCode.MAGENTA.getCCode();
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
				colorSelected = CCode.PURPLE.getCCode();
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
				colorSelected = CCode.BLUE.getCCode();
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
				colorSelected = CCode.CYAN.getCCode();
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
				colorSelected = CCode.GREEN.getCCode();
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

				colorSelected = CCode.YELLOW.getCCode();
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
				colorSelected = CCode.ORANGE.getCCode();
			}
		});

		// Add the eight color buttons to the color selection panel

		panel.add(colors[CCode.RED.getCCode()]);
		panel.add(colors[CCode.MAGENTA.getCCode()]);
		panel.add(colors[CCode.PURPLE.getCCode()]);
		panel.add(colors[CCode.BLUE.getCCode()]);
		panel.add(colors[CCode.CYAN.getCCode()]);
		panel.add(colors[CCode.GREEN.getCCode()]);
		panel.add(colors[CCode.YELLOW.getCCode()]);
		panel.add(colors[CCode.ORANGE.getCCode()]);

	}

}
