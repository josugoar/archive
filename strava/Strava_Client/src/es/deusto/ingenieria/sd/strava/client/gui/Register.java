package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Register {

	private JFrame frmStravaLogin;

	/**
	 * Create the application.
	 */
	public Register() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStravaLogin = new JFrame();
		frmStravaLogin.setTitle("STRAVA Register");
		frmStravaLogin.setBounds(100, 100, 492, 474);
		frmStravaLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStravaLogin.getContentPane().setLayout(new BoxLayout(frmStravaLogin.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_1);
		
		JPanel panel = new JPanel();
		frmStravaLogin.getContentPane().add(panel);
		
		JButton btnNewButton = new JButton("        Login with Google        ");
		panel.add(btnNewButton);
		btnNewButton.setBackground(new Color(128, 0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		JPanel panel_2 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_2);
		
		JButton btnNewButton_1 = new JButton("      Login with Facebook      ");
		panel_2.add(btnNewButton_1);
		btnNewButton_1.setBackground(new Color(0, 128, 255));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		JPanel panel_3 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_3);
		
		JButton btnNewButton_2 = new JButton("              Subscribe              ");
		panel_3.add(btnNewButton_2);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JPanel panel_4 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_4);
	}

}
