package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CompleteRegister extends JFrame {

	private JPanel contentPane;
	private JTextField emailField;
	private JTextField passField;
	private JTextField nameField;
	private JTextField heightField;
	private JTextField weightField;
	private JTextField birthField;
	private JTextField maxField;
	private JTextField restField;

	/**
	 * Create the frame.
	 */
	public CompleteRegister() {
		setTitle("Complete Registration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 662);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel_10 = new JPanel();
		contentPane.add(panel_10);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel("                                   Email  ");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel.add(lblNewLabel);
		
		emailField = new JTextField();
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(30);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("                             Password  ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_1.add(lblNewLabel_1);
		
		passField = new JTextField();
		passField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_1.add(passField);
		passField.setColumns(30);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JLabel lblNewLabel_2 = new JLabel("                                  Name  ");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_2.add(lblNewLabel_2);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_2.add(nameField);
		nameField.setColumns(30);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		
		JLabel lblNewLabel_3 = new JLabel("                       Height ( cm )  ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_3.add(lblNewLabel_3);
		
		heightField = new JTextField();
		heightField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_3.add(heightField);
		heightField.setColumns(30);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		
		JLabel lblNewLabel_4 = new JLabel("                       Weight ( kg )  ");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_4.add(lblNewLabel_4);
		
		weightField = new JTextField();
		weightField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_4.add(weightField);
		weightField.setColumns(30);
		
		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);
		
		JLabel lblNewLabel_5 = new JLabel("Date of Birth ( dd-MM-yyyy ) ");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_5.add(lblNewLabel_5);
		
		birthField = new JTextField();
		birthField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_5.add(birthField);
		birthField.setColumns(30);
		
		JPanel panel_6 = new JPanel();
		contentPane.add(panel_6);
		
		JLabel lblNewLabel_6 = new JLabel("Maximum Heart Rate ( bpm )");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_6.add(lblNewLabel_6);
		
		maxField = new JTextField();
		maxField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_6.add(maxField);
		maxField.setColumns(30);
		
		JPanel panel_7 = new JPanel();
		contentPane.add(panel_7);
		
		JLabel lblNewLabel_7 = new JLabel("Resting Heart Rate ( bpm )   ");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_7.add(lblNewLabel_7);
		
		restField = new JTextField();
		restField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_7.add(restField);
		restField.setColumns(30);
		
		JPanel panel_8 = new JPanel();
		contentPane.add(panel_8);
		
		JButton btnFacebook = new JButton("Register with Facebook");
		btnFacebook.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_8.add(btnFacebook);
		
		JButton btnGoogle = new JButton("  Register with Google  ");
		btnGoogle.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_8.add(btnGoogle);
		
		JPanel panel_9 = new JPanel();
		contentPane.add(panel_9);
		
		JButton btnBack = new JButton("      Back      ");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_9.add(btnBack);
		
		JButton btnCreate = new JButton("Create Account");
		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_9.add(btnCreate);
	}

}

