package es.deusto.ingenieria.sd.strava.client.gui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class LoginWindow extends JFrame {

	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField passwordField;

	private AthleteController athleteController;
    private RegisterWindow registerWindow;
    private MainWindow mainWindow;

	/**
	 * Create the frame.
	 */
	public LoginWindow(AthleteController athleteController) {
        this.athleteController = athleteController;
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 431, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel("     Email   ");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel.add(lblNewLabel);
		
		emailField = new JTextField();
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(emailField);
		emailField.setColumns(20);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("Password  ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_1.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setColumns(20);
		passwordField.setToolTipText("Password");
		panel_1.add(passwordField);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JButton btnLogin = new JButton("                     Login                       ");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_2.add(btnLogin);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		
		JButton btnRegister = new JButton("       Create a new Account       ");
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_3.add(btnRegister);
		
		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);

		// Button Actions
		btnLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        btnRegister.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerWindow.setVisible(true);
                setVisible(false);
            }
        });

	}

	public void setRegisterWindow(RegisterWindow r) {
        this.registerWindow = r;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

	public void login() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (athleteController.login(email, password)) {
            this.setVisible(false);
            mainWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Error in login");
        }
    }

}
